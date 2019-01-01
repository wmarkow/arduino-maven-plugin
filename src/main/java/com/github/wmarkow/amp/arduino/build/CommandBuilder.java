package com.github.wmarkow.amp.arduino.build;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.arduino.variable.MultiVariableStorage;
import com.github.wmarkow.amp.arduino.variable.Variable;
import com.github.wmarkow.amp.arduino.variable.VariableStorage;

public abstract class CommandBuilder
{
    private final static String VAR_BUILD_ARCH = "build.arch";
    private final static String VAR_RUNTIME_IDE_VERSION = "runtime.ide.version";
    private final static String VAR_COMPILER_PATH = "compiler.path";

    protected MultiVariableStorage variableStorage;
    protected Platform platform;

    private String toolchainBinDirPath;

    public CommandBuilder( Platform platform, PlatformVariables platformVariables,
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

    public abstract String buildCommand();

    public void setToolchainBinDirPath( String toolchainBinDirPath )
    {
        this.toolchainBinDirPath = toolchainBinDirPath;
    }

    /***
     * A way to set build.arch variable. More details here: https://github.com/arduino/Arduino/issues/4878
     * 
     * @param platform
     * @param storage
     */
    protected void setBuildArch( Platform platform, VariableStorage storage )
    {
        final String buildArch = platform.getArchitecture().toUpperCase();

        variableStorage.putVariable( new Variable( VAR_BUILD_ARCH, buildArch ) );
    }

    protected void setRuntimeIdeVersion( VariableStorage storage )
    {
        variableStorage.putVariable( new Variable( VAR_RUNTIME_IDE_VERSION, "10609" ) );
    }

    protected void setCompilerPath( VariableStorage storage )
    {
        variableStorage.putVariable( new Variable( VAR_COMPILER_PATH, this.toolchainBinDirPath ) );
    }
}
