package com.soat.exosSgcib.ws;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.soat.exosSgcib.itf.RomanConverterItf;
import com.soat.exosSgcib.model.ConvertedDecimal;
import com.soat.exosSgcib.utils.DecimalMapQual;
import com.soat.exosSgcib.utils.RomanMapQual;

@Path("/romanconverter")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@SessionScoped
public class RomanConverter implements Serializable, RomanConverterItf{

	private  transient @Inject @RomanMapQual Map<String,Integer> romanMap;
	private  transient @Inject @DecimalMapQual Map<Integer,String> decimalMap;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger _l = Logger.getLogger(RomanConverter.class);
	
	/**
	 * pas le bon exercice
	 */
	@GET
	@Path("/romanToDecimal")
	public Integer romanToDecimal(@QueryParam("roman")String roman) {
		_l.info("roman : " + roman );
		Integer result = 0;
		char[] romanCharTab = roman.toCharArray();
		StringBuilder sb = new StringBuilder();
		Integer next;
		
		for(int i = 0 ; i < romanCharTab.length; i ++){
			
			char currentChar = romanCharTab[i];
			
			// teste si la prochaine lettre designe une valeur comme 5-1, 50-10, 500-100, 10-1, 100-10, 1000-100
			Character nextChar = romanCharTab.length > i + 1? romanCharTab[i+1]:null;
			if(nextChar!=null){
				sb.delete(0, sb.length());
				sb.append(currentChar);
				sb.append(nextChar);
				if(romanMap.containsKey(sb.toString())){
					next= romanMap.get(sb.toString());
					_l.info(sb + " : " + next );
					result += next!=null?next:0;
					// on saute le prochain charactere qui vient d etre traite
					i++;
				}else{
					next= romanMap.get(""+currentChar);
					_l.info(currentChar + " : " + next );
					result += next!=null?next:0;
				}
			}else{
				next= romanMap.get(""+currentChar);
				result += next!=null?next:0;
			}
			_l.info(result + " : " + result );
		}
		
		return result;
	}

	@GET
	@Path("/decimalToRoman")
	public ConvertedDecimal decimalToRoman(@QueryParam("decimal")Integer decimal) {
		_l.info("decimal : " + decimal );
		ConvertedDecimal result = new ConvertedDecimal();
		StringBuilder sbResult = new StringBuilder();
		String asString = String.valueOf(decimal);
		
		if(decimal>0&&asString.length()<5){
			int revCounter = asString.length();
			for(int i = 0; i < asString.length(); i ++ ){
				
				Integer charAsInt = Integer.parseInt("" + asString.charAt(i));
				// on ajoute les puissance de 10
				switch(revCounter){
					case 4 :
						charAsInt=charAsInt*1000;
						break;
					case 3 :
						charAsInt=charAsInt*100;
						break;
					case 2 :
						charAsInt=charAsInt*10;
						break;
				}
				
				sbResult.append(this.decimalMap.get(charAsInt));
				revCounter --;
			}
		}else{
			sbResult.append("invalid");
		}
		_l.info("result : " + sbResult.toString() );
		
		result.setDecimalValue(decimal);
		result.setRomanValue(sbResult.toString());
		
		return result;
	}
	
	@javax.enterprise.inject.Produces @DecimalMapQual
	private Map<Integer,String> initDecimalMap(){
		
		_l.info("initialising decimal map ...");
		
		Map<Integer, String> result = new HashMap<Integer, String>();
		
		result.put( 0   , "" );
		
		result.put( 1   , "I" );
		result.put( 2   , "II" );
		result.put( 3   , "III" );
		result.put( 4   , "IV");
		result.put( 5   , "V" );
		result.put( 6   , "VI" );
		result.put( 7   , "VII" );
		result.put( 8   , "VIII" );
		result.put( 9  , "IX");
		
		
		result.put( 10  , "X" );
		result.put( 20  , "XX" );
		result.put( 30  , "XXX" );
		result.put( 40  , "XL");
		result.put( 50  , "L" );
		result.put( 50  , "L" );
		result.put( 60  , "LX" );
		result.put( 70  , "LXX" );
		result.put( 80  , "LXXX" );
		result.put( 90  , "XC");
		
		result.put( 100 , "C" );
		result.put( 200 , "CC" );
		result.put( 300 , "CCC" );
		result.put( 400 , "CD");
		result.put( 500 , "D" );
		result.put( 600 , "DC" );
		result.put( 700 , "DCC" );
		result.put( 800 , "DCCC" );
		result.put( 900 , "CM");
		result.put( 1000, "M" );
		
		
		return result;
	}
	
	@javax.enterprise.inject.Produces @RomanMapQual
	private Map<String,Integer> initRomanMap(){
		
		_l.info("initialising roman map ...");
		
		Map<String,Integer> result = new HashMap<String,Integer>();
		
		result.put("I"    , 1);
		result.put("IV"   , 4);
		result.put("V"    , 5);
		result.put("IX"   , 9);
		result.put("X"    , 10);
		result.put("XL"   , 40);
		result.put("L"    , 50);
		result.put("XC"   , 90);
		result.put("C"    , 100);
		result.put("CD"   , 400);
		result.put("D"    , 500);
		result.put("CM"   , 900);
		result.put("M"    , 1000);
		
		return result;
	}
	
	
	
}
