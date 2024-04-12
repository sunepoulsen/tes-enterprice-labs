#!/bin/bash
psql=( psql -v ON_ERROR_STOP=1)
"${psql[@]}"  --username postgres <<-ENDOFSQL

  CREATE ROLE @telTestDataAdminUsername@
    PASSWORD '@telTestDataAdminPassword@'
    NOCREATEDB
    NOCREATEROLE
    NOSUPERUSER
    LOGIN
  ;

  CREATE ROLE @telTestDataApplicationUsername@
    PASSWORD '@telTestDataApplicationPassword@'
    NOCREATEDB
    NOCREATEROLE
    NOSUPERUSER
    LOGIN
  ;


  CREATE DATABASE @telTestDataDatabase@
    OWNER @telTestDataAdminUsername@
    ENCODING 'UTF8'
  ;


  REVOKE ALL PRIVILEGES ON DATABASE @telTestDataDatabase@
    FROM public;

ENDOFSQL

"${psql[@]}" --username @telTestDataAdminUsername@ --dbname @telTestDataDatabase@ <<-ENDOFSQL
  CREATE SCHEMA @telTestDataDatabase@
      AUTHORIZATION @telTestDataAdminUsername@
    ;

    ALTER DATABASE @telTestDataDatabase@
      SET search_path
      TO @telTestDataDatabase@
    ;

    GRANT CONNECT ON DATABASE @telTestDataDatabase@
      TO @telTestDataApplicationUsername@
    ;

    GRANT USAGE ON SCHEMA @telTestDataDatabase@
      TO @telTestDataApplicationUsername@
    ;

    ALTER DEFAULT PRIVILEGES IN SCHEMA @telTestDataDatabase@
      GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO @telTestDataApplicationUsername@
    ;

    ALTER DEFAULT PRIVILEGES IN SCHEMA @telTestDataDatabase@
      GRANT USAGE, SELECT ON SEQUENCES TO @telTestDataApplicationUsername@
    ;

ENDOFSQL
