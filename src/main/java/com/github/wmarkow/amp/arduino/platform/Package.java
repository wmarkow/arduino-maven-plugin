package com.github.wmarkow.amp.arduino.platform;

import java.util.ArrayList;
import java.util.List;

public class Package
{
    private String name;
    private String maintainer;
    private List< Platform > platforms = new ArrayList< Platform >();

    public String getName()
    {
        return name;
    }

    public String getMaintainer()
    {
        return maintainer;
    }

    public List< Platform > getPlatforms()
    {
        return platforms;
    }

    @Override
    public String toString()
    {
        return "Package [name=" + name + ", maintainer=" + maintainer + "]";
    }

}


