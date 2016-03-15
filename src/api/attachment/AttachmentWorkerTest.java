package api.attachment;

import static org.junit.Assert.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class AttachmentWorkerTest
{
	@Test
	public void testGetFromJSONArray() throws Exception
	{
		String JSON = "[{"
							+ "type: 'link',"
							+ "link: {"
								+ "url: 'http://test.com',"
								+ "title: 'Title',"
								+ "caption: 'Caption',"
								+ "description: 'Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet',"
								+ "photo: {id: 450,"
									+ "album_id: -2,"
									+ "owner_id: 410,"
									+ "width: 150,"
									+ "height: 150,"
									+ "text: '',"
									+ "	date: 1457631805"
								+ "},"
								+ "	is_external: 1"
							+ "}"
						+ "},"
						+ "{"
							+ "type: 'photo',"
							+ "photo: {"
								+ "id: 100,"
								+ "album_id: -6,"
								+ "owner_id: 1,"
								+ "text: '',"
								+ "date: 1328126422,"
								+ "post_id: 45430,"
								+ "likes: {"
									+ "user_likes: 0,count: 577904"
								+ "},"
								+ "comments: {"
									+ "count: 421"
								+ "},"
								+ "can_comment: 1"
							+"}}]";
		
		Attachment[] atts = AttachmentWorker.getFromJSONArray((new JSONArray(JSON)));
		
		assertEquals(2, atts.length);
		assertTrue (atts[0] instanceof Link);
		assertTrue (atts[1] instanceof MediaAttachment);
	}

	@Test
	public void testGetFromJSON() throws JSONException 
	{
		String JSON="{"
				+ "type: 'link',"
				+ "link: {"
					+ "url: 'http://test.com',"
					+ "title: 'Title',"
					+ "caption: 'Caption',"
					+ "description: 'Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet',"
					+ "photo: {id: 450,"
						+ "album_id: -2,"
						+ "owner_id: 410,"
						+ "width: 150,"
						+ "height: 150,"
						+ "text: '',"
						+ "	date: 1457631805"
					+ "},"
					+ "	is_external: 1"
				+ "}"
			+ "}";
		
		Attachment att = AttachmentWorker.getFromJSON(new JSONObject(JSON));
		
		assertTrue(att instanceof Link);
		
		JSON = "{"
				+ "type: 'photo',"
				+ "photo: {"
					+ "id: 278184324,"
					+ "album_id: -6,"
					+ "owner_id: 1,"
					+ "text: '',"
					+ "date: 1328126422,"
					+ "post_id: 45430,"
					+ "likes: {"
						+ "user_likes: 0,count: 577904"
					+ "},"
					+ "comments: {"
						+ "count: 421"
					+ "},"
					+ "can_comment: 1"
				+"}}";
		att = AttachmentWorker.getFromJSON(new JSONObject(JSON));
		
		assertTrue(att instanceof MediaAttachment);
		assertEquals(1,((MediaAttachment)att).ID.ownerID());
		assertEquals(278184324, ((MediaAttachment)att).ID.mediaID());
		
		JSON = "{"
				+ "type: 'wall',"
				+ "wall: {"
					+ "id: 278184324,"
					+ "from_id: 1,"
					+ "text: '',"
					+ "date: 1328126422,"
					+ "post_id: 45430,"
					+ "likes: {"
						+ "user_likes: 0,count: 577904"
					+ "},"
					+ "comments: {"
						+ "count: 421"
					+ "},"
					+ "can_comment: 1"
				+"}}";
		
		att = AttachmentWorker.getFromJSON(new JSONObject(JSON));
		
		assertTrue(att instanceof MediaAttachment);
		assertEquals(1, ((MediaAttachment)att).ID.ownerID());
		assertEquals(278184324, ((MediaAttachment)att).ID.mediaID());
	}

	@Test
	public void testGetLinkFromJSON() throws JSONException 
	{
		String JSON = "{"
				+ "url: 'http://test.com',"
				+ "title: 'Title',"
				+ "caption: 'Caption',"
				+ "description: 'Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet Lorem ipsum dolor sit amet',"
						+ "photo: {id: 450,"
						+ "album_id: -2,"
						+ "owner_id: 410,"
						+ "width: 150,"
						+ "height: 150,"
						+ "text: '',"
						+ "	date: 1457631805"
						+ "},"
						+ "	is_external: 1"
						+ "}";
		
		Link l = AttachmentWorker.getLinkFromJSON(new JSONObject(JSON));
		
		assertEquals(l.caption, "Caption");
		assertEquals(l.URL, "http://test.com");
		assertEquals(l.previewURL, "");
		assertEquals(l.canAttach(), false);
		assertFalse(l.photo()==null);
	}
}
