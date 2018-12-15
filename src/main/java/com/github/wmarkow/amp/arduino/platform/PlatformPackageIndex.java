package com.github.wmarkow.amp.arduino.platform;

import java.util.ArrayList;
import java.util.List;

public class PlatformPackageIndex
{
    private List< Package > packages = new ArrayList< Package >();

    public List< Package > getPackages()
    {
        return packages;
    }

    public Package getPackage( String name )
    {
        for( Package _package : packages )
        {
            if( name.equals( _package.getName() ) )
            {
                return _package;
            }
        }

        return null;
    }
}
