# Vérification backend OBI

## Configuration vérifiée

| Élément | Valeur |
|--------|--------|
| **Context-path** | `/obi/v1/` |
| **Port** | `8086` |
| **Profil par défaut** | `dev` |

## CORS

- Origines autorisées : `http://localhost:*`, `http://127.0.0.1:*`
- Credentials : autorisés (JWT)
- Méthodes : GET, POST, PUT, DELETE, PATCH, OPTIONS

## Endpoints publics (sans JWT)

- `/auth/**` (dont POST `/auth/login`)
- `/utilisateur/user/add-user/**`, `/utilisateur/activation/**`
- `/swagger-ui/**`, `/v3/api-docs/**`
- `/static/**`, `/errors/**`, `/logs/**`

## Test en local

1. Démarrer PostgreSQL (port 5432, base `obi_v1`).
2. Lancer le backend : `mvn spring-boot:run` (depuis `OBI_Backend_V1`).
3. Vérifier : `curl -s -o /dev/null -w "%{http_code}" http://localhost:8086/obi/v1/auth/login` (réponse 405 = méthode GET non autorisée, normal ; POST attendu).
4. Login : `curl -X POST http://localhost:8086/obi/v1/auth/login -H "Content-Type: application/json" -d '{"login":"...","password":"..."}'`

## Frontend

- En dev, le front utilise `apiUrl: 'http://localhost:8086/obi/v1'` (environment.development.ts). Backend à lancer sur le port 8086.
