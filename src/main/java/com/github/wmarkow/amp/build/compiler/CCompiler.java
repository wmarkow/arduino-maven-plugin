package com.github.wmarkow.amp.build.compiler;


public class CCompiler extends AbstractCompiler
{
    @Override
    protected String[] getFilesExtensions()
    {
        return new String[]
        { "c" };
    }

}
