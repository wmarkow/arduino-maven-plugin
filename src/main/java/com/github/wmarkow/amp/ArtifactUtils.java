package com.github.wmarkow.amp;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

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

    public static Artifact mavenToAether( org.apache.maven.artifact.Artifact mavenArtifact )
    {
        return new DefaultArtifact( mavenArtifact.getGroupId(), mavenArtifact.getArtifactId(),
            mavenArtifact.getClassifier(), mavenArtifact.getType(), mavenArtifact.getVersion() );
    }
}
