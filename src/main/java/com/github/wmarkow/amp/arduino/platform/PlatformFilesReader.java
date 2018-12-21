package com.github.wmarkow.amp.arduino.platform;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import com.github.wmarkow.amp.arduino.variable.Variable;
import com.google.gson.Gson;

public class PlatformFilesReader
{
    public PlatformPackageIndex readFromJson( File jsonFile ) throws IOException
    {
        String jsonString = FileUtils.readFileToString( jsonFile, "UTF-8" );
        Gson gson = new Gson();

        return gson.fromJson( jsonString, PlatformPackageIndex.class );
    }

    public PlatformVariables readPlatformVariablesFromFile( File file ) throws IOException
    {
        PlatformVariables result = new PlatformVariables();

        List< Variable > variables = loadFromFile( file );

        for( Variable variable : variables )
        {
            result.putVariable( variable );
        }

        return result;
    }

    public BoardsVariables readBoardsVariables( File file ) throws IOException
    {
        BoardsVariables result = new BoardsVariables();
        List< Variable > variables = loadFromFile( file );

        for( Variable variable : variables )
        {
            if( "menu.cpu".equals( variable.getName() ) )
            {
                continue;
            }

            int indexOfFirstDot = variable.getName().indexOf( '.' );
            String boardName = variable.getName().substring( 0, indexOfFirstDot );
            String newVariableName = variable.getName().substring( indexOfFirstDot + 1 );

            BoardVariables boardVariables = result.getBoardVariables( boardName );
            if( boardVariables == null )
            {
                boardVariables = new BoardVariables();
                result.addBoardVariables( boardName, boardVariables );
            }
            boardVariables.putVariable( new Variable( newVariableName, variable.getValue() ) );
        }

        return result;
    }

    private List< Variable > loadFromFile( File file ) throws IOException
    {
        Properties properties = new Properties();

        FileInputStream fis = new FileInputStream( file );

        try
        {
            properties.load( fis );
        }
        finally
        {
            if( fis != null )
            {
                fis.close();
            }
        }

        List< Variable > result = new ArrayList< Variable >();
        for( Object key : properties.keySet() )
        {
            final String name = (String)key;
            final String value = properties.getProperty( name );
            result.add( new Variable( name, value ) );
        }

        return result;
    }
}
