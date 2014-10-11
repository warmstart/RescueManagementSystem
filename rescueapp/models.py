from django.db import models

# Create your models here.

## class people: Everyone on the team with name, mobile number and role
class people(models.Model):
    name         = models.CharField(max_length=50)
    phoneNumber  = models.CharField(max_length=20)
    admin        = models.BooleanField(default=0)

## class missions: Every mission with starttime and title
class missions(models.Model):
    startTime    = models.DateTimeField()
    missionTitle = models.CharField(max_length=120)

class texts(models.Model):
    fromMission  = models.ForeignKey(missions)
    receiveTime  = models.DateTimeField()
    fromUser     = models.ForeignKey(people)
    text         = models.CharField(max_length=400)

class feedback(models.Model):
    fromUser     = models.ForeignKey(people)
    jn           = models.BooleanField()
    text         = models:CharField(max_length=400)

class gpsdata(models.Model):
    fromUser     = models.ForeignKey(people)
    fromMission  = models.ForeignKey(missions)
    data         = models.CharField(max_length=200)
    timestamp    = models.DateTimeField()

class gpsinfo(models.Model):
    timestamp    = models.DateTimeField()
    data         = models.CharField(max_length=200)
    fromMission  = models.ForeignKey(missions)
