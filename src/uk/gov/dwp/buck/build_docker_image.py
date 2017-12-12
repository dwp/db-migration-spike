from optparse import OptionParser

import subprocess

parser = OptionParser()
parser.add_option('-o', '--out', dest='out', help='Script to create the docker image', default='build_docker_image.sh')
parser.add_option('-f', '--file', dest='dockerfile')
parser.add_option('-t', '--tag', dest='tag')
parser.add_option('--tmp', help='Temporary directory')

(options, args) = parser.parse_args()

tmp_dir = options.tmp
root = tmp_dir[:tmp_dir.index('buck-out')-1]

cmd = [
    'docker', 'build',
    '--tag', options.tag,
    '--file', options.dockerfile,
    '.'
]
with open(options.out,'w') as f:
    r = subprocess.call(cmd, stdout = f, stderr=subprocess.STDOUT, cwd = root)
    if r != 0:
        raise subprocess.CalledProcessError(r, ' '.join(cmd))