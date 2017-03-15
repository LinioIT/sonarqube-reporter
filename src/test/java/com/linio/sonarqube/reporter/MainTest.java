package com.linio.sonarqube.reporter;

import com.linio.sonarqube.reporter.datasource.DataSource;
import com.linio.sonarqube.reporter.entity.Project;
import com.linio.sonarqube.reporter.output.Output;
import org.junit.Test;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class MainTest
{
    @Test
    public void isRunning() throws IOException, SQLException, URISyntaxException
    {
        ArrayList<Project> projects = new ArrayList<>();

        Main.logger = mock(Logger.class);

        DataSource dataSource = mock(DataSource.class);
        when(dataSource.getProjectIssues()).thenReturn(projects);

        Output output = mock(Output.class);

        Main main = new Main(dataSource, output);
        main.run();

        verify(dataSource).getProjectIssues();
        verify(output).renderProjects(projects);
    }
}
