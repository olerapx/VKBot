package api.attachment;

import api.object.VKObject;

/**
 * Base class for all attachments.
 */
public abstract class Attachment extends VKObject
{
	private static final long serialVersionUID = 6699121504240728108L;

	public enum Type
	{
		ATTACH_PHOTO,
		ATTACH_VIDEO,
		ATTACH_AUDIO,
		ATTACH_DOC,
		ATTACH_WALL,
		ATTACH_COMMENT,
		ATTACH_LINK,
		ATTACH_OTHER
	}
	
	Type type;
	
	public Type type() {return this.type;}
	
	public boolean canAttach()
	{
		if (this instanceof MediaAttachment && this.type != Type.ATTACH_OTHER) return true;
		return false;
	}
	
	/**
	 * Converts enum Type to string for using in API requests. 
	 * API methods such as messages.getById returns type of wall reply attachment as "wall_reply" but to attach reply to a message we need to write "wall".
	 * So we need to convert it that way.
	 */
	protected String typeToString (Type type)
	{
		switch (type)
		{
		case ATTACH_PHOTO:{ return "photo";}
		case ATTACH_VIDEO:{ return "video";}
		case ATTACH_AUDIO:{ return "audio";}
		case ATTACH_DOC:{ return "doc";}
		case ATTACH_WALL:{ return "wall";}
		case ATTACH_COMMENT:{ return "wall";}
		case ATTACH_LINK: {return "link";}
		default: return "";	
		}
	}
			
	protected Type stringToType(String type)
	{
		switch (type)
		{
			case "photo":{ return Type.ATTACH_PHOTO;}
			case "video":{ return Type.ATTACH_VIDEO;}
			case "audio":{ return Type.ATTACH_AUDIO;}
			case "doc":{ return Type.ATTACH_DOC;}
			case "wall":{ return Type.ATTACH_WALL;}
			case "wall_reply":{ return Type.ATTACH_COMMENT;}
			case "link": {return Type.ATTACH_LINK;}
			default: return Type.ATTACH_OTHER;	
		}
	}
}
