package com.github.wmarkow.amp.arduino.build.imager;

import java.io.File;

import org.apache.commons.io.FilenameUtils;

import com.github.wmarkow.amp.arduino.build.CommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.util.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.util.variable.Variable;
import com.github.wmarkow.amp.util.variable.VariableStorage;

public class EepromImageCommandBuilder extends CommandBuilder
{
    private final static String VAR_RECEIPE_EEPROM_IMAGE_PATTERN = "recipe.objcopy.eep.pattern";
    private final static String VAR_BUILD_PATH = "build.path";
    private final static String VAR_BUILD_PROJECT_NAME = "build.project_name";

    private File elfFile;

    public EepromImageCommandBuilder( Platform aPlatform, PlatformVariables aPlatformVariables,
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
        setInputElfFile( variableStorage, elfFile );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        return evaluator.evaluate(
            variableStorage.getVariable( VAR_RECEIPE_EEPROM_IMAGE_PATTERN ).getValue(),
            variableStorage );
    }

    public void setInputElfFile( File elfFile )
    {
        this.elfFile = elfFile;
    }

    private void setInputElfFile( VariableStorage variableStorage, File elfFile )
    {
        final String buildPath = elfFile.getParentFile().getPath();
        final String buildProjectName = FilenameUtils.getBaseName( elfFile.getName() );

        variableStorage.putVariable( new Variable( VAR_BUILD_PATH, buildPath ) );
        variableStorage.putVariable( new Variable( VAR_BUILD_PROJECT_NAME, buildProjectName ) );
    }
}
