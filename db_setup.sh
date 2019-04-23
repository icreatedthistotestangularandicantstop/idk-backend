cat sql/*.sql > sql/all.sql

mysql -u root -p < sql/all.sql

rm sql/all.sql
