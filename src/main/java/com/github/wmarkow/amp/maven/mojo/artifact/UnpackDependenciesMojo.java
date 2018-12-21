package com.github.wmarkow.amp.maven.mojo.artifact;

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
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import com.github.wmarkow.amp.maven.mojo.GenericMojo;
import com.github.wmarkow.amp.util.ArtifactUtils;
import com.github.wmarkow.amp.util.CompressUtil;

@Mojo( name = "unpack-dependencies", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class UnpackDependenciesMojo extends GenericMojo
{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        List< Artifact > arduinoLibs = getArduinoDependencies();

        for( Artifact arduinoLib : arduinoLibs )
        {
            getLog().info( String.format( "Processing library %s", artifactToString( arduinoLib ) ) );

            File sourceZip = resolveFileOfDependency( arduinoLib );

            File dstDir =
                new File( getGeneratedSourcesDirFile(), ArtifactUtils.getBaseFileName( arduinoLib ) );
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
            }
            catch( IOException e )
            {
                throw new MojoExecutionException( e.getMessage(), e );
            }
        }
    }

    private File resolveFileOfDependency( Artifact artifact ) throws MojoFailureException
    {
        ArtifactRequest req = new ArtifactRequest().setArtifact( artifact );
        ArtifactResult resolutionResult;
        try
        {
            resolutionResult = repoSystem.resolveArtifact( repoSession, req );

            if( resolutionResult.isMissing() )
            {
                throw new MojoFailureException( String.format( "Unable to resolve dependency %s",
                    artifactToString( artifact ) ) );
            }

            return resolutionResult.getArtifact().getFile();
        }
        catch( ArtifactResolutionException e )
        {
            throw new MojoFailureException( String.format( "Unable to resolve dependency %s",
                artifactToString( artifact ) ) );
        }
    }
}
