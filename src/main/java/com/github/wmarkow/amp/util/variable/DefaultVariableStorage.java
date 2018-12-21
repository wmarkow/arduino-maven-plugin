package com.github.wmarkow.amp.util.variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultVariableStorage implements VariableStorage
{
    private Map< String, Variable > variables = new HashMap< String, Variable >();

    @Override
    public void putVariable( Variable variable )
    {
        variables.put( variable.getName(), variable );
    }

    @Override
    public Variable getVariable( String name )
    {
        return variables.get( name );
    }

    @Override
    public Set< String > getNames()
    {
        return variables.keySet();
    }
}


