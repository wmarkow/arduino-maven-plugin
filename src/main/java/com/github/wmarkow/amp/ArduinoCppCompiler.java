package com.github.wmarkow.amp;

import java.util.Vector;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.OptimizationEnum;
import com.github.maven_nar.cpptasks.compiler.LinkType;
import com.github.maven_nar.cpptasks.compiler.Linker;
import com.github.maven_nar.cpptasks.gcc.GccCompatibleCCompiler;
import com.github.maven_nar.cpptasks.gcc.GppLinker;

public class ArduinoCppCompiler extends GccCompatibleCCompiler
{
    private final static String[] sourceExtensions = new String[]
    { ".cpp", };

    private final static String[] headerExtensions = new String[]
    { ".h" };

    public ArduinoCppCompiler()
    {
        this( "g++", sourceExtensions, headerExtensions, false, new ArduinoCppCompiler( "g++",
            sourceExtensions, headerExtensions, true, null, false, null ), false, null );
    }

    private ArduinoCppCompiler( final String command, final String[] sourceExtensions,
        final String[] headerExtensions, final boolean isLibtool, final ArduinoCppCompiler libtoolCompiler,
        final boolean newEnvironment, final Environment env )
    {
        super( command, null, sourceExtensions, headerExtensions, isLibtool, libtoolCompiler, newEnvironment,
            env );
    }

    @Override
    public Linker getLinker( final LinkType linkType )
    {
        return GppLinker.getInstance().getLinker( linkType );
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
