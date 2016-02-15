package dialog;

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
		ATTACH_REPLY,
		ATTACH_LINK,
		ATTACH_OTHER
	}
	
	Type type;
	int ownerID;
	int attachID;
	String accessKey;
	
	private String typeToString (Type type)
	{
		switch (type)
		{
		case ATTACH_PHOTO:{ return "photo";}
		case ATTACH_VIDEO:{ return "video";}
		case ATTACH_AUDIO:{ return "audio";}
		case ATTACH_DOC:{ return "doc";}
		case ATTACH_WALL:{ return "wall";}
		case ATTACH_REPLY:{ return "wall";}
		case ATTACH_LINK:{ return "link";}
		default: return "";	
		}
	}
	
	private String mediaTypeToString (Media media)
	{
		if (media instanceof Audio)
			return "audio";	
		if (media instanceof Photo)
			return "photo";
		if (media instanceof Video)
			return "video";
		if (media instanceof Doc)
			return "doc";
		if (media instanceof WallPost)
			return "wall";
		if (media instanceof WallPostReply)
			return "wall_reply";
		if (media instanceof Link)
			return "link";
		return "";
	}
		
	private Type stringToType(String type)
	{
		switch (type)
		{
		case "photo":{ return Type.ATTACH_PHOTO;}
		case "video":{ return Type.ATTACH_VIDEO;}
		case "audio":{ return Type.ATTACH_AUDIO;}
		case "doc":{ return Type.ATTACH_DOC;}
		case "wall":{ return Type.ATTACH_WALL;}
		case "wall_reply":{ return Type.ATTACH_REPLY;}
		case "link":{ return Type.ATTACH_LINK;}
		default: return Type.ATTACH_OTHER;	
		}
	}

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
		String attachment=mediaTypeToString(media);
		
		attachment+= media.ownerID()+"_"+ media.ID();
		this.fromString(attachment);
	}
		
	public String toString()
	{
		String result=typeToString(type);
		result+=""+ownerID+"_"+attachID;		
		return result;
	}
	
	private void fromString(String attachment)
	{
		String[] splitted = attachment.split("_");
		String attach = splitted[splitted.length-1];
		this.attachID = new Integer(attach);	
		
		String typeWithOwner = attachment.substring(0, (attachment.length()-attach.length()-1));
		
		int firstDigitPos=0;
		while (!Character.isDigit(typeWithOwner.charAt(firstDigitPos)) && typeWithOwner.charAt(firstDigitPos)!='-') firstDigitPos++;
				
		String type = attachment.substring(0, firstDigitPos);
		this.ownerID = new Integer(typeWithOwner.substring(firstDigitPos));
		
		this.type = stringToType(type);
	}
	
	public Type type() {return this.type;}
	public int ownerID() {return this.ownerID;}
	public int attachID() {return this.attachID;}
	public String accessKey() {return this.accessKey;}
}
