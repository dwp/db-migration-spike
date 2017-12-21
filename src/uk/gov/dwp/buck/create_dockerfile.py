# Example execution:
# src/uk/gov/dwp/buck/create_dockerfile.py \
#   --c 'CMD java -cp /srv/app/lib/. uk.gov.dwp.personal.details.server.PersonalDetailsServer \
#   --lib src/uk/gov/dwp/personal/details/server/BUCK:src/uk/gov/dwp/personal/details/server/ApplicationListenerConfiguration.java \
#   --tmp /tmp/test \
#   --out Dockerfile2
from __future__ import print_function

from optparse import OptionParser
from os import makedirs, path
from shutil import rmtree, copyfile

parser = OptionParser()
parser.add_option('-c', '--cmd', dest='cmd', help='Command to execute')
parser.add_option('-o', '--out', dest='out', help='Path to write Dockerfile to', default='output')
parser.add_option('--home', help='Full path of the application home', default='/srv/app')
parser.add_option('--lib', action='append')

(options, args) = parser.parse_args()

jars = set()

def prune(l):
    return [j for e in l for j in e.split(':')]

def make_clean_dir(dir):
    if path.isdir(dir):
        rmtree(dir)
    makedirs(dir)

def copy_jars(libs, directory):
    make_clean_dir(path.join(directory))
    for j in libs:
        jar = path.join(directory, path.basename(j))
        if jar not in jars:
            jars.add(jar)
            copyfile(j, jar)

make_clean_dir(path.join(options.out, 'lib'))

if options.lib:
    copy_jars(prune(options.lib), path.join(options.out, 'lib'))

dockerfile_path = path.join(options.out, 'Dockerfile')
with open(dockerfile_path, 'w') as dockerfile:
    dockerfile.write('FROM openjdk:8-jre-slim\n\n')
    for jar in sorted(jars):
        relative_jar = jar[jar.find('buck-out'):]
        dockerfile.write('COPY %s %s/lib/\n' % (relative_jar, options.home))
    dockerfile.write('\n%s\n' % options.cmd)