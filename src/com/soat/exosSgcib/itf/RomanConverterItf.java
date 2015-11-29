package com.soat.exosSgcib.itf;

import com.soat.exosSgcib.model.ConvertedDecimal;

public interface RomanConverterItf {

	public Integer romanToDecimal(String roman);
	
	public ConvertedDecimal decimalToRoman(Integer decimal);
}
