package com.github.wmarkow.amp.variable;

import java.util.HashMap;
import java.util.Map;

public class VariableStorage
{
    private Map< String, Variable > variables = new HashMap< String, Variable >();

    public void addVariable( Variable variable )
    {
        variables.put( variable.getName(), variable );
    }
    
    public Variable get( String name )
    {
        return variables.get( name );
    }
}
