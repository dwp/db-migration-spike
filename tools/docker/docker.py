from __future__ import print_function

import sys
from optparse import OptionParser
from os import makedirs, path, symlink
from subprocess import check_call

opts = OptionParser()
opts.add_option('-o', help='path to write Dockerfile to')
opts.add_option('--conf', action='append')
opts.add_option('--lib', action='append')
opts.add_option('--tmp', help='temporary directory')
args, ctx = opts.parse_args()

war = args.tmp
jars = set()

def prune(l):
    return [j for e in l for j in e.split(':')]

def link_jars(libs, directory):
    makedirs(directory)
    for j in libs:
        if j not in jars:
            jars.add(j)
            n = path.basename(j)
            if j.find('buck-out/gen/gerrit-') > 0:
                n = j[j.find('buck-out'):].split('/')[2] + '-' + n
            symlink(j, path.join(directory, n))

if args.lib:
    link_jars(prune(args.lib), path.join(war, 'WEB-INF', 'lib'))
if args.pgmlib:
    link_jars(prune(args.pgmlib), path.join(war, 'WEB-INF', 'pgm-lib'))
try:
    for s in ctx:
        check_call(['unzip', '-q', '-d', war, s])
    check_call(['zip', '-9qr', args.o, '.'], cwd=war)
except KeyboardInterrupt:
    print('Interrupted by user', file=sys.stderr)
    exit(1)