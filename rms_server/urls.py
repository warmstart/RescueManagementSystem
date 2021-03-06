from django.conf.urls import patterns, include, url

from django.contrib import admin
admin.autodiscover()

import rescueapp.views
import hello.views
import testapp.views

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'gettingstarted.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^$', rescueapp.views.index, name='index'),
    url(r'^db', hello.views.db, name='db'),
    url(r'^admin/', include(admin.site.urls)),

    url(r'^test', testapp.views.test, name='test'),
    url(r'incomingMessage', rescueapp.views.incomingMessage, name='incomingMessage'),
    url(r'incomingLocation', rescueapp.views.incomingLocation, name='incomingLocation'),
    url(r'alarmWebStart', rescueapp.views.alarmWebStart, name='alarmWebStart'),
    url(r'alarmWebCreate', rescueapp.views.alarmWebCreate, name='alarmWebCreate'),
    url(r'viewMission', rescueapp.views.viewMission, name='viewMission'),
    url(r'getTeam', rescueapp.views.getTeam, name='getTeam'),
    url(r'getGPX', rescueapp.views.getGPX, name='getGPX'),
    url(r'getGPX.gpx', rescueapp.views.getGPX, name='getGPX'),

)
