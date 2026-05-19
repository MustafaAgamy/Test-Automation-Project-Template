FROM node:20-alpine
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install --production
COPY server.js ./
COPY public/ public/
EXPOSE 3000
HEALTHCHECK --interval=5s --timeout=5s --retries=12 \
  CMD wget -qO /dev/null http://localhost:3000/ || exit 1
CMD ["node", "server.js"]
