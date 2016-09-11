package api.attachment;

import static org.junit.Assert.*;

import org.junit.Test;

public class AttachmentTest
{
	@Test
	public void testCanAttach() 
	{
		Attachment att = new MediaAttachment("photo23_342");
		assertTrue(att.canAttach());
		
		att =  new MediaAttachment("some23_342");
		assertFalse(att.canAttach());
		
		att = new Link();
		assertFalse(att.canAttach());
	}

	@Test
	public void testTypeToString() 
	{
		Attachment att = new MediaAttachment ("photo3_35432");
		
		assertEquals(att.typeToString(att.type), "photo");
		
		att = new MediaAttachment ("wall3_35432");
		assertEquals(att.typeToString(att.type), "wall");	
		att = new MediaAttachment ("wall_reply3_35432");
		assertEquals(att.typeToString(att.type), "wall");	
	}

	@Test
	public void testStringToType()
	{		
		Attachment att = new MediaAttachment("photo3_5");
		assertEquals(att.stringToType("photo"), Attachment.Type.ATTACH_PHOTO);
		assertEquals(att.stringToType("wall"), Attachment.Type.ATTACH_WALL);	
		assertEquals(att.stringToType("wall_reply"), Attachment.Type.ATTACH_COMMENT);
	}
}
