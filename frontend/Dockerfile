FROM node:18-alpine
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm install --production
COPY . .
EXPOSE 3000
ENV BACKEND_URL=http://backend:8080
CMD ["npm","start"]
