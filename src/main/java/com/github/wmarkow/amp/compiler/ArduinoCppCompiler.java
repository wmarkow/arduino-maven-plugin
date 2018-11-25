package com.github.wmarkow.amp.compiler;


public class ArduinoCppCompiler extends ArduinoCompiler
{
    private final static String[] sourceExtensions = new String[]
    { ".cpp", };

    public ArduinoCppCompiler()
    {
        super( "g++", sourceExtensions );
    }
}
