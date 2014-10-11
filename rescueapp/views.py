import json

from django.http import HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt

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

    response_data = {}
    response_data['statusCode'] = 2000;
    response_data['statusMessage'] = "ok"


    return HttpResponse(json.dumps(response_data), content_type="application/json")
