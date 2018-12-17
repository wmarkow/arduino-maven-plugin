package com.github.wmarkow.amp.build.compiler;


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
