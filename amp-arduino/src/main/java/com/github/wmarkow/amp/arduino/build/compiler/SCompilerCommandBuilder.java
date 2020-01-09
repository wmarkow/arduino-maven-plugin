package com.github.wmarkow.amp.arduino.build.compiler;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

public class SCompilerCommandBuilder extends CompilerCommandBuilder
{
    private final static String RECEIPE_S_O_PATTERN = "recipe.S.o.pattern";

    public SCompilerCommandBuilder( Platform aPlatform, PlatformVariables aPlatformVariables,
        BoardVariables aBoardVariables )
    {
        super( aPlatform, aPlatformVariables, aBoardVariables );
    }

    @Override
    protected String getCompileReceipePatternVariableName()
    {
        return RECEIPE_S_O_PATTERN;
    }
}
