package com.linio.sonarqube.reporter.entity;

import com.linio.sonarqube.reporter.protobuf.DbFileSources;

public class Issue
{
    private String severity;
    private String message;
    private int line;
    private int effort;

    public String getSeverity()
    {
        return severity;
    }

    public void setSeverity(String severity)
    {
        this.severity = severity;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public int getLine()
    {
        return line;
    }

    public void setLine(int line)
    {
        this.line = line;
    }

    public int getEffort()
    {
        return effort;
    }

    public void setEffort(int effort)
    {
        this.effort = effort;
    }
}
