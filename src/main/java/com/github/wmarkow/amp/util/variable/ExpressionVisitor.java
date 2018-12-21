package com.github.wmarkow.amp.util.variable;

abstract class ExpressionVisitor
{

    protected void visit( String expression )
    {
        for( int q = 0; q < expression.length(); q++ )
        {
            String character = String.valueOf( expression.charAt( q ));

            if( "{".equals( character ) )
            {
                onVariableBegin();
                continue;
            }
            if( "}".equals( character ) )
            {
                onVariableEnd();
                continue;
            }

            onCharacterFound( character );
        }
    }

    abstract protected void onCharacterFound( String character );

    abstract protected void onVariableBegin();

    abstract protected void onVariableEnd();
}


