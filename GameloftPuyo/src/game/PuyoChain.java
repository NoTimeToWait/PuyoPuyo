package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class PuyoChain {
	private HashSet<Puyo> puyos;
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
	
	public boolean merge(PuyoChain anotherChain) {
		if (this.equals(anotherChain) || type!=anotherChain.getType()) return false;
		puyos.addAll(anotherChain.puyos);
		return true;
	}
	
	public int getType() {
		return type;
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
