package api.media;

import api.object.VKObject;

public class Like extends VKObject
{
	private static final long serialVersionUID = -4994568432864025094L;
	
	int number;
	boolean canLike;
	boolean isLiked;
	boolean canRepost;
	
	public int number() {return this.number;}
	public boolean canLike() {return this.canLike;}
	public boolean isLiked() {return this.isLiked;}
	public boolean canRepost() {return this.canRepost;}
}
