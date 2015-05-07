# Create your views here.
from datetime import timedelta

import dateutil.parser

from django.core import serializers
from django.http.response import JsonResponse
from django.views.decorators.csrf import csrf_exempt
from django.views.generic.base import TemplateView
from ipware.ip import get_real_ip

from ajax_chat.models import Message


class IndexView(TemplateView):
    template_name = 'index.html'


@csrf_exempt
def chat_api(request):
    if request.method == 'POST':
        msg = request.POST.get('new_message')

        ip = get_real_ip(request)
        message_object = Message.objects.create(msg=msg, ip_addr=ip)
        data = serializers.serialize('json', [message_object, ])
        return JsonResponse(data, safe=False)

    time = request.GET['last_update']
    if time != '-1':
        try:
            time = dateutil.parser.parse(time)
        except Exception:
            time = None
    else:
        time = None
    res = []
    if time is not None:
        messages = Message.objects.filter(timestamp__gt=time)
        for message in messages:
            if (message.timestamp - time) > timedelta(seconds=1):
                res.append(message)
        data = serializers.serialize('json', res)
        return JsonResponse(data, safe=False)
    else:
        messages = Message.objects.order_by('timestamp')
        data = serializers.serialize('json', messages)
        return JsonResponse(data, safe=False)
