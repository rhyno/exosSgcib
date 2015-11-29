package com.soat.exosSgcib.ws;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.soat.exosSgcib.itf.RpnCalculatorItf;
import com.soat.exosSgcib.utils.OperatorRpnQual;

@Path("/rpn")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@SessionScoped
public class RpnCalculator implements Serializable, RpnCalculatorItf{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger _l = Logger.getLogger(RpnCalculator.class);
	
	
	private transient @Inject @OperatorRpnQual Map<String,BiFunction<Float,Float,Float>> operators;
		
	@GET
	@Path("/compute")
	public String computeRpnCalculators(@QueryParam("input") String input) {
		
		_l.info("Receiving input :" + input);
		
		String result = new String();
			
				
		result = this.recursiveCompute(input.split("\\s+"))[0];
		
		return result;
	}
	
	/**
	 * a completer, ne fonctionne pas dans tout les cas.
	 * @param input
	 * @return
	 */
	private String[] recursiveCompute(String[] input){
		_l.info("recursiveCompute :");
		String[] result = new String[1];
		
		if(input.length > 0){
			
			try{
				_l.info("checking if first element is a number");
				Float.parseFloat(input[input.length-1]);
				_l.info("first element is a number");
				result[0] = input[input.length-1];
			}catch(NumberFormatException e){
				_l.info("first element is not a number");
				// Si une exception se leve on doit avoir un operateur
				_l.info("checking operator type");
				if(operators.containsKey(input[input.length-1])){
					_l.info("operator is : " + input[input.length-1]);
					_l.info("Trying to define E1 : ");
					String[] newInput = Arrays.copyOf(input, input.length-1);
					String[] e1Tab = recursiveCompute(newInput);
					_l.info(" E1 is : " + e1Tab[0]);
					_l.info("Trying to define E2 : ");
					newInput = Arrays.copyOf(input, input.length-2);
					String[] e2Tab = recursiveCompute(newInput);
					_l.info(" E2 is : " + e2Tab[0]);
					
					Float operationResult = operators.get(input[input.length-1]).apply(Float.parseFloat(e2Tab[0]), Float.parseFloat(e1Tab[0]));
					_l.info(" Executing operation " + e2Tab[0] + input[input.length-1] + e1Tab[0] + " = " + operationResult);
					
					result[0] = ""+operationResult;
					
				}else{
					_l.info(" Could not identify operator ");
					result[0] = input[input.length-1] +" character could not be interpreted";
				}
			}
		}
		_l.info("recursiveCompute result :" + result[0]);
		return result;
	}
	
	@javax.enterprise.inject.Produces @OperatorRpnQual
	private Map<String,BiFunction<Float,Float,Float>> initOperators(){
		
		_l.info("initialising operators ...");
		
		Map<String,BiFunction<Float,Float,Float>> result = new HashMap<String,BiFunction<Float,Float,Float>>();
		
		BiFunction<Float,Float,Float> division = (f1,f2) -> {
			return f1 / f2;
		};
		BiFunction<Float,Float,Float> addition = (f1,f2) -> {
			return f1 + f2;
		};
		BiFunction<Float,Float,Float> substraction = (f1,f2) -> {
			return f1 - f2;
		};
		BiFunction<Float,Float,Float> multiplication = (f1,f2) -> {
			return f1 * f2;
		};
		
		result.put("/", division);
		result.put("*", multiplication);
		result.put("u", addition);
		result.put("-", substraction);
		
		return result;
	}
	
}
