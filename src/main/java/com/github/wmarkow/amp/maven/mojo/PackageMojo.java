package com.github.wmarkow.amp.maven.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.arduino.build.imager.EepromDumper;
import com.github.wmarkow.amp.arduino.build.imager.EepromImageCommandBuilder;
import com.github.wmarkow.amp.arduino.build.imager.HexDumper;
import com.github.wmarkow.amp.arduino.build.imager.HexImageCommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.util.ArtifactUtils;

@Mojo( name = "package", defaultPhase = LifecyclePhase.PACKAGE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class PackageMojo extends ArduinoAbstractMojo
{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            createHex();
            createEeprom();
        }
        catch( Exception e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }
    }

    private void createHex() throws IOException, InterruptedException
    {
        PlatformPackageManager ppm = new PlatformPackageManager( new File( "target/arduino-maven-plugin" ) );
        ppm.addPackageUrl( getPackageIndexUrl() );
        ppm.update();

        Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        final Platform platform =
            ppm.getPlatform( arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
        final PlatformVariables platformVariables = getPlatformVariables( arduinoCoreArtifact );
        final BoardVariables boardVariables = getBoardVariables( arduinoCoreArtifact, getBoard() );

        HexDumper hexDumper =
            new HexDumper( new HexImageCommandBuilder( platform, platformVariables, boardVariables ) );
        hexDumper.setCommandExecutionDirectory( new File( "." ) );

        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact );
        File inputElfFile = new File( "target/" + elfFileName + ".elf" );

        hexDumper.makeHex( inputElfFile );
    }

    private void createEeprom() throws IOException, InterruptedException
    {
        PlatformPackageManager ppm = new PlatformPackageManager( new File( "target/arduino-maven-plugin" ) );
        ppm.addPackageUrl( getPackageIndexUrl() );
        ppm.update();

        Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        final Platform platform =
            ppm.getPlatform( arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
        final PlatformVariables platformVariables = getPlatformVariables( arduinoCoreArtifact );
        final BoardVariables boardVariables = getBoardVariables( arduinoCoreArtifact, getBoard() );

        EepromDumper eepromDumper =
            new EepromDumper( new EepromImageCommandBuilder( platform, platformVariables, boardVariables ) );

        eepromDumper.setCommandExecutionDirectory( new File( "." ) );

        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact );
        File inputElfFile = new File( "target/" + elfFileName + ".elf" );

        eepromDumper.makeEeprom( inputElfFile );
    }
}
