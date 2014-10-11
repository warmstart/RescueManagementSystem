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

    url(r'^$', hello.views.index, name='index'),
    url(r'^db', hello.views.db, name='db'),
    url(r'^admin/', include(admin.site.urls)),

    url(r'^test', testapp.views.test, name='test'),
    url(r'incomingMessage', rescueapp.views.incomingMessage, name='incomingMessage'),

)
