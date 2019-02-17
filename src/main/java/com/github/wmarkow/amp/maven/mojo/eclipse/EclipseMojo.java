package com.github.wmarkow.amp.maven.mojo.eclipse;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.arduino.variable.Variable;
import com.github.wmarkow.amp.eclipse.CProjectFileContentCreator;
import com.github.wmarkow.amp.eclipse.GccIncludesScanner;
import com.github.wmarkow.amp.eclipse.ProjectFileContentCreator;
import com.github.wmarkow.amp.maven.mojo.phase.compile.CompileMojo;

@Mojo( name = "eclipse", defaultPhase = LifecyclePhase.NONE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class EclipseMojo extends CompileMojo
{
    private final static String PROJECT_TEMPLATE_FILE_PATH = "/project-template";
    private final static String PROJECT_FILE_NAME = ".project";
    private final static String C_PROJECT_TEMPLATE_FILE_PATH = "/cproject-template";
    private final static String C_PROJECT_FILE_NAME = ".cproject";

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            generateProjectFile();
            generateCProjectFile();
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

    private void generateCProjectFile() throws IOException
    {
        InputStream inputStream = getClass().getResourceAsStream( C_PROJECT_TEMPLATE_FILE_PATH );

        String templateContent = IOUtils.toString( inputStream, "UTF-8" );

        List< String > sourcesPaths = new ArrayList< String >();
        sourcesPaths.add( getPathToMainSourcesDir().getPath() );
        for( File filePath : getPathsToUnpackedArduinoCoreSourcesDir() )
        {
            sourcesPaths.add( filePath.getPath() );
        }
        for( File filePath : getPathsToUnpackedLibrarySourcesDir() )
        {
            sourcesPaths.add( filePath.getPath() );
        }

        CProjectFileContentCreator creator = new CProjectFileContentCreator( mavenProject.getName() );
        creator.setSourcesDirs( sourcesPaths );

        try
        {
            PlatformVariables platformVariables = getPlatformVariables();
            Variable compilerCCmd = platformVariables.getVariable( "compiler.c.cmd" );

            GccIncludesScanner scanner = new GccIncludesScanner();
            scanner.scan( new File( getToolChainBinDirPath() + compilerCCmd.getValue() ) );

            creator.setCIncludesDirs( scanner.getCIncludes() );
            creator.setCppIncludesDirs( scanner.getCppIncludes() );
            creator.setAssemblerIncludesDirs( scanner.getAsmIncludes() );
        }
        catch( IOException e )
        {
            getLog().warn( "There was some error while creating default GCC includes list.", e );
        }

        final String fileContent = creator.create( templateContent );

        FileUtils.writeStringToFile( new File( C_PROJECT_FILE_NAME ), fileContent, "UTF-8" );
    }

}
