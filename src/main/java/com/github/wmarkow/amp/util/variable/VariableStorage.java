package com.github.wmarkow.amp.util.variable;

import java.util.Set;

public interface VariableStorage
{
    public void putVariable( Variable variable );
    
    public Variable getVariable( String name );

    public Set< String > getNames();
}
