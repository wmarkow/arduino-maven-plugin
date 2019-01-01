package com.github.wmarkow.amp.maven.mojo.build;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.arduino.build.archiver.Archiver;
import com.github.wmarkow.amp.arduino.build.archiver.ArchiverCommandBuilder;
import com.github.wmarkow.amp.arduino.build.compiler.Compiler;
import com.github.wmarkow.amp.arduino.build.linker.Linker;
import com.github.wmarkow.amp.arduino.build.linker.LinkerCommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

@Mojo( name = "build", defaultPhase = LifecyclePhase.COMPILE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class BuildMojo extends ProcessorMojo
{

    @Parameter( defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true )
    private String sourceDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            FileUtils.forceMkdir( getObjectDir() );

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
        final Platform platform = getPlatform();
        final PlatformVariables platformVariables = getPlatformVariables();
        final BoardVariables boardVariables = getBoardVariables();

        Compiler compiler = new Compiler( platform, platformVariables, boardVariables );

        compiler.setToolchainBinDirPath( getToolChainBinDirPath() );
        compiler.setCommandExecutionDirectory( getCommandExecutionDirectory() );
        compiler.setObjDirectory( getObjectDir() );

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
        final Platform platform = getPlatform();
        final PlatformVariables platformVariables = getPlatformVariables();
        final BoardVariables boardVariables = getBoardVariables();

        ArchiverCommandBuilder acb = new ArchiverCommandBuilder( platform, platformVariables, boardVariables );
        acb.setToolchainBinDirPath( getToolChainBinDirPath() );

        Archiver archiver = new Archiver( acb );
        archiver.setCommandExecutionDirectory( getCommandExecutionDirectory() );

        archiver.archive( getObjectDir(), getArchiveFile() );
    }

    private void link() throws IOException, InterruptedException
    {
        final Platform platform = getPlatform();
        final PlatformVariables platformVariables = getPlatformVariables();
        final BoardVariables boardVariables = getBoardVariables();

        LinkerCommandBuilder lcb = new LinkerCommandBuilder( platform, platformVariables, boardVariables );
        lcb.setToolchainBinDirPath( getToolChainBinDirPath() );

        Linker linker = new Linker( lcb );
        linker.setCommandExecutionDirectory( getCommandExecutionDirectory() );

        linker.link( getObjectDir(), getElfFile() );
    }
}
