package api.worker;

import java.io.Serializable;

import api.attachment.AttachmentWorker;
import api.client.Client;
import api.database.DatabaseWorker;
import api.dialog.MessageWorker;
import api.media.AudioWorker;
import api.media.DocWorker;
import api.media.LikeWorker;
import api.media.PhotoWorker;
import api.media.VideoWorker;
import api.media.WallPostWorker;
import api.media.comment.CommentWorker;
import api.user.UserWorker;

/**
 * Provides a single-access interface to all API functions.
 */
public class WorkerInterface implements Serializable
{
	private static final long serialVersionUID = -3231232496938595736L;
	
	UserWorker userWorker;	
	MessageWorker messageWorker;
	AttachmentWorker attachmentWorker;
	AudioWorker audioWorker;
	PhotoWorker photoWorker;
	VideoWorker videoWorker;
	DocWorker docWorker;
	WallPostWorker wallPostWorker;
	CommentWorker commentWorker;
	LikeWorker likeWorker;
	DatabaseWorker databaseWorker;
	
	public WorkerInterface (Client client)
	{
		userWorker = new UserWorker(client);
		messageWorker = new MessageWorker(client);
		attachmentWorker = new AttachmentWorker(client);
		audioWorker = new AudioWorker(client);
		photoWorker = new PhotoWorker(client);
		videoWorker = new VideoWorker(client);
		docWorker = new DocWorker(client);
		wallPostWorker = new WallPostWorker(client);
		commentWorker = new CommentWorker(client);
		likeWorker = new LikeWorker(client);
		databaseWorker = new DatabaseWorker(client);
	}
	
	public UserWorker userWorker(){return this.userWorker;}
	public MessageWorker messageWorker(){return this.messageWorker;}
	public AttachmentWorker attachmentWorker(){return this.attachmentWorker;}
	public AudioWorker audioWorker(){return this.audioWorker;}
	public PhotoWorker photoWorker(){return this.photoWorker;}
	public VideoWorker videoWorker(){return this.videoWorker;}
	public DocWorker docWorker(){return this.docWorker;}
	public WallPostWorker wallPostWorker(){return this.wallPostWorker;}
	public CommentWorker commentWorker(){return this.commentWorker;}
	public LikeWorker likeWorker(){return this.likeWorker;}
	public DatabaseWorker databaseWorker() {return this.databaseWorker;}
}
