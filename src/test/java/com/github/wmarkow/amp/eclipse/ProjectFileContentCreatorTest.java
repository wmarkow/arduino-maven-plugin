package com.github.wmarkow.amp.eclipse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

public class ProjectFileContentCreatorTest
{

    @Test
    public void testCreateForSimpleTemplate()
    {
        ProjectFileContentCreator creator = new ProjectFileContentCreator( "test-project" );

        final String content = creator.create( "<name>${projectName}</name>" );

        assertEquals( "<name>test-project</name>", content );
    }

    @Test
    public void testForTemplateFileExists()
    {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File( classLoader.getResource( "project-template" ).getFile() );

        assertTrue( file.exists() );
    }
}


