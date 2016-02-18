package dialog;

import media.Audio;
import media.Doc;
import media.Link;
import media.Media;
import media.MediaID;
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
	MediaID ID;
	
	/**
	 * 
	 * @param type
	 * Convert enum Type to string to use in API requests. 
	 * API methods such as messages.getById returns type of wall reply attachment as "wall_reply" but to attach reply to a message we need to write "wall".
	 * So we need to convert it that way.
	 * 
	 */
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
	
	@SuppressWarnings("unused")
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
	
	private Type mediaTypeToType(Media media)
	{
		if (media instanceof Audio)
			 return Type.ATTACH_AUDIO;
		if (media instanceof Photo)
			 return Type.ATTACH_PHOTO;
		if (media instanceof Video)
			return Type.ATTACH_VIDEO;
		if (media instanceof Doc)
			 return Type.ATTACH_DOC;
		if (media instanceof WallPost)
			return Type.ATTACH_WALL;
		if (media instanceof WallPostReply)
			return Type.ATTACH_REPLY;
		if (media instanceof Link)
			return Type.ATTACH_LINK;
		return Type.ATTACH_OTHER;	
	}
	
	private void fromString(String attachment, String accessKey)
	{
		String[] splitted = attachment.split("_");
		
		String attach = splitted[splitted.length-1];
		
		String typeWithOwner = attachment.substring(0, (attachment.length()-attach.length()-1));
		
		int firstDigitPos=0;
		while (!Character.isDigit(typeWithOwner.charAt(firstDigitPos)) && typeWithOwner.charAt(firstDigitPos)!='-') firstDigitPos++;
				
		String type = attachment.substring(0, firstDigitPos);
		
		this.type = stringToType(type);
		
		this.ID = new MediaID(new Integer(typeWithOwner.substring(firstDigitPos)), new Integer(attach), accessKey);
	}

	public Attachment(Type type, MediaID ID)
	{
		this.type=type;
		this.ID = ID;
	}
	
	public Attachment(String attachment)
	{
		this.fromString(attachment, "");
	}
	
	public Attachment(String attachment, String accessKey)
	{
		this.fromString(attachment, accessKey);
	}
	
	public Attachment(Media media)
	{
		this.type = mediaTypeToType(media);
		this.ID = media.ID();
	}
		
	public String toString()
	{
		String result=typeToString(type);
		result+=""+this.ID.ownerID()+"_"+this.ID.mediaID();		
		return result;
	}

	public Type type() {return this.type;}
	public MediaID ID() {return this.ID;}
}
