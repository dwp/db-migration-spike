from __future__ import print_function
from optparse import OptionParser
from subprocess import check_call, CalledProcessError
from sys import stderr

parser = OptionParser()
parser.add_option('-o', '--out', dest='out', help='Docker image')
parser.add_option('-s', '--src', dest='src', help='Directory containing the Dockerfile and contents to be added')
parser.add_option('-t', '--tag', dest='tag')

(options, args) = parser.parse_args()

try:
    check_call([
        'cd', options.src, ';',
        'docker', 'build', '--tag', options.tag, '--file', 'Dockerfile', '.',
    ])
except CalledProcessError as err:
    print('Error occurred executing docker build %s' % err, file=stderr)
    exit(1)

try:
    check_call([
        'docker', 'save', '--output', options.out, options.tag
    ])
except CalledProcessError as err:
    print('Error occured executing docker save %s' % err, file=stderr)
    exit(1)

try:
    check_call([
        'docker', 'rmi', options.tag
    ])
except CalledProcessError as err:
    print('Error occurred executing docker rmi %s' % err, file=stderr)
    exit(1)