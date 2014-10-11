import json
import send_sms

from django.http import HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.utils import timezone

from rescueapp.models import texts

# Create your views here.
@csrf_exempt
def incomingMessage(request):
    try:
        data = json.loads(request.body)
        label = data['messageType']
        url = data['senderAddress']
        print label,url
        print 'json received'
    except Exception, e:
        print 'json receiving error'
        print str(e)

    ## TODO: Only return the OK status, if processing was actually ok ;) Not only when no exception is risen

    text = texts(receiveTime=timezone.now(), sender=data['senderAddress'], text=data['textMessageContent'])
    text.save()

    response_data = {}
    response_data['statusCode'] = 2000;
    response_data['statusMessage'] = "ok"


    return HttpResponse(json.dumps(response_data), content_type="application/json")

def sendSMS(request):
    
