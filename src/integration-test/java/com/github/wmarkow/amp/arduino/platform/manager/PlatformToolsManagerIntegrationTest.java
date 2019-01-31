package com.github.wmarkow.amp.arduino.platform.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformRepository;
import com.github.wmarkow.amp.arduino.platform.System;
import com.github.wmarkow.amp.arduino.platform.Tool;

public class PlatformToolsManagerIntegrationTest {
	private final static File PLATFORM_DIR = new File("target/arduino-maven-plugin");

	@Before
	public void init() throws IOException {
		File downloadsDir = new File(PLATFORM_DIR, "downloads");

		if (downloadsDir.exists()) {
			FileUtils.cleanDirectory(downloadsDir);
		}
	}

	@Test
	public void test() {
		java.lang.System.getProperty("os.arch");
		java.lang.System.getProperty("os.name");
		java.lang.System.getProperty("sun.desktop");
		java.lang.System.getProperties();
	}

	@Test
	public void testResolveForArduinoWindows() throws IOException {
		final File downloadsDir = new File(PLATFORM_DIR, "downloads");
		assertFalse(new File(downloadsDir, "arduinoOTA-1.2.1-windows_386.zip").exists());
		assertFalse(new File(downloadsDir, "avrdude-6.3.0-arduino14-i686-w64-mingw32.zip").exists());
		assertFalse(new File(downloadsDir, "avr-gcc-5.4.0-atmel3.6.1-arduino2-i686-w64-mingw32.zip").exists());

		PlatformFilesReader pir = new PlatformFilesReader();
		PlatformRepository platformRepository = new PlatformRepository();
		platformRepository.addIndex(pir.readFromJson(new File("src/test/resources/package_index.json")));

		PlatformToolsManager tm = new PlatformToolsManager(PLATFORM_DIR);
		Platform platform = platformRepository.getPackageByName("arduino").getPlatformByVersion("1.6.23");

		tm.resolve(platformRepository, platform);

		assertTrue(new File(downloadsDir, "arduinoOTA-1.2.1-windows_386.zip").exists());
		assertTrue(new File(downloadsDir, "avrdude-6.3.0-arduino14-i686-w64-mingw32.zip").exists());
		assertTrue(new File(downloadsDir, "avr-gcc-5.4.0-atmel3.6.1-arduino2-i686-w64-mingw32.zip").exists());
	}

	@Test
	public void testFindSystemForLinux() throws IOException {
		PlatformFilesReader pir = new PlatformFilesReader();
		PlatformRepository platformRepository = new PlatformRepository();
		platformRepository.addIndex(pir.readFromJson(new File("src/test/resources/package_index.json")));

		Tool tool = platformRepository.getPackageByName("arduino").getToolByNameAndVersion("avr-gcc",
				"5.4.0-atmel3.6.1-arduino2");

		HostInfo hostInfo = new HostInfo("amd64", "Linux");
		System system = tool.getSystemByHostInfo(hostInfo);
		
		assertEquals("x86_64-linux-gnu", system.getHost());
		assertEquals("avr-gcc-5.4.0-atmel3.6.1-arduino2-x86_64-pc-linux-gnu.tar.bz2", system.getArchiveFileName());
	}

    @Test
    public void testFindSystemForWindows() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();
        PlatformRepository platformRepository = new PlatformRepository();
        platformRepository.addIndex( pir.readFromJson( new File( "src/test/resources/package_index.json" ) ) );

        Tool tool =
            platformRepository.getPackageByName( "arduino" ).getToolByNameAndVersion( "avr-gcc",
                "5.4.0-atmel3.6.1-arduino2" );

        HostInfo hostInfo = new HostInfo( "amd64", "Windows 7" );
        System system = tool.getSystemByHostInfo( hostInfo );

        assertEquals( "i686-mingw32", system.getHost() );
        assertEquals( "avr-gcc-5.4.0-atmel3.6.1-arduino2-i686-w64-mingw32.zip",
            system.getArchiveFileName() );
    }
}
