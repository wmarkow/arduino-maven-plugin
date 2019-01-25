package com.github.wmarkow.amp.eclipse;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformRepository;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformToolsManager;

public class GccIncludesScannerIntegrationTest
{

    @Test
    public void testForArduino() throws IOException, InterruptedException
    {
        PlatformFilesReader pfr = new PlatformFilesReader();
        PlatformRepository platformRepository = new PlatformRepository();
        platformRepository.addIndex( pfr.readFromJson( new File( "src/test/resources/package_index.json" ) ) );

        Package _package = platformRepository.getPackageByName( "arduino" );
        Platform platform = _package.getPlatformByVersion( "1.6.17" );

        // update Arduino Platform so 1.6.17 is available
        PlatformToolsManager tm = new PlatformToolsManager( getPlatformDir() );
        // tm.resolve( platformRepository, platform );

        PlatformToolsManager toolsManager = new PlatformToolsManager( getPlatformDir() );
        File toolchainBinDirPath = toolsManager.getToolchainBinDirPath( platformRepository, platform );

        GccIncludesScanner gccIncludesScanner = new GccIncludesScanner();
        gccIncludesScanner.getDefaultIncludes( new File( toolchainBinDirPath, "avr-gcc" ) );
    }

    private File getPlatformDir()
    {
        String path = System.getProperty( "user.home" );
        return new File( path, ".arduino-maven-plugin" );
    }
}
