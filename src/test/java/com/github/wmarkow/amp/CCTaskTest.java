package com.github.wmarkow.amp;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.Project;
import org.junit.Test;

import com.github.maven_nar.cpptasks.CCTask;

public class CCTaskTest
{
    @Test
    public void testCompileCpp() throws MojoFailureException, MojoExecutionException
    {
        Project project = new Project();
        project.addBuildListener( new BuildConsoleListener() );

        ArduinoCppCompilerDef cppDef = new ArduinoCppCompilerDef( project );

        cppDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ).getAbsolutePath() );
        cppDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ).getAbsolutePath() );
        cppDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ).getAbsolutePath() );

        cppDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ).getAbsolutePath() );
        cppDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ).getAbsolutePath() );
        cppDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ).getAbsolutePath() );

        CCTask task = new CCTask();
        task.setProject( project );
        task.addConfiguredCompiler( cppDef );
        // object directory
        File objDir = new File( new File( "" ).getAbsolutePath(), "target/obj" );
        objDir.mkdirs();
        task.setObjdir( objDir );

        task.setOutfile( new File( new File( "" ).getAbsolutePath(), "target/output.elf" ) );

        ArduinoLinkerDef linkerDef = new ArduinoLinkerDef();
        task.addConfiguredLinker( linkerDef );

        task.execute();
    }
}
