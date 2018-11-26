package com.github.wmarkow.amp.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.BuildConsoleListener;
import com.github.wmarkow.amp.IntegrationTest;

@Category( IntegrationTest.class )
public class ArduinoBuilderIntegrationTest
{
    @Test
    public void testCompileBlink()
    {
        ArduinoBuilder builder = new ArduinoBuilder( "blink", "1.0.0-SNAPSHOT", "avr-standard" );
        builder.addBuildListener( new BuildConsoleListener() );
        builder.setTargetBuildDirectory( new File( new File( "" ).getAbsolutePath(), "target" ) );

        builder.cleanTargetBuildDirectory();

        assertFalse( new File( new File( "" ).getAbsolutePath(),
            "target/blink-1.0.0-SNAPSHOT-avr-standard.elf" ).exists() );

        builder.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ) );
        builder.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        builder.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );

        builder.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ) );
        builder.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        builder.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );

        builder.build();
        
        assertTrue( new File( new File( "" ).getAbsolutePath(),
            "target/blink-1.0.0-SNAPSHOT-avr-standard.elf" ).exists() );
    }
}


