package com.linio.sonarqube.reporter.datasource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.linio.sonarqube.reporter.entity.File;
import com.linio.sonarqube.reporter.entity.Issue;
import com.linio.sonarqube.reporter.entity.Project;
import org.junit.Test;
import org.slf4j.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MySqlDataSourceTest
{
    @Test
    public void isMappingRowsFromDatabase() throws IOException, SQLException
    {
        Logger logger = mock(Logger.class);
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(Boolean.TRUE, Boolean.FALSE);

        when(resultSet.getString("projectName")).thenReturn("project-name");
        when(resultSet.getString("filePath")).thenReturn("file-path");
        when(resultSet.getString("projectUuid")).thenReturn("project-uuid");
        when(resultSet.getBytes("fileContents")).thenReturn("".getBytes());
        when(resultSet.getString("issueSeverity")).thenReturn("issue-severity");
        when(resultSet.getString("issueMessage")).thenReturn("issue-message");
        when(resultSet.getInt("issueLine")).thenReturn(15);
        when(resultSet.getInt("issueEffort")).thenReturn(5);

        DataSource dataSource = new MySqlDataSource(connection, logger);

        ArrayList<Project> actual = dataSource.getProjectIssues();

        assertEquals(1, actual.size());

        Project actualProject = actual.get(0);
        assertEquals(actualProject.getName(), "project-name");
        assertEquals(actualProject.getUuid(), "project-uuid");
        assertTrue(actualProject.getIssueCount().containsKey("issue-severity"));
        assertEquals(actualProject.getIssueCount().get("issue-severity").intValue(), 1);
        assertEquals(1, actualProject.getFiles().size());

        File actualFile = actualProject.getFiles().get(0);
        assertEquals(actualFile.getPath(), "file-path");
        assertEquals(actualFile.getIssues().size(), 1);

        Issue actualIssue = actualFile.getIssues().get(0);
        assertEquals(actualIssue.getMessage(), "issue-message");
        assertEquals(actualIssue.getEffort(), 5);
        assertEquals(actualIssue.getLine(), 15);
        assertEquals(actualIssue.getSeverity(), "issue-severity");
    }
}
