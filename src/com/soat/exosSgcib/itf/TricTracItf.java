package com.soat.exosSgcib.itf;

import com.soat.exosSgcib.model.TricTrac;

public interface TricTracItf {

	public TricTrac validate(Boolean user, Integer row, Integer column);
	
	public TricTrac reset();
	
}
