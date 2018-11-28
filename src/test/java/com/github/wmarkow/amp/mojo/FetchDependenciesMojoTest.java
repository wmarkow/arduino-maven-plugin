package com.github.wmarkow.amp.mojo;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

public class FetchDependenciesMojoTest extends AbstractMojoTestCase
{

    @Override
    protected void setUp() throws Exception
    {
        // required for mojo lookups to work
        super.setUp();
    }

    @Test
    public void testMojoGoal() throws Exception
    {
        File pom = new File( getBasedir(), "src/test/resources/project-blink/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        FetchDependenciesMojo mojo = (FetchDependenciesMojo)lookupMojo( "download-dependencies", pom );

        // FetchDependenciesMojo mojo = new FetchDependenciesMojo();
        // mojo = (FetchDependenciesMojo) configureMojo(
        // mojo,
        // extractPluginConfiguration( "arduino-maven-plugin", testPom ) );
        // mojo.execute();
        
        assertNotNull( mojo );
    }
}

