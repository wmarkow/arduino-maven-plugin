package com.github.wmarkow.amp.maven.mojo.build;

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

    protected File getPathToMainSourcesDir()
    {
        File baseDir = new File( "." );
        File sourceDir = new File( sourceDirectory );

        String relative = baseDir.toURI().relativize( sourceDir.toURI() ).getPath();

        return new File( relative );
    }

    protected File[] getPathsToUnpackedArduinoCoreSourcesDir() throws IOException
    {
        final BoardVariables boardVariables = getBoardVariables();

        final String core = boardVariables.getVariable( BoardVariables.VAR_BUILD_CORE ).getValue();
        final String variant = boardVariables.getVariable( BoardVariables.VAR_BUILD_VARIANT ).getValue();

        return getPathToUnpackedArduinoCoreSourcesDir( core, variant );
    }

    protected File[] getPathsToUnpackedLibrarySourcesDir() throws IOException
    {
        List< File > paths = new ArrayList< File >();

        for( Artifact arduinoDependency : getArduinoLibDependencies() )
        {
            if( GenericMojo.ARDUINO_LIB_EXTENSION.equals( arduinoDependency.getExtension() ) )
            {
                paths.add( getPathToUnpackedLibrarySourcesDir( arduinoDependency ) );
            }

            if( GenericMojo.ARDUINO_CORE_LIB_EXTENSION.equals( arduinoDependency.getExtension() ) )
            {
                File path = getPathToUnpackedCoreLibrarySourcesDir( arduinoDependency.getArtifactId() );
                if( path == null )
                {
                    throw new IOException( String.format( "Can't find a path to internal library %s",
                        arduinoDependency.getArtifactId() ) );
                }
                paths.add( path );
            }
        }

        return paths.toArray( new File[]
        {} );
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

        compiler.addSrcDirectory( getPathToMainSourcesDir() );

        for( File path : getPathsToUnpackedArduinoCoreSourcesDir() )
        {
            compiler.addSrcDirectory( path );
            compiler.addIncludeDirectory( path );
        }

        for( File path : getPathsToUnpackedLibrarySourcesDir() )
        {
            compiler.addSrcDirectory( path );
            compiler.addIncludeDirectory( path );
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
