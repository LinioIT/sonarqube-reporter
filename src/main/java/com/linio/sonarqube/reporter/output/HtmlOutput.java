package com.linio.sonarqube.reporter.output;

import com.hubspot.jinjava.Jinjava;
import com.linio.sonarqube.reporter.entity.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class HtmlOutput implements Output
{
    private String outputDir;
    private Jinjava jinjava;
    private static Logger logger;

    public HtmlOutput(String outputDir, Jinjava jinjava)
    {
        this.outputDir = outputDir;
        this.jinjava = jinjava;

        logger = LoggerFactory.getLogger(HtmlOutput.class);

        logger.info("Output directory: " + outputDir);
    }

    public void renderProjects(ArrayList<Project> projects) throws IOException, URISyntaxException
    {
        for (Project project : projects) {
            HashMap<String, Object> context = new HashMap<>();
            context.put("project", project);

            logger.info("Rendering " + project.getName());
            String renderedTemplate = jinjava.render(loadTemplate("html/project_issues.html"), context);

            writeFile(project.getName(), renderedTemplate);
        }
    }

    private String loadTemplate(String path) throws FileNotFoundException
    {
        ClassLoader classLoader = getClass().getClassLoader();
        Scanner scanner = new Scanner(classLoader.getResourceAsStream(path));
        StringBuilder result = new StringBuilder("");

        while (scanner.hasNextLine()) {
            result.append(scanner.nextLine()).append("\n");
        }

        return result.toString();
    }

    private void writeFile(String name, String renderedTemplate)
    {
        try{
            PrintWriter writer = new PrintWriter(outputDir + "/" + name + ".html", "UTF-8");
            writer.print(renderedTemplate);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
