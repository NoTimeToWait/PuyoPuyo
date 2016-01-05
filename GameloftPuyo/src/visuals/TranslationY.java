package visuals;

import engine.Options;
import game.GameObject;
/**
 * vertical translation animation
 * performs falling animation
 *
 */
public class TranslationY extends Animation{
	private int fromY;
	private int toY;
	private float step;
	protected TranslationY (GameObject gameObject, int animationLength){
		super(gameObject, animationLength);
		fromY = (gameObject.getLine()-1)*Options.CELL_WIDTH;
		this.toY = gameObject.getLine()*Options.CELL_WIDTH;
		step = (toY - fromY)/Options.getFramesPerTick();
	}
	
	@Override
	public int getDrawY() {
		return (int) (fromY+step*Math.floor(iteration/animationLength));
	}
}
