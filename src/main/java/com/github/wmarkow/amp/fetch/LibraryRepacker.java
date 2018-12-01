package com.github.wmarkow.amp.fetch;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.zeroturnaround.zip.ZipUtil;

public class LibraryRepacker
{
    public void repack( File inputZipFile, String folderToUnpack, File outputZipFile ) throws IOException
    {
        if( inputZipFile == null )
        {
            throw new IllegalArgumentException( "Input ZIP file must not be null" );
        }

        final File workDir = inputZipFile.getParentFile();

        // unzip input file to a temporary file
        final File unzipDir = new File( inputZipFile.getAbsolutePath() + "_unzip" );
        ZipUtil.unwrap( inputZipFile, unzipDir );

        // copy specific folder from source directory into a destination directory
        final File dstDir = new File( workDir, "extracted_arduino_library" );
        FileUtils.copyDirectory( new File( unzipDir, folderToUnpack ), dstDir );

        // pack destination directory into ZIP
        ZipUtil.pack( dstDir, outputZipFile );
    }
}
