package com.github.wmarkow.amp.eclipse;

public class CProjectFileContentCreator extends AbstractProjectFileContentCreator
{

    public CProjectFileContentCreator( String projectName )
    {
        super( projectName );
    }

    @Override
    public String create( String template )
    {
        return replaceVariable( template, PROJECT_NAME_VARIABLE_NAME, getProjectName() );
    }
}
