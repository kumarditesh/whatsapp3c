from yowsup.layers.interface                           import YowInterfaceLayer, ProtocolEntityCallback
from yowsup.layers.protocol_messages.protocolentities  import TextMessageProtocolEntity
from yowsup.layers.protocol_receipts.protocolentities  import OutgoingReceiptProtocolEntity
from yowsup.layers.protocol_acks.protocolentities      import OutgoingAckProtocolEntity
import urllib2
import urllib
import json


class EchoLayer(YowInterfaceLayer):

    # Can be used to send message directly
    def send_message(self, number, content):
        outgoingMessage = TextMessageProtocolEntity(content, to=number)
        self.toLower(outgoingMessage)


    @ProtocolEntityCallback("message")
    def onMessage(self, messageProtocolEntity):
        #send receipt otherwise we keep receiving the same message over and over

        if True:
            receipt = OutgoingReceiptProtocolEntity(messageProtocolEntity.getId(), messageProtocolEntity.getFrom(), 'read', messageProtocolEntity.getParticipant())
            print messageProtocolEntity.getBody() + ' from ' + messageProtocolEntity.getFrom()

            if messageProtocolEntity.getBody() == 'hi' or messageProtocolEntity.getBody() == 'help':
                req = urllib2.Request('http://localhost:8989/help')
                response = urllib2.urlopen(req)
                output = response.read()

                outgoingMessageProtocolEntity = TextMessageProtocolEntity(
                    output,
                    to = messageProtocolEntity.getFrom())

                self.toLower(receipt)
                self.toLower(outgoingMessageProtocolEntity)

            else:
                post_params = {'caller'    :messageProtocolEntity.getFrom(),
                               'messageid' : messageProtocolEntity.getId(),
                               'message'   : messageProtocolEntity.getBody().lower()}
                params = urllib.urlencode(post_params)
                print post_params

                req = urllib2.Request('http://localhost:8989/chat')
                req.add_header('Content-Type', 'application/json')
                response = urllib2.urlopen(req, json.dumps(post_params))
                self.toLower(receipt)


    @ProtocolEntityCallback("receipt")
    def onReceipt(self, entity):
        ack = OutgoingAckProtocolEntity(entity.getId(), "receipt", entity.getType(), entity.getFrom())
        self.toLower(ack)
