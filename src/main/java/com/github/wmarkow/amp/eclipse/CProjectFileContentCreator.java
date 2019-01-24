package com.github.wmarkow.amp.eclipse;

import java.util.ArrayList;
import java.util.List;

public class CProjectFileContentCreator extends AbstractProjectFileContentCreator
{
    private final static String SOURCE_ENTRIES_VARIABLE_NAME = "sourceEntries";
    private final static String SOURCE_ENTRY_TEMPLATE =
        "<entry flags=\"VALUE_WORKSPACE_PATH\" kind=\"sourcePath\" name=\"%s\"/>";

    private List< String > sourcesDirs = new ArrayList<>();

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

        return replaceVariable( templateWithProjectName, SOURCE_ENTRIES_VARIABLE_NAME,
            createSourceEntries( sourcesDirs ) );
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

        return sb.toString();
    }

    private String createSourceEntry( String sourcePath )
    {
        String result = String.format( SOURCE_ENTRY_TEMPLATE, sourcePath );

        return result.replaceAll( "\\\\", "\\\\\\\\" );
    }
}
