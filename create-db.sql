SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = 'crowdsourcing';

DROP DATABASE IF EXISTS crowdsourcing;
DROP USER IF EXISTS crowdsourcing;
CREATE USER crowdsourcing WITH PASSWORD 'crowdsourcing';
CREATE DATABASE crowdsourcing;
GRANT ALL PRIVILEGES ON DATABASE crowdsourcing TO crowdsourcing;