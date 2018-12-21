package com.github.wmarkow.amp.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Collection;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class CompressUtil
{
    public static boolean unpack( File inputFile, File targetDir ) throws IOException
    {
        boolean result = unpackGeneric( inputFile, targetDir );

        if( result )
        {
            return true;
        }

        return unpackTarBzip2( inputFile, targetDir );
    }

    public static boolean packZip( File dir, File outputFile ) throws IOException
    {
        OutputStream archiveStream = new FileOutputStream( outputFile );
        ArchiveOutputStream archive;
        try
        {
            archive =
                new ArchiveStreamFactory()
                    .createArchiveOutputStream( ArchiveStreamFactory.ZIP, archiveStream );
        }
        catch( ArchiveException e )
        {
            throw new IOException( e.getMessage(), e );
        }

        Collection< File > fileList = FileUtils.listFiles( dir, null, true );

        for( File file : fileList )
        {
            int index = dir.getAbsolutePath().length() + 1;
            String path = file.getCanonicalPath();
            String entryName = path.substring( index );

            ZipArchiveEntry entry = new ZipArchiveEntry( entryName );
            archive.putArchiveEntry( entry );

            BufferedInputStream input = new BufferedInputStream( new FileInputStream( file ) );

            IOUtils.copy( input, archive );
            input.close();
            archive.closeArchiveEntry();
        }

        archive.finish();
        archiveStream.close();

        return true;
    }

    private static boolean unpackGeneric( File inputFile, File targetDir ) throws IOException
    {
        InputStream fi = null;
        InputStream bi = null;
        ArchiveInputStream i = null;

        try
        {
            fi = Files.newInputStream( inputFile.toPath() );
            bi = new BufferedInputStream( fi );
            i = new ArchiveStreamFactory().createArchiveInputStream( bi );
        }
        catch( ArchiveException e1 )
        {
            bi.close();
            fi.close();

            return false;
        }

        try
        {
            unpack( i, targetDir );
        }
        finally
        {
            if( i != null )
            {
                i.close();
            }
            if( bi != null )
            {
                bi.close();
            }
            if( fi != null )
            {
                fi.close();
            }
        }

        return true;
    }

    private static boolean unpackTarBzip2( File inputFile, File targetDir ) throws IOException
    {
        final String fileBase = FilenameUtils.getBaseName( inputFile.getName() );
        final File tarFile = new File( targetDir, fileBase + ".tar" );

        InputStream fin = null;
        BufferedInputStream in = null;
        OutputStream out = null;
        BZip2CompressorInputStream bzIn = null;
        try
        {
            fin = Files.newInputStream( inputFile.toPath() );
            in = new BufferedInputStream( fin );

            out = Files.newOutputStream( tarFile.toPath() );

            bzIn = new BZip2CompressorInputStream( in );
            final byte[] buffer = new byte[ 512 ];
            int n = 0;
            while( -1 != (n = bzIn.read( buffer )) )
            {
                out.write( buffer, 0, n );
            }
        }
        finally
        {
            if( out != null )
            {
                out.close();
            }
            if( bzIn != null )
            {
                bzIn.close();
            }
            if( in != null )
            {
                in.close();
            }
            if( fin != null )
            {
                fin.close();
            }
        }

        // now unpack from tar
        boolean result = unpackGeneric( tarFile, targetDir );

        tarFile.delete();

        return result;
    }

    private static void unpack( ArchiveInputStream i, File targetDir ) throws IOException
    {
        ArchiveEntry entry = null;
        while( (entry = i.getNextEntry()) != null )
        {
            if( !i.canReadEntryData( entry ) )
            {
                // log something?
                continue;
            }
            String name = fileName( targetDir, entry );
            File f = new File( name );
            if( entry.isDirectory() )
            {
                if( !f.isDirectory() && !f.mkdirs() )
                {
                    throw new IOException( "failed to create directory " + f );
                }
            }
            else
            {
                File parent = f.getParentFile();
                if( !parent.isDirectory() && !parent.mkdirs() )
                {
                    throw new IOException( "failed to create directory " + parent );
                }
                try (OutputStream o = Files.newOutputStream( f.toPath() ))
                {
                    IOUtils.copy( i, o );
                }
            }
        }
    }

    private static String fileName( File targetDir, ArchiveEntry entry )
    {
        return new File( targetDir, entry.getName() ).getPath();
    }
}
