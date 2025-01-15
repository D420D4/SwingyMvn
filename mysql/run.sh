#!/bin/bash

COMMAND=$1

# Nom du conteneur MySQL
CONTAINER_NAME="mysql_container"

if [ ! -f .env ]; then
  echo "Error: .env file not found. Please create it and define the necessary variables."
  exit 1
fi

# Charger les variables d'environnement depuis .env
export $(grep -v '^#' .env | xargs)

case $COMMAND in
  start)
    echo "Starting MySQL with Docker..."
    docker-compose up -d
    ;;
  stop)
    echo "Stopping MySQL with Docker..."
    docker-compose down
    ;;
  status)
    echo "Checking MySQL container status..."
    docker ps | grep $CONTAINER_NAME
    ;;
  logs)
    echo "Displaying logs for MySQL..."
    docker logs $CONTAINER_NAME
    ;;
  show-db)
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
      echo "Displaying database content..."
      docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SHOW TABLES;"
    else
      echo "Error: MySQL container is not running."
    fi
    ;;
  show-all)
      if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        echo "Displaying content of all tables in the database..."
        TABLES=$(docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SHOW TABLES;" | awk 'NR>1')
        if [ -z "$TABLES" ]; then
          echo "No tables found in the database."
        else
          for TABLE in $TABLES; do
            echo "Content of table: $TABLE"
            docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SELECT * FROM $TABLE;"
            echo "--------------------------------------------------"
          done
        fi
      else
        echo "Error: MySQL container is not running."
      fi
      ;;
    clear-db)
        if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
          echo "Deleting all tables in the database..."
          TABLES=$(docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SHOW TABLES;" | awk 'NR>1')
          if [ -z "$TABLES" ]; then
            echo "No tables to delete in the database."
          else
            for TABLE in $TABLES; do
              echo "Dropping table: $TABLE"
              docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "DROP TABLE $TABLE;"
            done
            echo "All tables have been deleted."
          fi
        else
          echo "Error: MySQL container is not running."
        fi
        ;;
  query)
    if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
      echo "Enter your SQL query (end with a semicolon):"
      read -r SQL_QUERY
      docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "$SQL_QUERY"
    else
      echo "Error: MySQL container is not running."
    fi
    ;;
  *)
    echo "Usage: $0 {start|stop|status|logs|show-db|query}"
    exit 1
    ;;
esac
