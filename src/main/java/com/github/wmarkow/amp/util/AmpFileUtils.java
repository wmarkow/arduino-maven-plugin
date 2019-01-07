package com.github.wmarkow.amp.util;

import java.io.File;

public class AmpFileUtils
{

    public static File stepIntoSingleFolderIfPossible( File file )
    {
        if( file == null )
        {
            throw new IllegalArgumentException( "File must not benull" );
        }

        if( !file.exists() )
        {
            return file;
        }

        if( file.isFile() )
        {
            return file;
        }

        File[] subFiles = file.listFiles();

        if( subFiles.length == 0 )
        {
            return file;
        }

        if( subFiles.length > 1 )
        {
            return file;
        }

        if( subFiles[ 0 ].isDirectory() )
        {
            return new File( file, subFiles[ 0 ].getName() );
        }

        return file;
    }
}
