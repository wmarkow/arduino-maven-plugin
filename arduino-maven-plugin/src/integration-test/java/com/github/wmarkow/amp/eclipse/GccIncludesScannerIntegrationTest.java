package com.github.wmarkow.amp.eclipse;

import static org.junit.Assert.assertEquals;

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

        GccIncludesScanner scanner = new GccIncludesScanner();
        scanner.scan( new File( toolchainBinDirPath, "avr-gcc" ) );

        assertEquals( 3, scanner.getCIncludes().size() );
        assertEquals( 3, scanner.getCppIncludes().size() );
        assertEquals( 0, scanner.getAsmIncludes().size() );
    }

    private File getPlatformDir()
    {
        String path = System.getProperty( "user.home" );
        return new File( path, ".arduino-maven-plugin" );
    }
}
