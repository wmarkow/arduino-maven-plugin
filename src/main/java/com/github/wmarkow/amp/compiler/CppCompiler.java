package com.github.wmarkow.amp.compiler;

public class CppCompiler extends AbstractCompiler
{
    @Override
    protected String[] getFilesExtensions()
    {
        return new String[]
        { "cpp" };
    }

}