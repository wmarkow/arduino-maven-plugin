package com.github.wmarkow.amp.eclipse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.util.cmd.CommandExecutor;

public class GccIncludesScanner
{
    private Logger logger = LoggerFactory.getLogger( GccIncludesScanner.class );

    private final static String BOGUS_TEST_FILE = "target/GccIncludesScanner.c";

    private List< File > cIncludes;
    private List< File > cppIncludes;
    private List< File > asmIncludes;

    public void scan( File pathToGccExecutable )
    {
        logger.info( "Trying to get default includes paths of GCC." );

        try
        {
            cIncludes = scanForDefaultCIncludes( pathToGccExecutable );
        }
        catch( Exception e )
        {
        }
        try
        {
            cppIncludes = scanForDefaultCppIncludes( pathToGccExecutable );
        }
        catch( Exception e )
        {
        }
        try
        {
            asmIncludes = scanForDefaultAsmIncludes( pathToGccExecutable );
        }
        catch( Exception e )
        {
        }
    }

    public List< File > getCIncludes()
    {
        return cIncludes;
    }

    public List< File > getCppIncludes()
    {
        return cppIncludes;
    }

    public List< File > getAsmIncludes()
    {
        return asmIncludes;
    }

    private List< File > scanForDefaultCIncludes( File pathToGccExecutable ) throws IOException,
        InterruptedException
    {
        List< String > cmd = new ArrayList< String >();

        cmd.add( pathToGccExecutable.getAbsolutePath() );
        cmd.add( "-E" );
        cmd.add( "-v" );
        cmd.add( "-xc" );
        cmd.add( BOGUS_TEST_FILE );

        return this.scanForDefaultIncludes( cmd );
    }

    private List< File > scanForDefaultCppIncludes( File pathToGccExecutable ) throws IOException,
        InterruptedException
    {
        List< String > cmd = new ArrayList< String >();

        cmd.add( pathToGccExecutable.getAbsolutePath() );
        cmd.add( "-E" );
        cmd.add( "-v" );
        cmd.add( "-xc++" );
        cmd.add( BOGUS_TEST_FILE );

        return this.scanForDefaultIncludes( cmd );
    }

    private List< File > scanForDefaultAsmIncludes( File pathToGccExecutable ) throws IOException,
        InterruptedException
    {
        List< String > cmd = new ArrayList< String >();

        cmd.add( pathToGccExecutable.getAbsolutePath() );
        cmd.add( "-E" );
        cmd.add( "-v" );
        cmd.add( "-xcassembler" );
        cmd.add( BOGUS_TEST_FILE );

        return this.scanForDefaultIncludes( cmd );
    }

    private List< File > scanForDefaultIncludes( List< String > commandArguments ) throws IOException,
        InterruptedException
    {
        File bogusTestFile = new File( BOGUS_TEST_FILE );
        FileUtils.forceMkdirParent( bogusTestFile );
        FileUtils.touch( bogusTestFile );

        CommandExecutor cmd = new CommandExecutor();
        GccIncludesScannerStreamGobbler gccIncludesScannerStreamGobbler =
            new GccIncludesScannerStreamGobbler();
        cmd.setStreamGobbler( gccIncludesScannerStreamGobbler );
        cmd.execute( commandArguments, new File( "." ) );

        return gccIncludesScannerStreamGobbler.getDefaultIncludes();
    }
}
