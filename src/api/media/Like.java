package api.media;

import api.object.VKObject;

public class Like extends VKObject
{
	int number;
	boolean canLike;
	boolean isLiked;
	boolean canRepost;
	
	public int number() {return this.number;}
	public boolean canLike() {return this.canLike;}
	public boolean isLiked() {return this.isLiked;}
	public boolean canRepost() {return this.canRepost;}
}
