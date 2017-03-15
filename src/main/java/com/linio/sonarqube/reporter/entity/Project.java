package com.linio.sonarqube.reporter.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class Project
{
    private String name;
    private String uuid;
    private ArrayList<File> files = new ArrayList<>();
    private HashMap<String, Integer> issueCount = new HashMap<>();

    public Project()
    {
        issueCount.put("BLOCKER", 0);
        issueCount.put("CRITICAL", 0);
        issueCount.put("MAJOR", 0);
        issueCount.put("MINOR", 0);
        issueCount.put("INFO", 0);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public ArrayList<File> getFiles()
    {
        return files;
    }

    public void setFiles(ArrayList<File> files)
    {
        this.files = files;
    }

    public HashMap<String, Integer> getIssueCount()
    {
        return issueCount;
    }

    public void setIssueCount(HashMap<String, Integer> issueCount)
    {
        this.issueCount = issueCount;
    }
}
