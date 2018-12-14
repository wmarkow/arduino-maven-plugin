package com.github.wmarkow.amp.variable;

public class MultiVariableStorage extends DefaultVariableStorage
{
    private MultiVariableStorage()
    {

    }

    public final static MultiVariableStorage populateFrom( VariableStorage ... inputStorages )
    {
        MultiVariableStorage result = new MultiVariableStorage();

        for( VariableStorage storage : inputStorages )
        {
            for( String name : storage.getNames() )
            {
                result.addVariable( new Variable( storage.getVariable( name ) ) );
            }
        }
        return result;
    }
}
