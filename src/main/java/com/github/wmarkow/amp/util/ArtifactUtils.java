package com.github.wmarkow.amp.util;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.maven.mojo.GenericMojo;

public class ArtifactUtils
{

    public static String getBaseFileName( Artifact artifact )
    {
        StringBuilder sb = new StringBuilder();
        sb.append( artifact.getArtifactId() );
        sb.append( "-" );
        sb.append( artifact.getBaseVersion() );
        if( !artifact.getClassifier().isEmpty() )
        {
            sb.append( "-" );
            sb.append( artifact.getClassifier() );
        }

        return sb.toString();
    }

    public static String getZipFileName( Artifact artifact )
    {
        return getBaseFileName( artifact ) + ".zip";
    }

    public static Artifact toArtifact( Package _package, Platform platform )
    {
        final String groupId = "put whatever you want";
        final String artifactId = toArtifactId( _package, platform );
        final String extension = GenericMojo.ARDUINO_CORE_EXTENSION;
        final String version = platform.getVersion();

        return new DefaultArtifact( groupId, artifactId, extension, version );
    }

    public static String toArtifactId( Package _package, Platform platform )
    {
        return _package.getName() + "-" + platform.getArchitecture();
    }
}
