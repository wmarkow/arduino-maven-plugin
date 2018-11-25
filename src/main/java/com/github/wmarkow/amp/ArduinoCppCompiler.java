package com.github.wmarkow.amp;

public class ArduinoCppCompiler extends ArduinoCompiler
{
    private final static String[] sourceExtensions = new String[]
    { ".cpp", };

    public ArduinoCppCompiler()
    {
        super( "g++", sourceExtensions );
    }
}
