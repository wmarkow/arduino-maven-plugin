package com.github.wmarkow.amp.arduino.build.compiler;


public class CppCompiler extends AbstractCompiler
{
    public CppCompiler( CompilerCommandBuilder aCompilerCommandBuilder )
    {
        super( aCompilerCommandBuilder );
    }

    @Override
    protected String[] getFilesExtensions()
    {
        return new String[]
        { "cpp" };
    }

}
