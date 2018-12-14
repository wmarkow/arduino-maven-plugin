package com.github.wmarkow.amp.variable;

public class Variable
{
    private String name;
    private String value;

    public Variable( Variable other )
    {
        this.name = other.getName();
        this.value = other.getValue();
    }

    public Variable( String name, String value )
    {
        this.name = name;
        this.value = value;
    }

    public Variable( String name )
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue( String aValue )
    {
        value = aValue;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return "Variable [name=" + name + ", value=" + value + "]";
    }
}
