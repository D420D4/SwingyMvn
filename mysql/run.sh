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
      docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SHOW TABLES;"  2>/dev/null
    else
      echo "Error: MySQL container is not running."
    fi
    ;;
  show-all)
      if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
        TABLES=$(docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SHOW TABLES;"  2>/dev/null | awk 'NR>1')
        if [ -z "$TABLES" ]; then
          echo "No tables found in the database."
        else
          for TABLE in $TABLES; do
            echo "Content of table: $TABLE"
            docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SELECT * FROM $TABLE;" 2>/dev/null
            echo "--------------------------------------------------"
          done
        fi
      else
        echo "Error: MySQL container is not running."
      fi
      ;;
    clear-db)
        if [ "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
          # Tables to delete in a specific order
                PRIORITY_TABLES=("HeroInventory" "Artifact" "Hero")

                # Delete priority tables first
                for TABLE in "${PRIORITY_TABLES[@]}"; do
                  echo "Dropping table: $TABLE"
                  docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "DROP TABLE IF EXISTS $TABLE;"
                done

                # Delete any remaining tables
                OTHER_TABLES=$(docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "SHOW TABLES;" | awk 'NR>1')
                for TABLE in $OTHER_TABLES; do
                  echo "Dropping table: $TABLE"
                  docker exec -i $CONTAINER_NAME mysql -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -D "$MYSQL_DATABASE" -e "DROP TABLE IF EXISTS $TABLE;"
                done

                echo "All tables have been deleted."
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
    echo "Usage: $0 {start|stop|status|logs|show-db|show-all|clear-db|query}"
    exit 1
    ;;
esac
