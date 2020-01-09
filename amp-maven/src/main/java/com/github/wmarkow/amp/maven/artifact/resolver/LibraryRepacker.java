package com.github.wmarkow.amp.maven.artifact.resolver;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.util.CompressUtil;

public class LibraryRepacker
{
    private Logger logger = LoggerFactory.getLogger( LibraryRepacker.class );

    public void repack( File inputZipFile, String folderToUnpack, File outputZipFile ) throws IOException
    {
        if( inputZipFile == null )
        {
            throw new IllegalArgumentException( "Input ZIP file must not be null" );
        }

        logger.info( String.format( "Repack the library into %s ", outputZipFile.getAbsolutePath() ) );

        final File workDir = inputZipFile.getParentFile();

        // unzip input file to a temporary file
        final File unzipDir = new File( inputZipFile.getAbsolutePath() + "_unzip" );
        CompressUtil.unpack( inputZipFile, unzipDir );

        // copy specific folder from source directory into a destination directory
        final File dstDir = new File( workDir, outputZipFile.getName() + "_repacked" );

        if( dstDir.exists() )
        {
            FileUtils.cleanDirectory( dstDir );
        }

        File folderToCopy = new File( unzipDir, folderToUnpack );
        if( folderToCopy.exists() )
        {

        }
        else
        {
            String[] subfiles = unzipDir.list();
            if( subfiles.length != 1 )
            {
                throw new IllegalArgumentException( String.format(
                    "Input ZIP file %s contains invalid file lauout", folderToCopy.getAbsolutePath() ) );
            }

            folderToCopy = new File( unzipDir, subfiles[ 0 ] + "/" + folderToUnpack );
        }

        if( hasSubfolder( folderToCopy, "src" ) )
        {
            // 'src' folder already exist, copy it as well
            FileUtils.copyDirectory( folderToCopy, dstDir );
        }
        else
        {
            File dstDirWithSrc = new File( dstDir, "src" );
            FileUtils.forceMkdir( dstDirWithSrc );
            FileUtils.copyDirectory( folderToCopy, dstDirWithSrc );
        }

        // pack destination directory into ZIP
        CompressUtil.packZip( dstDir, outputZipFile );
    }

    private boolean hasSubfolder( File rootFolder, String subfolder )
    {
        File subdirFile = new File( rootFolder, subfolder );

        return subdirFile.exists();
    }
}
