from django.urls import path
from .views import (
    RegisterView, LoginView, 
    LogoutView, ProtectedView,
    UserProfileView )
from rest_framework_simplejwt.views import TokenRefreshView

urlpatterns = [
    path('register/', RegisterView.as_view(), name='register'),
    path('login/', LoginView.as_view(), name='login'),
    path('me-infos/', UserProfileView.as_view(), name='user-connected'),
    path('logout/', LogoutView.as_view(), name='logout'),
    path('refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('protected/', ProtectedView.as_view(), name='protected'),
]