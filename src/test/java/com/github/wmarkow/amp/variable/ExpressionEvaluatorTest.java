package com.github.wmarkow.amp.variable;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ExpressionEvaluatorTest
{

    @Test
    public void testEvaluateForNoVariableExpression()
    {
        VariableStorage storage = new VariableStorage();

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " this expression has no variables  ", storage );

        assertEquals( " this expression has no variables  ", result );
    }

    @Test
    public void testEvaluateForOneVariableExpressionButMissingVariable()
    {
        VariableStorage storage = new VariableStorage();

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " blabla {variable} haha", storage );

        assertEquals( " blabla {variable} haha", result );
    }

    @Test
    public void testEvaluateForOneVariableExpression()
    {
        VariableStorage storage = new VariableStorage();
        storage.addVariable( new Variable( "variable", "15002900" ) );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " blabla {variable} haha", storage );

        assertEquals( " blabla 15002900 haha", result );
    }

    @Test
    public void testEvaluateForOneNestedVariableExpression()
    {
        VariableStorage storage = new VariableStorage();
        storage.addVariable( new Variable( "variable", "15002900" ) );
        storage.addVariable( new Variable( "variable2", "123 {variable}" ) );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " blabla {variable} haha {variable2}", storage );

        assertEquals( " blabla 15002900 haha 123 15002900", result );
    }

    @Test
    public void testEvaluateSimple()
    {
        VariableStorage storage = new VariableStorage();
        storage.addVariable( new Variable( "compiler.path", "" ) );
        storage.addVariable( new Variable( "compiler.cpp.cmd", "avr-gcc" ) );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = evaluator.evaluate( "{compiler.path} {compiler.cpp.cmd}", storage );

        assertEquals( "avr-gcc", result );
    }

    // recipe.cpp.o.pattern="{compiler.path}{compiler.cpp.cmd}" {compiler.cpp.flags} -mmcu={build.mcu}
    // -DF_CPU={build.f_cpu} -DARDUINO={runtime.ide.version} -DARDUINO_{build.board}
    // -DARDUINO_ARCH_{build.arch} {compiler.cpp.extra_flags} {build.extra_flags} {includes} "{source_file}"
    // -o "{object_file}"

}
