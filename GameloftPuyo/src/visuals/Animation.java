package visuals;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

import javax.swing.JComponent;
import javax.swing.JPanel;

import engine.NetworkPlayer;
import engine.Options;
import game.GameObject;
import game.Puyo;

public class Animation {
	
	protected GameObject object;
	protected int animationLength;
	protected int iteration;
	protected boolean isFinished = false;
	//protected int startTick;
	protected AnimationListener listener;
	public static int MARGIN = 0;
	
	//TODO: load images in background thread if heavy, not necessary now
	public static final Image[] images;
	static {
		images = new Image[Options.getImageLinks().length];
		for (int i=0; i<images.length; i++)
			images[i] = Toolkit.getDefaultToolkit().getImage(Options.getImageLinks()[i]);
	}
	
	public Animation (GameObject gameObject, int animationLength){
		object = gameObject;
		//this.startTick = startTick;
		this.animationLength = animationLength;
	}
	
	public void setAnimationListener(AnimationListener listener) {
		this.listener = listener;
	}
	
	
	public void animate(Graphics graphics, JPanel pane) {
		iteration++;
		if (animationLength*Options.getFramesPerTick()<=iteration)
			isFinished = true;
		if (animationLength*Options.getFramesPerTick()<=iteration && listener!=null) {
			isFinished = true;
			listener.onAnimationEnd();
		}
		if (object!=null) drawObject(graphics, pane, object.getType(), getDrawX(), getDrawY());
		
	}
	
	protected static void drawObject(Graphics g, JComponent pane, int type, int x, int y) {
		int marginX = pane.getWidth()/2-Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH/2;
		int marginY = MARGIN;
		switch (type) {
		case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[0], marginX+x, marginY+y, pane); break;
		case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[1], marginX+x, marginY+y, pane); break;
		case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[2], marginX+x, marginY+y, pane); break;
		case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[3], marginX+x, marginY+y, pane); break;
		default: g.drawImage(images[4], marginX+x, marginY+y, pane); 
		};
	}
	
	protected int getDrawX() {
		return object.getDrawX();
	}
	
	protected int getDrawY() {
		return object.getDrawY();
	}
	
	public static Animation getUpdateCounter(NetworkPlayer player, FieldView fieldView) {
		Animation result = new Animation(null, 1);
		result.setAnimationListener(getUpdateAnimationsListener(player, fieldView));
		return result;
	}
	
	public static AnimationListener getUpdateAnimationsListener(NetworkPlayer player, FieldView fieldView) {
		return new AnimationListener(){
			@Override
			public void onAnimationEnd() {
				fieldView.updateObjects(player);
			}
		};
	}

}
