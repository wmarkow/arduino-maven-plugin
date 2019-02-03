package com.github.wmarkow.amp.arduino.platform;

import java.util.List;

public class Library
{
    private String name;
    private String version;
    private String author;
    private List< String > architectures;
    private List< String > types;
    private String repository;
    private String url;
    private String archiveFileName;
    private int size;
    private String checksum;

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public String getAuthor()
    {
        return author;
    }

    public List< String > getArchitectures()
    {
        return architectures;
    }

    public List< String > getTypes()
    {
        return types;
    }

    public String getRepository()
    {
        return repository;
    }

    public String getUrl()
    {
        return url;
    }

    public String getArchiveFileName()
    {
        return archiveFileName;
    }

    public int getSize()
    {
        return size;
    }

    public String getChecksum()
    {
        return checksum;
    }
}
