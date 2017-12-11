from optparse import OptionParser
import os
import stat

parser = OptionParser()
parser.add_option('-o', '--out', dest='out', help='Script to create the docker image', default='build_docker_image.sh')
parser.add_option('-f', '--file', dest='dockerfile')
parser.add_option('-t', '--tag', dest='tag')
parser.add_option('--tmp', help='Temporary directory')

(options, args) = parser.parse_args()

tmp_dir = options.tmp
root = tmp_dir[:tmp_dir.index('buck-out')-1]

cmd = [
    'docker build',
    '--tag %s' % options.tag,
    '--file %s' % options.dockerfile,
    root
]
with open(options.out,'w') as output_file:
    output_file.write(' '.join(cmd))

st = os.stat(options.out)
os.chmod(options.out, st.st_mode | stat.S_IEXEC)