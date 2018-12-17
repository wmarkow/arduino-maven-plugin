package com.github.wmarkow.amp.mojo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.build.compiler.Compiler;
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
        PlatformPackageManager ppm = new PlatformPackageManager( new File( "target/arduino-maven-plugin" ) );
        ppm.addPackageUrl( new URL( "https://downloads.arduino.cc/packages/package_index.json" ) );
        ppm.update();

        Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        final Platform platform =
            ppm.getPlatform( arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
        final PlatformVariables platformVariables = getPlatformVariables( arduinoCoreArtifact );
        final BoardVariables boardVariables = getBoardVariables( arduinoCoreArtifact, "uno" );

        Compiler compiler = new Compiler( platform, platformVariables, boardVariables );
        File objDir = new File( "target/obj" );

        FileUtils.forceMkdir( objDir );

        compiler.setCommandExecutionDirectory( new File( "." ) );
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

    private PlatformVariables getPlatformVariables( Artifact arduinoCoreArtifact ) throws IOException
    {
        File platformTxtFile =
            new File( getPathToUnpackedLibrarySourcesDir( arduinoCoreArtifact ), "avr/platform.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readPlatformVariablesFromFile( platformTxtFile );
    }

    private BoardVariables getBoardVariables( Artifact arduinoCoreArtifact, String board ) throws IOException
    {
        File boardsTxtFile =
            new File( getPathToUnpackedLibrarySourcesDir( arduinoCoreArtifact ), "avr/boards.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readBoardsVariables( boardsTxtFile ).getBoardVariables( board );
    }
}
