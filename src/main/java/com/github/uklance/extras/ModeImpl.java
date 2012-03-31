package com.github.uklance.extras;

public class ModeImpl implements Mode {
	public static final Mode DEFAULT = new ModeImpl(null);
	
	private String mode;

	public ModeImpl(String mode) {
		super();
		this.mode = mode;
	}
	
	public String getMode() {
		return mode;
	}
}
