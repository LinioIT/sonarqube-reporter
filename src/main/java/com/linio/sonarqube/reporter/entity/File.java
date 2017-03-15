package com.linio.sonarqube.reporter.entity;

import com.linio.sonarqube.reporter.protobuf.DbFileSources;

import java.util.ArrayList;

public class File
{
    private String path;
    private DbFileSources.Data content;
    private ArrayList<Issue> issues = new ArrayList<>();

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public DbFileSources.Data getContent()
    {
        return content;
    }

    public void setContent(DbFileSources.Data content)
    {
        this.content = content;
    }

    public ArrayList<Issue> getIssues()
    {
        return issues;
    }

    public void setIssues(ArrayList<Issue> issues)
    {
        this.issues = issues;
    }
}
