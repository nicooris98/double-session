FROM node:22-alpine AS build
WORKDIR /app
ENV NG_CLI_ANALYTICS=false
COPY package.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/web-angular/browser /usr/share/nginx/html/nuevo
EXPOSE 80
