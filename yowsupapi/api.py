from bottle import route, error, post, get, run, static_file, abort, redirect, response, request, template
import os
import yowsup.demos.cli.layer
from TestImageSend import SendMediaStack
from constants import CREDENTIALS
from constants import directory
import random
import urllib 
import urlparse

#############################################################################
## Globals
#############################################################################
help_string = 'Quick Commands:' + '\n' + 'help' + '\n' + 'search <search string>' + '\n' + 'reccom' + '\n'
#CREDENTIALS = ("919555535035", "veKEGp2CZK3RzOaW+1KiO7EYEm8=") # replace with your phone and password
              
#############################################################################
## Interract with User
#############################################################################

@route('/')
@route('/hi')
@route('/help')
def hi():
    return 'Wassup! Welcome to Snapdeal!' + '\n' + 'Type your message to chat with us. Or, use our quick commands-\n' + help_string



#############################################################################
## Interract with Backend
#############################################################################

@route('/chat', method='POST')
def chat():
    try:
        print request.json
        # Call Backend Api here
    except Exception as e:
        print('OOPS:')
        print e

@route('/reply', method='POST')
def reply():
    print request.json

    try:
        if request.json.get('number') is None:
            return {'error': 'Error. Phone number is required' }

        print 'Sending message <' + request.json.get('message') + '> to <' + request.json.get('number') + '>'
        command = 'yowsup-cli demos --config yowsup-cli.config --send ' + request.json.get('number') + ' "' + request.json.get('message') + '"'

        os.system(command)
    except Exception as e:
        print('OOPS:')
        print e


@route('/replymedia', method='POST')
def replyImage():
    print request.json

    try:
        if request.json.get('number') is None:
            return {'error': 'Error. Phone number is required' }
    
        localuserdir = request.json.get('path')
        if localuserdir.startswith('http') or localuserdir.startswith('https'):
            ## Path is a URL
            ## Save file to local file system
            localuserdir = directory + '/' + request.json.get('number')
            if not os.path.exists(localuserdir):
                os.makedirs(localuserdir)

            localfilepath = os.path.abspath(localuserdir + '/' + str(random.getrandbits(6)) + '-' + request.json.get('path').split('/')[-1])

            print('Path is a URI, Saving media to :' + localfilepath)
            urllib.urlretrieve(request.json.get('path'), localfilepath)

        ## Forward call to whatsapp
        stack = SendMediaStack(CREDENTIALS, [([request.json.get('number'), localfilepath, request.json.get('caption')])])
        stack.start()
    except Exception as e:
        print('OOPS:')
        print e


#############################################################################
## Start Server
#############################################################################
#run(host='localhost', port=8989, reloader=True)
run(host='10.20.61.106', port=8989, reloader=True)
