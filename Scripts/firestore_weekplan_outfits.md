# Estructura de Firestore: weekPlan y outfits

## Colección `users/{uid}/outfits`
Un documento por outfit. El id del documento es el `outfitId`.

```json
{
  "garmentIds": [1, 2, 3],
  "paletteHex": ["#F5F5F5", "#2F4F7F", "#1A1A1A"],
  "occasion": "CASUAL",
  "createdAt": "<timestamp>"
}
```

## Colección `users/{uid}/weekPlan`
Un documento por día. El id del documento es la fecha (`2026-10-05`).

```json
{
  "date": "2026-10-05",
  "occasion": "CASUAL",
  "outfitId": "abc123"
}
```

## Notas
- `uid` es el id del usuario autenticado (se define con la estructura de users de Luisa).
- `garmentIds` son los ids numéricos de las prendas guardadas en Room (`GarmentEntity.id`, de Alejandro).
- Los nombres de campo coinciden con las clases `Outfit` y `DayPlan`.