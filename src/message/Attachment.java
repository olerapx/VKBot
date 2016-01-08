package message;
import java.util.HashMap;
import java.util.Set;

import media.Audio;
import media.Media;

/**
 * 
 * @class Attachment
 * @brief Attachment in messages
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
		ATTACH_WALL
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
		}
	};
	
	public Type type;
	public int ownerId;
	public int attachId;
	
	
	public Attachment(Type type, int ownerId, int attachId)
	{
		this.type=type;
		this.ownerId=ownerId;
		this.attachId=attachId;
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
		//else if other types
		
		attachment+= media.ownerId()+"_"+ media.id();
		this.fromString(attachment);
	}
	
	public String toString()
	{
		String result=types.get(type);
		result+=ownerId+"_"+attachId;		
		return result;
	}
	
	private void fromString(String attachment)
	{
		this.attachId = new Integer(attachment.split("_")[1]);	
		
		String typeWithOwner = attachment.split("_")[0];
		
		int firstDigitPos=0;
		while (!Character.isDigit(typeWithOwner.charAt(firstDigitPos++)));
		
		firstDigitPos-=1;
		
		String type = typeWithOwner.substring(0, firstDigitPos);
		this.ownerId = new Integer(typeWithOwner.substring(firstDigitPos));
		
		Set<HashMap.Entry<Attachment.Type,String>> entrySet=types.entrySet();

		for (HashMap.Entry<Attachment.Type,String> pair : entrySet) {
		    if (type.equals(pair.getValue())) {
		       this.type = pair.getKey();
		    }
		}
	}
}
