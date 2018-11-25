package com.github.wmarkow.amp;

import java.io.File;
import java.util.Vector;

import org.apache.tools.ant.Project;

import com.github.maven_nar.cpptasks.CompilerDef;
import com.github.maven_nar.cpptasks.WarningLevelEnum;
import com.github.maven_nar.cpptasks.compiler.Processor;
import com.github.maven_nar.cpptasks.types.CompilerArgument;
import com.github.maven_nar.cpptasks.types.ConditionalFileSet;

public class ArduinoCCompilerDef extends CompilerDef
{
    private final Vector< String > includePaths = new Vector<>();

    public ArduinoCCompilerDef( Project project )
    {
        super();

        setProject( project );
        setCompilerPrefix( "avr-" );

        WarningLevelEnum wle = new WarningLevelEnum();
        wle.setValue( "diagnostic" );
        setWarnings( wle );

        addCompilerArg( "-c" );
        addCompilerArg( "-g" );
        addCompilerArg( "-Os" );
        addCompilerArg( "-Wall" );
        addCompilerArg( "-Wextra" );
        addCompilerArg( "-std=gnu11" );
        addCompilerArg( "-ffunction-sections" );
        addCompilerArg( "-fdata-sections" );
        addCompilerArg( "-flto" );
        addCompilerArg( "-fno-fat-lto-objects" );
        addCompilerArg( "-mmcu=atmega328p" );
        addCompilerArg( "-DF_CPU=16000000L" );
        addCompilerArg( "-DARDUINO=10609" );
        addCompilerArg( "-DARDUINO_AVR_UNO" );
        addCompilerArg( "-DARDUINO_ARCH_AVR" );
    }

    @Override
    public Processor getProcessor()
    {
        return new ArduinoCCompiler();
    }

    public void addIncludePath( String absolutePath )
    {
        includePaths.addElement( absolutePath );
    }

    public void addSourceFileDir( String aboslutePath )
    {
        if( getProject() == null )
        {
            throw new IllegalStateException( "Project must not be null" );
        }

        final ConditionalFileSet fileSet = new ConditionalFileSet();
        fileSet.setProject( getProject() );
        fileSet.setIncludes( "*.c" );
        fileSet.setDir( new File( aboslutePath ) );

        addFileset( fileSet );
    }

    @Override
    public String[] getActiveIncludePaths()
    {
        return includePaths.toArray( new String[]
        {} );
    }

    private void addCompilerArg( String param )
    {
        CompilerArgument cp = new CompilerArgument();
        cp.setValue( param );

        this.addConfiguredCompilerArg( cp );
    }
}
