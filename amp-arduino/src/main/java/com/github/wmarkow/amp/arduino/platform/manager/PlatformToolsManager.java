package com.github.wmarkow.amp.arduino.platform.manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformRepository;
import com.github.wmarkow.amp.arduino.platform.System;
import com.github.wmarkow.amp.arduino.platform.Tool;
import com.github.wmarkow.amp.arduino.platform.ToolsDependency;
import com.github.wmarkow.amp.util.CompressUtil;

public class PlatformToolsManager extends PlatformManager {
	private Logger logger = LoggerFactory.getLogger(PlatformToolsManager.class);

	private final static String DOWNLOADS_DIR = "downloads";

	public PlatformToolsManager(File platformDir) {
		super(platformDir);
	}

	public void resolve(PlatformRepository platformRepository, Platform platform)
			throws MalformedURLException, IOException {
		logger.info(String.format("Resolving tools dependencies for platform %s ...", platform));

		for (ToolsDependency td : platform.getToolsDependencies()) {
			Tool tool = platformRepository.getToolByPackagerAndNameAndVersion(td.getPackager(), td.getName(),
					td.getVersion());

			downlaodToolIfNeeded(td.getPackager(), tool);
			unpackToolIfNeeded(td.getPackager(), tool);

		}
	}

	public File getToolchainBinDirPath(PlatformRepository platformRepository, Platform platform) {
		for (ToolsDependency td : platform.getToolsDependencies()) {
			if (td.getName().toLowerCase().contains("gcc")) {
				Tool tool = platformRepository.getToolByPackagerAndNameAndVersion(td.getPackager(), td.getName(),
						td.getVersion());

				File packagesDir = new File(getPlatformDir(), "packages");
				File dir = new File(packagesDir,
						td.getPackager() + "/tools/" + tool.getName() + "/" + tool.getVersion());

				// sometimes the distribution contains one additional dir in the structure.
				// Strip it.
				File[] filesInside = dir.listFiles();
				if (filesInside.length == 1) {
					return new File(filesInside[0], "/bin/");
				}
			}
		}

		return null;
	}

	private void downlaodToolIfNeeded(String packageName, Tool tool) throws MalformedURLException, IOException {
		System system = tool.getSystemByHostInfo(HostInfo.populateLocalhostInfo());
		File downloadDir = new File(getPlatformDir(), DOWNLOADS_DIR);
		File downloadFile = new File(downloadDir, system.getArchiveFileName());

		if (downloadFile.exists()) {
			return;
		}

		logger.info(String.format("Downloading %s into %s", system.getUrl(), downloadFile));

		FileUtils.copyURLToFile(new URL(system.getUrl()), downloadFile, 5000, 2500);
	}

	private void unpackToolIfNeeded(String packageName, Tool tool) throws IOException {
		File packagesDir = new File(getPlatformDir(), "packages");
		File targetDir = new File(packagesDir, packageName + "/tools/" + tool.getName() + "/" + tool.getVersion());

		if (targetDir.exists()) {
			return;
		}

		System system = tool.getSystemByHostInfo(HostInfo.populateLocalhostInfo());

		File downloadDir = new File(getPlatformDir(), DOWNLOADS_DIR);
		File downloadFile = new File(downloadDir, system.getArchiveFileName());

		logger.info(String.format("Unpacking %s into %s", downloadFile, targetDir));

		CompressUtil.unpack(downloadFile, targetDir);
	}
}
