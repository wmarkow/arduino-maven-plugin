package com.github.wmarkow.amp.compiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.cmd.CommandExecutor;

public abstract class AbstractCompiler
{
    private Logger logger = LoggerFactory.getLogger( AbstractCompiler.class );

    private String cmd;
    private List< String > cmdArgs = new ArrayList< String >();
    private List< File > srcDirs = new ArrayList< File >();
    private List< File > incDirs = new ArrayList< File >();
    private File objDir;
    private File cmdExeDir;

    public void setCommand( String cmd )
    {
        this.cmd = cmd;
    }

    public void setCommandExecutionDirectory( File dir )
    {
        this.cmdExeDir = dir;
    }

    public void addCompilerArg( String arg )
    {
        cmdArgs.add( arg );
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

    public void compile() throws IOException, InterruptedException
    {
        List< String > baseCmd = prepareBaseCommand();

        for( File srcDir : srcDirs )
        {
            Collection< File > files = FileUtils.listFiles( srcDir, getFilesExtensions(), true );

            for( File file : files )
            {
                logger.info( String.format( "Compiling %s", file ) );

                List< String > cmd = prepareCommand( baseCmd, file );

                CommandExecutor ce = new CommandExecutor();
                int exitCode = ce.execute( cmd, cmdExeDir );
                if( exitCode != 0 )
                {
                    throw new RuntimeException( String.format( "Compiler exited with exit code %s", exitCode ) );
                }
            }
        }
    }

    protected abstract String[] getFilesExtensions();

    private List< String > prepareBaseCommand()
    {
        List< String > result = new ArrayList< String >();

        result.add( cmd );
        result.addAll( cmdArgs );

        for( File incDir : incDirs )
        {
            result.add( "-I" + incDir.getPath() );
        }

        return result;
    }

    private List< String > prepareCommand( List< String > baseCommand, File srcFile )
    {
        List< String > cmd = new ArrayList< String >();
        cmd.addAll( baseCommand );

        cmd.add( "-o" );

        String srcFileName = srcFile.getName();
        File objFile = new File( objDir, srcFileName + ".o" );

        cmd.add( objFile.getPath() );
        cmd.add( srcFile.getPath() );

        return cmd;
    }
}
