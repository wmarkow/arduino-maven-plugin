package com.github.wmarkow.amp.arduino.platform;

import java.util.ArrayList;
import java.util.List;

public class Tool
{
    private String name;
    private String version;
    private List< System > systems = new ArrayList< System >();

    public String getName()
    {
        return name;
    }

    public String getVersion()
    {
        return version;
    }

    public List< System > getSystems()
    {
        return systems;
    }

}
