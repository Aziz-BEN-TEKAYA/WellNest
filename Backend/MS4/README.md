# MS4 — Sleep & Mental Wellness

Suivi du sommeil, de l'humeur, du stress et des activités de détente des retraités.
**Toutes les données sont saisies manuellement par le retraité** (pas de montre connectée ni de capteur).

## Base de données (PostgreSQL)

```sql
CREATE DATABASE ms4_db;   -- utilisateur postgres / postgres, port 5432
```

Les tables sont créées automatiquement par Hibernate (`ddl-auto=update`) :

| Table | Contenu |
|---|---|
| `sleep_records` | Une nuit par utilisateur et par date de réveil (contrainte unique `user_id, sleep_date`) |
| `mood_entries` | Humeur déclarée (`VERY_GOOD`, `GOOD`, `NEUTRAL`, `NOT_GREAT`, `BAD`) + score 1–5 calculé |
| `stress_entries` | Niveau de stress 1–5, cause, notes |
| `wellness_activities` | Séances de détente (`BREATHING`, `MEDITATION`, `RELAXATION`, `STRETCHING`, `MUSIC`) |
| `wellness_reminders` | Rappels quotidiens de saisie (`SLEEP_LOG`, `MOOD_LOG`, `STRESS_LOG`, `RELAXATION`) |

`user_id` est un identifiant externe : aucune clé étrangère vers les autres microservices.

## Lancer

```bash
cd Backend/eureka-server && ./mvnw spring-boot:run  # 1. serveur de découverte (port 8761)
cd Backend/MS4 && ./mvnw spring-boot:run            # 2. MS4 (Java 17 minimum)
```

Swagger : http://localhost:8084/swagger-ui.html

## Eureka (découverte de services)

MS4 est un client Eureka : au démarrage, il s'enregistre sous le nom **MS4** auprès de
http://localhost:8761/eureka, puis envoie un heartbeat toutes les 30 s.

- `eureka.instance.prefer-ip-address=true` : MS4 s'annonce avec son adresse IP.
- `eureka.client.healthcheck.enabled=true` : le statut publié dans Eureka est celui de
  `/actuator/health`. Si PostgreSQL tombe, l'instance passe **DOWN** et n'est plus proposée.
- L'URL du serveur peut être changée avec la variable d'environnement `EUREKA_URL`.

Deuxième instance (montée en charge) :

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8094
```

Dans IntelliJ : *Run → Edit Configurations → Copy Configuration*, puis VM options `-Dserver.port=8094`.

## Actuator (observabilité)

| Endpoint | Contenu |
|---|---|
| `/actuator/health` | Statut global + détails (base `db`, disque, `discoveryComposite`) |
| `/actuator/info` | Nom, description et version du service |
| `/actuator/metrics` | Métriques Micrometer (JVM, requêtes HTTP...) |

## API — base `/api/v1/sleep-wellness`

En attendant Keycloak, l'utilisateur est identifié par l'en-tête **`X-User-Id`** (obligatoire).

| Ressource | Endpoints |
|---|---|
| `/sleep-records` | `POST`, `GET` (liste), `GET /{id}`, `PUT /{id}`, `DELETE /{id}` |
| `/mood-entries` | idem |
| `/stress-entries` | idem |
| `/wellness-activities` | idem |
| `/wellness-reminders` | idem (liste sans pagination) |

Les listes acceptent `from`, `to` (dates `AAAA-MM-JJ`), `page` et `size` (max 100).

Exemple :

```http
POST /api/v1/sleep-wellness/sleep-records
X-User-Id: 1
Content-Type: application/json

{ "bedtime": "2026-11-12T22:30:00", "wakeupTime": "2026-11-13T06:30:00",
  "sleepQuality": 4, "nightWakings": 1, "notes": "Réveil en forme" }
```

→ `201 Created` avec `durationMinutes: 480` et `sleepDate: "2026-11-13"`.

## Règles de saisie manuelle

- La durée du sommeil est **calculée par le backend**, jamais saisie.
- Le réveil doit être après le coucher, pas dans le futur, et la nuit ne dépasse pas 16 h.
- Une seule nuit par date : une deuxième saisie renvoie `409 Conflict` (il faut modifier la nuit existante).
- Qualité du sommeil et stress : de 1 à 5. Le score d'humeur est dérivé du niveau choisi.
- Sans date fournie, l'humeur, le stress et l'activité sont datés de « maintenant » ; une date future est refusée.
- Un utilisateur ne voit et ne modifie que ses propres données (sinon `404`).
- Les erreurs sont renvoyées au format `ProblemDetail`, avec le détail par champ dans `errors`.

## Tests

```bash
./mvnw test     # base H2 en mémoire, PostgreSQL non nécessaire
```
