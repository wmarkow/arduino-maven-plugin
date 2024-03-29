package com.github.wmarkow.amp.maven.mojo.phase.gensources;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.maven.mojo.GenericMojo;
import com.github.wmarkow.amp.maven.mojo.GenericPlatformMojo;
import com.github.wmarkow.amp.util.ArtifactUtils;
import com.github.wmarkow.amp.util.CompressUtil;

@Mojo( name = "unpack-dependencies", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class UnpackDependenciesMojo extends GenericPlatformMojo
{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        List< Artifact > arduinoDeps = getArduinoDependencies();

        for( Artifact arduinoDep : arduinoDeps )
        {
            if( GenericMojo.ARDUINO_CORE_EXTENSION.equals( arduinoDep.getExtension() )
                || GenericMojo.ARDUINO_LIB_EXTENSION.equals( arduinoDep.getExtension() ) )
            {
                unpackArduinoCoreOrArduinoLib( arduinoDep );
            }

            if( GenericMojo.ARDUINO_CORE_LIB_EXTENSION.equals( arduinoDep.getExtension() ) )
            {
                // there is nothing to unpack here as the internal library is already unpacked together with
                // Arduino Core
            }
        }
    }

    private void unpackArduinoCoreOrArduinoLib( Artifact arduinoDep ) throws MojoExecutionException,
        MojoFailureException
    {
        getLog().info( String.format( "Processing library %s", artifactToString( arduinoDep ) ) );

        File sourceZip = resolveFileOfDependency( arduinoDep );

        File dstDir = new File( getGeneratedSourcesDirFile(), ArtifactUtils.getBaseFileName( arduinoDep ) );
        try
        {
            FileUtils.forceMkdir( dstDir );
        }
        catch( IOException e )
        {
            throw new MojoExecutionException( String.format( "Can't create directory %s",
                dstDir.getAbsolutePath() ) );
        }

        getLog().info(
            String.format( "Unpacking library from %s to %s", sourceZip.getAbsolutePath(),
                dstDir.getAbsolutePath() ) );

        try
        {
            CompressUtil.unpack( sourceZip, dstDir );

            File[] listed = dstDir.listFiles();
            if( listed.length == 1 && !listed[ 0 ].getName().equals( "src" ) )
            {
                // copy content of that directory one level higher
                FileUtils.copyDirectory( listed[ 0 ], dstDir, false );
                // delete that directory
                FileUtils.forceDelete( listed[ 0 ] );
            }

        }
        catch( IOException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }

    private File resolveFileOfDependency( Artifact artifact ) throws MojoFailureException
    {
        String localPath = repoSession.getLocalRepositoryManager().getPathForLocalArtifact( artifact );
        if( localPath == null )
        {
            throw new MojoFailureException( String.format(
                "Unable to find dependency %s in local repository", artifactToString( artifact ) ) );
        }

        File localRepoBaseDir = repoSession.getLocalRepository().getBasedir();

        File file = new File( localRepoBaseDir, localPath );
        if( !file.exists() )
        {
            throw new MojoFailureException( String.format(
                "Unable to find dependency file %s in local repository", file.getAbsolutePath() ) );
        }

        return file;
    }
}
