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
	private static final long serialVersionUID = 1591576383053211624L;

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
	
	private String[] getCountryNames (String ids) throws Exception
	{
		String str = "database.getCountriesById?"+
				 "country_ids=" + ids;
		str = client.executeCommand(str);
		
		JSONObject obj = new JSONObject(str);
		JSONArray data =  getArrayFromJSON(obj, "response");	
		
		int count = data.length();
		
		String[] names = new String [count];
		for (int i=0;i<count;i++)
			names[i] = getStringFromJSON(getObjectFromJSONArray(data, i), "title");
		
		return names;
	}
	
	public String[] getCountryNames (int[] countryIDs) throws Exception
	{
		String ids="";
		
		for (int i=0; i<countryIDs.length; i++)
		{
			ids+=countryIDs[i];
			if (i<countryIDs.length-1) ids+=",";
		}
		
		return getCountryNames(ids);
	}
	
	public String getCountryName (int countryID) throws Exception
	{
		String ids = String.valueOf(countryID);
		
		return getCountryNames(ids)[0];
	}

	
	
	private String[] getCityNames (String ids) throws Exception
	{
		String str = "database.getCitiesById?"+
				 "city_ids=" + ids;
		str = client.executeCommand(str);
		
		JSONObject obj = new JSONObject(str);
		JSONArray data =  getArrayFromJSON(obj, "response");	
		
		int count = data.length();
		
		String[] names = new String [count];
		for (int i=0;i<count;i++)
			names[i] = getStringFromJSON(getObjectFromJSONArray(data, i), "title");
		
		return names;
	}
	
	public String[] getCityNames (int[] cityIDs) throws Exception
	{
		String ids="";
		
		for (int i=0; i<cityIDs.length; i++)
		{
			ids+=cityIDs[i];
			if (i<cityIDs.length-1) ids+=",";
		}
		
		return getCityNames(ids);
	}
	
	public String getCityName (int cityID) throws Exception
	{
		String ids = String.valueOf(cityID);
		
		return getCityNames(ids)[0];
	}
	
	/**
	  * @param countryID
	 * @param city - Search query. Can be inaccurate.
	 * @return First found city identificator in country with countryID in region with regionID.
	 */
	public int getRegionID(int countryID, String city) throws Exception
	{
		String str = client.executeCommand("database.getRegions?"+
				   "&country_id=" + countryID+
				   "&q="+encodeStringToURL(city));
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");	
		JSONArray arr = getArrayFromJSON(data, "items");
		
		return getIntFromJSON(getObjectFromJSONArray(arr, 0), "id");			
	}
	
	/**
	 * 
	 * @param countryID
	 * @param regionID
	 * @param city - Search query. Can be inaccurate.
	 * @return First found city identificator in country with countryID in region with regionID.
	 */
	public int getCityID(int countryID, int regionID, String city) throws Exception
	{
		String region = (regionID==0? "" : "&region_id="+regionID);
		
		String str = client.executeCommand("database.getCities?"+
										   "&country_id=" + countryID+
										   region+
										   "&q="+encodeStringToURL(city));
		
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");	
		JSONArray arr = getArrayFromJSON(data, "items");
		
		return getIntFromJSON(getObjectFromJSONArray(arr,0), "id");	
	}

	/**
	 * 
	 * @param countryID
	 * @param city - Search query. Can be inaccurate.
	 * @return First found city identificator in country with countryID in region with regionID.
	 */
	public int getCityID(int countryID, String city) throws Exception
	{
		return getCityID(countryID, 0, city);
	}
	
	
	private int getUniversityID(int countryID, int cityID, String university) throws Exception
	{
		String country = (countryID == 0? "" : "&country_id="+countryID);
		String city = (cityID == 0? "" : "&city_id="+cityID);
		
		
		String str = "database.getUniversities?"+
					 "&q="+encodeStringToURL(university)+
					 country+
					 city;
				
		str = client.executeCommand(str);
		
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");	
		JSONArray arr = getArrayFromJSON(data, "items");
		
		return getIntFromJSON(getObjectFromJSONArray(arr,0), "id");
	}
	
	public int getUniversityIDByCountry (int countryID, String university) throws Exception
	{
		return getUniversityID(countryID, 0, university);
	}
	
	public int getUniversityIDByCity (int cityID, String university) throws Exception
	{
		return getUniversityID(0, cityID, university);
	}
	
	public int getUniversityID (String university) throws Exception
	{
		return getUniversityID(0, 0, university);
	}

	public int getSchoolID (int cityID, String school) throws Exception
	{
		
		String str = "database.getSchools?"+
					 "&city_id="+cityID+
					 "&q="+encodeStringToURL(school);
				
		str = client.executeCommand(str);
		
		JSONObject obj = new JSONObject(str);
		JSONObject data =  getObjectFromJSON(obj, "response");	
		JSONArray arr = getArrayFromJSON(data, "items");
		
		return getIntFromJSON(getObjectFromJSONArray(arr,0), "id");
	}
}

