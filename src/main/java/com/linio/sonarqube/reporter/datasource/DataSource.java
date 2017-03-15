package com.linio.sonarqube.reporter.datasource;

import com.linio.sonarqube.reporter.entity.Project;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DataSource
{
    ArrayList<Project> getProjectIssues() throws SQLException, IOException;
}
