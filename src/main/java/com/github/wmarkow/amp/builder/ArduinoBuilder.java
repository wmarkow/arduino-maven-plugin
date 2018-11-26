package com.github.wmarkow.amp.builder;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;

import com.github.maven_nar.cpptasks.CCTask;
import com.github.wmarkow.amp.compiler.ArduinoCCompilerDef;
import com.github.wmarkow.amp.compiler.ArduinoCppCompilerDef;
import com.github.wmarkow.amp.linker.ArduinoLinkerDef;

public class ArduinoBuilder
{
    private final static String OBJ_DIR_NAME = "obj";
    private String artifactId;
    private String version;
    private String classifier;
    private File targetBuildDirectory;

    private Project project;
    private ArduinoCppCompilerDef cppDef;
    private ArduinoCCompilerDef cDef;
    private ArduinoLinkerDef linkerDef;
    private CCTask task;

    public ArduinoBuilder( String artifactId, String version, String classifier )
    {
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;

        this.project = new Project();

        cppDef = new ArduinoCppCompilerDef( project );
        cDef = new ArduinoCCompilerDef( project );
        linkerDef = new ArduinoLinkerDef();
        task = new CCTask();

        task.setProject( project );
        task.addConfiguredCompiler( cppDef );
        task.addConfiguredCompiler( cDef );
        task.addConfiguredLinker( linkerDef );
    }

    public void setTargetBuildDirectory( File absolutePath )
    {
        if( absolutePath == null )
        {
            throw new IllegalArgumentException( "Absolute path must not be null" );
        }

        targetBuildDirectory = absolutePath;
        task.setObjdir( new File( absolutePath, OBJ_DIR_NAME ) );

        final String outputElfFileNameWithoutExt = artifactId + "-" + version + "-" + classifier;
        task.setOutfile( new File( absolutePath, outputElfFileNameWithoutExt ) );
    }

    public void cleanTargetBuildDirectory()
    {
        if( targetBuildDirectory == null )
        {
            throw new IllegalStateException( "setTargetBuildDirectory not called" );
        }

        FileUtils.deleteQuietly( new File( targetBuildDirectory, OBJ_DIR_NAME ) );
        FileUtils.deleteQuietly( new File( targetBuildDirectory, getTargetElfFileName() ) );
    }

    public void addSourceFileDir( File absolutePath )
    {
        cppDef.addSourceFileDir( absolutePath );
        cDef.addSourceFileDir( absolutePath );
    }

    public void addIncludePath( File absolutePath )
    {
        cppDef.addIncludePath( absolutePath );
        cDef.addIncludePath( absolutePath );
    }

    public void addBuildListener( BuildListener listener )
    {
        project.addBuildListener( listener );
    }

    public void build()
    {
        if( targetBuildDirectory == null )
        {
            throw new IllegalStateException( "setTargetBuildDirectory not called" );
        }

        File objTargetDirectory = new File( targetBuildDirectory, OBJ_DIR_NAME );

        if( !objTargetDirectory.exists() && !objTargetDirectory.mkdirs() )
        {
            throw new IllegalStateException( "Target object directory not created" );
        }

        task.execute();
    }

    private String getTargetElfFileName()
    {
        return artifactId + "-" + version + "-" + classifier + ".elf";
    }
}
