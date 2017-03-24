package com.m800.log.management.actor.message;

import java.io.Serializable;

public class Parse implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String filePath;
    public Parse(String filePath) {
        this.filePath = filePath;
    }
}