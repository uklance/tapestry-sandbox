package com.github.uklance.pages;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectPage;

import com.github.uklance.mode.Mode;

public class ModePage2 {
	@Environmental(false)
	Mode mode;
	
	@InjectPage
	private ModePage1 page1;
	
	ModePage1 onPage1Event() {
		return page1;
	}
	
	public String getMode() {
		return mode == null ? null : mode.getMode();
	}

}
