package com.github.wmarkow.amp.compiler;

import java.util.Vector;

import com.github.maven_nar.cpptasks.OptimizationEnum;
import com.github.maven_nar.cpptasks.compiler.LinkType;
import com.github.maven_nar.cpptasks.compiler.Linker;
import com.github.maven_nar.cpptasks.gcc.GccCompatibleCCompiler;
import com.github.wmarkow.amp.linker.ArduinoLinker;

public abstract class ArduinoCompiler extends GccCompatibleCCompiler
{

    private final static String[] headerExtensions = new String[]
    { ".h" };

    protected ArduinoCompiler( String command, String[] sourceExtensions )
    {
        super( command, null, sourceExtensions, headerExtensions, false, null, false, null );
    }

    @Override
    public Linker getLinker( final LinkType linkType )
    {
        return new ArduinoLinker().getLinker( linkType );
    }

    @Override
    public int getMaximumCommandLength()
    {
        return isWindows() ? 20000 : Integer.MAX_VALUE;
    }

    @Override
    public void addImpliedArgs( final Vector< String > args, final boolean debug,
        final boolean multithreaded, final boolean exceptions, final LinkType linkType, final Boolean rtti,
        final OptimizationEnum optimization )
    {
        // do nothing
    }
}
