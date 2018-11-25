package com.github.wmarkow.amp;

import java.util.Vector;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.OptimizationEnum;
import com.github.maven_nar.cpptasks.compiler.LinkType;
import com.github.maven_nar.cpptasks.compiler.Linker;
import com.github.maven_nar.cpptasks.gcc.GccCompatibleCCompiler;

public class ArduinoCCompiler extends GccCompatibleCCompiler
{
    private final static String[] sourceExtensions = new String[]
    { ".c", };

    private final static String[] headerExtensions = new String[]
    { ".h" };

    public ArduinoCCompiler()
    {
        this( "gcc", sourceExtensions, headerExtensions, false, new ArduinoCCompiler( "gcc",
            sourceExtensions, headerExtensions, true, null, false, null ), false, null );
    }

    private ArduinoCCompiler( final String command, final String[] sourceExtensions,
        final String[] headerExtensions, final boolean isLibtool, final ArduinoCCompiler libtoolCompiler,
        final boolean newEnvironment, final Environment env )
    {
        super( command, null, sourceExtensions, headerExtensions, isLibtool, libtoolCompiler, newEnvironment,
            env );
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
