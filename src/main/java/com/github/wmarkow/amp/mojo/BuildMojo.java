package com.github.wmarkow.amp.mojo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.ArtifactUtils;
import com.github.wmarkow.amp.build.compiler.CCompilerIntegrationTest;
import com.github.wmarkow.amp.build.compiler.Compiler;
import com.github.wmarkow.amp.build.compiler.CppCompilerIntegrationTest;
import com.github.wmarkow.amp.build.compiler.SCompilerIntegrationTest;
import com.github.wmarkow.amp.build.linker.Linker;

@Mojo( name = "build", defaultPhase = LifecyclePhase.COMPILE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class BuildMojo extends ArduinoAbstractMojo
{

    @Parameter( defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true )
    private String sourceDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            compile();
            link();
        }
        catch( IOException e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }
        catch( InterruptedException e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }
    }

    private void compile() throws IOException, InterruptedException
    {
        Compiler compiler = new Compiler();
        File objDir = new File( "target/obj" );

        FileUtils.forceMkdir( objDir );

        compiler.setCppCompilerCommand( "avr-g++" );
        compiler.setCCompilerCommand( "avr-gcc" );
        compiler.setSCompilerCommand( "avr-gcc" );
        compiler.setCommandExecutionDirectory( new File( "." ) );
        compiler.addCCompilerArgs( getDefaultCCompilerCommandArgs() );
        compiler.addCppCompilerArgs( getDefaultCppCompilerCommandArgs() );
        compiler.addSCompilerArgs( getDefaultSCompilerCommandArgs() );

        compiler.addCppCompilerArgs( CppCompilerIntegrationTest.getDefaultCommandArgs() );
        compiler.addCCompilerArgs( CCompilerIntegrationTest.getDefaultCommandArgs() );
        compiler.addSCompilerArgs( SCompilerIntegrationTest.getDefaultCommandArgs() );

        compiler.setObjDirectory( objDir );

        compiler.addSrcDirectory( new File( sourceDirectory ) );
        for( Artifact arduinoLib : getArduinoDependencies() )
        {
            File libSourcesDir = getPathToUnpackedLibrarySourcesDir( arduinoLib );

            compiler.addSrcDirectory( libSourcesDir );
            compiler.addIncludeDirectory( libSourcesDir );
        }

        compiler.compile();
    }

    private void link() throws IOException, InterruptedException
    {
        Linker linker = new Linker();
        linker.setCommand( "avr-gcc" );
        linker.setCommandExecutionDirectory( new File( "." ) );
        linker.addCommandArgs( getDefaultLinkerCommandArgs() );

        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact ) + ".elf";
        File outputElfFile = new File( "target/" + elfFileName );

        linker.link( new File( "target/obj" ), outputElfFile );
    }

    private File getPathToUnpackedLibrarySourcesDir( Artifact artifact )
    {
        File baseDir =
            new File( new File( "target/generated-sources/" ).getAbsolutePath(),
                ArtifactUtils.getBaseFileName( artifact ) );

        File dirWithSrc = new File( baseDir, "src" );
        if( dirWithSrc.exists() )
        {
            return dirWithSrc;
        }

        return baseDir;
    }

    private final static List< String > getDefaultCCompilerCommandArgs()
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

    private final static List< String > getDefaultCppCompilerCommandArgs()
    {
        List< String > args = new ArrayList< String >();

        args.add( "-c" );
        args.add( "-g" );
        args.add( "-Os" );
        args.add( "-Wall" );
        args.add( "-Wextra" );
        args.add( "-std=gnu++11" );
        args.add( "-fpermissive" );
        args.add( "-fno-exceptions" );
        args.add( "-ffunction-sections" );
        args.add( "-fdata-sections" );
        args.add( "-fno-threadsafe-statics" );
        args.add( "-flto" );
        args.add( "-mmcu=atmega328p" );
        args.add( "-DF_CPU=16000000L" );
        args.add( "-DARDUINO=10609" );
        args.add( "-DARDUINO_AVR_UNO" );
        args.add( "-DARDUINO_ARCH_AVR" );

        return args;
    }

    private final static List< String > getDefaultSCompilerCommandArgs()
    {
        List< String > args = new ArrayList< String >();

        args.add( "-c" );
        args.add( "-g" );
        args.add( "-x" );
        args.add( "assembler-with-cpp" );
        args.add( "-flto" );
        args.add( "-MMD" );
        args.add( "-MP" );
        args.add( "-mmcu=atmega328p" );
        args.add( "-DF_CPU=16000000L" );
        args.add( "-DARDUINO=10609" );
        args.add( "-DARDUINO_AVR_UNO" );
        args.add( "-DARDUINO_ARCH_AVR" );

        return args;
    }

    private final static List< String > getDefaultLinkerCommandArgs()
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
