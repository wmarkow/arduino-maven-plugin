package com.github.wmarkow.amp.eclipse;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.github.wmarkow.amp.util.cmd.CommandExecutor;

public class GccIncludesScanner
{
    private final static String BOGUS_TEST_FILE = "target/GccIncludesScanner.c";

    public File[] getDefaultIncludes( File pathToGccExecutable ) throws IOException, InterruptedException
    {
        File bogusTestFile = new File( BOGUS_TEST_FILE );
        FileUtils.forceMkdirParent( bogusTestFile );
        FileUtils.touch( bogusTestFile );

        CommandExecutor cmd = new CommandExecutor();
        GccIncludesScannerStreamGobbler gccIncludesScannerStreamGobbler =
            new GccIncludesScannerStreamGobbler();
        cmd.setStreamGobbler( gccIncludesScannerStreamGobbler );
        cmd.execute( prepareCommandArgs( pathToGccExecutable ), new File( "." ) );

        return gccIncludesScannerStreamGobbler.getDefaultIncludes();
    }

    private List< String > prepareCommandArgs( File pathToGccExecutable )
    {
        List< String > result = new ArrayList< String >();

        result.add( pathToGccExecutable.getAbsolutePath() );
        result.add( "-E" );
        result.add( "-v" );
        result.add( "-xc" );
        result.add( BOGUS_TEST_FILE );
        return result;
    }
}
