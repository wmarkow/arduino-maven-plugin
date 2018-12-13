package com.github.wmarkow.amp.build.compiler;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.IntegrationTest;
import com.github.wmarkow.amp.build.compiler.CCompiler;

@Category( IntegrationTest.class )
public class CCompilerIntegrationTest
{
    private CCompiler compiler;
    private File objDir = new File( "target/obj" );

    @Before
    public void init() throws IOException
    {
        FileUtils.forceMkdir( objDir );

        compiler = new CCompiler();

        compiler.setCommand( "avr-gcc" );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-blink-project" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.addIncludeDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler
            .addIncludeDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.setObjDirectory( objDir );
        compiler.setCommandExecutionDirectory( new File( "." ) );

        compiler.addCommandArgs( getDefaultCommandArgs() );

        FileUtils.cleanDirectory( objDir );
        assertEquals( 0, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }

    @Test
    public void testCompile() throws IOException, InterruptedException
    {
        compiler.compile();

        assertEquals( 7, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }

    public final static List< String > getDefaultCommandArgs()
    {
        List< String > args = new ArrayList< String >();

        args.add( "-c" );
        args.add( "-g" );
        args.add( "-Os" );
        args.add( "-Wall" );
        args.add( "-Wextra" );
        args.add( "-std=gnu11" );
        args.add( "-ffunction-sections" );
        args.add( "-fdata-sections" );
        args.add( "-flto" );
        args.add( "-fno-fat-lto-objects" );
        args.add( "-mmcu=atmega328p" );
        args.add( "-DF_CPU=16000000L" );
        args.add( "-DARDUINO=10609" );
        args.add( "-DARDUINO_AVR_UNO" );
        args.add( "-DARDUINO_ARCH_AVR" );

        return args;
    }
}