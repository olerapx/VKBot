package api.user;

import api.user.User.Relation;
import api.user.User.Sex;

/**
 * Contains almost all parameters for user searching.
 */
public class UserSearchParameters 
{
	String city="";
	String country="";
	
	String region="";
	String schoolRegion="";
	
	String schoolCity="";
	String schoolCountry="";
	
	public String universityCountry="";
	
	String school="";
	
	String universityCity="";
	String university="";
		
	public String query="";
	public boolean sortByPopularity=true;
	
	public int offset=-1;
	public int count=-1;
	
	public String homeTown="";
	
	public int graduationYearUniversity=-1;	
	public int graduationYearSchool=-1;
	
	public Sex sex=Sex.ANY;
	public Relation relation = Relation.NO_INFORMATION;
	
	public int startAge=-1;
	public int endAge=-1;
	public int birthDay=-1;
	public int birthMonth=-1;
	public int birthYear=-1;
	
	public boolean needOnline=false;
	public boolean needPhoto=false;
	
	public String religion="";
	public String interests="";
	public String company="";
	public String position="";
	public boolean searchFriends = false;
	public boolean searchSubscriptions = false;
	public int groupID = 0;
	
	public void setCountry(String ANSICountryCode)
	{
		this.country = ANSICountryCode;
	}
	
	public void setCity(String ANSICountryCode, String city)
	{
		this.country = ANSICountryCode;
		this.city = city;
	}
	
	public void setCity(String ANSICountryCode, String region, String city)
	{
		this.country = ANSICountryCode;
		this.region = region;
		this.city = city;
	}
	
	public void setSchoolCity (String ANSICountryCode, String city)
	{
		this.schoolCountry = ANSICountryCode;
		this.schoolCity = city;
	}
	
	public void setSchoolCity (String ANSICountryCode, String region, String city)
	{
		this.schoolCountry = ANSICountryCode;
		this.schoolRegion = region;
		this.schoolCity = city;	
	}

	public void setSchool(String school, String city, String country)
	{
		this.school = school;
		this.schoolCity = city;
		this.schoolCountry = country;
	}
	
	public void setSchool(String school, String city, String country, String region)
	{
		this.school = school;
		this.schoolCity = city;
		this.schoolCountry = country;
		this.schoolRegion = region;
	}
	
	public void setUniversity(String university, String city, String country)
	{
		this.university = university;
		this.universityCity = city;
		this.universityCountry = country;
	}
	
	public void setUniversity(String university, String country)
	{
		this.university = university;
		this.universityCountry = country;
	}
}
