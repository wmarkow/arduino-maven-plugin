package com.github.wmarkow.amp.linker;

import com.github.maven_nar.cpptasks.compiler.LinkType;
import com.github.maven_nar.cpptasks.compiler.Linker;
import com.github.maven_nar.cpptasks.gcc.AbstractLdLinker;

public class ArduinoLinker extends AbstractLdLinker
{
    private static final String[] objFilesIgnoredExtensions = new String[ 0 ];

    private static final String[] objFilesExtensions = new String[]
    { ".o" };

    public ArduinoLinker()
    {
        this( "g++", "-dumpversion", objFilesExtensions, objFilesIgnoredExtensions, "", ".elf", false, null );
    }

    protected ArduinoLinker( String aCommand, String aIdentifierArg, String[] aExtensions,
        String[] aIgnoredExtensions, String aOutputPrefix, String aOutputSuffix, boolean aIsLibtool,
        AbstractLdLinker aLibtoolLinker )
    {
        super( aCommand, aIdentifierArg, aExtensions, aIgnoredExtensions, aOutputPrefix, aOutputSuffix,
            aIsLibtool, aLibtoolLinker );
    }

    @Override
    public Linker getLinker( LinkType aLinkType )
    {
        return new ArduinoLinker();
    }

}
