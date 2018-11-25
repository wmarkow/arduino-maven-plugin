package com.github.wmarkow.amp;

import com.github.maven_nar.cpptasks.LinkerDef;
import com.github.maven_nar.cpptasks.compiler.Processor;
import com.github.maven_nar.cpptasks.types.LinkerArgument;

public class ArduinoLinkerDef extends LinkerDef
{
    public ArduinoLinkerDef()
    {
        super();

        setLinkerPrefix( "avr-" );
        
        addLinkerArg( "-Wall" );
        addLinkerArg( "-Wextra" );
        addLinkerArg( "-Os" );
        addLinkerArg( "-g" );
        addLinkerArg( "-flto" );
        addLinkerArg( "-fuse-linker-plugin" );
        addLinkerArg( "-Wl,--gc-sections" );
        addLinkerArg( "-mmcu=atmega328p" );
    }

    @Override
    public Processor getProcessor()
    {
        return new ArduinoLinker();
    }

    private void addLinkerArg( String param )
    {
        LinkerArgument cp = new LinkerArgument();
        cp.setValue( param );

        this.addConfiguredLinkerArg( cp );
    }
}
