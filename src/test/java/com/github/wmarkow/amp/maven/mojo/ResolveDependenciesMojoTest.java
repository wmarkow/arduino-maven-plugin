package com.github.wmarkow.amp.maven.mojo;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Test;

import com.github.wmarkow.amp.maven.mojo.ResolveDependenciesMojo;

public class ResolveDependenciesMojoTest extends AbstractMojoTestCase
{

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    @Test
    public void testMojoGoal() throws Exception
    {
        File pom = new File( getBasedir(), "src/test/resources/project-blink/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        ResolveDependenciesMojo mojo = (ResolveDependenciesMojo)lookupMojo( "fetch-dependencies", pom );

        assertNotNull( mojo );
    }
}

