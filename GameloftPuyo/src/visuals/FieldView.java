package visuals;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import engine.GameContext;
import engine.NetworkPlayer;
import engine.Options;
import game.GameField;
import game.GameObject;
import game.GameObjectState;

public class FieldView extends JPanel {
	private static ArrayList<Animation> animations;
	private JLabel score;
	private JLabel nextLabel;
	private JPanel scorePane;
	private JPanel nextPane;
	private boolean switched=false;
	
	public FieldView() {
		super();
		animations = new ArrayList<Animation>();
		this.setLayout(null);
		int fieldWidth =  Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH;
		scorePane = new JPanel();
		scorePane.setBounds(fieldWidth, 0, 100, 30);
		score = new JLabel(Options.getStrings().getScoreLblText()+":");
		scorePane.add(score);
		nextPane = new JPanel(){
			public void paintComponent(Graphics g) {
				//super.paintComponent(g);
				int[] puyos = GameContext.getPlayer().getPuyos();
				for (int i=0; i<puyos.length; i++)
					Animation.drawObject(g, nextPane, puyos[i], 18, 24+i*Options.CELL_WIDTH);
			}
		};
		nextLabel = new JLabel(Options.getStrings().getNextBtnText()+":");
		nextPane.add(nextLabel);
		nextPane.setBounds(fieldWidth, Options.CELL_WIDTH, Options.CELL_WIDTH*2+8, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT);

		this.add(scorePane);
		this.add(nextPane);
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
	
	public void switched() {
		switched = true;
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
		animations = newAnimations;
	}
		
		
	
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.GRAY);
		if (switched) {
			g.clearRect(0, 0, Options.WINDOW_WIDTH, Options.WINDOW_HEIGHT);
			switched = false;
		}
		g.fillRect(0, 0, Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT+4);
		for (Animation anim:animations)
			anim.animate(g, this);
	}
	
	public void updateScore() {
		switched = true;
		score.setText(Options.getStrings().getScoreLblText()+":"+GameContext.getPlayer().getScore());
		nextLabel.setText(Options.getStrings().getNextBtnText()+":");
	}
	
	
	
}
