# Example execution:
# src/uk/gov/dwp/buck/create_dockerfile.py \
#   --c 'CMD java -cp /srv/app/lib/. uk.gov.dwp.personal.details.server.PersonalDetailsServer \
#   --lib src/uk/gov/dwp/personal/details/server/BUCK:src/uk/gov/dwp/personal/details/server/ApplicationListenerConfiguration.java \
#   --tmp /tmp/test \
#   --out Dockerfile2
from __future__ import print_function

from optparse import OptionParser
from os import makedirs, path, symlink
from shutil import rmtree

parser = OptionParser()
parser.add_option('-c', '--cmd', dest='cmd', help='Command to execute')
parser.add_option('-o', '--out', dest='out', help='Path to write Dockerfile to', default='Dockerfile')
parser.add_option('--home', help='Full path of the application home', default='/srv/app')
parser.add_option('--lib', action='append')
parser.add_option('--tmp', help='Temporary directory')

(options, args) = parser.parse_args()

working_dir = options.tmp
jars = set()

def prune(l):
    return [j for e in l for j in e.split(':')]

def make_clean_dir(dir):
    if path.isdir(dir):
        rmtree(dir)
    makedirs(dir)

def link_jars(libs, directory):
    make_clean_dir(path.join(directory))
    for j in libs:
        if j not in jars:
            jars.add(j)
            n = path.basename(j)
            symlink(j, path.join(directory, n))

if options.lib:
    link_jars(prune(options.lib), path.join(working_dir, 'lib'))

make_clean_dir(path.join(working_dir, 'docker'))

with open(path.join(working_dir, 'docker', options.out), 'w') as dockerfile:
    dockerfile.write('FROM openjdk:8-jre-slim\n\n')
    for jar in jars:
        dockerfile.write('COPY %s %s/lib/\n' % (jar[jar.find('buck-out'):], options.home))
    dockerfile.write('\n%s\n' % options.cmd)
        # try:
        #     check_call(['tar', 'cvf', options.out, '.'], cwd=working_dir)
        # except KeyboardInterrupt:
        #     print('Interrupted by user', file=sys.stderr)
        #     exit(1)