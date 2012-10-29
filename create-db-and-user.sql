DROP DATABASE IF EXISTS crowdsourcing;
DROP USER IF EXISTS crowdsourcing;
CREATE USER crowdsourcing WITH PASSWORD 'crowdsourcing';
CREATE DATABASE crowdsourcing;
GRANT ALL PRIVILEGES ON DATABASE crowdsourcing TO crowdsourcing;