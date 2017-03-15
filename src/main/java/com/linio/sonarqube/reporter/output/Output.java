package com.linio.sonarqube.reporter.output;

import com.linio.sonarqube.reporter.entity.Project;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public interface Output
{
    void renderProjects(ArrayList<Project> projects) throws IOException, URISyntaxException;
}