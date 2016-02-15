package dialog;
import java.util.HashMap;
import java.util.Set;

import media.Audio;
import media.Doc;
import media.Link;
import media.Media;
import media.Photo;
import media.Video;
import media.WallPost;
import media.WallPostReply;

/**
 * 
 * Attachment in messages and on walls (limited).
 *
 */

public class Attachment
{
	public enum Type
	{
		ATTACH_PHOTO,
		ATTACH_VIDEO,
		ATTACH_AUDIO,
		ATTACH_DOC,
		ATTACH_WALL,
		ATTACH_LINK
	}
	
	private HashMap <Type, String> types= new HashMap <Type, String>()
	{
		private static final long serialVersionUID = 1L;
		{
			put(Type.ATTACH_PHOTO,"photo");
			put(Type.ATTACH_VIDEO,"video"); 
			put(Type.ATTACH_AUDIO,"audio");
			put(Type.ATTACH_DOC,"doc");
			put(Type.ATTACH_WALL,"wall");
			put (Type.ATTACH_LINK, "link");
		}
	};
	
	Type type;
	int ownerID;
	int attachID;
		
	public Attachment(Type type, int ownerID, int attachID)
	{
		this.type=type;
		this.ownerID=ownerID;
		this.attachID=attachID;
	}
	
	public Attachment(String attachment)
	{
		this.fromString(attachment);
	}
	
	public Attachment(Media media)
	{
		String attachment="";
		if (media instanceof Audio)
			 attachment= "audio";	
		else if (media instanceof Photo)
			attachment="photo";
		else if (media instanceof Video)
			attachment="video";
		else if (media instanceof Doc)
			attachment = "doc";
		else if (media instanceof WallPost)
			attachment="wall";
		else if (media instanceof WallPostReply)
			attachment="wall";
		else if (media instanceof Link)
			attachment="link";
		
		attachment+= media.ownerID()+"_"+ media.ID();
		this.fromString(attachment);
	}
	
	public String toString()
	{
		String result=types.get(type);
		result+=""+ownerID+"_"+attachID;		
		return result;
	}
	
	private void fromString(String attachment)
	{
		this.attachID = new Integer(attachment.split("_")[1]);	
		
		String typeWithOwner = attachment.split("_")[0];
		
		int firstDigitPos=0;
		while (!Character.isDigit(typeWithOwner.charAt(firstDigitPos)) && typeWithOwner.charAt(firstDigitPos)!='-') firstDigitPos++;
				
		String type = typeWithOwner.substring(0, firstDigitPos);
		this.ownerID = new Integer(typeWithOwner.substring(firstDigitPos));
		
		Set<HashMap.Entry<Attachment.Type,String>> entrySet=types.entrySet();

		for (HashMap.Entry<Attachment.Type,String> pair : entrySet) 
		{
		    if (type.equals(pair.getValue())) 
		       this.type = pair.getKey();		    
		}
	}
	
	public Type type() {return this.type;}
	public int ownerID() {return this.ownerID;}
	public int attachID() {return this.attachID;}
}
