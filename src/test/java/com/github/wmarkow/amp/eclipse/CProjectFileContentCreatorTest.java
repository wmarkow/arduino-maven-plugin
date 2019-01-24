package com.github.wmarkow.amp.eclipse;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CProjectFileContentCreatorTest
{

    @Test
    public void testCreateForSourcePathWithSlashes()
    {
        List< String > sourcesDirs = new ArrayList< String >();
        sourcesDirs.add( "src/main/cpp" );

        CProjectFileContentCreator creator = new CProjectFileContentCreator( "test-project", sourcesDirs );

        final String content = creator.create( "${sourceEntries}" );

        assertEquals( "<entry flags=\"VALUE_WORKSPACE_PATH\" kind=\"sourcePath\" name=\"src/main/cpp\"/>",
            content );
    }

    @Test
    public void testCreateForSourcePathWithBackslashes()
    {
        List< String > sourcesDirs = new ArrayList< String >();
        sourcesDirs.add( "src\\main\\cpp" );

        CProjectFileContentCreator creator = new CProjectFileContentCreator( "test-project", sourcesDirs );

        final String content = creator.create( "${sourceEntries}" );

        assertEquals( "<entry flags=\"VALUE_WORKSPACE_PATH\" kind=\"sourcePath\" name=\"src\\main\\cpp\"/>",
            content );
    }

    @Test
    public void testReplaceAllWithBackslash()
    {
        assertEquals( "abc", "asd".replaceAll( "asd", "a\\b\\c" ) );
    }

    @Test
    public void testReplaceAllWithBackslash2()
    {
        final String stringWithBackshlashes = "a\\b\\c";
        assertEquals( "a\\b\\c",
            "asd".replaceAll( "asd", stringWithBackshlashes.replaceAll( "\\\\", "\\\\\\\\" ) ) );
    }

    @Test
    public void test2()
    {
        File baseDir = new File( "src\\test\\resources\\project-blink" );
        File sourceDir =
            new File(
                "C:\\Users\\wmarkowski\\dev-test\\sources\\arduino-maven-plugin\\src\\test\\resources\\project-blink\\src\\main\\cpp" );

        String relative = baseDir.toURI().relativize( sourceDir.toURI() ).getPath();

        File file = new File( relative );
        
        assertEquals( "src\\main\\cpp", file.getPath() );
    }
}
