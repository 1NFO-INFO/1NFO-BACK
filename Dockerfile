FROM mysql:lts

ENV MYSQL_ROOT_PASSWORD=0000
ENV MYSQL_DATABASE=info
ENV MYSQL_USER=info_back
ENV MYSQL_PASSWORD=0000

EXPOSE 3306

VOLUME /var/lib/mysql

HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD mysqladmin ping -h localhost -u root -p$MYSQL_ROOT_PASSWORD || exit 1

CMD ["mysqld"]
