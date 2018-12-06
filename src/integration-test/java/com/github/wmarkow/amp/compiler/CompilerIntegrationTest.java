package com.github.wmarkow.amp.compiler;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.IntegrationTest;

@Category( IntegrationTest.class )
public class CompilerIntegrationTest
{

    Compiler compiler;
    File objDir = new File( "target/obj" );

    @Before
    public void init() throws IOException
    {
        compiler = new Compiler();

        compiler.setCppCompilerCommand( "avr-g++" );
        compiler.setCCompilerCommand( "avr-gcc" );
        compiler.setSCompilerCommand( "avr-gcc" );

        compiler.addCppCompilerArgs( CppCompilerIntegrationTest.getDefaultCommandArgs() );
        compiler.addCCompilerArgs( CCompilerIntegrationTest.getDefaultCommandArgs() );
        compiler.addSCompilerArgs( SCompilerIntegrationTest.getDefaultCommandArgs() );

        compiler.addSrcDirectory( new File( "src/test/resources/arduino-blink-project" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.addIncludeDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler
            .addIncludeDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.setObjDirectory( new File( "target/obj" ) );
        compiler.setCommandExecutionDirectory( new File( "." ) );

        FileUtils.cleanDirectory( objDir );
        assertEquals( 0, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }

    @Test
    public void testCompile() throws IOException, InterruptedException
    {
        compiler.compile();

        assertEquals( 26, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }
}
