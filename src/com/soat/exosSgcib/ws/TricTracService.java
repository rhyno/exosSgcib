package com.soat.exosSgcib.ws;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.soat.exosSgcib.itf.TricTracItf;
import com.soat.exosSgcib.model.TricTrac;
import com.soat.exosSgcib.utils.TricTracQual;
import com.soat.exosSgcib.utils.TricTracRules;


@Path("/trictrac")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
@SessionScoped
public class TricTracService implements Serializable, TricTracItf{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger _l = Logger.getLogger(TricTracService.class);
	
	private transient @Inject @TricTracQual TricTrac tricTrac;
	
	@GET
	@Path("/validate")
	public TricTrac validate(
			@QueryParam("player")Boolean player, 
			@QueryParam("row")Integer row, 
			@QueryParam("column")Integer column) {
		
		_l.info("Receiving validation order!!");
		_l.info("player value :" + player);
		_l.info("row value :" + row);
		_l.info("column value :" + column);
		
		if(player!=null&&row!=null&&column!=null){
				TricTracRules.processTurn(this.tricTrac, player, row, column);
		}
		
		_l.info(this.tricTrac.getTricTracTab()[0][0]+" "+this.tricTrac.getTricTracTab()[0][1]+" "+this.tricTrac.getTricTracTab()[0][2]);
		_l.info(this.tricTrac.getTricTracTab()[1][0]+" "+this.tricTrac.getTricTracTab()[1][1]+" "+this.tricTrac.getTricTracTab()[1][2]);
		_l.info(this.tricTrac.getTricTracTab()[2][0]+" "+this.tricTrac.getTricTracTab()[2][1]+" "+this.tricTrac.getTricTracTab()[2][2]);
		
		return this.tricTrac;
	}

	@GET
	@Path("/reset")
	public TricTrac reset() {
		_l.info("Reseting game");
		
		this.tricTrac = this.initTricTrac();
		return this.tricTrac;
	}

	@javax.enterprise.inject.Produces @TricTracQual
	private TricTrac initTricTrac(){
		
		_l.info("initialising tric trac ...");
		
		TricTrac result = new TricTrac();
		
		result.setGameOver(false);
		result.setTricTracTab(new Boolean[3][3]);
		result.setWinner(null);
		
		return result;
	}
}
