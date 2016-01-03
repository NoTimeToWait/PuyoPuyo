package visuals;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;

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
	
	
	public void animate(Graphics graphics, ImageObserver imgObs) {
		iteration++;
		if (animationLength*Options.getFramesPerTick()<=iteration)
			isFinished = true;
		if (animationLength*Options.getFramesPerTick()<=iteration && listener!=null) {
			isFinished = true;
			listener.onAnimationEnd();
		}
		drawObject(graphics, imgObs);
		
	}
	
	protected void drawObject(Graphics g, ImageObserver imgObs) {
		if (object==null) return;
		switch (object.getType()) {
		case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[0], getDrawX(), getDrawY(), imgObs); break;
		case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[1], getDrawX(), getDrawY(), imgObs); break;
		case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[2], getDrawX(), getDrawY(), imgObs); break;
		case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
			g.drawImage(images[3], getDrawX(), getDrawY(), imgObs); break;
		default: g.drawImage(images[4], getDrawX(), getDrawY(), imgObs);
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
