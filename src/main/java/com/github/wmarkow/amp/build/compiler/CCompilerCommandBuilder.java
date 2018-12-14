package com.github.wmarkow.amp.build.compiler;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.variable.MultiVariableStorage;

public class CCompilerCommandBuilder
{
    private final static String RECEIPE_C_O_PATTERN = "recipe.c.o.pattern";
    private MultiVariableStorage variableStorage;

    public CCompilerCommandBuilder( PlatformVariables platformVariables, BoardVariables boardVariables )
    {
        if( platformVariables == null )
        {
            throw new IllegalArgumentException( "Platform variables must not be null" );
        }
        if( boardVariables == null )
        {
            throw new IllegalArgumentException( "Board variables must not be null" );
        }

        variableStorage = MultiVariableStorage.populateFrom( platformVariables, boardVariables );
    }

    public String buildCommand()
    {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String compilePatternExpression = variableStorage.getVariable( RECEIPE_C_O_PATTERN ).getValue();

        return evaluator.evaluate( compilePatternExpression, variableStorage );
    }
}
