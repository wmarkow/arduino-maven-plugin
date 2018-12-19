package com.github.wmarkow.amp.mojo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.ArtifactUtils;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.build.imager.EepromDumper;
import com.github.wmarkow.amp.build.imager.EepromImageCommandBuilder;
import com.github.wmarkow.amp.build.imager.HexDumper;
import com.github.wmarkow.amp.build.imager.HexImageCommandBuilder;

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
        ppm.addPackageUrl( new URL( "https://downloads.arduino.cc/packages/package_index.json" ) );
        ppm.update();

        Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        final Platform platform =
            ppm.getPlatform( arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
        final PlatformVariables platformVariables = getPlatformVariables( arduinoCoreArtifact );
        final BoardVariables boardVariables = getBoardVariables( arduinoCoreArtifact, "uno" );

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
        ppm.addPackageUrl( new URL( "https://downloads.arduino.cc/packages/package_index.json" ) );
        ppm.update();

        Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        final Platform platform =
            ppm.getPlatform( arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
        final PlatformVariables platformVariables = getPlatformVariables( arduinoCoreArtifact );
        final BoardVariables boardVariables = getBoardVariables( arduinoCoreArtifact, "uno" );

        EepromDumper eepromDumper =
            new EepromDumper( new EepromImageCommandBuilder( platform, platformVariables, boardVariables ) );

        eepromDumper.setCommandExecutionDirectory( new File( "." ) );

        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact );
        File inputElfFile = new File( "target/" + elfFileName + ".elf" );

        eepromDumper.makeEeprom( inputElfFile );
    }
}
