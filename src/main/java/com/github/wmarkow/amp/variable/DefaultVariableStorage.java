package com.github.wmarkow.amp.variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultVariableStorage implements VariableStorage
{
    private Map< String, Variable > variables = new HashMap< String, Variable >();

    @Override
    public void addVariable( Variable variable )
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


