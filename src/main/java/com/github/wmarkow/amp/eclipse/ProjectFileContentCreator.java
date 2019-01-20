package com.github.wmarkow.amp.eclipse;

public class ProjectFileContentCreator
{
    private final static String PROJECT_NAME_VARIABLE_NAME = "projectName";
    private String projectName;

    public ProjectFileContentCreator( String projectName )
    {
        if( projectName == null )
        {
            throw new IllegalArgumentException( "Project name must not be null." );
        }
        this.projectName = projectName;
    }

    public String create( String template )
    {
        return template.replaceAll( "\\$\\{" + PROJECT_NAME_VARIABLE_NAME + "\\}", projectName );
    }
}
