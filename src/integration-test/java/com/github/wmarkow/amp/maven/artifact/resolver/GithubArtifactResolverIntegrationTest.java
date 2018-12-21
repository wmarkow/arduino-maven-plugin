package com.github.wmarkow.amp.maven.artifact.resolver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.github.wmarkow.amp.maven.artifact.resolver.GithubArtifactResolver;
import com.github.wmarkow.amp.maven.artifact.resolver.GithubFetchDescriptor;

public class GithubArtifactResolverIntegrationTest
{
    @Test
    public void testFetchLibraryForArduinoCoreFromExistingTag() throws IOException
    {
        GithubFetchDescriptor descriptor = new GithubFetchDescriptor();

        descriptor.username = "arduino";
        descriptor.repoName = "ArduinoCore-avr";
        descriptor.refName = "1.6.23";

        GithubArtifactResolver fetcher = new GithubArtifactResolver();

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

        GithubArtifactResolver fetcher = new GithubArtifactResolver();

        File targetDir = new File( "target/arduino-maven-plugin/" );
        FileUtils.forceMkdir( targetDir );

        File targetFile = fetcher.fetchLibrary( descriptor, targetDir );

        assertNotNull( targetFile );
        assertTrue( targetFile.exists() );
    }
}
