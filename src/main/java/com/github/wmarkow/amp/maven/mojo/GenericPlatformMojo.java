package com.github.wmarkow.amp.maven.mojo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.arduino.platform.BoardsVariables;
import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformPackageManager;
import com.github.wmarkow.amp.util.AmpFileUtils;
import com.github.wmarkow.amp.util.ArtifactUtils;

public abstract class GenericPlatformMojo extends GenericMojo
{

    @Parameter( property = "arduino-maven-plugin.packageIndexes", required = true )
    private URL[] packageIndexes;

    private PlatformPackageManager ppm;

    protected URL[] getPackageIndexesUrls()
    {
        return packageIndexes;
    }

    protected synchronized PlatformPackageManager getPlatformPackageManager()
    {
        if( ppm == null )
        {
            PlatformPackageManager ppm = new PlatformPackageManager( getArduinoPlatformDirFile() );
            for( URL url : getPackageIndexesUrls() )
            {
                ppm.addPackageUrl( url );
            }
            ppm.update();

            this.ppm = ppm;
        }

        return ppm;
    }

    protected Package getPackage()
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreDependency();

        return getPlatformPackageManager().getPlatformRepository().getPackage(
            arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
    }

    protected Platform getPlatform()
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreDependency();

        return getPlatformPackageManager().getPlatformRepository().getPlatform(
            arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
    }

    protected File getPathToUnpackedArduinoCore()
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreDependency();

        return getPathToUnpackedArduinoCore( arduinoCoreArtifact );
    }

    protected BoardsVariables getBoardsVariables() throws IOException
    {
        File boardsTxtFile = new File( getPathToUnpackedArduinoCore(), "/boards.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();

        return pfr.readBoardsVariables( boardsTxtFile );
    }

    private File getPathToUnpackedArduinoCore( Artifact arduinoCoreArtifact )
    {
        File baseDir =
            new File( getGeneratedSourcesDirFile(), ArtifactUtils.getBaseFileName( arduinoCoreArtifact ) );

        return AmpFileUtils.stepIntoSingleFolderIfPossible( baseDir );
    }
}
