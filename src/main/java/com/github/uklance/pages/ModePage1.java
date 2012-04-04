package com.github.uklance.pages;

import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.InjectPage;

import com.github.uklance.mode.Mode;

public class ModePage1 {
	@Environmental
	Mode mode;
	
	@InjectPage
	private ModePage2 page2;
	
	ModePage2 onPage2Event() {
		return page2;
	}
	
	public String getMode() {
		return mode == null ? null : mode.getMode();
	}
}
