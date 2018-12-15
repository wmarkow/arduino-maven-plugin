package com.github.wmarkow.amp.build.compiler;

import java.io.File;
import java.util.List;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.variable.MultiVariableStorage;
import com.github.wmarkow.amp.variable.Variable;
import com.github.wmarkow.amp.variable.VariableStorage;

public abstract class CompilerCommandBuilder
{
    private final static String VAR_BUILD_ARCH = "build.arch";
    private final static String VAR_INCLUDES = "includes";
    private final static String VAR_SOURCE_FILE = "source_file";
    private final static String VAR_OBJECT_FILE = "object_file";
    private final static String VAR_RUNTIME_IDE_VERSION = "runtime.ide.version";

    private MultiVariableStorage variableStorage;
    private Platform platform;

    public CompilerCommandBuilder( Platform platform, PlatformVariables platformVariables,
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

    public String buildCommand()
    {
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        setBuildArch( platform, variableStorage );
        setRuntimeIdeVersion( variableStorage );

        final String compileReceipePatternVariableName = getCompileReceipePatternVariableName();
        final String compilePatternExpression =
            variableStorage.getVariable( compileReceipePatternVariableName ).getValue();

        return evaluator.evaluate( compilePatternExpression, variableStorage );
    }

    protected abstract String getCompileReceipePatternVariableName();

    /***
     * A way to set build.arch variable. More details here: https://github.com/arduino/Arduino/issues/4878
     * 
     * @param platform
     * @param storage
     */
    private void setBuildArch( Platform platform, VariableStorage storage )
    {
        final String buildArch = platform.getArchitecture().toUpperCase();

        variableStorage.putVariable( new Variable( VAR_BUILD_ARCH, buildArch ) );
    }

    private void setRuntimeIdeVersion( VariableStorage storage )
    {
        variableStorage.putVariable( new Variable( VAR_RUNTIME_IDE_VERSION, "10609" ) );
    }
}
