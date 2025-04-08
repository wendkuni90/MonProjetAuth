# 🔐 Authenticator App - Android x Django JWT Auth

Cette application Android permet aux utilisateurs de se connecter à un backend Django sécurisé avec des JWT (JSON Web Tokens).  
La connexion, la déconnexion (blacklist de tokens), et la récupération de profil sont entièrement fonctionnelles.

---

## ⚙️ Technologies utilisées

- 🧠 Backend : Django + Django REST Framework + `djangorestframework-simplejwt`
- 📱 Frontend : Android Studio (Java), Retrofit2, Material UI
- 🔐 Authentification : JWT (access + refresh), blacklisting des tokens
- 🛠️ Persistance locale : SharedPreferences

---

## 🛠️ Configuration backend Django

### 1. Installer les paquets nécessaires :

```bash
pip install djangorestframework djangorestframework-simplejwt
```

### 2. Configuration `settings.py` :

```python
REST_FRAMEWORK = {
    'DEFAULT_AUTHENTICATION_CLASSES': (
        'rest_framework_simplejwt.authentication.JWTAuthentication',
    ),
}

SIMPLE_JWT = {
    'AUTH_HEADER_TYPES': ('Bearer',),
    'BLACKLIST_AFTER_ROTATION': True,
    'ROTATE_REFRESH_TOKENS': True,
}

INSTALLED_APPS += [
    'rest_framework',
    'rest_framework_simplejwt.token_blacklist',
]
```

### 3. `urls.py` :

```python
from rest_framework_simplejwt.views import TokenObtainPairView, TokenRefreshView

urlpatterns = [
    path('api/login/', TokenObtainPairView.as_view(), name='token_obtain_pair'),
    path('api/refresh/', TokenRefreshView.as_view(), name='token_refresh'),
    path('api/logout/', LogoutView.as_view(), name='token_blacklist'),
    path('api/profile/', UserProfileView.as_view(), name='user_profile'),
]
```

---

## 🤖 Configuration Android

### 1. ⚠️ Important : Activer les requêtes HTTP claires vers le serveur Django en local

> **Android ne permet pas les requêtes HTTP non sécurisées par défaut.**

Ajoutez le fichier suivant dans `res/xml/network_security_config.xml` :

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <domain includeSubdomains="true">192.168.1.100</domain>
    </domain-config>
</network-security-config>
```

Remplacez **`192.168.1.100`** par l’**adresse IP de votre serveur Django local (Adresse de votre machine)** (même réseau Wi-Fi).

### 2. Ajoutez dans le `AndroidManifest.xml` :

```xml
<application
    android:networkSecurityConfig="@xml/network_security_config"
    ... >
```

### 3. Ajoutez aussi la permission internet :

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

---

## 🔄 Exemple de séquence complète (Frontend)

- Connexion → Token access + refresh stockés en `SharedPreferences`
- Appels sécurisés → Token `Bearer` ajouté dans les headers Retrofit
- Déconnexion → Envoi du `refresh token` pour blacklister côté Django
- Les tokens expirés renvoient 401 → possibilité de rafraîchir automatiquement

---

## ✅ Fonctionnalités à tester

- [x] Connexion utilisateur avec username + password
- [x] Récupération du profil avec token
- [x] Déconnexion avec blacklist
- [x] Affichage dynamique avec Retrofit
- [x] Sécurité correcte en local avec les IP autorisées

---

## 🌐 Exemple d’IP Django locale

Tu peux obtenir l’IP de ton PC avec :

```bash
ipconfig    # (Windows)
ifconfig    # (Linux/macOS)
```

Et dans Android, pense à remplacer **localhost** par **192.168.x.x** dans tes URLs API dans Retrofit !

---

## 🚀 Bonus : structure des fichiers Android (extraits)

```
com.elieltech.authenticator
├── activities
│   ├── ConnexionActivity.java
│   └── MainActivity.java
├── api
│   ├── ApiService.java
│   └── RetrofitClient.java
├── models
│   ├── LoginResponse.java
│   ├── LogoutRequest.java
│   └── UserProfileResponse.java
├── utils
│   └── SharedPrefManager.java
```

---

## 📌 À venir / idées d'amélioration

- [ ] Renouvellement automatique du token si expiré
- [ ] Page d’inscription
- [ ] Chiffrement local des tokens
- [ ] Dark mode
- [ ] Utilisation de Biometric Auth pour se connecter

---

## 👨‍💻 Auteur

Développé avec ❤️ par ELiel NIKIEMA @ElielTech
elielnikiema646@gmail.com
elielnikiema16@gmail.com

---