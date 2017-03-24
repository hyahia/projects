package com.m800.log.management.actor.message;

import java.io.Serializable;

public class Scan implements Serializable {
	private static final long serialVersionUID = 1L;
	public final String path;
    public Scan(String path) {
        this.path = path;
    }
}