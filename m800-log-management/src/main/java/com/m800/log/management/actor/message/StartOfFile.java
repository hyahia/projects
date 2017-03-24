package com.m800.log.management.actor.message;

import java.io.Serializable;

public class StartOfFile implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String message;
    public StartOfFile(String message) {
        this.message = message;
    }
}