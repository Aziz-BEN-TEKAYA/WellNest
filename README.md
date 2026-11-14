# WellNest— Architecture Microservices

Equipe de **6 membres** — **6 microservices metier** (Spring Boot, 1 par membre) + **1 module commun : WellNest AI Engine** (Python , voir [Backend/MS7/README.md](Backend/MS7/README.md)).

## Organisation du depot

```
.
├── Backend/          # microservices
│   ├── MS1 ... MS6   # Spring Boot, une base dediee chacun (MySQL ou PostgreSQL)
│   └── MS7           # WellNest AI Engine (Python ou Node.js)
├── Frontend/         # application Angular
└── Documentation/
    ├── Presentation/ # presentation de la seance 4
    └── Diagrams/     # architecture globale et diagrammes du projet
```

> L'infrastructure (Eureka Server, API Gateway, Keycloak) sera ajoutée dans une étape ultérieure.

| Module        | Port | Base de donnees      | Responsable    |
|---------------|------|----------------------|----------------|
| MS1           | 8081 | ms1_db (MySQL)       | Fatma Boussaada       |
| MS2           | 8082 | ms2_db (PostgreSQL)  | Aziz ben Tekaya       |
| MS3           | 8083 | ms3_db (MySQL)       | Jihen Sliti       |
| MS4           | 8084 | ms4_db (PostgreSQL)       | Mohamed Aziz garwachi      |
| MS5           | 8085 | ms5_db (MySQL)       | Hiba Ben Youssef       |
| MS6           | 8086 | ms6_db (PostgreSQL)       | Molka Jmal      |
| MS7 AI Engine | 8087 | -          | collectif |



## Structure d'un microservice Spring Boot (MS1 a MS6)

```
tn.esprit.msX
├── client       # appels vers les autres MS (ex: le MS commun)
├── config       # SecurityConfig, OpenAPI, etc.
├── controller   # REST controllers
├── dto          # objets d'echange (ne jamais exposer les entites)
├── entity       # entites JPA
├── exception    # exceptions + @RestControllerAdvice
├── mapper       # conversions entity <-> dto
├── repository   # Spring Data JPA
└── service      # logique metier
```

## Demarrage

0. **IntelliJ** : ouvrir le dossier racine du depot. Le `pom.xml` racine (agregateur) importe
   automatiquement les 6 microservices Spring Boot (MS1 a MS6), sans « Add as Maven Project ».
   Depuis la racine, `mvn clean install` compile et teste tous les services.
1. MySQL sur `localhost:3306` (user `root`, mot de passe vide — a adapter dans `application.properties`).
   Pour MS2 : PostgreSQL sur `localhost:5432` (user `postgres` / `postgres`), base a creer une fois : `CREATE DATABASE ms2_db;`
   Chaque base `msX_db` est creee automatiquement.
2. Lancer le microservice :

```bash
cd Backend/MS1 && ./mvnw spring-boot:run
```

Swagger : `http://localhost:808X/swagger-ui.html`

## Tests

Les tests utilisent une base **H2 en memoire** (`src/test/resources/application.properties`),
donc `./mvnw test` fonctionne sans MySQL.

## Convention Git

- `master` : branche stable, pas de push direct
- une branche par membre / fonctionnalite : `feature/ms3-gestion-xxx`
- Pull Request + relecture par un autre membre avant merge
- Chacun ne modifie **que son dossier `Backend/MSx`** ; les changements sur le module commun (`Backend/MS7`, AI Engine) et sur `Frontend/` se font en concertation.
- Le contrat d'echange avec l'AI Engine (format commun) est defini dans `Backend/MS7/README.md`.
