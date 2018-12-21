package com.github.wmarkow.amp.arduino.variable;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.wmarkow.amp.arduino.variable.DefaultVariableStorage;
import com.github.wmarkow.amp.arduino.variable.ExpressionEvaluator;
import com.github.wmarkow.amp.arduino.variable.Variable;
import com.github.wmarkow.amp.arduino.variable.VariableStorage;

public class ExpressionEvaluatorTest
{

    @Test
    public void testEvaluateForNoVariableExpression()
    {
        VariableStorage storage = new DefaultVariableStorage();

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " this expression has no variables  ", storage );

        assertEquals( " this expression has no variables  ", result );
    }

    @Test
    public void testEvaluateForOneVariableExpressionButMissingVariable()
    {
        VariableStorage storage = new DefaultVariableStorage();

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " blabla {variable} haha", storage );

        assertEquals( " blabla {variable} haha", result );
    }

    @Test
    public void testEvaluateForOneVariableExpression()
    {
        VariableStorage storage = new DefaultVariableStorage();
        storage.putVariable( new Variable( "variable", "15002900" ) );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " blabla {variable} haha", storage );

        assertEquals( " blabla 15002900 haha", result );
    }

    @Test
    public void testEvaluateForOneNestedVariableExpression()
    {
        VariableStorage storage = new DefaultVariableStorage();
        storage.putVariable( new Variable( "variable", "15002900" ) );
        storage.putVariable( new Variable( "variable2", "123 {variable}" ) );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();

        final String result = evaluator.evaluate( " blabla {variable} haha {variable2}", storage );

        assertEquals( " blabla 15002900 haha 123 15002900", result );
    }

    @Test
    public void testEvaluateSimple()
    {
        VariableStorage storage = new DefaultVariableStorage();
        storage.putVariable( new Variable( "compiler.path", "" ) );
        storage.putVariable( new Variable( "compiler.cpp.cmd", "avr-gcc" ) );

        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        String result = evaluator.evaluate( "{compiler.path}{compiler.cpp.cmd}", storage );

        assertEquals( "avr-gcc", result );
    }
}
