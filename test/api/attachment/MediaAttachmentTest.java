package api.attachment;

import static org.junit.Assert.*;

import org.junit.Test;

import api.attachment.Attachment.Type;
import api.media.MediaID;
import api.media.Photo;
import api.media.WallPost;
import api.media.comment.PhotoComment;
import api.media.comment.WallComment;

public class MediaAttachmentTest 
{
	@Test
	public void testMediaAttachmentString() 
	{
		MediaAttachment att = new MediaAttachment("photo3435_5756");
		assertTrue(att.ID().ownerID() == 3435);
		assertTrue(att.ID.mediaID() == 5756);
		assertTrue (att.ID.accessKey() == "");
		assertTrue(att.type == Type.ATTACH_PHOTO);
	}

	@Test
	public void testMediaAttachmentStringString() 
	{
		MediaAttachment att = new MediaAttachment("photo3435_5756", "983748fjdfdhfufskew23");
		assertTrue(att.ID().ownerID() == 3435);
		assertTrue(att.ID.mediaID() == 5756);
		assertTrue (att.ID.accessKey() == "983748fjdfdhfufskew23");
		assertTrue(att.type == Type.ATTACH_PHOTO);
	}

	@Test
	public void testMediaAttachmentMedia() 
	{
		MediaAttachment att = new MediaAttachment (new Photo());
		assertTrue(att.type == Type.ATTACH_PHOTO);
		
		att = new MediaAttachment (new WallPost());
		assertTrue(att.type == Type.ATTACH_WALL);
		
		att = new MediaAttachment (new WallComment());
		assertTrue(att.type == Type.ATTACH_COMMENT);
		
		att = new MediaAttachment (new Photo());
		assertTrue(att.type == Type.ATTACH_PHOTO);
						
		try
		{
			att = new MediaAttachment (new PhotoComment());
		} catch (Exception ex)
		{
			assertTrue (ex instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testMediaAttachmentStringMediaID()
	{
		MediaAttachment att = new MediaAttachment ("video", new MediaID(123, 345, "435"));
		assertTrue(att.ID().ownerID() == 123);
		assertTrue(att.ID.mediaID() == 345);
		assertTrue (att.ID.accessKey() == "435");
		assertTrue(att.type == Type.ATTACH_VIDEO);	
	}

	@Test
	public void testToString() 
	{
		MediaAttachment att = new MediaAttachment("photo3435_5756", "983748fjdfdhfufskew23");
		assertTrue(att.toString().equals("photo3435_5756"));
	}

}
