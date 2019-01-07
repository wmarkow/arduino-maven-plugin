package com.github.wmarkow.amp.arduino.build.linker;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.github.wmarkow.amp.arduino.build.CommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.arduino.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.arduino.variable.Variable;
import com.github.wmarkow.amp.arduino.variable.VariableStorage;

public class LinkerCommandBuilder extends CommandBuilder
{
    private final static String VAR_RECEIPE_LINK_PATTERN = "recipe.c.combine.pattern";
    private final static String VAR_OBJECT_FILES = "object_files";
    private final static String VAR_BUILD_PATH = "build.path";
    private final static String VAR_BUILD_PROJECT_NAME = "build.project_name";
    private final static String VAR_ARCHIVE_FILE = "archive_file";
    private final static String VAR_ARCHIVE_FILE_PATH = "archive_file_path";

    private File objDir;
    private File targetElfFile;

    public LinkerCommandBuilder( Platform aPlatform, PlatformVariables aPlatformVariables,
        BoardVariables aBoardVariables )
    {
        super( aPlatform, aPlatformVariables, aBoardVariables );
    }

    @Override
    public String buildCommand()
    {
        setBuildArch( platform, variableStorage );
        setRuntimeIdeVersion( variableStorage );
        setCompilerPath( variableStorage );
        setObjectFiles( variableStorage, objDir );
        setArchiveFile( variableStorage, targetElfFile );
        setTargetElfFile( variableStorage, targetElfFile );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        return evaluator.evaluate( variableStorage.getVariable( VAR_RECEIPE_LINK_PATTERN ).getValue(),
            variableStorage );
    }

    public void setObjectsDir( File objDir )
    {
        this.objDir = objDir;
    }

    public void setOutputElfFile( File elfFile )
    {
        this.targetElfFile = elfFile;
    }

    private void setObjectFiles( VariableStorage variableStorage, File objDir )
    {
        Collection< File > objFiles = FileUtils.listFiles( objDir, new String[]
        { "o" }, true );
        StringBuilder sb = new StringBuilder();
        for( File objFile : objFiles )
        {
            sb.append( objFile.getPath() );
            sb.append( " " );
        }
        final String objectFiles = sb.toString();

        variableStorage.putVariable( new Variable( VAR_OBJECT_FILES, objectFiles ) );
    }

    private void setArchiveFile( VariableStorage variableStorage, File targetElfFile )
    {
        final String buildPath = targetElfFile.getParentFile().getPath();
        final String archiveFileName = FilenameUtils.getBaseName( targetElfFile.getName() ) + ".ar";

        variableStorage.putVariable( new Variable( VAR_ARCHIVE_FILE, archiveFileName ) );
        variableStorage.putVariable( new Variable( VAR_ARCHIVE_FILE_PATH, new File( buildPath,
            archiveFileName ).getPath() ) );
    }

    private void setTargetElfFile( VariableStorage variableStorage, File targetElfFile )
    {
        final String buildPath = targetElfFile.getParentFile().getPath();
        final String buildProjectName = FilenameUtils.getBaseName( targetElfFile.getName() );

        variableStorage.putVariable( new Variable( VAR_BUILD_PATH, buildPath ) );
        variableStorage.putVariable( new Variable( VAR_BUILD_PROJECT_NAME, buildProjectName ) );
    }
}
