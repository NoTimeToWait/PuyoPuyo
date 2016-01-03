package visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import engine.GameContext;
import engine.NetworkPlayer;
import engine.Options;
import game.GameField;
import game.GameObject;
import game.GameObjectState;
import game.Puyo;

public class FieldView extends JPanel {
	private GameObject[] drawObjects;
	private static ArrayList<Animation> animations;
	public FieldView() {
		super();
		animations = new ArrayList<Animation>(); 
	}
	
	public static void shift(GameObject obj) {
		for (Animation anim:animations)
			if (obj.equals(anim.object)) {
				Animation newAnim = new TranslationY(obj, anim.animationLength);
				newAnim.iteration = anim.iteration;
				newAnim.listener = anim.listener;
				animations.remove(anim);
				animations.add(newAnim);
				break;
			}
	}
	
	public void updateObjects(NetworkPlayer player) {
		ArrayList<Animation> newAnimations = new ArrayList<Animation>();
		b1: for (GameObject obj:player.getGameObjects(true)) {
			if (obj.getState().equals(GameObjectState.FALLING_SLOW)) {
				for (Animation anim:animations)
					if (!anim.isFinished && obj.equals(anim.object)) {
						newAnimations.add(anim);
						continue b1;
					}
				if (GameField.tickCount>GameField.spawnTick+1) newAnimations.add(new TranslationY(obj, Options.SLOW_DROP_TICKS));
				else newAnimations.add(new Animation(obj, 1));
			} else if (obj.getState().equals(GameObjectState.FALLING_FAST)) {
				newAnimations.add(new TranslationY(obj, 1));
			} else newAnimations.add(new Animation(obj, 1));
		}
		//newAnimations.add(Animation.getUpdateCounter(player, this));
		animations = newAnimations;
		//System.out.println("Updated animations:"+animations.size());
	}
		
	
	
	public void drawObjects(NetworkPlayer player) {
		/*List<GameObject> newObjects = Arrays.asList(player.getGameObjects(true));
		List<GameObject> objects = Arrays.asList(drawObjects);
		ArrayList<GameObject> animateObjectsList = new ArrayList<GameObject>(Arrays.asList(drawObjects));
		animateObjectsList.retainAll(objects);
		for (GameObject obj:objects) {
			int index = newObjects.indexOf(obj);
			if (index>-1 && obj.getLine()!=newObjects.get(index).getLine()) {
				//Animation anim = Animation.
				//if ()
			}
		}
		for (GameObject obj:player.getGameObjects(true)) {
			Animation anim = new Animation(obj, 1);
			if (!animations.contains(anim)) {
				if (obj.getState().equals(GameObjectState.FALLING_SLOW))
					anim = new TranslationY
			}
		}*/
			
		drawObjects = player.getGameObjects(true);
		repaint();
	}
	
	
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		for (Animation anim:animations)
			anim.animate(g, this);
		
		Image[] images = Animation.images;
		drawObjects = GameContext.getPlayer().getGameObjects(true);
		if (drawObjects==null) return;
		if ( GameField.cells!=null)
		for (int i=0; i<6; i++)
			for (int j=0; j<12; j++) {
				GameObject obj = GameField.cells[i][j].gameObject;
				if (obj!=null)
				switch (obj.getType()) {
				case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[0],200+i*32, j*32, this); break;
				case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[1],200+i*32, j*32, this); break;
				case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[2],200+i*32, j*32, this); break;
				case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[3],200+i*32, j*32, this); break;
				default: g.drawImage(images[4],200+i*32, j*32, this);
			};
			}
			
		/*
		for (GameObject obj:drawObjects) 
			switch (obj.getType()) {
				case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[0],obj.getDrawX(), obj.getDrawY(), this); break;
				case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[1],obj.getDrawX(), obj.getDrawY(), this); break;
				case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[2],obj.getDrawX(), obj.getDrawY(), this); break;
				case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
					g.drawImage(images[3],obj.getDrawX(), obj.getDrawY(), this); break;
				default: g.drawImage(images[4],obj.getDrawX(), obj.getDrawY(), this);
			};
		drawObjects = null;*/
	}
}
