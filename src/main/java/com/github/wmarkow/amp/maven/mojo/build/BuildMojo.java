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
import com.github.wmarkow.amp.maven.mojo.GenericMojo;

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
        compiler.setCurrentArduinoCorePath( getPathToUnpackedArduinoCore().getAbsolutePath() );
        compiler.setCommandExecutionDirectory( getCommandExecutionDirectory() );
        compiler.setObjDirectory( getObjectDir() );

        compiler.addSrcDirectory( new File( sourceDirectory ) );

        final String core = boardVariables.getVariable( BoardVariables.VAR_BUILD_CORE ).getValue();
        final String variant = boardVariables.getVariable( BoardVariables.VAR_BUILD_VARIANT ).getValue();

        File[] paths = getPathToUnpackedArduinoCoreSourcesDir( core, variant );
        for( File path : paths )
        {
            compiler.addSrcDirectory( path );
            compiler.addIncludeDirectory( path );
        }

        for( Artifact arduinoDependency : getArduinoLibDependencies() )
        {
            if( GenericMojo.ARDUINO_LIB_EXTENSION.equals( arduinoDependency.getExtension() ) )
            {
                File path = getPathToUnpackedLibrarySourcesDir( arduinoDependency );
                compiler.addSrcDirectory( path );
                compiler.addIncludeDirectory( path );
            }

            if( GenericMojo.ARDUINO_CORE_LIB_EXTENSION.equals( arduinoDependency.getExtension() ) )
            {
                File path = getPathToUnpackedCoreLibrarySourcesDir( arduinoDependency.getArtifactId() );
                if( path == null )
                {
                    throw new IOException( String.format( "Can't find a path to internal library %s",
                        arduinoDependency.getArtifactId() ) );
                }
                compiler.addSrcDirectory( path );
                compiler.addIncludeDirectory( path );
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
        lcb.setCurrentArduinoCorePath( getPathToUnpackedArduinoCore().getAbsolutePath() );

        Linker linker = new Linker( lcb );
        linker.setCommandExecutionDirectory( getCommandExecutionDirectory() );

        linker.link( getObjectDir(), getElfFile() );
    }
}
