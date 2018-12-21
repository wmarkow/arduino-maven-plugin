package com.github.wmarkow.amp.arduino.build.archiver;

import java.io.File;

import com.github.wmarkow.amp.arduino.build.CommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.variable.Variable;
import com.github.wmarkow.amp.variable.VariableStorage;

public class ArchiverCommandBuilder extends CommandBuilder
{
    private final static String VAR_RECEIPE_ARCHIVE_PATTERN = "recipe.ar.pattern";
    private final static String VAR_ARCHIVE_FILE_PATH = "archive_file_path";
    private final static String VAR_OBJECT_FILE = "object_file";

    private File objFile;
    private File targetArchiveFile;

    public ArchiverCommandBuilder( Platform aPlatform, PlatformVariables aPlatformVariables,
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
        setObjectFile( variableStorage, objFile );
        setTargetArchiveFile( variableStorage, targetArchiveFile );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        return evaluator.evaluate( variableStorage.getVariable( VAR_RECEIPE_ARCHIVE_PATTERN ).getValue(),
            variableStorage );
    }

    public void setObjFile( File objFile )
    {
        this.objFile = objFile;
    }

    public void setOutputArchiveFile( File file )
    {
        this.targetArchiveFile = file;
    }

    private void setObjectFile( VariableStorage variableStorage, File objFile )
    {
        variableStorage.putVariable( new Variable( VAR_OBJECT_FILE, objFile.getPath() ) );
    }

    private void setTargetArchiveFile( VariableStorage variableStorage, File targetArchiveFile )
    {
        variableStorage.putVariable( new Variable( VAR_ARCHIVE_FILE_PATH, targetArchiveFile.getPath() ) );
    }
}
