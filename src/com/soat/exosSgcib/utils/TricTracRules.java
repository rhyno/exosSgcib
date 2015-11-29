package com.soat.exosSgcib.utils;

import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.jboss.logging.Logger;

import com.soat.exosSgcib.model.TricTrac;

public class TricTracRules{

	static Logger  _l = Logger.getLogger(TricTracRules.class);
	
	/**
	 * Returns the given trictrac and sets the winner (false for player 1 true for player 2 null for no winner),
	 * Sets the gameOver flag.
	 * 
	 * @param tricTrac
	 * @return
	 */
	public static void processTurn(TricTrac tricTrac, Boolean player, Integer row, Integer column){
		
		_l.info("setting player turn ...");
		// le tour du joueur en cours
		tricTrac.getTricTracTab()[row][column] = player;
		
		_l.info("checking for a winner ...");
		// on verifie si il y a un vaincqueur
		tricTrac.setWinner(returnWinner(tricTrac));
		
		if(tricTrac.getWinner()==null){
			
			_l.info("No winner, checking if game is over ...");
			
			// si tout les champs sont pris la partie est finie
			tricTrac.setGameOver(returnRuleIsAllFieldTaken().test(tricTrac));
			_l.info(tricTrac.getGameOver()?"All field taken game is over": "Game is not over");
			
		}else{
			// si il ya un vainqueur la partie est finie
			tricTrac.setGameOver(true);
			_l.info("The winner is " + (tricTrac.getWinner()?"player 2" : "player 1"));
		}
		
	}
	
	/**
	 * Retourne le vainqueur, false pour le joueur 1 et true pour le joueur 2,
	 * null si pas de vainqueur
	 * @param tricTrac
	 * @return
	 */
	private static Boolean returnWinner(TricTrac tricTrac){
		Boolean result = null;
		
		// on verifie si le joueur 1 (false) poss�de une colonne
		result = returnRuleIsColumnTakenByPlayer().apply(tricTrac, false)?false:null;
		// on verifie si le joueur 2 (true) poss�de une colonne
		result = result==null?(returnRuleIsColumnTakenByPlayer().apply(tricTrac, true)?true:null):result;
		
		// on verifie si le joueur 1 (false) poss�de une ligne
		result = result==null?(returnRuleIsRowTakenByPlayer().apply(tricTrac, false)?false:null):result;
		// on verifie si le joueur 2 (true) poss�de une ligne
		result = result==null?(returnRuleIsRowTakenByPlayer().apply(tricTrac, true)?true:null):result;
		
		// on verifie si le joueur 1 (false) poss�de une ligne
		result = result==null?(returnRuleIsDiagonalTakenByPlayer().apply(tricTrac, false)?false:null):result;
		// on verifie si le joueur 2 (true) poss�de une ligne
		result = result==null?(returnRuleIsDiagonalTakenByPlayer().apply(tricTrac, true)?true:null):result;
		
		return result;
	}
	
	
	/**
	 * Returns a bi function, that returns true if the given player has all field in one diagonale.
	 * only works on "square" tabs
	 * @return
	 */
	private static BiFunction<TricTrac,Boolean,Boolean> returnRuleIsDiagonalTakenByPlayer(){
		BiFunction<TricTrac,Boolean, Boolean> predicate = (tricTrac, player) -> {
			
			Boolean result = false; 
			
			int counter = 0;
			// on fait 2 parcours (2 diagonales)
			for(int i = 0; i < tricTrac.getTricTracTab().length; i ++){
				
				// on verifie si le champ possede la valeur booleene de player
				if(player.equals(tricTrac.getTricTracTab()[i][i])){
					counter ++;
					// si la diagonale est remplie on arrete la boucle
					if(counter == tricTrac.getTricTracTab().length ){
						result = true;
						break;
					}
				}
			}
			counter = 0;
			for(int i = tricTrac.getTricTracTab().length-1; i >= 0; i --){
				// on verifie si le champ possede la valeur booleene de player
				if(player.equals(tricTrac.getTricTracTab()[i][tricTrac.getTricTracTab().length-1-i])){
					counter ++;
					// si la diagonale est remplie on arrete la boucle
					if(counter == tricTrac.getTricTracTab().length ){
						result = true;
						break;
					}
				}
			}
			
			return result;
		};
		
		return predicate;
	}
	
	/**
	 * Returns a bi function, that returns true if the given player has all field in one row
	 * @return
	 */
	private static BiFunction<TricTrac,Boolean,Boolean> returnRuleIsRowTakenByPlayer(){
		BiFunction<TricTrac,Boolean, Boolean> predicate = (tricTrac, player) -> {
			
			Boolean result = false; 
			
			for(int i = 0; i < tricTrac.getTricTracTab().length; i ++){
				int counter= 0;
				Boolean[] row = tricTrac.getTricTracTab()[i];
				for(int j = 0; j < row.length; j ++){
					// on verifie si le champ possede la valeur booleene de player
					if(player.equals(row[j])){
						counter ++;
						// si la ligne est remplie on arrete la boucle
						if(counter == tricTrac.getTricTracTab().length ){
							result = true;
							break;
						}
					}
				}
				if(result){
					break;
				}
			}
			return result;
		};
		return predicate;
	}
	
	/**
	 * Returns a bi function, that returns true if the given player has all field in one column
	 * @return
	 */
	private static BiFunction<TricTrac,Boolean,Boolean> returnRuleIsColumnTakenByPlayer(){
		BiFunction<TricTrac,Boolean, Boolean> predicate = (tricTrac, player) -> {
			Boolean result = false;
			for(int i = 0; i < tricTrac.getTricTracTab().length; i ++){
				int counter= 0;
				for(int j = 0; j < tricTrac.getTricTracTab().length; j ++){
					// on verifie si le champ possede la valeur booleene de player
					if(player.equals(tricTrac.getTricTracTab()[j][i])){
						counter ++;
						// si la colonne est remplie on arrete la boucle
						if(counter == tricTrac.getTricTracTab()[0].length ){
							result = true;
							break;
						}
					}
				}
				if(result){
					break;
				}
			}
			return result;
		};
		
		return predicate;
	}
	
	/**
	 * returns a predicate wich sends true if all field are taken
	 * @return
	 */
	private static Predicate<TricTrac> returnRuleIsAllFieldTaken(){
		Predicate<TricTrac> predicate = (tricTrac) -> {
			
			Boolean result = true; 
			
			for(int i = 0; i < tricTrac.getTricTracTab().length; i ++){
				Boolean[] row = tricTrac.getTricTracTab()[i];
				for(int j = 0; j < row.length; j ++){
					// si un champ est vide on renvoie false;
					if(returnCheckIsFieldEmpty().test(row[j])){
						result = false; 
						break;
					}
				}
			}
			
			return result;
		};
		return predicate;
	}

	/**
	 * returns a predicate wich sends true if the field is empty
	 * @return
	 */
	private static Predicate<Boolean> returnCheckIsFieldEmpty(){
		Predicate<Boolean> result = (field) -> {
			Boolean r = field==null?true:false;
			return r;
		};
		return result;
	}
}
