package game;

import java.util.ArrayList;
import java.util.Collection;

public class ChainCombo {
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
	
	public int getScore() {
		int score=0;
		for (PuyoChain chain:chains) 
			score+=chain.score();
		return chains.size()*score;
	}
	
	public int size() {
		return chains.size();
	}
	
}
