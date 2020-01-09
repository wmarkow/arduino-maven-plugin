package com.github.wmarkow.amp.eclipse;

public class ProjectFileContentCreator extends AbstractProjectFileContentCreator
{
    public ProjectFileContentCreator( String projectName )
    {
        super( projectName );
    }

    @Override
    public String create( String template )
    {
        return replaceVariable( template, PROJECT_NAME_VARIABLE_NAME, getProjectName() );
    }
}
