package com.github.wmarkow.amp.eclipse;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CProjectFileContentCreator extends AbstractProjectFileContentCreator
{
    private final static String RANDOM_ID_1 = "randomId1";
    private final static String RANDOM_ID_2 = "randomId2";
    private final static String RANDOM_ID_3 = "randomId3";
    private final static String RANDOM_ID_4 = "randomId4";
    private final static String RANDOM_ID_5 = "randomId5";
    private final static String RANDOM_ID_6 = "randomId6";
    private final static String RANDOM_ID_7 = "randomId7";
    private final static String RANDOM_ID_8 = "randomId8";
    private final static String RANDOM_ID_9 = "randomId9";
    private final static String RANDOM_ID_10 = "randomId10";
    private final static String RANDOM_ID_11 = "randomId11";
    private final static String RANDOM_ID_12 = "randomId12";
    private final static String RANDOM_ID_13 = "randomId13";
    private final static String RANDOM_ID_14 = "randomId14";
    private final static String RANDOM_ID_15 = "randomId15";
    private final static String SOURCE_ENTRIES_VARIABLE_NAME = "sourceEntries";
    private final static String SOURCE_ENTRY_TEMPLATE =
        "<entry flags=\"VALUE_WORKSPACE_PATH\" kind=\"sourcePath\" name=\"%s\"/>";

    private List< String > sourcesDirs = new ArrayList<>();
    private Random randomNumber;

    public CProjectFileContentCreator( String projectName, List< String > sourcesDirs )
    {
        super( projectName );

        this.sourcesDirs.addAll( sourcesDirs );
    }

    @Override
    public String create( String template )
    {
        String templateWithProjectName =
            replaceVariable( template, PROJECT_NAME_VARIABLE_NAME, getProjectName() );

        String temp =
            replaceVariable( templateWithProjectName, SOURCE_ENTRIES_VARIABLE_NAME,
                createSourceEntries( sourcesDirs ) );

        temp = replaceVariable( temp, RANDOM_ID_1, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_2, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_3, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_4, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_5, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_6, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_7, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_8, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_9, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_10, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_11, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_12, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_13, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_14, String.valueOf( getRandomNumber() ) );
        temp = replaceVariable( temp, RANDOM_ID_15, String.valueOf( getRandomNumber() ) );

        return temp;
    }

    private String createSourceEntries( List< String > sourcesDirs )
    {
        StringBuilder sb = new StringBuilder();

        for( int q = 0; q < sourcesDirs.size(); q++ )
        {
            String sourceDir = sourcesDirs.get( q );

            sb.append( createSourceEntry( sourceDir ) );
            if( q < sourcesDirs.size() - 1 )
            {
                sb.append( "\n\r" );
            }
        }
        getRandomNumber();
        return sb.toString();
    }

    private String createSourceEntry( String sourcePath )
    {
        String result = String.format( SOURCE_ENTRY_TEMPLATE, sourcePath );

        return result.replaceAll( "\\\\", "\\\\\\\\" );
    }

    /***
     * Generates a random number. Based on
     * {@link org.eclipse.cdt.managedbuilder.core.ManagedBuildManager#getRandomNumber}
     * 
     * @return
     */
    private int getRandomNumber()
    {
        if( randomNumber == null )
        {
            randomNumber = new Random();
            randomNumber.setSeed( System.currentTimeMillis() );
        }

        return Math.abs( randomNumber.nextInt() );
    }
}
