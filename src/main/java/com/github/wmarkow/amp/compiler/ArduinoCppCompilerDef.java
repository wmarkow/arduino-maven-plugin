package com.github.wmarkow.amp.compiler;

import org.apache.tools.ant.Project;

import com.github.maven_nar.cpptasks.compiler.Processor;
import com.github.maven_nar.cpptasks.types.ConditionalFileSet;

public class ArduinoCppCompilerDef extends ArduinoCompilerDef
{

    public ArduinoCppCompilerDef( Project project )
    {
        super( project );

        addCompilerArg( "-c" );
        addCompilerArg( "-g" );
        addCompilerArg( "-Os" );
        addCompilerArg( "-Wall" );
        addCompilerArg( "-Wextra" );
        addCompilerArg( "-std=gnu++11" );
        addCompilerArg( "-fpermissive" );
        addCompilerArg( "-fno-exceptions" );
        addCompilerArg( "-ffunction-sections" );
        addCompilerArg( "-fdata-sections" );
        addCompilerArg( "-fno-threadsafe-statics" );
        addCompilerArg( "-flto" );
        addCompilerArg( "-mmcu=atmega328p" );
        addCompilerArg( "-DF_CPU=16000000L" );
        addCompilerArg( "-DARDUINO=10609" );
        addCompilerArg( "-DARDUINO_AVR_UNO" );
        addCompilerArg( "-DARDUINO_ARCH_AVR" );
    }

    @Override
    public Processor getProcessor()
    {
        return new ArduinoCppCompiler();
    }

    @Override
    public void addSourceFileDir( String absolutePath )
    {
        final ConditionalFileSet fileSet = createConditionalFileSet( absolutePath );
        fileSet.setIncludes( "*.cpp" );

        addFileset( fileSet );
    }
}
