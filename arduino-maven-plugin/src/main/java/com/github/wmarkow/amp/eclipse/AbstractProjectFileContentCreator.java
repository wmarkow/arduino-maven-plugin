package com.github.wmarkow.amp.eclipse;

abstract class AbstractProjectFileContentCreator
{
    protected final static String PROJECT_NAME_VARIABLE_NAME = "projectName";
    private String projectName;

    public AbstractProjectFileContentCreator( String projectName )
    {
        if( projectName == null )
        {
            throw new IllegalArgumentException( "Project name must not be null." );
        }
        this.projectName = projectName;
    }

    abstract public String create( String template );

    protected String getProjectName()
    {
        return projectName;
    }

    protected String replaceVariable( String template, String variableName, String variableValue )
    {
        return template.replaceAll( "\\$\\{" + variableName + "\\}", variableValue );
    }
}
