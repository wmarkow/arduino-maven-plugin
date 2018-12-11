package com.github.wmarkow.amp.mojo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.ArtifactUtils;
import com.github.wmarkow.amp.elf.EepromDumper;
import com.github.wmarkow.amp.elf.HexDumper;

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
        HexDumper hexDumper = new HexDumper();

        hexDumper.setCommand( "avr-objcopy" );
        hexDumper.setCommandExecutionDirectory( new File( "." ) );
        hexDumper.addCommandArgs( getDefaultHexCommandArgs() );

        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact );
        File inputElfFile = new File( "target/" + elfFileName + ".elf" );
        File outputHexFile = new File( "target/" + elfFileName + ".hex" );

        hexDumper.makeHex( inputElfFile, outputHexFile );
    }

    private void createEeprom() throws IOException, InterruptedException
    {
        EepromDumper eepromDumper = new EepromDumper();

        eepromDumper.setCommand( "avr-objcopy" );
        eepromDumper.setCommandExecutionDirectory( new File( "." ) );
        eepromDumper.addCommandArgs( getDefaultEepromCommandArgs() );

        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact );
        File inputElfFile = new File( "target/" + elfFileName + ".elf" );
        File outputEepromFile = new File( "target/" + elfFileName + ".eep" );

        eepromDumper.makeEeprom( inputElfFile, outputEepromFile );
    }

    private List< String > getDefaultHexCommandArgs()
    {
        List< String > args = new ArrayList< String >();
        args.add( "-O" );
        args.add( "ihex" );
        args.add( "-R" );
        args.add( ".eeprom" );

        return args;
    }

    private List< String > getDefaultEepromCommandArgs()
    {
        List< String > args = new ArrayList< String >();
        args.add( "-O" );
        args.add( "ihex" );
        args.add( "-j" );
        args.add( ".eeprom" );
        args.add( "--set-section-flags=.eeprom=alloc,load" );
        args.add( "--no-change-warnings" );
        args.add( "--change-section-lma" );
        args.add( ".eeprom=0" );

        return args;
    }
}
