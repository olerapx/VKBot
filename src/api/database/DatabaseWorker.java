package api.database;

import org.json.JSONArray;
import org.json.JSONObject;

import api.client.Client;
import api.worker.Worker;

/**
 * Provides an access to VK database.
 */
public class DatabaseWorker extends Worker 
{
	public DatabaseWorker(Client client) 
	{
		super(client);
	}

	public int getCountryID (String ANSICountryCode) throws Exception
	{
		if (ANSICountryCode=="") return 1;
		
		String str = "database.getCountries?"+
					 "&code="+ANSICountryCode;
				
		str = client.executeCommand(str);
		
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");	
		JSONArray arr = getArrayFromJSON(data, "items");
		
		return getIntFromJSON(getObjectFromJSONArray(arr,0), "id");
	}
	
	public int getRegionID(int countryID, String city) throws Exception
	{
		String str = client.executeCommand("database.getRegions?"+
				   "&country_id=" + countryID+
				   "&q="+city);
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");	
		JSONArray arr = getArrayFromJSON(data, "items");
		
		return getIntFromJSON(getObjectFromJSONArray(arr, 0), "id");			
	}
	
	public int getCityID(int countryID, int regionID, String city) throws Exception
	{
		String region = (regionID==-1? "" : "&region_id="+regionID);
		
		String str = client.executeCommand("database.getCities?"+
										   "&country_id=" + countryID+
										   region+
										   "&q="+city);
		
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");	
		JSONArray arr = getArrayFromJSON(data, "items");
		
		return getIntFromJSON(getObjectFromJSONArray(arr,0), "id");	
	}
}
