package com.github.wmarkow.amp.build.compiler;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Compiler
{
    private CppCompiler cppCompiler = new CppCompiler();
    private CCompiler cCompiler = new CCompiler();
    private SCompiler sCompiler = new SCompiler();

    public void setCppCompilerCommand( String command )
    {
        cppCompiler.setCommand( command );
    }

    public void setCCompilerCommand( String command )
    {
        cCompiler.setCommand( command );
    }

    public void setSCompilerCommand( String command )
    {
        sCompiler.setCommand( command );
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

    public void addCppCompilerArgs( List< String > args )
    {
        cppCompiler.addCommandArgs( args );
    }

    public void addCCompilerArgs( List< String > args )
    {
        cCompiler.addCommandArgs( args );
    }

    public void addSCompilerArgs( List< String > args )
    {
        sCompiler.addCommandArgs( args );
    }

    public void compile() throws IOException, InterruptedException
    {
        cppCompiler.compile();
        cCompiler.compile();
        sCompiler.compile();
    }
}
