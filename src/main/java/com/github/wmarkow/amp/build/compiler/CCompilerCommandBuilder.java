package com.github.wmarkow.amp.build.compiler;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

public class CCompilerCommandBuilder extends CompilerCommandBuilder
{
    private final static String RECEIPE_C_O_PATTERN = "recipe.c.o.pattern";

    public CCompilerCommandBuilder( Platform aPlatform, PlatformVariables aPlatformVariables,
        BoardVariables aBoardVariables )
    {
        super( aPlatform, aPlatformVariables, aBoardVariables );
    }

    @Override
    protected String getCompileReceipePatternVariableName()
    {
        return RECEIPE_C_O_PATTERN;
    }
}
