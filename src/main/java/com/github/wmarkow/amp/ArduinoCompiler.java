package com.github.wmarkow.amp;

import java.util.Vector;

import org.apache.tools.ant.types.Environment;

import com.github.maven_nar.cpptasks.OptimizationEnum;
import com.github.maven_nar.cpptasks.compiler.LinkType;
import com.github.maven_nar.cpptasks.compiler.Linker;
import com.github.maven_nar.cpptasks.gcc.GccCompatibleCCompiler;
import com.github.maven_nar.cpptasks.gcc.GppLinker;

public class ArduinoCompiler extends GccCompatibleCCompiler
{
    private final static String[] sourceExtensions = new String[]
    { ".c", ".cpp", ".s", ".ino" };

    private final static String[] headerExtensions = new String[]
    { ".h" };

    private static final ArduinoCompiler cppInstance = new ArduinoCompiler( "c++", sourceExtensions,
        headerExtensions, false, new ArduinoCompiler( "c++", sourceExtensions, headerExtensions, true,
            null, false, null ), false, null );

    private static final ArduinoCompiler gppInstance = new ArduinoCompiler( "g++", sourceExtensions,
        headerExtensions, false, new ArduinoCompiler( "g++", sourceExtensions, headerExtensions, true,
            null, false, null ), false, null );

    private ArduinoCompiler( final String command, final String[] sourceExtensions,
        final String[] headerExtensions, final boolean isLibtool, final ArduinoCompiler libtoolCompiler,
        final boolean newEnvironment, final Environment env )
    {
        super( command, null, sourceExtensions, headerExtensions, isLibtool, libtoolCompiler, newEnvironment,
            env );
    }

    public static ArduinoCompiler getCppInstance()
    {
        return cppInstance;
    }

    public static ArduinoCompiler getGppInstance()
    {
        return gppInstance;
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
