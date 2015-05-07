# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('ajax_chat', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='message',
            name='ip_addr',
            field=models.GenericIPAddressField(),
        ),
    ]
