package util;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
public class Effects 
{
	public static InnerShadow imageButtonActive = new InnerShadow();
	
	public static void init()
	{
		initImageButtonActive();
	}
	
	private static void initImageButtonActive()
	{
		imageButtonActive.setBlurType(BlurType.GAUSSIAN);
		imageButtonActive.setRadius(1.75);
		imageButtonActive.setChoke(0.8);
		imageButtonActive.setColor(Color.web("#00000345"));
		imageButtonActive.setWidth(4.5);
		imageButtonActive.setHeight(4.5);
	}	
}
