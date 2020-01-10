package com.github.wmarkow.amp.arduino.platform;

import java.util.ArrayList;
import java.util.List;

public class PlatformRepository {
    private List<PlatformPackageIndex> indexes = new ArrayList<PlatformPackageIndex>();

    public void addIndex(PlatformPackageIndex index) {
        indexes.add(index);
    }

    public List<Package> getPackages() {
        List<Package> result = new ArrayList<Package>();

        for (PlatformPackageIndex index : indexes) {
            result.addAll(index.getPackages());
        }

        return result;
    }

    public Package getPackage(String artifactId, String version) {
        for (Package _package : getPackages()) {
            for (Platform platform : _package.getPlatforms()) {
                final String _artifactId = _package.getName() + "-" + platform.getArchitecture();
                if (artifactId.equals(_artifactId) && version.equals(platform.getVersion())) {
                    return _package;
                }
            }
        }

        return null;
    }

    public Package getPackageByName(String name) {
        for (Package _package : getPackages()) {
            if (_package.getName().equals(name)) {
                return _package;
            }
        }

        return null;
    }

    public Platform getPlatform(String packageName, String platformArchitecture, String version) {
        for (Package _package : getPackages()) {
            for (Platform platform : _package.getPlatforms()) {
                if (_package.getName().equals(packageName) && platform.getArchitecture().equals(platformArchitecture)
                        && version.equals(platform.getVersion())) {
                    return platform;
                }
            }
        }

        return null;
    }

    public Tool getToolByPackagerAndNameAndVersion(String packager, String name, String version) {
        for (Package _package : getPackages()) {
            if (_package.getName().equals(packager)) {
                for (Tool tool : _package.getTools()) {
                    if (tool.getName().equals(name) && tool.getVersion().equals(version)) {
                        return tool;
                    }
                }
            }
        }

        return null;
    }
}
