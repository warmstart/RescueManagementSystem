import json
#import send_sms

from django.http import HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt
from django.utils import timezone

from send_sms import send_sms

from rescueapp.models import *

# Create your views here.
@csrf_exempt
def incomingMessage(request):
    ## This is called by websms when they receive a SMS for us
    try:
        ## Get the JSON data and extract the relevant parts
        data = json.loads(request.body)
        text = data['textMessageContent']
        sender = data['senderAddress']

        print "Received textmessage: " + text + " from " + str(sender)


        if (text.startswith('alarm') or text.startswith('Alarm') or text.startswith('ALARM')):
            ## Alarm message detected -> Bounce alarm to all people
            alarm("Bergwacht " + text)

        elif (text.startswith('j ') or text.startswith('J ') or text.startswith('ja') or text.startswith('Ja') or test.startswith('JA')):
          ## Feedback case YES -> Save Feedback and add person to group
            m = getLatestMission()
            f = feedback(fromUser=people.objects.get(phoneNumber=sender), jn = True, text=text, mission=m)
            f.save()

        elif (text.startswith('n ') or text.startswith('N ') or text.startswith('ne') or text.startswith('Ne') or text.startswith('NE')):
          ## Feedback case NO -> Save Feedback
            m = getLatestMission()
            f = feedback(fromUser=people.objects.get(phoneNumber=sender), jn = false, text=text, mission=m)
            f.save()

        else:
	  ## Add the current time and save the SMS for the group chat
          text = texts(receiveTime=timezone.now(), sender=sender, text=text)
          text.save()
#          send_sms("received", url)


        response_data = {}
        response_data['statusCode'] = 2000;
        response_data['statusMessage'] = "ok"


        return HttpResponse(json.dumps(response_data), content_type="application/json")
    except Exception, e:
        print 'json receiving error'
        print str(e)

    ## TODO: Only return the OK status, if processing was actually ok ;) Not only when no exception is risen
    return HttpResponse("This is an error")

    
def alarm(message):
    ## This function is triggered when a alarm is detected. Alarm can be triggered:
    ## - through the web interface
    ## - through a SMS with the code ALARM at the beginning

    ## Check if there is already a mission
    if missions.objects.filter(disabled=0).count() != 0:
        ##for testing purposes set it to 1
        m = missions.objects.get(disabled=0)
        m.disabled = 1
        m.save()

    ## take message and relay it to all people
    ## create mission
    newmission = missions(startTime = timezone.now(), missionTitle=message);
    newmission.save()

    ## TODO: check that only one mission is active at once!
    all_people = people.objects.all()
    
    for p in all_people:
        send_sms(message, p.phoneNumber)

def index(request):
    ## On call to index, check if there is an active mission

    return render(request, 'editor.html', {})

def alarmWebStart(request):
    return render(request, 'editor.html', {})


def alarmWebCreate(request):
    try:
        missionCreate = { 'title':    request.POST['titel'],
                          'text':     request.POST['comment'],
                          'status':   request.POST['status'] }
    except(KeyError):
        print "KeyError " + str(KeyError)

    alarm("Bergwacht Alarm " + missionCreate['title'] + " - " + missionCreate['text'])
    return HttpResponse("Alarm wurde gesendet")

def viewMission(request):
    all_texts = texts.objects.all()
    for t in all_texts:
        t.receiveTime = t.receiveTime.strftime('%H:%M')
    return render(request, 'viewer.html', {'output_texts': all_texts})

@csrf_exempt
def incomingLocation(request):
    ## This is called by websms when they receive a SMS for us
    try:
        ## Get the JSON data and extract the relevant parts
        data = json.loads(request.body)
        text = data['pos']
        sender = data['msisdn']

        print "Received position: " + text + " from " + str(sender)
	## Lookup the sender by phonenumber to get the relation
        p = people.objects.get(phoneNumber=sender)
	g = gpsdata(fromUser=p, fromMission=getLatestMission(), data=text, timestamp=timezone.now())
	g.save()


    except Exception, e:
        print "Error parsing location"
        print str(e)

def getLatestMission():
    return missions.objects.get(disabled=0)

def getTeam(request):
    m = getLatestMission()
    team = people.objects.filter(feedback__jn=True).filter(feedback__fromMission=m)

    print team
