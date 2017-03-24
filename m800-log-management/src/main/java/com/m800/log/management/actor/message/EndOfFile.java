package com.m800.log.management.actor.message;

import java.io.Serializable;

public class EndOfFile implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String message;
    public EndOfFile(String message) {
        this.message = message;
    }
}