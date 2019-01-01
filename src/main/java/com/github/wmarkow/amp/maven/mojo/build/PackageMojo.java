package com.github.wmarkow.amp.maven.mojo.build;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

import com.github.wmarkow.amp.arduino.build.imager.EepromDumper;
import com.github.wmarkow.amp.arduino.build.imager.EepromImageCommandBuilder;
import com.github.wmarkow.amp.arduino.build.imager.HexDumper;
import com.github.wmarkow.amp.arduino.build.imager.HexImageCommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

@Mojo( name = "package", defaultPhase = LifecyclePhase.PACKAGE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class PackageMojo extends ProcessorMojo
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
        final Platform platform = getPlatform();
        final PlatformVariables platformVariables = getPlatformVariables();
        final BoardVariables boardVariables = getBoardVariables();

        HexImageCommandBuilder hicb =
            new HexImageCommandBuilder( platform, platformVariables, boardVariables );
        hicb.setToolchainBinDirPath( getToolChainBinDirPath() );
        HexDumper hexDumper = new HexDumper( hicb );

        hexDumper.setCommandExecutionDirectory( getCommandExecutionDirectory() );

        hexDumper.makeHex( getElfFile() );
    }

    private void createEeprom() throws IOException, InterruptedException
    {
        final Platform platform = getPlatform();
        final PlatformVariables platformVariables = getPlatformVariables();
        final BoardVariables boardVariables = getBoardVariables();

        EepromImageCommandBuilder eicb =
            new EepromImageCommandBuilder( platform, platformVariables, boardVariables );
        eicb.setToolchainBinDirPath( getToolChainBinDirPath() );

        EepromDumper eepromDumper = new EepromDumper( eicb );

        eepromDumper.setCommandExecutionDirectory( getCommandExecutionDirectory() );

        eepromDumper.makeEeprom( getElfFile() );
    }
}
