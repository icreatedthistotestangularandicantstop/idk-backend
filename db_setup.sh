cat /usr/sql/*.sql > /usr/sql/all.sql

mysql --login-path=local -u $MYSQL_USER -p$MYSQL_ROOT_PASSWORD -h 0.0.0.0 $MYSQL_DATABASE < /usr/sql/all.sql

rm /usr/sql/all.sql
