import json
#import send_sms

from django.http import HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.utils import timezone

from send_sms import send_sms

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
        text = texts(receiveTime=timezone.now(), sender=data['senderAddress'], text=data['textMessageContent'])
        text.save()
        send_sms("received", url)


        response_data = {}
        response_data['statusCode'] = 2000;
        response_data['statusMessage'] = "ok"


        return HttpResponse(json.dumps(response_data), content_type="application/json")
    except Exception, e:
        print 'json receiving error'
        print str(e)

    ## TODO: Only return the OK status, if processing was actually ok ;) Not only when no exception is risen
    return HttpResponse("This is an error")

def sendSMS(message, nr):
    send_sms.send(message,nr)    
    
def alarm(message):
    ## This function is triggered when a alarm is detected. Alarm can be triggered:
    ## - through the web interface
    ## - through a SMS with the code ALARM at the beginning

    ## take message and relay it to all people
    ## create mission
    newmission = missions(startTime = timezone.now(), missionTitle=message);

    ## TODO: check that only one mission is active at once!
    all_people = people.objects.all()
    
    for p in all_people:
        sendSMS(message, p.phoneNumber)

