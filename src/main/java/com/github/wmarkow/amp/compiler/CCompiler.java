package com.github.wmarkow.amp.compiler;

public class CCompiler extends AbstractCompiler
{
    @Override
    protected String[] getFilesExtensions()
    {
        return new String[]
        { "c" };
    }

}
