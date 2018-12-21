package com.github.wmarkow.amp.arduino.build.compiler;

import java.io.File;
import java.io.IOException;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

public class Compiler
{
    private CppCompiler cppCompiler;
    private CCompiler cCompiler;
    private SCompiler sCompiler;

    public Compiler( Platform platform, PlatformVariables platformVariables, BoardVariables boardVariables )
    {
        cppCompiler =
            new CppCompiler( new CCompilerCommandBuilder( platform, platformVariables, boardVariables ) );
        cCompiler =
            new CCompiler( new CCompilerCommandBuilder( platform, platformVariables, boardVariables ) );
        sCompiler =
            new SCompiler( new CCompilerCommandBuilder( platform, platformVariables, boardVariables ) );
    }

    public void setCommandExecutionDirectory( File dir )
    {
        cppCompiler.setCommandExecutionDirectory( dir );
        cCompiler.setCommandExecutionDirectory( dir );
        sCompiler.setCommandExecutionDirectory( dir );
    }

    public void setObjDirectory( File dir )
    {
        cppCompiler.setObjDirectory( dir );
        cCompiler.setObjDirectory( dir );
        sCompiler.setObjDirectory( dir );
    }

    public void addSrcDirectory( File dir )
    {
        cppCompiler.addSrcDirectory( dir );
        cCompiler.addSrcDirectory( dir );
        sCompiler.addSrcDirectory( dir );
    }

    public void addIncludeDirectory( File dir )
    {
        cppCompiler.addIncludeDirectory( dir );
        cCompiler.addIncludeDirectory( dir );
        sCompiler.addIncludeDirectory( dir );
    }

    public void compile() throws IOException, InterruptedException
    {
        cppCompiler.compile();
        cCompiler.compile();
        sCompiler.compile();
    }
}
