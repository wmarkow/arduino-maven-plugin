package com.github.wmarkow.amp.compiler;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.Project;

import com.github.maven_nar.cpptasks.CompilerDef;
import com.github.maven_nar.cpptasks.WarningLevelEnum;
import com.github.maven_nar.cpptasks.types.CompilerArgument;
import com.github.maven_nar.cpptasks.types.ConditionalFileSet;

public abstract class ArduinoCompilerDef extends CompilerDef
{
    private final Vector< String > includePaths = new Vector<>();

    public ArduinoCompilerDef( Project project )
    {
        super();
        setProject( project );
        setCompilerPrefix( "avr-" );

        WarningLevelEnum wle = new WarningLevelEnum();
        wle.setValue( "diagnostic" );
        setWarnings( wle );
    }

    public void addIncludePath( String absolutePath )
    {
        includePaths.addElement( absolutePath );
    }

    @Override
    public String[] getActiveIncludePaths()
    {
        return includePaths.toArray( new String[]
        {} );
    }

    public abstract void addSourceFileDir( String absolutePath );

    protected void addCompilerArg( String param )
    {
        CompilerArgument cp = new CompilerArgument();
        cp.setValue( param );

        this.addConfiguredCompilerArg( cp );
    }

    protected ConditionalFileSet createConditionalFileSet( String aboslutePath )
    {
        if( getProject() == null )
        {
            throw new IllegalStateException( "Project must not be null" );
        }

        final ConditionalFileSet fileSet = new ConditionalFileSet();
        fileSet.setProject( getProject() );
        fileSet.setDir( new File( aboslutePath ) );

        return fileSet;
    }
}
