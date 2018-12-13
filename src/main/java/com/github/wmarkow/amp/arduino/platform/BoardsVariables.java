package com.github.wmarkow.amp.arduino.platform;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BoardsVariables
{
    private Map< String, BoardVariables > boardsVariables = new HashMap< String, BoardVariables >();

    public void addBoardVariables( String boardName, BoardVariables boardVariables )
    {
        boardsVariables.put( boardName, boardVariables );
    }

    public Set< String > getBoardNames()
    {
        return boardsVariables.keySet();
    }

    public BoardVariables getBoardVariables( String boardName )
    {
        return boardsVariables.get( boardName );
    }
}
