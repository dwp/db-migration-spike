#!/usr/bin/env bash

set -euo pipefail
IFS=$'\n\t'

export LANG=en_GB.UTF-8
export LC_ALL=en_GB.UTF-8
export TZ='UTC'

# we assume this is set to the developer's preference and in CI it'll be set in bb-ci
#export NO_BUCKD=

BUCK_DIST=buck-dwp-0.0.9.2.gz
BUCK_URL=https://s3-eu-west-1.amazonaws.com/ucds-build-binaries/third-party/buck/${BUCK_DIST}
WATCHMAN_ROOT_URL=https://s3-eu-west-1.amazonaws.com/ucds-build-binaries/third-party/watchman

# add buck dir to path so that we find watchman
echo ${PATH} | grep -q "buck-out/opt/buck/bin" || export PATH=buck-out/opt/buck/bin:$PATH

BUCK=buck-out/opt/buck/bin/buck

# buck targets --show-output $(buck query 'kind(prebuilt_jar, deps(//buck-support/junit:pull-xslt-transform-deps))') | cut -d' ' -f 2 | tr '\n' ':'
TEST_TRANSFORM_CLASSPATH=buck-out/gen/lib/__serializer__/serializer-2.7.2.jar:buck-out/gen/lib/__xalan__/xalan-2.7.2.jar:buck-out/gen/lib/__xerces__/xerces-2.11.0.jar:buck-out/gen/lib/__xml-apis__/xml-apis-1.4.01.jar
RUNS_TESTS="n"

PROXY_OPTS=""
RUNNING_NESTED="n"

OPTIND=1

function show_help {
    echo "Usage: ./bb <command>"
    echo "Available commands:"
    echo "  test                   - compile and run tests"
    echo "  build                  - compile and run tests"
    echo "  project                - configure intellij project files"
}

while getopts "h?np" opt; do
    case "$opt" in
    h|\?)
        show_help
        exit 0
        ;;
    n)
        RUNNING_NESTED="y"
        ;;
    p)
        PROXY_OPTS="-x http://httpproxy.dmz.uc:3128"
        ;;
    esac
done

shift $((OPTIND-1))

if [[ ! -f .buckconfig ]]; then
    echo Must be run from the root of the project, exiting.
    exit 2
fi

start_time=$(date +%s)
function packet {
cat <<EOF
{
  "start": ${start_time}000,
  "end": ${end_time}000,
  "user": "$USER",
  "hostname": "$HOSTNAME",
  "ant_args": "$cmd",
  "build_tool": "buck"
}
EOF
}

function record_time {
    end_time=$(date +%s)
    echo Build time: $((end_time-start_time)) seconds
    if [[ $RUNNING_NESTED == "n" ]]; then
        curl -X POST \
             --max-time 5 \
             --noproxy '*' \
             -d "$(packet)" \
             http://eeportal.services.ucds.io:9102 || true
    fi
}

function bootstrap_env {

    echo -- Setting up .buckjavaargs
    local OS=$(uname -s)
    if [ ${OS} == "Darwin" ]; then
    echo --- Macs get 2g heap
cat > .buckjavaargs <<EOF
-Xmx2g
EOF
    elif [ ${OS} == "Linux" ]; then
    echo --- Linux get 4g heap
    cat > .buckjavaargs <<EOF
-Xmx4g
EOF
    fi

    if [[ ! -f buck-out/opt/buck/bin/buck ]] || [[ ! -f buck-out/opt/buck-version ]] || grep -vq ${BUCK_DIST} buck-out/opt/buck-version; then
        echo -- Found existing Buck version:
        cat buck-out/opt/buck-version || true
        echo -- Upgrading Buck to ${BUCK_DIST}
        echo -- Shutting down buckd
        [[ -f buck-out/opt/buck/bin/buckd ]] && buck-out/opt/buck/bin/buckd --kill
        killall watchman || true
        echo -- Cleaning buck-out/
        ./bb -n clean
        echo -- Downloading buck from ${BUCK_URL}
        echo -- Proxy opts: $PROXY_OPTS
        rm -rf buck-out/opt/buck
        mkdir -p buck-out/opt buck-out/tmp/buck
        (cd buck-out/tmp/buck && eval curl -v -L $PROXY_OPTS ${BUCK_URL} -o buck.tmp)
        (cd buck-out/opt && eval tar xf ../tmp/buck/buck.tmp)
        echo ${BUCK_DIST} > buck-out/opt/buck-version
    fi

    setup_watchman
}

function setup_watchman() {
  local OS=$(uname -s)
  local WATCHMAN=buck-out/opt/buck/bin/watchman

  # make sure the tmp dir exists (gets deleted if you restart your mac)
  if [ ${OS} == "Darwin" ]; then
    mkdir -p /tmp/.watchman
  fi

  # if watchman is there then we're done
  if [ -x ${WATCHMAN} ]; then return; fi


  if [ ${OS} == "Darwin" ]; then
    echo -- Downloading watchman from ${WATCHMAN_ROOT_URL}/watchman-osx
    eval curl --fail -L $PROXY_OPTS ${WATCHMAN_ROOT_URL}/watchman-osx -o ${WATCHMAN}
  elif [ ${OS} == "Linux" ]; then
    echo -- Downloading watchman from ${WATCHMAN_ROOT_URL}/watchman-centos66
    eval curl -v --fail -L $PROXY_OPTS ${WATCHMAN_ROOT_URL}/watchman-centos66 -o ${WATCHMAN}
    mkdir -p /var/lib/jenkins/var/run/watchman
  fi
  chmod +x ${WATCHMAN}
}

cmd=${1:-help}
[[ $# -ne 0 ]] && shift

echo "$@"
echo "$cmd"

case "$cmd" in
    test)
        echo "bb build step 1/2: bootstrapping"
        bootstrap_env
        echo "bb build step 2/2: buck tests"
        eval $BUCK test "$@"
        ;;
    clean)
        echo "cleaning buck-out/"
        [[ -f buck-out/opt/buck/bin/buckd ]] && buck-out/opt/buck/bin/buckd --kill || true
        rm -rf buck-out
        echo "cleaned"
        ;;
    project)
        bootstrap_env
        eval $BUCK project --intellij-aggregation-mode none --run-ij-cleaner
#        buck-support/bin/fix-intellij.sh
        ;;
    help)
        show_help
        ;;
    *)
        echo Invalid command: $cmd
        show_help
        exit 5
        ;;
esac
