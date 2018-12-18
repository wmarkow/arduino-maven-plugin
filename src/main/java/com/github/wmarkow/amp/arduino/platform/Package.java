package com.github.wmarkow.amp.arduino.platform;

import java.util.ArrayList;
import java.util.List;

/***
 * https://github.com/arduino/arduino/wiki/arduino-ide-1.5-3rd-party-hardware-specification
 */
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

    public Platform getPlatformByVersion( String version )
    {
        for( Platform platform : getPlatforms() )
        {
            if( version.equals( platform.getVersion() ) )
            {
                return platform;
            }
        }

        return null;
    }

    @Override
    public String toString()
    {
        return "Package [name=" + name + ", maintainer=" + maintainer + "]";
    }

}
