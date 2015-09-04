from bottle import route, error, post, get, run, static_file, abort, redirect, response, request, template
import os
import yowsup.demos.cli.layer
from TestImageSend import SendMediaStack
from constants import CREDENTIALS
from constants import directory
import random
import urllib 
import urlparse
import sys
import urllib2
import urllib
import json
from constants import processMessageApi
import requests

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
    print('-------------------------\n REQUEST help\n-------------------------')
    return 'Wassup! Welcome to Snapdeal!' + '\n' + 'Type your message to chat with us. Or, use our quick commands-\n' + help_string



#############################################################################
## Interract with Backend
#############################################################################

@route('/chat', method='POST')
def chat():
    try:
        ## Call B/E Api Here
        print('Calling Backend Api here: '+ processMessageApi)

        post_params = {'caller'    : request.json.get('caller').split('@')[0],
                       'messageid' : request.json.get('messageid'),
                       'message'   : request.json.get('message')}

        params = urllib.urlencode(post_params)
        print post_params
        req = urllib2.Request(processMessageApi)
        req.add_header('Content-Type', 'application/json')
        response = urllib2.urlopen(req, json.dumps(post_params))

        #headers = { 'Content-Type' : 'application/json' }
        #r = requests.post(processMessageApi, data=post_params, headers=headers)
        #print(r.status_code, r.reason)

    except Exception as e:
        print('OOPS:')
        print 'Error on line {}'.format(sys.exc_info()[-1].tb_lineno)
        print e

@route('/reply', method='POST')
def reply():
    print('-------------------------\n REQUEST reply\n-------------------------')
    print request.json

    try:
        if request.json.get('number') is None:
            return {'error': 'Error. Phone number is required' }

        print 'Sending message <' + request.json.get('message') + '> to <' + request.json.get('number') + '>'
        command = 'yowsup-cli demos --config yowsup-cli.config --send ' + request.json.get('number') + ' "' + request.json.get('message') + '"'

        os.system(command)
    except Exception as e:
        print('OOPS:')
        print 'Error on line {}'.format(sys.exc_info()[-1].tb_lineno)
        print e


@route('/replymedia', method='POST')
def replyImage():
    print('-------------------------\n REQUEST replymedia\n-------------------------')
    try:
        print request.json
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
        else:
            localfilepath = request.json.get('path')

        print('Final path:' + localfilepath)

        ## Forward call to whatsapp
        stack = SendMediaStack(CREDENTIALS, [([request.json.get('number'), localfilepath, request.json.get('caption')])])
        stack.start()
    except Exception as e:
        print('OOPS:')
        print 'Error on line {}'.format(sys.exc_info()[-1].tb_lineno)
        print e


#############################################################################
## Start Server
#############################################################################
#run(host='localhost', port=8989, reloader=True)
run(host='10.20.61.106', port=8989, reloader=True)
