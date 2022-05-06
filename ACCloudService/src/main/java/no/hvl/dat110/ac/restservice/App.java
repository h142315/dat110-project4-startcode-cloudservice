package no.hvl.dat110.ac.restservice;

import static spark.Spark.after;
import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.put;
import static spark.Spark.post;
import static spark.Spark.delete;

import com.google.gson.Gson;

/**
 * Hello world!
 *
 */
public class App {
	
	static AccessLog accesslog = null;
	static AccessCode accesscode = null;
	
	public static void main(String[] args) {

		if (args.length > 0) {
			port(Integer.parseInt(args[0]));
		} else {
			port(8080);
		}

		// objects for data stored in the service
		
		accesslog = new AccessLog();
		accesscode  = new AccessCode();
		
		after((req, res) -> {
  		  res.type("application/json");
  		});
		
		// for basic testing purposes
		get("/accessdevice/hello", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	return gson.toJson("IoT Access Control Device");
		});
		
		// TODO: implement the routes required for the access control service
		// as per the HTTP/REST operations describined in the project description
		
		/**
		 * Skal returnere en JSON-representasjon av alle loggene i systemet,
		 * altså alle som har forsøkt å få tilgang - en samling av typen AccessEntry.
		 */
		get("/accessdevice/log", (req, res) -> {
			
		 	return accesslog.toJson();
		});
		
		
		/**
		 * Post skal her lagre når vi forsøker å få tilgang, 
		 * ved å lagre logg-meldinga som fins i body. 
		 */
		post("/accessdevice/log", (req, res) -> {
			
		 	Gson gson = new Gson();
		 	
		 	AccessMessage msg = gson.fromJson(req.body(), AccessMessage.class);
		 	int id = accesslog.add(msg.getMessage());
		 	
		 	return gson.toJson(accesslog.get(id));
		});
		
		/**
		 * Returnerer en JSON-representasjon av tilgang til en viss ID
		 */
		get("/accessdevice/log/:id", (req, res) -> {
			Gson gson = new Gson();
			
			int id = Integer.parseInt(req.params(":id"));
			
		 	return gson.toJson(accesslog.get(id));
		});
		
		/**
		 * Oppdaterer accesscode som er lagret i skytjenesten, til en kombinasjon av knappene. 
		 * Formatet er JSON og skal representere et objekt av Javaklassen AccessCode
		 * 
		 */
		put("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			
			AccessCode kode = gson.fromJson(req.body(), AccessCode.class);
			accesscode.setAccesscode(kode.getAccesscode());
			
			return gson.toJson(kode);
		});
		
		/**
		 * Henter nåværende accesskode fra server. 
		 */
		get("/accessdevice/code", (req, res) -> {
			Gson gson = new Gson();
			
			//Brukt for å oppdatere accesscode.
		 	return gson.toJson(accesscode);
		});
		
		/**
		 * Sletter accessloggen, og returnerer tomt JSONobjekt som body i HTTP response
		 */
		delete("/accessdevice/log", (req, res) -> {
			accesslog.clear();
			return accesslog.toJson();
		});
		
    }
    
}
