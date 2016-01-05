package game;

import java.util.ArrayList;
import java.util.Collection;
/**
 * class which processes chain combo
 *
 */
public class ChainCombo {
	/**
	 * game tick when the last chain was edded to the combo
	 * we need this value to calculate a time when chain combo expires
	 */
	protected int tick;
	private ArrayList<PuyoChain> chains = new ArrayList<PuyoChain>();
	
	public ChainCombo(PuyoChain chain, int tick) {
		this.tick = tick;
		chains.add(chain);
	}
	
	public void add(PuyoChain chain) {
		chains.add(chain);
	}
	
	public void addAll(Collection<PuyoChain> moreChains) {
		chains.addAll(moreChains);
	}
	
	/**
	 * calculate score amount for this combo
	 * @return total score
	 */
	public int getScore() {
		int score=0;
		for (PuyoChain chain:chains) 
			score+=chain.score();
		return chains.size()*score;
	}
	
	/**
	 * number of chains in this combo
	 */
	public int size() {
		return chains.size();
	}
	
}
