package com.github.wmarkow.amp.arduino.build.compiler;


public class SCompiler extends AbstractCompiler
{
    public SCompiler( CompilerCommandBuilder aCompilerCommandBuilder )
    {
        super( aCompilerCommandBuilder );
    }

    @Override
    protected String[] getFilesExtensions()
    {
        return new String[]
        { "S" };
    }

}
