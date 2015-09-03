import os
import subprocess
import time,sched

pid = 0

def getPid():
    return pid

def setPid(p):
    global pid
    pid = p

def refereshYowsupServer():
    proc = subprocess.Popen(['python', 'run.py'], shell=False)
    time.sleep(3)
    print('Started Yowsup server. PID:' + str(proc.pid))
    setPid(proc.pid)


def poll(): 
    print('Checking Yowsup server health..')
    try:
        os.kill(getPid(), 0)
    except OSError:
        print('Refreshing Yowsup Server !')
        refereshYowsupServer()
    time.sleep(20)


if __name__ == '__main__':
    refereshYowsupServer()

    try:
        while True:
            poll()
    except Exception as e:
        print e
