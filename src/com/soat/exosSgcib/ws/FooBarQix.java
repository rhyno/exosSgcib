package com.soat.exosSgcib.ws;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.context.SessionScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.soat.exosSgcib.itf.FooBarQixItf;

@Path("/foobarqix")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@SessionScoped
public class FooBarQix implements Serializable, FooBarQixItf{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger _l = Logger.getLogger(FooBarQix.class);
	
	@GET
	@Path("/returnFooBarQix")
	public List<String> returnFooBarQix() {
		
		List<String> result = new ArrayList<String>();
		
		// construction de notre iterator
		IntUnaryOperator iteratorOperator = i -> i + 1;
		
		// construction du stream, on applique la valeur de départ et notre iterateur
		IntStream intStream = IntStream.iterate(1, iteratorOperator);
		
		// Fonction affichant dans les logs la valeur de i et transforme les valeurs
		IntFunction<String> integerMapper = i -> {
			
			String integerMapperResult = "";
			
			// division has precedence
			String isDivisibleBy3 = replaceIfDivisible().apply(i, 3);
			String isDivisibleBy5 = replaceIfDivisible().apply(i, 5);
			String isDivisibleBy7 = replaceIfDivisible().apply(i, 7);
			
			integerMapperResult = isDivisibleBy3!=null?integerMapperResult+isDivisibleBy3:integerMapperResult;
			integerMapperResult = isDivisibleBy5!=null?integerMapperResult+isDivisibleBy5:integerMapperResult;
			integerMapperResult = isDivisibleBy7!=null?integerMapperResult+isDivisibleBy7:integerMapperResult;
			
			String s = i+"";
			char[] sTab = s.toCharArray();
			
			// the content is analysed in the order they appear
			for(int j = 0; j < sTab.length; j ++){
				String contains3 = replaceIfContains().apply(sTab[j], 3);
				String contains5 = replaceIfContains().apply(sTab[j], 5);
				String contains7 = replaceIfContains().apply(sTab[j], 7);
				
				integerMapperResult = contains3!=null?integerMapperResult+contains3:integerMapperResult;
				integerMapperResult = contains5!=null?integerMapperResult+contains5:integerMapperResult;
				integerMapperResult = contains7!=null?integerMapperResult+contains7:integerMapperResult;
			}
			
			integerMapperResult=integerMapperResult.equals("")?i+"":integerMapperResult;
			
			_l.info( i + " : " + integerMapperResult) ;
			
			integerMapperResult= integerMapperResult+"</br>";
			
			return integerMapperResult;
		};
		
		result = intStream
			.limit(100)
			.mapToObj(integerMapper)
			.collect(Collectors.toList());
		
		return result;
	}

	private BiFunction<Integer,Integer,String> replaceIfDivisible(){
		BiFunction<Integer,Integer,String> result = (i,j) -> {
			String s = new String();
			s=j==3?"Foo":s;
			s=j==5?"Bar":s;
			s=j==7?"Qix":s;
			
			String functionResult = i % j==0?s:null;
			return functionResult;
		};
		return result;
	}
	
	private BiFunction<Character,Integer,String> replaceIfContains(){
		BiFunction<Character,Integer,String> result = (i,j) -> {
			String s = new String();
			s=j==3?"Foo":s;
			s=j==5?"Bar":s;
			s=j==7?"Qix":s;
			
			String asString = i+"";
			asString.toCharArray();
			String functionResult = asString.contains(j+"")?s:null;
			return functionResult;
		};
		return result;
	}
}
