package com.linio.sonarqube.reporter.datasource;


import com.linio.sonarqube.reporter.entity.File;
import com.linio.sonarqube.reporter.entity.Issue;
import com.linio.sonarqube.reporter.entity.Project;
import com.linio.sonarqube.reporter.protobuf.DbFileSources;
import net.jpountz.lz4.LZ4BlockInputStream;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlDataSource implements DataSource
{
    private Connection connection;
    private Logger logger;

    public MySqlDataSource(Connection connection, Logger logger)
    {
        this.connection = connection;
        this.logger = logger;
    }

    public ArrayList<Project> getProjectIssues() throws SQLException, IOException
    {
        logger.info("Retrieving data from database");
        ResultSet resultSet = retrieveIssuesFromDatabase();
        ArrayList<Project> projects = new ArrayList<>();
        Project currentProject = null;
        File currentFile = null;

        logger.info("Hydrating objects");
        while (resultSet.next()) {
            String projectName = resultSet.getString("projectName");
            String filePath = resultSet.getString("filePath");

            if (currentProject == null || !currentProject.getName().equalsIgnoreCase(projectName)) {
                if (currentProject != null) {
                    projects.add(currentProject);
                }

                currentProject = new Project();
                currentProject.setName(projectName);
                currentProject.setUuid(resultSet.getString("projectUuid"));
            }

            if (currentFile == null || !currentFile.getPath().equalsIgnoreCase(filePath)) {
                if (currentFile != null) {
                    currentProject.getFiles().add(currentFile);
                }

                currentFile = new File();
                currentFile.setPath(filePath);

                byte[] binaryData = resultSet.getBytes("fileContents");

                if (binaryData.length > 0) {
                    LZ4BlockInputStream lz4Input = new LZ4BlockInputStream(new ByteArrayInputStream(binaryData));
                    currentFile.setContent(DbFileSources.Data.parseFrom(lz4Input));
                }
            }

            Issue issue = new Issue();
            issue.setSeverity(resultSet.getString("issueSeverity"));
            issue.setMessage(resultSet.getString("issueMessage"));
            issue.setLine(resultSet.getInt("issueLine"));
            issue.setEffort(resultSet.getInt("issueEffort"));

            currentFile.getIssues().add(issue);

            int issueCount = currentProject.getIssueCount().containsKey(issue.getSeverity()) ? currentProject.getIssueCount().get(issue.getSeverity()) + 1 : 1;
            currentProject.getIssueCount().put(issue.getSeverity(), issueCount);
        }

        if (currentProject != null) {
            currentProject.getFiles().add(currentFile);
        }

        projects.add(currentProject);

        return projects;
    }

    private ResultSet retrieveIssuesFromDatabase() throws SQLException
    {
        return connection.prepareStatement("SELECT " +
                "   p.name as 'projectName', " +
                "   p.uuid as 'projectUuid', " +
                "   i.severity as 'issueSeverity', " +
                "   i.message as 'issueMessage', " +
                "   i.line as 'issueLine', " +
                "   i.effort as 'issueEffort', " +
                "   fs.binary_data as 'fileContents', " +
                "   f.path as 'filePath' " +
                "FROM issues i " +
                "   JOIN projects p ON p.scope = 'PRJ' AND p.project_uuid = i.project_uuid " +
                "   JOIN file_sources fs ON fs.file_uuid = i.component_uuid " +
                "   JOIN projects f ON f.scope = 'FIL' AND f.uuid = fs.file_uuid " +
                "ORDER BY p.name, f.path, i.line"
            )
            .executeQuery();
    }
}
