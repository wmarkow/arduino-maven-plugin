package com.github.wmarkow.amp.arduino.platform;

public class ToolsDependency
{
    private String packager;
    private String name;
    private String version;

    public String getPackager()
    {
        return packager;
    }

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    @Override
    public String toString()
    {
        return "ToolsDependency [packager=" + packager + ", name=" + name + ", version=" + version + "]";
    }

}
