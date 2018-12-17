package com.github.wmarkow.amp.build.compiler;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

public class CppCompilerCommandBuilder extends CompilerCommandBuilder
{
    private final static String RECEIPE_CPP_O_PATTERN = "recipe.cpp.o.pattern";

    public CppCompilerCommandBuilder( Platform aPlatform, PlatformVariables aPlatformVariables,
        BoardVariables aBoardVariables )
    {
        super( aPlatform, aPlatformVariables, aBoardVariables );
    }

    @Override
    protected String getCompileReceipePatternVariableName()
    {
        return RECEIPE_CPP_O_PATTERN;
    }
}
