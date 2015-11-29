package com.soat.exosSgcib.ws;

import java.io.Serializable;
import java.util.function.BiFunction;

import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.soat.exosSgcib.itf.FizzBuzzItf;
import com.soat.exosSgcib.model.FizzBuzz;

@Path("/fizzBuzz")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@SessionScoped
public class FizzBuzzService implements Serializable, FizzBuzzItf{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger _l = Logger.getLogger(FizzBuzzService.class);
	
	@GET
	@Path("/fizzBuzz")
	public FizzBuzz returnFizzBuzz(@QueryParam("input") Integer input) {
		
		FizzBuzz result = new FizzBuzz();
		
		String outputValue = "";
		
		String isDivisibleBy3 = replaceIfDivisible().apply(input, 3);
		String isDivisibleBy5 = replaceIfDivisible().apply(input, 5);
		
		outputValue = isDivisibleBy3!=null?outputValue+isDivisibleBy3:outputValue;
		outputValue = isDivisibleBy5!=null?outputValue+isDivisibleBy5:outputValue;
		
		outputValue = "".equals(outputValue)?input+"":outputValue;
		
		result.setInput(input);
		result.setOutput(outputValue);
		
		return result;
	}

	private BiFunction<Integer,Integer,String> replaceIfDivisible(){
		BiFunction<Integer,Integer,String> result = (i,j) -> {
			String s = new String();
			s=j==3?"Fizz":s;
			s=j==5?"Buzz":s;
			
			String functionResult = i % j==0?s:null;
			return functionResult;
		};
		return result;
	}
	
}
