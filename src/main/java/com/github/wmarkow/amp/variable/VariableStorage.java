package com.github.wmarkow.amp.variable;

import java.util.Set;

public interface VariableStorage
{
    public void addVariable( Variable variable );
    
    public Variable getVariable( String name );

    public Set< String > getNames();
}
