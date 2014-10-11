from django.contrib import admin
from rescueapp.models import people, missions, texts, feedback, gpsdata, gpsinfo

# Register your models here.
admin.site.register(people)
admin.site.register(missions)
admin.site.register(texts)
admin.site.register(feedback)
admin.site.register(gpsdata)
admin.site.register(gpsinfo)
