package com.linio.sonarqube.reporter;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.hubspot.jinjava.Jinjava;
import com.linio.sonarqube.reporter.datasource.DataSource;
import com.linio.sonarqube.reporter.datasource.MySqlDataSource;
import com.linio.sonarqube.reporter.entity.Project;
import com.linio.sonarqube.reporter.output.HtmlOutput;
import com.linio.sonarqube.reporter.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main
{
    @Parameter(names = "--host")
    public static String host = "localhost";

    @Parameter(names = "--port")
    public static int port = 3306;

    @Parameter(names = "--database")
    public static String database = "sonarqube";

    @Parameter(names = "--user")
    public static String user = "root";

    @Parameter(names = "--password")
    public static String password = "";

    @Parameter(names = "--output-dir")
    public static String outputDir = ".";

    @Parameter(names = {"--help", "-h"})
    public static boolean printUsage = false;

    public static Logger logger;
    private DataSource dataSource;
    private Output output;

    public Main(DataSource dataSource, Output output)
    {
        this.dataSource = dataSource;
        this.output = output;
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, URISyntaxException
    {
        logger = LoggerFactory.getLogger("sonarqube-reporter");
        logger.info("Starting SonarQube Reporter");

        logger.info("Connecting to database");
        logger.info("MySQL connection parameters: " + "Host: " + host + ", port: " + port + ", database: " + database + ", username: " + user + ", " + (password.isEmpty() ? "without password." : "with password."));
        Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + String.valueOf(port) + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&user=" + user + "&password=" + password);

        logger.info("Initializing data source adapter");
        DataSource dataSource = new MySqlDataSource(connection, logger);

        logger.info("Initializing output adapter");
        Jinjava jinjava = new Jinjava();
        Output output = new HtmlOutput(outputDir, jinjava);

        Main command = new Main(dataSource, output);
        new JCommander(command, args);

        if (printUsage) {
            printUsage();

            return;
        }

        command.run();

        logger.info("Finished");
    }

    public void run() throws IOException, SQLException, URISyntaxException
    {
        logger.info("Retrieving Projects, files and issues");
        ArrayList<Project> projects = dataSource.getProjectIssues();

        logger.info("Rendering output");
        output.renderProjects(projects);
    }

    private static void printUsage()
    {
        System.out.println("Usage:");
        System.out.println("--host          MySQL host (default: localhost)");
        System.out.println("--port          MySQL port (default: 3306)");
        System.out.println("--database      MySQL database (default: sonarqube)");
        System.out.println("--user          MySQL user (default: root)");
        System.out.println("--password      MySQL password (default: empty)");
        System.out.println("--output-dir    Output directory (default: .)");
    }
}