package visuals;

import engine.Options;
import game.GameObject;

public class TranslationY extends Animation{
	private int fromY;
	private int toY;
	private float step;
	protected TranslationY (GameObject gameObject, int animationLength, int line){
		super(gameObject, animationLength);
		fromY = gameObject.getLine()*Options.CELL_WIDTH;
		this.toY = line*Options.CELL_WIDTH;
		step = (toY - fromY)/Options.getFramesPerTick();
	}
	
	@Override
	public int getDrawY() {
		return (int) (fromY+step*iteration/animationLength);
	}
}
