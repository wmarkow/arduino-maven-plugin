package com.github.wmarkow.amp.compiler;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.IntegrationTest;

@Category( IntegrationTest.class )
public class CCompilerIntegrationTest
{
    @Test
    public void testCompile() throws IOException, InterruptedException
    {
        CCompiler compiler = new CCompiler();

        compiler.setCommand( "avr-gcc" );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-blink-project" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.addIncludeDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler
            .addIncludeDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.setObjDirectory( new File( "target/obj" ) );
        compiler.setCommandExecutionDirectory( new File( "." ) );

        compiler.addCompilerArg( "-c" );
        compiler.addCompilerArg( "-g" );
        compiler.addCompilerArg( "-Os" );
        compiler.addCompilerArg( "-Wall" );
        compiler.addCompilerArg( "-Wextra" );
        compiler.addCompilerArg( "-std=gnu11" );
        compiler.addCompilerArg( "-ffunction-sections" );
        compiler.addCompilerArg( "-fdata-sections" );
        compiler.addCompilerArg( "-flto" );
        compiler.addCompilerArg( "-fno-fat-lto-objects" );
        compiler.addCompilerArg( "-mmcu=atmega328p" );
        compiler.addCompilerArg( "-DF_CPU=16000000L" );
        compiler.addCompilerArg( "-DARDUINO=10609" );
        compiler.addCompilerArg( "-DARDUINO_AVR_UNO" );
        compiler.addCompilerArg( "-DARDUINO_ARCH_AVR" );

        compiler.compile();
    }
}


