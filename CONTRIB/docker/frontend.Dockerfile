# ============================================================================
# This is a Dockerfile for running the frontend application in a container
# 
# To build: (while being in the repo root directory)
# > docker build -t ptracker-frontend -f ./CONTRIB/docker/frontend.Dockerfile ./FRONTEND
# ============================================================================

# Build container
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Run container
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
