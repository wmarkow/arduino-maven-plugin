package com.github.wmarkow.amp.maven.mojo.platform;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.wmarkow.amp.maven.mojo.artifact.UnpackDependenciesMojo;

@Mojo( name = "list-boards", defaultPhase = LifecyclePhase.NONE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class ListBoardsMojo extends UnpackDependenciesMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        super.execute();

        try
        {
            for( String board : getBoardsVariables().getBoardNames() )
            {
                getLog().info( board );
            }
        }
        catch( IOException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }
}
