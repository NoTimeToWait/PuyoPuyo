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
	private static JLabel score;
	private static JPanel scorePane;
	private static JPanel nextPane;
	private boolean switched=false;
	
	public FieldView() {
		super();
		animations = new ArrayList<Animation>();
		this.setLayout(null);
		//fieldPane.setBounds(0, 36, 192, 384);
		int fieldWidth =  Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH;
		scorePane = new JPanel();
		//scorePane.setBounds(0, 0, 192, 30);
		scorePane.setBounds(fieldWidth, 0, 100, 30);
		score = new JLabel(Options.getStrings().getScoreLblText()+":");
		scorePane.add(score);
		nextPane = new JPanel(){
			public void paintComponent(Graphics g) {
				//super.paintComponent(g);
				int[] puyos = GameContext.getPlayer().getPuyos();
				for (int i=0; i<puyos.length; i++)
					Animation.drawObject(g, this, puyos[i], 18, 24+i*Options.CELL_WIDTH);
			}
		};
		JLabel nextLabel = new JLabel(Options.getStrings().getNextBtnText()+":");
		nextPane.add(nextLabel);
		//nextPane.setBounds(200, 30, 36, 100);
		nextPane.setBounds(fieldWidth, Options.CELL_WIDTH, Options.CELL_WIDTH*2+8, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT);
		//this.add(fieldPane);
		this.add(scorePane);
		this.add(nextPane);
		/*this.setLayout(new BorderLayout());
		this.setAlignmentY(TOP_ALIGNMENT);
		
		fieldPane = getFieldPane();
		fieldPane.setPreferredSize(new Dimension(Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, 
														Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT));
		
		scorePane = new JPanel();
		score = new JLabel(Options.getStrings().getScoreLblText()+":");
		score.setPreferredSize(new Dimension(Options.CELL_WIDTH*(Options.DEFAULT_FIELD_WIDTH+2), 18));
		score.setHorizontalAlignment(JLabel.LEFT);
		score.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		scorePane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		scorePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		scorePane.add(score);
		
		nextPane = new JPanel(){
			public void paint(Graphics g) {
				super.paint(g);
				int[] puyos = GameContext.getPlayer().getPuyos();
				for (int i=0; i<puyos.length; i++)
					Animation.drawObject(g, fieldPane, puyos[i], 18, 24+i*Options.CELL_WIDTH);
			}
		};
		nextPane.setPreferredSize(new Dimension(Options.CELL_WIDTH*2+8, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT));
		JLabel nextLabel = new JLabel(Options.getStrings().getNextBtnText()+":");
		nextLabel.setVerticalAlignment(JLabel.TOP);
		nextLabel.setPreferredSize(new Dimension(Options.CELL_WIDTH*2, Options.CELL_WIDTH*3));
		nextLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		nextPane.add(nextLabel);
		
		
		JPanel northPane = new JPanel();
		northPane.add(Box.createHorizontalGlue());
		northPane.add(scorePane);
		northPane.add(Box.createHorizontalGlue());
		JPanel centerPane = new JPanel();
		centerPane.add(fieldPane);
		centerPane.add(nextPane);
		this.add(northPane, BorderLayout.NORTH);
		this.add(centerPane, BorderLayout.CENTER);*/
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
		int marginX = this.getWidth()/2-Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH/2;
		int marginY = Animation.MARGIN;
		g.fillRect(0, 0, Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT+4);
		for (Animation anim:animations)
			anim.animate(g, this);
		//if(chainCombo>1)
		//	g.drawImage(getChainComboImage(Options.getStrings().getChainComboString()+" x"+chainCombo), 0, 0, this);
	}
	
	public static void repaintAdditionalInfo() {
		score.setText(Options.getStrings().getScoreLblText()+":"+GameContext.getPlayer().getScore());
	}
	
	
	
}
