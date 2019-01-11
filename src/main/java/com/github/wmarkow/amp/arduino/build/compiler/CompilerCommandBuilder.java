package com.github.wmarkow.amp.arduino.build.compiler;

import java.io.File;
import java.util.List;

import com.github.wmarkow.amp.arduino.build.CommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.arduino.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.arduino.variable.Variable;

public abstract class CompilerCommandBuilder extends CommandBuilder
{
    private final static String VAR_INCLUDES = "includes";
    private final static String VAR_SOURCE_FILE = "source_file";
    private final static String VAR_OBJECT_FILE = "object_file";


    public CompilerCommandBuilder( Platform aPlatform, PlatformVariables aPlatformVariables,
        BoardVariables aBoardVariables )
    {
        super( aPlatform, aPlatformVariables, aBoardVariables );
    }

    public void setSourceFile( File srcFile )
    {
        variableStorage.putVariable( new Variable( VAR_SOURCE_FILE, srcFile.getPath() ) );
    }

    public void setIncludes( List< File > includes )
    {
        StringBuilder sb = new StringBuilder();

        for( File incDir : includes )
        {
            sb.append( "-I" );
            sb.append( incDir.getPath() );
            sb.append( " " );
        }

        variableStorage.putVariable( new Variable( VAR_INCLUDES, sb.toString() ) );
    }

    public void setObjectFile( File objFile )
    {
        variableStorage.putVariable( new Variable( VAR_OBJECT_FILE, objFile.getPath() ) );
    }

    @Override
    public String buildCommand()
    {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        setBuildArch( platform, variableStorage );
        setRuntimeIdeVersion( variableStorage );
        setCompilerPath( variableStorage );
        setRuntimePlatformPath( variableStorage );

        final String compileReceipePatternVariableName = getCompileReceipePatternVariableName();
        final String compilePatternExpression =
            variableStorage.getVariable( compileReceipePatternVariableName ).getValue();

        return evaluator.evaluate( compilePatternExpression, variableStorage );
    }

    protected abstract String getCompileReceipePatternVariableName();
}
