from bottle import route, error, post, get, run, static_file, abort, redirect, response, request, template
import os
import yowsup.demos.cli.layer
from TestImageSend import SendMediaStack

#############################################################################
## Globals
#############################################################################
help_string = 'Quick Commands:' + '\n' + 'help' + '\n' + 'search <search string>' + '\n' + 'reccom' + '\n'
CREDENTIALS = ("919555535035", "Tbpn2wBDVHDTcYWZeUq6GcQ+pMw=") # replace with your phone and password
              
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
    print request.json
    # Call Backend Api here

@route('/reply', method='POST')
def reply():
    if request.json.get('number') is None:
        return {'error': 'Error. Phone number is required' }

    print 'Sending message <' + request.json.get('message') + '> to <' + request.json.get('number') + '>'
    command = 'yowsup-cli demos --config yowsup-cli.config --send ' + request.json.get('number') + ' "' + request.json.get('message') + '"'

    os.system(command)


@route('/replyimg', method='POST')
def replyImage():
    if request.json.get('number') is None:
        return {'error': 'Error. Phone number is required' }

    stack = SendMediaStack(CREDENTIALS, [([request.json.get('number'), request.json.get('path'), request.json.get('caption')])])
    stack.start()

#############################################################################
## Start Server
#############################################################################
run(host='localhost', port=8989, reloader=True)
