package com.github.wmarkow.amp.compiler;

import java.io.File;

import org.apache.tools.ant.Project;

import com.github.maven_nar.cpptasks.compiler.Processor;
import com.github.maven_nar.cpptasks.types.ConditionalFileSet;

public class ArduinoCCompilerDef extends ArduinoCompilerDef
{
    public ArduinoCCompilerDef( Project project )
    {
        super( project );

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

    @Override
    public void addSourceFileDir( File absolutePath )
    {
        final ConditionalFileSet fileSet = createConditionalFileSet( absolutePath );
        fileSet.setIncludes( "*.c" );

        addFileset( fileSet );
    }
}
