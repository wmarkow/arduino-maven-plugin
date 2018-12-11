package com.github.wmarkow.amp.platform;

import java.util.ArrayList;
import java.util.List;

public class Platform
{
    private String name;
    private String architecture;
    private String version;
    private String category;
    private String url;
    private String archiveFileName;
    private String checksum;
    private int size;
    private List< Board > boards = new ArrayList< Board >();

    public String getName()
    {
        return name;
    }

    public String getArchitecture()
    {
        return architecture;
    }

    public String getVersion()
    {
        return version;
    }

    public String getCategory()
    {
        return category;
    }

    public String getUrl()
    {
        return url;
    }

    public String getArchiveFileName()
    {
        return archiveFileName;
    }

    public String getChecksum()
    {
        return checksum;
    }

    public int getSize()
    {
        return size;
    }

    public List< Board > getBoards()
    {
        return boards;
    }

}
