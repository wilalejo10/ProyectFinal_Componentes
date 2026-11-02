# Firestore: estructura de `users` y reglas de seguridad

## Documento `users/{uid}`
`uid` = id del usuario de Firebase Auth. Un documento por usuario.
**Nunca se guarda la contraseña**: de eso se encarga Firebase Auth.

```json
{
  "displayName": "Luisa",
  "email": "luisa@correo.com",
  "createdAt": "<timestamp>",
  "settings": {
    "notificationsEnabled": true,
    "reminderHour": 8,
    "reminderMinute": 0
  }
}
```

Subcolecciones del usuario (definidas en `firestore_weekplan_outfits.md`):
- `users/{uid}/outfits/{outfitId}`
- `users/{uid}/weekPlan/{yyyy-MM-dd}`

Los campos `settings` coinciden con `SessionManager` (`notificationsEnabled`, `reminderHour`, `reminderMinute`).

## Reglas de seguridad (pegar en Firebase Console > Firestore > Rules)