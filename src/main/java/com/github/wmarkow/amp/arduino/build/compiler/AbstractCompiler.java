package com.github.wmarkow.amp.arduino.build.compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.arduino.build.AbstractProcessor;

public abstract class AbstractCompiler extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( AbstractCompiler.class );

    private List< File > srcDirs = new ArrayList< File >();
    private List< File > incDirs = new ArrayList< File >();
    private File objDir;
    private String toolchainBinDir;
    private String currentArduinoCorePath;
    private CompilerCommandBuilder compilerCommandBuilder;

    public AbstractCompiler( CompilerCommandBuilder compilerCommandBuilder )
    {
        if( compilerCommandBuilder == null )
        {
            throw new IllegalArgumentException( "CompilerCommandBuilder must not be null" );
        }

        this.compilerCommandBuilder = compilerCommandBuilder;
    }

    public void addSrcDirectory( File srcDir )
    {
        srcDirs.add( srcDir );
    }

    public void addIncludeDirectory( File incDir )
    {
        incDirs.add( incDir );
    }

    public void setObjDirectory( File dir )
    {
        this.objDir = dir;
    }

    public void setToolchainBinDir( String file )
    {
        this.toolchainBinDir = file;
    }

    public void setCurrentArduinoCorePath( String file )
    {
        this.currentArduinoCorePath = file;
    }

    public void compile() throws IOException, InterruptedException
    {
        compilerCommandBuilder.setToolchainBinDirPath( toolchainBinDir );
        compilerCommandBuilder.setCurrentArduinoCorePath( currentArduinoCorePath );

        FileUtils.forceMkdir( objDir );

        compilerCommandBuilder.setIncludes( incDirs );

        for( File srcDir : srcDirs )
        {
            Collection< File > files = FileUtils.listFiles( srcDir, getFilesExtensions(), true );

            for( File file : files )
            {
                logger.info( "" );
                logger.info( String.format( "Compiling %s", file ) );

                compilerCommandBuilder.setSourceFile( file );

                String srcFileName = file.getName();
                File objFile = new File( objDir, srcFileName + ".o" );

                compilerCommandBuilder.setObjectFile( objFile );

                final String cmd = compilerCommandBuilder.buildCommand();

                executeCommand( cmd );
            }
        }
    }

    protected abstract String[] getFilesExtensions();
}
