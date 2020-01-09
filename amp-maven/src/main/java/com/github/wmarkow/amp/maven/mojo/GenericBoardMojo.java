package com.github.wmarkow.amp.maven.mojo;

import java.net.MalformedURLException;

import org.apache.maven.plugins.annotations.Parameter;

public abstract class GenericBoardMojo extends GenericPlatformMojo
{
    @Parameter( property = "arduino-maven-plugin.board", required = true )
    private String board;

    protected String getBoard() throws MalformedURLException
    {
        return board;
    }
}
