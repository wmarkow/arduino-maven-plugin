package com.github.wmarkow.amp.fetch;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class GithubArtifactFetcherIntegrationTest
{
    @Test
    public void testFetchLibraryForArduinoCoreFromExistingTag() throws IOException
    {
        GithubFetchDescriptor descriptor = new GithubFetchDescriptor();

        descriptor.username = "arduino";
        descriptor.repoName = "ArduinoCore-avr";
        descriptor.refName = "1.6.23";

        GithubArtifactFetcher fetcher = new GithubArtifactFetcher();

        File targetDir = new File( "target/arduino-maven-plugin/" );
        FileUtils.forceMkdir( targetDir );

        File targetFile = fetcher.fetchLibrary( descriptor, targetDir );

        assertNotNull( targetFile );
        assertTrue( targetFile.exists() );
    }

    @Test( expected = FileNotFoundException.class )
    public void testFetchLibraryForArduinoCoreFromNotExistingTag() throws IOException
    {
        GithubFetchDescriptor descriptor = new GithubFetchDescriptor();

        descriptor.username = "arduino";
        descriptor.repoName = "ArduinoCore-avr";
        descriptor.refName = "1.6.23blablabla";

        GithubArtifactFetcher fetcher = new GithubArtifactFetcher();

        File targetDir = new File( "target/arduino-maven-plugin/" );
        FileUtils.forceMkdir( targetDir );

        File targetFile = fetcher.fetchLibrary( descriptor, targetDir );

        assertNotNull( targetFile );
        assertTrue( targetFile.exists() );
    }
}
