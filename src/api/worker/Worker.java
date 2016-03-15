package api.worker;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.client.Client;

public abstract class Worker 
{
	protected Client client;
		
	public Worker (Client client)
	{
		this.client=client;
	}

	protected static int getIntFromJSON(JSONObject data, String name) throws JSONException
	{
		if (data.has(name))
			return data.getInt(name);
		return 0;
	}
	
	protected static long getLongFromJSON(JSONObject data, String name) throws JSONException
	{
		if (data.has(name))
			return data.getLong(name);
		return 0L;
	}
	
	protected static boolean getBooleanFromJSON(JSONObject data, String name) throws JSONException
	{
		if (data.has(name))
			return data.getInt(name)!=0;
		return false;
	}
	
	protected static String getStringFromJSON(JSONObject data, String name)throws JSONException
	{
		if (data.has(name))
			return data.getString(name);
		return "";
	}

	protected static JSONObject getObjectFromJSON(JSONObject data, String name)throws JSONException
	{
		if (data.has(name))
			return data.getJSONObject(name);
		
		return null;
	}
	
	protected static JSONObject getObjectFromJSONArray(JSONArray data, int index)throws JSONException
	{
		if (index<0 || index >= data.length()) return null;
		
		return data.getJSONObject(index);
	}
	
	protected static JSONArray getArrayFromJSON(JSONObject data, String name)throws JSONException
	{
		if (data.has(name))
			return data.getJSONArray(name);
		return null;
	}

	protected static String encodeStringToURL(String str) throws UnsupportedEncodingException
	{
		return URLEncoder.encode(str, "UTF-8".replace(".", "&#046;"));
	}
}