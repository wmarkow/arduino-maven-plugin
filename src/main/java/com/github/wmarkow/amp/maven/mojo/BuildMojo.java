package com.github.wmarkow.amp.maven.mojo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.ArtifactUtils;
import com.github.wmarkow.amp.arduino.build.archiver.Archiver;
import com.github.wmarkow.amp.arduino.build.archiver.ArchiverCommandBuilder;
import com.github.wmarkow.amp.arduino.build.compiler.Compiler;
import com.github.wmarkow.amp.arduino.build.linker.Linker;
import com.github.wmarkow.amp.arduino.build.linker.LinkerCommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

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
            archive();
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

        final String arch = platform.getArchitecture();
        final String core = boardVariables.getVariable( BoardVariables.VAR_BUILD_CORE ).getValue();
        final String variant = boardVariables.getVariable( BoardVariables.VAR_BUILD_VARIANT ).getValue();

        for( Artifact arduinoDependency : getArduinoDependencies() )
        {
            if( ARDUINO_LIB_EXTENSION.equals( arduinoDependency.getExtension() ) )
            {
                File path = getPathToUnpackedLibrarySourcesDir( arduinoDependency );
                compiler.addSrcDirectory( path );
                compiler.addIncludeDirectory( path );
            }

            if( ARDUINO_CORE_EXTENSION.equals( arduinoDependency.getExtension() ) )
            {
                File[] paths =
                    getPathToUnpackedCoreLibrarySourcesDir( arduinoDependency, arch, core, variant );

                for( File path : paths )
                {
                    compiler.addSrcDirectory( path );
                    compiler.addIncludeDirectory( path );
                }
            }
        }

        compiler.compile();
    }

    private void archive() throws IOException, InterruptedException
    {
        PlatformPackageManager ppm = new PlatformPackageManager( new File( "target/arduino-maven-plugin" ) );
        ppm.addPackageUrl( new URL( "https://downloads.arduino.cc/packages/package_index.json" ) );
        ppm.update();

        Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        final Platform platform =
            ppm.getPlatform( arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
        final PlatformVariables platformVariables = getPlatformVariables( arduinoCoreArtifact );
        final BoardVariables boardVariables = getBoardVariables( arduinoCoreArtifact, "uno" );

        ArchiverCommandBuilder acb = new ArchiverCommandBuilder( platform, platformVariables, boardVariables );
        Archiver archiver = new Archiver( acb );
        archiver.setCommandExecutionDirectory( new File( "." ) );

        final Artifact projectArtifact = getProjectArtifact();
        final String archiveFileName = ArtifactUtils.getBaseFileName( projectArtifact ) + ".ar";
        File outputArchiveFile = new File( "target/" + archiveFileName );

        archiver.archive( new File( "target/obj" ), outputArchiveFile );
    }

    private void link() throws IOException, InterruptedException
    {
        PlatformPackageManager ppm = new PlatformPackageManager( new File( "target/arduino-maven-plugin" ) );
        ppm.addPackageUrl( new URL( "https://downloads.arduino.cc/packages/package_index.json" ) );
        ppm.update();

        Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        final Platform platform =
            ppm.getPlatform( arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
        final PlatformVariables platformVariables = getPlatformVariables( arduinoCoreArtifact );
        final BoardVariables boardVariables = getBoardVariables( arduinoCoreArtifact, "uno" );

        LinkerCommandBuilder lcb = new LinkerCommandBuilder( platform, platformVariables, boardVariables );
        Linker linker = new Linker( lcb );
        linker.setCommandExecutionDirectory( new File( "." ) );

        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact ) + ".elf";
        File outputElfFile = new File( "target/" + elfFileName );

        linker.link( new File( "target/obj" ), outputElfFile );
    }
}
