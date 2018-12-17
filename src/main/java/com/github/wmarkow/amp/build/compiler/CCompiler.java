package com.github.wmarkow.amp.build.compiler;


public class CCompiler extends AbstractCompiler
{
    public CCompiler( CompilerCommandBuilder aCompilerCommandBuilder )
    {
        super( aCompilerCommandBuilder );
    }

    @Override
    protected String[] getFilesExtensions()
    {
        return new String[]
        { "c" };
    }

}
