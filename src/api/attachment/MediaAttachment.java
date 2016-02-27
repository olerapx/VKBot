package api.attachment;

import api.media.Audio;
import api.media.Doc;
import api.media.Media;
import api.media.MediaID;
import api.media.Photo;
import api.media.Video;
import api.media.WallPost;
import api.media.comment.WallComment;

/**
 * Represents attachments that can be attached to a message.
 */
public class MediaAttachment extends Attachment
{
	MediaID ID;
	
	public MediaID ID() {return this.ID;}
		
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
		if (media instanceof WallComment)
			return Type.ATTACH_COMMENT;
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

	MediaAttachment(Type type, MediaID ID)
	{
		this.type=type;
		this.ID = ID;
	}
	
	MediaAttachment(String attachment)
	{
		this.fromString(attachment, "");
	}
	
	MediaAttachment(String attachment, String accessKey)
	{
		this.fromString(attachment, accessKey);
	}
	
	public MediaAttachment(Media media)
	{
		this.type = mediaTypeToType(media);
		this.ID = media.ID();
	}
	
	MediaAttachment(String type, MediaID ID)
	{
		this.type =  stringToType(type);
		this.ID = ID;
	}
			
	public String toString()
	{
		String result=typeToString(type);
		result+=""+this.ID.ownerID()+"_"+this.ID.mediaID();		
		return result;
	}
}
