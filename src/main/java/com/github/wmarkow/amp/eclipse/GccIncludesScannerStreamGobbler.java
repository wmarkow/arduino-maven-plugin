package com.github.wmarkow.amp.eclipse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.util.cmd.StreamGobbler;

public class GccIncludesScannerStreamGobbler extends StreamGobbler
{
    private Logger logger = LoggerFactory.getLogger( GccIncludesScannerStreamGobbler.class );

    private boolean includesStartsHereDetected = false;
    private boolean includesFound = false;

    private ArrayList< File > defaultIncludes = new ArrayList< File >();

    public File[] getDefaultIncludes()
    {
        return defaultIncludes.toArray( new File[]
        {} );
    }

    @Override
    protected void onLineRead( String line )
    {
        if( includesFound )
        {
            return;
        }

        if( line.toLowerCase().matches( ".*include.+search starts here.*" ) )
        {
            includesStartsHereDetected = true;

            return;
        }

        if( includesStartsHereDetected && line.toLowerCase().matches( ".*end of search list.*" ) )
        {
            includesFound = true;

            return;
        }

        if( includesStartsHereDetected && includesFound == false )
        {
            try
            {
                File file = parseIntoFile( line );
                defaultIncludes.add( file );
                logger.info( String.format( " Found GCC inlude path as %s", file.getAbsolutePath() ) );
            }
            catch( IOException e )
            {
                logger.warn( "Exception when parsing GCC include path %s line", e );
            }
        }
    }

    private File parseIntoFile( String filePath ) throws IOException
    {
        File file = new File( filePath.trim() );
        return file.getCanonicalFile();
    }
}
