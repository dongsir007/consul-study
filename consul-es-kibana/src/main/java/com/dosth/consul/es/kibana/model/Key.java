package com.dosth.consul.es.kibana.model;

public class Key {
	private String key;

	public Key(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	@Override
	public String toString() {
		return "Key [key=" + key + "]";
	}
}
