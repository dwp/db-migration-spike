from __future__ import print_function

import sys
from optparse import OptionParser
from os import makedirs, path, symlink
from subprocess import check_call

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

def link_jars(libs, directory):
    makedirs(directory)
    for j in libs:
        print('Processing lib: %s' % j)
        if j not in jars:
            jars.add(j)
            n = path.basename(j)
            print('Basename: %s' % n)
            symlink(j, path.join(directory, n))

if options.lib:
    link_jars(prune(options.lib), path.join(working_dir, 'lib'))

# Add to method
mkdirs(path.join(working_dir, 'docker'))
dockerfile_path = path.join(working_dir, 'docker', 'Dockerfile')
print('Creating docker file: %s' % dockerfile_path)
dockerfile = open(dockerfile_path, 'w')
for jar in jars:
    dockerfile.write('COPY %s %s/lib' % (jar, options.home))
dockerfile.close()

try:
    check_call(['tar', 'cvf', options.out, '.'], cwd=working_dir)
except KeyboardInterrupt:
    print('Interrupted by user', file=sys.stderr)
    exit(1)