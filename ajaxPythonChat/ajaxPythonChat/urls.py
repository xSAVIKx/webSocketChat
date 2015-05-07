from django.conf.urls import include, url
from django.contrib import admin

from ajax_chat.views import IndexView, chat_api


urlpatterns = [
    # Examples:
    # url(r'^$', 'ajaxPythonChat.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^api/$', chat_api, name='chat_api'),
    url(r'^$', IndexView.as_view()),
]
