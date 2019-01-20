package com.github.wmarkow.amp.maven.mojo.eclipse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.wmarkow.amp.eclipse.ProjectFileContentCreator;
import com.github.wmarkow.amp.maven.mojo.GenericPlatformMojo;

@Mojo( name = "eclipse", defaultPhase = LifecyclePhase.NONE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class EclipseMojo extends GenericPlatformMojo
{
    private final static String PROJECT_TEMPLATE_FILE_PATH = "/project-template";
    private final static String PROJECT_FILE_NAME = ".project";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            generateProjectFile();
        }
        catch( IOException e )
        {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }

    private void generateProjectFile() throws IOException
    {
        InputStream inputStream = getClass().getResourceAsStream( PROJECT_TEMPLATE_FILE_PATH );

        String templateContent = IOUtils.toString( inputStream, "UTF-8" );

        ProjectFileContentCreator creator = new ProjectFileContentCreator( mavenProject.getName() );
        final String fileContent = creator.create( templateContent );

        FileUtils.writeStringToFile( new File( PROJECT_FILE_NAME ), fileContent, "UTF-8" );
    }
}
