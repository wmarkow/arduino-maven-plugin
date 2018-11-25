package com.github.wmarkow.amp;

public class ArduinoCCompiler extends ArduinoCompiler
{
    private final static String[] sourceExtensions = new String[]
    { ".c", };

    public ArduinoCCompiler()
    {
        super( "gcc", sourceExtensions );
    }
}
