from django.db import models

# Create your models here.
from django.db.models.fields import TextField, GenericIPAddressField, DateTimeField


class Message(models.Model):
    msg = TextField(max_length=250)
    ip_addr = GenericIPAddressField(blank=True, null=True)
    timestamp = DateTimeField(auto_now_add=True, blank=True, null=True)