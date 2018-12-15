package com.github.wmarkow.amp.build.compiler;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.variable.MultiVariableStorage;
import com.github.wmarkow.amp.variable.Variable;
import com.github.wmarkow.amp.variable.VariableStorage;

public class CCompilerCommandBuilder
{
    private final static String RECEIPE_C_O_PATTERN = "recipe.c.o.pattern";
    private final static String BUILD_ARCH = "build.arch";

    private MultiVariableStorage variableStorage;
    private Platform platform;

    public CCompilerCommandBuilder( Platform platform, PlatformVariables platformVariables,
        BoardVariables boardVariables )
    {
        if( platformVariables == null )
        {
            throw new IllegalArgumentException( "Platform variables must not be null" );
        }
        if( boardVariables == null )
        {
            throw new IllegalArgumentException( "Board variables must not be null" );
        }
        if( platform == null )
        {
            throw new IllegalArgumentException( "Platform must not be null" );
        }

        this.platform = platform;
        variableStorage = MultiVariableStorage.populateFrom( platformVariables, boardVariables );
    }

    public String buildCommand()
    {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        setBuildArch( platform, variableStorage );

        final String compilePatternExpression = variableStorage.getVariable( RECEIPE_C_O_PATTERN ).getValue();

        return evaluator.evaluate( compilePatternExpression, variableStorage );
    }

    /***
     * A way to set build.arch variable. More details here: https://github.com/arduino/Arduino/issues/4878
     * 
     * @param platform
     * @param storage
     */
    private void setBuildArch( Platform platform, VariableStorage storage )
    {
        final String buildArch = platform.getArchitecture().toUpperCase();

        variableStorage.addVariable( new Variable( BUILD_ARCH, buildArch ) );
    }
}
