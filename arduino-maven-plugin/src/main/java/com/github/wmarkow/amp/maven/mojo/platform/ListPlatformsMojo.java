package com.github.wmarkow.amp.maven.mojo.platform;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.maven.mojo.GenericPlatformMojo;
import com.github.wmarkow.amp.util.ArtifactUtils;

@Mojo( name = "list-platforms", defaultPhase = LifecyclePhase.NONE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class ListPlatformsMojo extends GenericPlatformMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        for( Package _package : getPlatformPackageManager().getPlatformRepository().getPackages() )
        {
            for( Platform platform : _package.getPlatforms() )
            {
                Artifact artifact = ArtifactUtils.toArtifact( _package, platform );

                getLog().info( artifactToDependencyString( artifact ) );
            }
        }
    }

    private String artifactToDependencyString( Artifact artifact )
    {
        StringBuilder sb = new StringBuilder();
        sb.append( String.format( "<dependency>" ) );
        sb.append( String.format( "    <groupId>%s</groupId>", artifact.getGroupId() ) );
        sb.append( String.format( "    <artifactId>%s</artifactId>", artifact.getArtifactId() ) );
        sb.append( String.format( "    <version>%s</version>", artifact.getVersion() ) );
        sb.append( String.format( "    <type>%s</type>", artifact.getExtension() ) );
        sb.append( String.format( "</dependency>" ) );

        return sb.toString();
    }
}
