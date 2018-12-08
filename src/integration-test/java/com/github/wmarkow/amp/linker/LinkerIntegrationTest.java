package com.github.wmarkow.amp.linker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.IntegrationTest;
import com.github.wmarkow.amp.compiler.CompilerIntegrationTest;

@Category( IntegrationTest.class )
public class LinkerIntegrationTest
{
    Linker linker;
    File outputElfFile = new File( "target/output.elf" );
    
    @Before
    public void init() throws IOException, InterruptedException
    {
        linker = new Linker();
        
        linker.setCommand( "avr-gcc" );
        linker.setCommandExecutionDirectory( new File( "." ) );
        linker.addCommandArgs( getDefaultCommandArgs() );

        if( outputElfFile.exists() )
        {
            FileUtils.forceDelete( outputElfFile );
        }

        CompilerIntegrationTest compiler = new CompilerIntegrationTest();
        compiler.init();
        compiler.testCompile();
    }

    @Test
    public void testLink() throws IOException, InterruptedException
    {
        assertFalse( outputElfFile.exists() );

        linker.link( new File( "target/obj" ), outputElfFile );

        assertTrue( outputElfFile.exists() );
    }

    public final static List< String > getDefaultCommandArgs()
    {
        List< String > args = new ArrayList< String >();

        args.add( "-Wall" );
        args.add( "-Wextra" );
        args.add( "-Os" );
        args.add( "-g" );
        args.add( "-flto" );
        args.add( "-fuse-linker-plugin" );
        args.add( "-Wl,--gc-sections" );
        args.add( "-mmcu=atmega328p" );

        return args;
    }
}
