FROM mysql:8.0
ENV MYSQL_DATABASE=bookdb
ENV MYSQL_USER=bookuser
ENV MYSQL_PASSWORD=bookpass
ENV MYSQL_ROOT_PASSWORD=root
COPY init.sql /docker-entrypoint-initdb.d/
EXPOSE 3306
