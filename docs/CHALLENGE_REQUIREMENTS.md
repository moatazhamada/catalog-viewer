# Catalog Viewer Challenge Requirements

## Challenge Overview

Create a single-screen list with a detail view for items from a small JSON catalog. Include search and the ability to favorite items.

## Starter Data

```json
{
  "updatedAt": "2025-01-15T10:00:00Z",
  "items": [
    { "id": "bk_001", "title": "The Blue Fox", "category": "Fiction", "price": 12.99, "rating": 4.4 },
    { "id": "bk_002", "title": "Data Sketches", "category": "Non-Fiction", "price": 32.00, "rating": 4.8 },
    { "id": "bk_003", "title": "Swift Patterns", "category": "Tech", "price": 24.50, "rating": 4.1 },
    { "id": "bk_004", "title": "Kotlin by Example", "category": "Tech", "price": 21.00, "rating": 4.3 },
    { "id": "bk_005", "title": "Windswept", "category": "Fiction", "price": 14.25, "rating": 3.9 }
  ]
}
```

## Functional Requirements

- **List**: Render all items with title, category, price, rating.
- **Search**: Filter by title as the user types (case-sensitive).
- **Detail**: Tapping an item shows a detail view with the full record.
- **Favorites**: Toggle favorite; favorites persist across app restarts (any local persistence is fine).
- **Empty/Error states**: Show friendly UI if the list is empty or if the JSON fails to load.
- **README**: How to run + 2–3 paragraphs on design choices/trade-offs.
- **Deliverable**: Should be runnable in ≤2 commands and include at least one unit test

## Non-requirements

- No real network or auth required; they can bundle the JSON or serve it locally.
- No design system or fancy visuals required

