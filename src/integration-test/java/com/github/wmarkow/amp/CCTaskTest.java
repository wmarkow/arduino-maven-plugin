package com.github.wmarkow.amp;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.tools.ant.Project;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.maven_nar.cpptasks.CCTask;
import com.github.wmarkow.amp.compiler.ArduinoCCompilerDef;
import com.github.wmarkow.amp.compiler.ArduinoCppCompilerDef;
import com.github.wmarkow.amp.linker.ArduinoLinkerDef;

@Category( IntegrationTest.class )
public class CCTaskTest
{
    @Test
    public void testCompileCpp() throws MojoFailureException, MojoExecutionException
    {
        Project project = new Project();
        project.addBuildListener( new BuildConsoleListener() );

        ArduinoCppCompilerDef cppDef = createCppCompilerDef( project );
        ArduinoCCompilerDef cDef = createCCompilerDef( project );

        CCTask task = new CCTask();
        task.setProject( project );
        task.addConfiguredCompiler( cppDef );
        task.addConfiguredCompiler( cDef );
        // object directory
        File objDir = new File( new File( "" ).getAbsolutePath(), "target/obj" );
        objDir.mkdirs();
        task.setObjdir( objDir );

        task.setOutfile( new File( new File( "" ).getAbsolutePath(), "target/output.elf" ) );

        ArduinoLinkerDef linkerDef = new ArduinoLinkerDef();
        task.addConfiguredLinker( linkerDef );

        task.execute();
    }
    
    private ArduinoCppCompilerDef createCppCompilerDef( Project project )
    {
        ArduinoCppCompilerDef cppDef = new ArduinoCppCompilerDef( project );

        cppDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ) );
        cppDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        cppDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );

        cppDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ) );
        cppDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        cppDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        
        return cppDef;
    }

    private ArduinoCCompilerDef createCCompilerDef( Project project )
    {
        ArduinoCCompilerDef cDef = new ArduinoCCompilerDef( project );

        cDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ) );
        cDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        cDef.addSourceFileDir( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );

        cDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-blink-project" ) );
        cDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        cDef.addIncludePath( new File( new File( "" ).getAbsolutePath(),
            "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );

        return cDef;
    }
}
