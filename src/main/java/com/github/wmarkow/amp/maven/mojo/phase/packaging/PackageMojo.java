package com.github.wmarkow.amp.maven.mojo.phase.packaging;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.maven.mojo.GenericMojo;

@Mojo( name = "package", defaultPhase = LifecyclePhase.PACKAGE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class PackageMojo extends GenericMojo
{

    @Component
    protected MavenProjectHelper mavenProjectHelper;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            Artifact projectArtifact = getProjectArtifact();
            if( ARDUINO_LIB_EXTENSION.equals( projectArtifact.getExtension() ) )
            {
                packageForArduinoLib();
            }
            else if( ARDUINO_ELF_EXTENSION.equals( projectArtifact.getExtension() ) )
            {
                packageForArduinoElf();
            }
        }
        catch( Exception e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }
    }

    private void packageForArduinoLib() throws IOException
    {
        Artifact projectArtifact = getProjectArtifact();
        // assuming that zip generated by maven-assembly-plugin is already available
        File inputZip =
            new File( String.format( "target/%s-%s.zip/", projectArtifact.getArtifactId(),
                projectArtifact.getVersion() ) );

        File mainFile =
            new File( String.format( "target/%s-%s.%s/", projectArtifact.getArtifactId(),
                projectArtifact.getVersion(), GenericMojo.ARDUINO_LIB_EXTENSION ) );

        FileUtils.copyFile( inputZip, mainFile );

        this.mavenProject.getArtifact().setFile( mainFile.getAbsoluteFile() );
    }

    private void packageForArduinoElf() throws IOException
    {
        Artifact projectArtifact = getProjectArtifact();
        // assuming that elf, hex and eeprom are already available
        File inputElf =
            new File( String.format( "target/%s-%s.elf/", projectArtifact.getArtifactId(),
                projectArtifact.getVersion() ) );

        File inputHex =
            new File( String.format( "target/%s-%s.hex/", projectArtifact.getArtifactId(),
                projectArtifact.getVersion() ) );

        File inputEeprom =
            new File( String.format( "target/%s-%s.eep/", projectArtifact.getArtifactId(),
                projectArtifact.getVersion() ) );

        File mainFile =
            new File( String.format( "target/%s-%s.%s/", projectArtifact.getArtifactId(),
                projectArtifact.getVersion(), GenericMojo.ARDUINO_ELF_EXTENSION ) );

        FileUtils.copyFile( inputElf, mainFile );

        this.mavenProject.getArtifact().setFile( mainFile.getAbsoluteFile() );

        mavenProjectHelper.attachArtifact( mavenProject, "hex", inputHex );
        mavenProjectHelper.attachArtifact( mavenProject, "eep", inputEeprom );
    }
}