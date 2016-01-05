package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class PuyoChain {
	private HashSet<Puyo> puyos;
	/**
	 * type of chained puyos
	 */
	private int type;
	
	public PuyoChain(Puyo...puyoChain){
		puyos = new HashSet<Puyo>();
		type = puyoChain[0].getType();
		for (Puyo puyo:puyoChain) {
			puyos.add(puyo);
		}
	}
	
	public PuyoChain(Collection<Puyo> puyoChain){
		puyos = new HashSet<Puyo>();
		type = puyoChain.iterator().next().getType();
		puyos.addAll(puyoChain);
	}
	
	public boolean contains(Puyo puyo) {
		return puyos.contains(puyo);
	}
	
	public void add(Puyo puyo) {
		puyos.add(puyo);
	}
	
	/**
	 *  merge this chain with another (use when puyo drops from above and connects two chains)
	 */
	public boolean merge(PuyoChain anotherChain) {
		if (this.equals(anotherChain) || type!=anotherChain.getType()) return false;
		puyos.addAll(anotherChain.puyos);
		return true;
	}
	/**
	 * get type of puyos in the chain
	 * @return type of any puyo in chain (since they all belong to the same type)
	 */
	public int getType() {
		return type;
	}
	
	public int size() {
		return puyos.size();
	}
	
	/**
	 * calculate a value of this chain
	 * @return score depending on chain length
	 */
	public int score() {
		return puyos.size()*250;
	}
	
	public Collection<Puyo> getChain() {
		return puyos;
	}
	
	public void unchain() {
		for (Puyo puyo:puyos)
			puyo.setState(GameObjectState.FALLING_FAST);
	}
}
