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
		int width = this.getWidth();
		this.setLayout(null);
		fieldPane = getFieldPane();
		//fieldPane.setBounds(0, 36, 192, 384);
		int fieldWidth =  Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH;
		fieldPane.setBounds(Options.WINDOW_WIDTH/2-fieldWidth/2, Options.CELL_WIDTH, fieldWidth,	Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT);
		scorePane = new JPanel();
		//scorePane.setBounds(0, 0, 192, 30);
		scorePane.setBounds((Options.WINDOW_WIDTH-fieldWidth)/2, 0, fieldWidth, 30);
		score = new JLabel(Options.getStrings().getScoreLblText()+":");
		scorePane.add(score);
		nextPane = new JPanel(){
			public void paint(Graphics g) {
				super.paint(g);
				int[] puyos = GameContext.getPlayer().getPuyos();
				for (int i=0; i<puyos.length; i++)
					Animation.drawObject(g, fieldPane, puyos[i], 18, 24+i*Options.CELL_WIDTH);
			}
		};
		//nextPane.setBounds(200, 30, 36, 100);
		nextPane.setBounds((Options.WINDOW_WIDTH+fieldWidth)/2, Options.CELL_WIDTH, Options.CELL_WIDTH*2+8, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT);
		this.add(fieldPane);
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
		
		
	public static void chainCombo(int comboCount) {
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
				//if (scorePane==null) {
				//	g.clearRect(0, 0, this.getWidth(), this.getHeight());
				//}
				int marginX = this.getWidth()/2-Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH/2;
				int marginY = Animation.MARGIN;
				g.fillRect(0, 0, Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT);
				for (Animation anim:animations)
					anim.animate(g, this);
				if(chainCombo>1)
					g.drawImage(getChainComboImage(Options.getStrings().getChainComboString()+" x"+chainCombo), 0, 0, this);
				
			}
		};
	}
	
}
