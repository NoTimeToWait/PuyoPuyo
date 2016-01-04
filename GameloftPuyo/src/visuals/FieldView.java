package visuals;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

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
	private static JLabel score;
	private static JPanel scorePane;
	private static JPanel fieldPane;
	private static JPanel nextPane;
	private static int chainCombo = 0;
	private BufferedImage comboImage;
	
	public FieldView() {
		super();
		animations = new ArrayList<Animation>(); 
		this.setLayout(new BorderLayout());
		this.setAlignmentY(TOP_ALIGNMENT);
		
		fieldPane = getFieldPane();
		fieldPane.setPreferredSize(new Dimension(Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, 
														Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT));
		
		scorePane = new JPanel();
		//scorePane.setPreferredSize(new Dimension(Options.CELL_WIDTH*(Options.DEFAULT_FIELD_WIDTH+1), 32));
		score = new JLabel(Options.getStrings().getScoreLblText()+":");
		score.setPreferredSize(new Dimension(Options.CELL_WIDTH*(Options.DEFAULT_FIELD_WIDTH+2), 18));
		//score.setPreferredSize(new Dimension(174, 18));
		score.setHorizontalAlignment(JLabel.LEFT);
		score.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		scorePane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//scorePane.setBounds(0, 4, 192, 32);
		scorePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		scorePane.add(score);
		
		nextPane = new JPanel(){
			public void paint(Graphics g) {
				super.paint(g);
				int[] puyos = GameContext.getPlayer().getPuyos();
				//g.clearRect(0, 0, nextPane.getWidth(), nextPane.getHeight());
				for (int i=0; i<puyos.length; i++)
					Animation.drawObject(g, fieldPane, puyos[i], 18, 24+i*Options.CELL_WIDTH);
			}
		};
		nextPane.setPreferredSize(new Dimension(Options.CELL_WIDTH*2+8, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT));
		//nextPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
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
		this.add(centerPane, BorderLayout.CENTER);
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
		
	
	/*
	public void drawObjects(NetworkPlayer player) {
					
		drawObjects = player.getGameObjects(true);
		repaint();
	}*/
	
	public static void chainCombo(int comboCount) {
		//score.setText(Options.getStrings().getChainComboString()+" x"+comboCount);
		//scorePane.repaint();
		chainCombo = comboCount;
		
	}
	
	public void paint(Graphics g) {
		fieldPane.repaint();
		
	}
	
	public static void repaintAdditionalInfo() {
		scorePane.repaint();
		nextPane.repaint();
		score.setText(Options.getStrings().getScoreLblText()+":"+GameContext.getPlayer().getScore());
	}
	
	private BufferedImage getChainComboImage(String str) {
		if (comboImage==null) {
			comboImage = getGraphicsConfiguration().createCompatibleImage(300, 100);
			Graphics g2 = comboImage.createGraphics();
			Font font = getFont();
            setFont(font.deriveFont(Font.PLAIN, 20));
			Graphics2D g2d = (Graphics2D)g2.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        FontRenderContext frc = g2d.getFontRenderContext();
	        String s = "CHAIN COMBO";
	        TextLayout textTl = new TextLayout(s, getFont(), frc);
	        Shape outline = textTl.getOutline(null);

	        FontMetrics fm = g2d.getFontMetrics(getFont());
	        int x = 30;//(getWidth() - outline.getBounds().width) / 2;
	        int y = 30;//((getHeight() - outline.getBounds().height) / 2) + fm.getAscent();
	        g2d.translate(x, y);

	        Stroke stroke = g2d.getStroke();
	        g2d.setColor(Color.BLACK);
	        g2d.fill(outline);
	        g2d.setStroke(new BasicStroke(3));
	        g2d.setColor(Color.RED);
	        g2d.draw(outline);
	        g2d.dispose();
		}
		return comboImage;
	}
	
	private JPanel getFieldPane() {
		return new JPanel() {
			public void paint(Graphics g)
			{
				g.setColor(Color.GRAY);
				if (scorePane==null) {
					g.clearRect(0, 0, this.getWidth(), this.getHeight());
				}
				int marginX = this.getWidth()/2-Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH/2;
				int marginY = Animation.MARGIN;
				g.fillRect(0, 0, Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT);
				for (Animation anim:animations)
					anim.animate(g, this);
				
				/*BufferedImage message = getGraphicsConfiguration().createCompatibleImage(300, 100);
				Graphics g2 = message.createGraphics();
				g2.setColor(Color.GRAY);
				g2.fillRect(0, 0, 300, 100);
				g2.setColor(Color.RED);
				g2.drawImage(Animation.images[0], 0, 0, this);
				//g2.drawChars("111".toCharArray(), 0, 3, 0, 0);
				//g2.drawRect(0, 0, 100, 100);
				 Graphics2D g2d = (Graphics2D)g;
			     g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				   
				g2d.drawString(Options.getStrings().getChainComboString()+" x"+chainCombo, 0, 0);
				g.drawImage(message, 0, 0, this);*/
				
				
				/*Image[] images = Animation.images;
				drawObjects = GameContext.getPlayer().getGameObjects(true);
				if (drawObjects==null) return;
				if ( GameField.cells!=null)
				for (int i=0; i<6; i++)
					for (int j=0; j<12; j++) {
						GameObject obj = GameField.cells[i][j].gameObject;
						if (obj!=null)
						switch (obj.getType()) {
						case Puyo.RED|GameObject.PUYO_TYPE_MASK: 
							g.drawImage(images[0],400+i*32, j*32, this); break;
						case Puyo.GREEN|GameObject.PUYO_TYPE_MASK: 
							g.drawImage(images[1],400+i*32, j*32, this); break;
						case Puyo.BLUE|GameObject.PUYO_TYPE_MASK: 
							g.drawImage(images[2],400+i*32, j*32, this); break;
						case Puyo.YELLOW|GameObject.PUYO_TYPE_MASK: 
							g.drawImage(images[3],400+i*32, j*32, this); break;
						default: g.drawImage(images[4],400+i*32, j*32, this);
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
		};
	}
	
}
