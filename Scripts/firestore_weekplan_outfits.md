# Estructura de Firestore: weekPlan y outfits

## Colección `users/{uid}/outfits`
Un documento por outfit. El id del documento es el `outfitId`.

```json
{
  "garmentIds": ["3f2b9c1e-...", "a81d44f0-...", "c5e7120a-..."],
  "paletteHex": ["#F5F5F5", "#2F4F7F", "#1A1A1A"],
  "occasion": "CASUAL",
  "createdAt": 1795219679946
}
```

## Colección `users/{uid}/weekPlan`
Un documento por día. El id del documento es la fecha (`2026-11-23`).

```json
{
  "date": "2026-11-23",
  "occasion": "CASUAL",
  "outfitId": "1795219679946"
}
```

## Notas
- `uid` es el id del usuario de Firebase Auth (el mismo que usa `users/{uid}/garments`).
- `garmentIds` son los `remoteId` de las prendas (`GarmentEntity.remoteId`), no el `id` de Room: ese número cambia de un celular a otro.
- La fecha ISO (`yyyy-MM-dd`) permite filtrar la semana con un rango sobre el campo `date`.
- Los nombres de campo coinciden con las clases `Outfit` y `DayPlan`.