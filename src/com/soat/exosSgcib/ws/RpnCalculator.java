package com.soat.exosSgcib.ws;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
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
import com.soat.exosSgcib.model.RpnOperator;
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
	public RpnOperator computeRpnCalculators(@QueryParam("input") String input) {
		
		_l.info("Receiving input :" + input);
		
		RpnOperator result = new RpnOperator();
		
		String resultedString = new String();
		Stack<String> stackCompute = new Stack<String>();
		stackCompute.addAll(Arrays.asList(input.split("\\s+")));
		
		while(!stackCompute.isEmpty()){
			resultedString = this.recursiveCompute(stackCompute) + " " + resultedString;
		}
		
		result.setInput(input);
		result.setOutput(resultedString.toString());
		
		return result;
	}
	
	/**
	 * a completer, ne fonctionne pas dans tout les cas.
	 * @param input
	 * @return
	 */
	private String recursiveCompute(Stack<String> input){
		_l.info("recursiveCompute :");
		String result = "";
		
		
		if(!input.isEmpty()){
			String firstEntry = input.pop();
			try{
				_l.info("checking if first element is a number");
				Float.parseFloat(firstEntry);
				_l.info("first element is a number");
				result = firstEntry;
				
			}catch(NumberFormatException e){
				_l.info("first element is not a number");
				// Si une exception se leve on doit avoir un operateur
				_l.info("checking operator type");
				if(operators.containsKey(firstEntry)){
					_l.info("operator is : " + firstEntry);
					_l.info("Trying to define E1 : ");
					String e1 = recursiveCompute(input);
					_l.info(" E1 is : " + e1);
					_l.info("Trying to define E2 : ");
					String e2 = recursiveCompute(input);
					_l.info(" E2 is : " + e2);
					
					Float operationResult = operators.get(firstEntry).apply(Float.parseFloat(e2), Float.parseFloat(e1));
					_l.info(" Executing operation " + e2 + firstEntry + e1 + " = " + operationResult);
					
					result = ""+operationResult;
										
				}else{
					_l.info(" Could not identify operator ");
					result = firstEntry +" character could not be interpreted";
				}
			}
		}

		
		_l.info("recursiveCompute result :" + result);
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
