# Book Website (Upload & Download)

Simple full-stack sample to upload and download book files.

## Stack
- **Backend**: Spring Boot 3 (Java 17), JPA, MySQL
- **Frontend**: Node.js + Express (serves static site, proxies to backend)
- **DB**: MySQL 8
- **CI/CD**: Jenkins with per-module Jenkinsfiles + root pipeline
- **Container/K8s**: Dockerfiles per module, manifests in `k8s/`

## Local Dev (Docker Compose-like quickstart)
1. Build backend jar:
   ```bash
   cd backend && mvn -DskipTests clean package
   ```
2. Run backend:
   ```bash
   docker build -t book-website-backend:dev backend
   docker run --rm -p 8080:8080 -v $(pwd)/uploads:/uploads --name backend      --env SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/bookdb?useSSL=false&allowPublicKeyRetrieval=true"      --env SPRING_DATASOURCE_USERNAME=bookuser --env SPRING_DATASOURCE_PASSWORD=bookpass      book-website-backend:dev
   ```
3. Run frontend:
   ```bash
   cd frontend && npm install && npm start
   ```
   Visit http://localhost:3000

## Kubernetes
Apply manifests (requires a cluster and Ingress controller):
```bash
kubectl apply -f k8s/mysql-configmap.yaml
kubectl apply -f k8s/mysql-deployment.yaml
kubectl apply -f k8s/backend-deployment.yaml
kubectl apply -f k8s/frontend-deployment.yaml
kubectl apply -f k8s/ingress.yaml
```
Add `book.local` to `/etc/hosts` pointing to your ingress IP.

## Jenkins
- Per-module Jenkinsfiles: `backend/Jenkinsfile`, `frontend/Jenkinsfile`
- Root pipeline: `jenkins/Jenkinsfile`
- Configure tool `Maven3` and a Docker environment on the Jenkins agent.
- Optional: set up credentials `docker-registry-creds` for pushing images.

## API
- `GET /api/books` — list books
- `POST /api/books/upload` (multipart: title, author, file) — upload
- `GET /api/books/{id}/download` — download file

> Uploaded files are stored at `/uploads` (volume).

---

Generated on 2025-08-17T10:35:08.144264Z
