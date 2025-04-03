from django.contrib import admin
from django.contrib.auth.admin import UserAdmin
from .models import CustomUser

# Register your models here.
class CustomUserAdmin(UserAdmin):
    list_display = ('email', 'username', 'is_staff')
    list_filter = ('is_staff',)
    search_fields = ('email', 'username',)
    ordering = ('email',)
    filter_horizontal = ('groups', 'user_permissions',)

    #Configuration des champs non modifiables
    readonly_fields = ('last_login', 'email', 'username',  'password')

    # Configuration des champs affichés lors de la modification d'un utilisateur
    fieldsets = (
        (None, {'fields': ('email', 'username', 'password')}),
        ('Informations personnelles', {'fields': ('is_staff', 'is_superuser', 'groups', 'user_permissions')}),
        ('Dates importantes', {'fields': ('last_login',)}),
    )

    # Configuration des champs affichés lors de la création d'un nouvel utilisateur
    add_fieldsets = (
        (None, {
            'classes': ('wide',),
            'fields': ('email', 'username', 'password1', 'password2'),
        }),
    )

# Enregistrement du modèle CustomUser dans l'interface d'administration
admin.site.register(CustomUser, CustomUserAdmin)