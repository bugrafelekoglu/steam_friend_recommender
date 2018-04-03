import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.apache.commons.io.*;
import static java.lang.Math.toIntExact;

public class Steam {
	
	static ArrayList<Game> gamesPlayed = new ArrayList<Game>();
	static int counter = 0;
	
	public static void main(String[] args) throws IOException, ParseException {
		long steam_id_64;
		
		for(steam_id_64 = 76561197989433345L; steam_id_64 < 76561198814613742L; steam_id_64 += 1000) {
			steam_user(steam_id_64);
			if(gamesPlayed.size() != 0)
				counter++;
			System.out.println(gamesPlayed.size());
			gamesPlayed = new ArrayList<Game>();
		}
		
//		steam_user(76561198086831699L); // Kalderia
//		System.out.println(gamesPlayed.size());
//		gamesPlayed = new ArrayList<Game>();
//		steam_user(76561197989433345L);	// Allahilation
//		System.out.println(gamesPlayed.size());
//		gamesPlayed = new ArrayList<Game>();
//		steam_user(76561198023391133L);	// Snesky
//		System.out.println(gamesPlayed.size());
//		gamesPlayed = new ArrayList<Game>();
		
		System.out.println(counter);	
				
//		for(int i = 0; i < gamesPlayed.size(); i++) 
//			System.out.println(gamesPlayed.get(i).getAppID() + ", " + gamesPlayed.get(i).getPlaytime());
	}
	
	public static void steam_user(Long steam_id_64) throws IOException, ParseException {
		
		// You can generate your own Steam API Key from: http://steamcommunity.com/dev/apikey
		String steam_api_key = "";
		String steam_api_url = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key="+ 
				steam_api_key +"&steamid="+ steam_id_64 +"&format=json&include_played_free_games=1";
		
		// Connection parameters and objects
		int statusCode;
		URL url = null;
		HttpURLConnection http;
		
		// Connection in progress
		try {
			url = new URL(steam_api_url);
			http = (HttpURLConnection)url.openConnection();
			statusCode = http.getResponseCode();
			
			// Finds out whether id valid or not
			if(statusCode != 200)
				return;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// New file and parser
		File file = new File("steam_user.json");
		JSONParser parser = new JSONParser();
		
		// URL to File
		try {
			FileUtils.copyURLToFile(url, file, 1000, 1000);
		}
		catch(Exception e) {
			System.out.println("Timeout");
			return;
		}
		
		
		// JSON parsing
		try {
			Object obj = parser.parse(new FileReader("steam_user.json"));

            		JSONObject jsonObject = (JSONObject) obj;
            	
            		JSONObject response = (JSONObject) jsonObject.get("response");

            		// if a profile is private
            		if(response.size() == 0)
            			return;
           
            		Long gameCount = (Long) response.get("game_count");
            
            		// if a profile has not any game
            		if(gameCount == 0)
            			return;
            
            		// Loop games array
            		JSONArray games = (JSONArray) response.get("games");
            		Iterator<JSONObject> iterator = games.iterator();
            		while (iterator.hasNext()) {
            			JSONObject game = iterator.next();
                		Long appid = (Long) game.get("appid");
                		Long playtime = (Long) game.get("playtime_forever");
                		if(playtime != 0)
                			gamesPlayed.add(new Game(toIntExact(appid), toIntExact(playtime)));
            		}
        	} catch (FileNotFoundException e) {
            		e.printStackTrace();
        	} catch (IOException e) {
            		e.printStackTrace();
        	} catch (ParseException e) {
            		e.printStackTrace();
		}		
	}
}
