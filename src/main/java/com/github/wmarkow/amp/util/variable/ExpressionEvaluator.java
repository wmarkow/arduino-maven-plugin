package com.github.wmarkow.amp.util.variable;

public class ExpressionEvaluator extends ExpressionVisitor
{
    private StringBuilder result = new StringBuilder();
    private StringBuilder variableName;

    private boolean inVariable = false;

    private VariableStorage storage;

    public String evaluate( String expression, VariableStorage storage )
    {
        this.storage = storage;

        visit( expression );

        return result.toString();
    }

    @Override
    protected void onCharacterFound( String character )
    {
        if( inVariable )
        {
            variableName.append( character );
        }
        else
        {
            result.append( character );
        }
    }

    @Override
    protected void onVariableBegin()
    {
        inVariable = true;
        variableName = new StringBuilder();
    }

    @Override
    protected void onVariableEnd()
    {
        inVariable = false;

        final String variablename = variableName.toString();

        final Variable variable = storage.getVariable( variablename );

        if( variable == null )
        {
            result.append( "{" );
            result.append( variablename );
            result.append( "}" );
        }
        else
        {
            ExpressionEvaluator eval = new ExpressionEvaluator();

            result.append( eval.evaluate( variable.getValue(), storage ) );
        }
    }
}
