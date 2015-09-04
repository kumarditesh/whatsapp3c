import os
import subprocess
from subprocess import Popen, PIPE
import time,sched
from constants import directory

pid = 0

def getPid():
    return pid

def setPid(p):
    global pid
    pid = p

def killDefunc():
    command="for i in `ps -ef | grep defunc | grep run.py |  tr -s ' ' | cut  -d' ' -f3 ` ; do kill $i; done"
    os.system(command)

def refereshYowsupServer():
    proc = subprocess.Popen(['python', 'run.py'], shell=False, stdout=PIPE, stderr=PIPE)
    stdout, stderr = proc.communicate()
    time.sleep(3)
    print('Started Yowsup server. PID:' + str(proc.pid))
    setPid(proc.pid)


def poll(): 
    print('Checking Yowsup server health..')
    #killDefunc()

    try:
        os.kill(getPid(), 0)
    except OSError:
        print('Refreshing Yowsup Server !')
        refereshYowsupServer()
    time.sleep(20)


if __name__ == '__main__':
    refereshYowsupServer()

    # Create media dir for downloads/uploads
    # directory imported from constants
    if not os.path.exists(directory):
        os.makedirs(directory)

    try:
        while True:
            poll()
    except Exception as e:
        print e
