package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class PuyoChain {
	private HashSet<Puyo> puyos;
	private int color;
	
	public PuyoChain(Puyo...puyoChain){
		puyos = new HashSet<Puyo>();
		color = puyoChain[0].getColor();
		for (Puyo puyo:puyoChain) {
			puyos.add(puyo);
		}
	}
	
	public PuyoChain(Collection<Puyo> puyoChain){
		puyos = new HashSet<Puyo>();
		color = puyoChain.iterator().next().getColor();
		puyos.addAll(puyoChain);
	}
	
	public boolean contains(Puyo puyo) {
		return puyos.contains(puyo);
	}
	
	public void add(Puyo puyo) {
		puyos.add(puyo);
	}
	
	public boolean merge(PuyoChain anotherChain) {
		if (this.equals(anotherChain) || color!=anotherChain.getColor()) return false;
		puyos.addAll(anotherChain.puyos);
		return true;
	}
	
	public int getColor() {
		return color;
	}
	
	public int size() {
		return puyos.size();
	}
	
	public Collection<Puyo> getChain() {
		return puyos;
	}
	
	public void unchain() {
		for (Puyo puyo:puyos)
			puyo.setState(GameObjectState.FALLING_FAST);
	}
}
