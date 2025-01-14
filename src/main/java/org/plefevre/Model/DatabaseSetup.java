package org.plefevre.Model;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DatabaseSetup {

    private static Connection connection;


    public static void initConnection() {
        Dotenv dotenv = Dotenv.load();

        String dbUrl = "jdbc:mysql://" + dotenv.get("DB_HOST") + ":" + dotenv.get("DB_PORT") + "/" + dotenv.get("MYSQL_DATABASE");
        String dbUser = dotenv.get("MYSQL_USER");
        String dbPassword = dotenv.get("MYSQL_PASSWORD");

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            System.out.println("Successfully connected to the database.");
        } catch (SQLException e) {
            System.err.println("Error while connecting to the database: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static boolean tableExists(String tableName) {
        String checkTableQuery = "SHOW TABLES LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(checkTableQuery)) {
            preparedStatement.setString(1, tableName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Error while checking existence of table " + tableName + ": " + e.getMessage());
        }
        return false;
    }


    public static void createHeroTable() {

        if (tableExists("Hero")) return;

        String createHeroTableQuery = """
                    CREATE TABLE IF NOT EXISTS Hero (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        class_name VARCHAR(50) NOT NULL,
                        lvl INT DEFAULT 1,
                        experience INT DEFAULT 0,
                        attack INT DEFAULT 1,
                        defense INT DEFAULT 1,
                        hit_point INT DEFAULT 1,
                        current_weapon INT DEFAULT NULL,
                        current_armor INT DEFAULT NULL,
                        current_helm INT DEFAULT NULL,
                        FOREIGN KEY (current_weapon) REFERENCES Artifact(id),
                        FOREIGN KEY (current_armor) REFERENCES Artifact(id),
                        FOREIGN KEY (current_helm) REFERENCES Artifact(id)
                    );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createHeroTableQuery);
        } catch (SQLException e) {
            System.err.println("Error while creating the Hero table: " + e.getMessage());
            System.exit(1);
        }
    }


    public static void createArtifactTable() {
        if (tableExists("Artifact")) return;

        String createArtifactTableQuery = """
                    CREATE TABLE IF NOT EXISTS Artifact (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        type INT NOT NULL,
                        lvl INT DEFAULT 1,
                        class_destination INT DEFAULT -1,
                        attack INT DEFAULT 0,
                        defense INT DEFAULT 0
                    );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createArtifactTableQuery);
        } catch (SQLException e) {
            System.err.println("Error while creating the Artifact table: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void createHeroInventoryTable() {
        if (tableExists("HeroInventory")) return;

        String createHeroInventoryTableQuery = """
                    CREATE TABLE IF NOT EXISTS HeroInventory (
                        hero_id INT NOT NULL,
                        artifact_id INT NOT NULL,
                        PRIMARY KEY (hero_id, artifact_id),
                        FOREIGN KEY (hero_id) REFERENCES Hero(id),
                        FOREIGN KEY (artifact_id) REFERENCES Artifact(id)
                    );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(createHeroInventoryTableQuery);
        } catch (SQLException e) {
            System.err.println("Error while creating the HeroInventory table: " + e.getMessage());
            System.exit(1);
        }
    }

    public static void createTable() {
        createHeroTable();
        createArtifactTable();
        createHeroInventoryTable();
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.err.println("Error while closing the database connection: " + e.getMessage());
            }
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}