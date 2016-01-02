package visuals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.GameContext;
import engine.Options;
import engine.Player;
import engine.Strings;

public class MainMenu extends JFrame{
	
	protected static GameContext gameContext;
	private JPanel btnPane;
	private JPanel optionsPane;
	private FieldView gamePane;
	private JPanel currentPane;
	private JButton continueBtn;
	
	
	public MainMenu(String title) {
		super(title);
		currentPane = new JPanel();
	}
	
	public static MainMenu createView(GameContext gameContext, int width, int height, String title) {
		MainMenu menu = new MainMenu(title);
		menu.setSize(width, height);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//menu.setLayout(new BorderLayout());
		menu.gameContext = gameContext;
		menu.setBackground(Color.GRAY);
		menu.getContentPane().add(new JPanel(), BorderLayout.WEST);
		menu.getContentPane().add(new JPanel(), BorderLayout.EAST);
		menu.switchToMenuPane();
		return menu;
	}
	
	private void switchToMenuPane() {
		if (btnPane==null) {
			btnPane = new JPanel();
			btnPane.setLayout(new BoxLayout(btnPane, BoxLayout.Y_AXIS));
			Strings strings = Options.getStrings();
			continueBtn = new JButton(strings.getContinueBtnText());
			JButton startBtn = new JButton(strings.getStartBtnText());
			JButton optionsBtn = new JButton(strings.getOptionsBtnText());
			JButton exitBtn = new JButton(strings.getExitBtnText());
			continueBtn.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					gameContext.continueGame();
					switchToGamePane();
				}
			});
			startBtn.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					gameContext.startGame();
					switchToGamePane();
					gamePane.repaint();
				}
			});
			optionsBtn.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					MainMenu.this.switchToOptionsPane();
				}
			});
			exitBtn.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			
			continueBtn.setAlignmentX(CENTER_ALIGNMENT);
			startBtn.setAlignmentX(CENTER_ALIGNMENT);
			optionsBtn.setAlignmentX(CENTER_ALIGNMENT);
			exitBtn.setAlignmentX(CENTER_ALIGNMENT);
						
			btnPane.add(Box.createVerticalGlue());
			btnPane.add(continueBtn);
			continueBtn.setVisible(false);
			btnPane.add(Box.createVerticalStrut(30));
			btnPane.add(startBtn);
			btnPane.add(Box.createVerticalStrut(30));
			btnPane.add(optionsBtn);
			btnPane.add(Box.createVerticalStrut(30));
			btnPane.add(exitBtn);
			btnPane.add(Box.createVerticalGlue());
		}
		
		currentPane.setVisible(false);
		this.getContentPane().remove(currentPane);
		currentPane = btnPane;
		currentPane.setVisible(true);
		this.getContentPane().add(btnPane, BorderLayout.CENTER);
	}
	
	private void switchToGamePane() {
		if (gamePane==null) {
			gamePane = new FieldView();
			//gamePane.setBounds(30, 30, Options.DEFAULT_FIELD_WIDTH*Options.CELL_WIDTH, Options.DEFAULT_FIELD_HEIGHT*Options.CELL_WIDTH);
			//gamePane.setSize(Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH);
			gamePane.setPreferredSize(new Dimension(Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH, Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH));
			//this.setBackground(Color.BLUE);
			gamePane.setBackground(Color.WHITE);
		}
		currentPane.setVisible(false);
		this.getContentPane().remove(currentPane);
		currentPane = gamePane;
		currentPane.setVisible(true);
		this.getContentPane().add(gamePane, BorderLayout.CENTER);
	}
	
	private void switchToOptionsPane() {
		if (optionsPane==null) { 
			optionsPane = new JPanel();
			JButton backBtn = new JButton(Options.getStrings().getBackBtnText());
			backBtn.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					MainMenu.this.switchToMenuPane();
				}
			});
			optionsPane.add(backBtn);
		}
		currentPane.setVisible(false);
		this.getContentPane().remove(currentPane);
		currentPane = optionsPane;
		currentPane.setVisible(true);
		this.getContentPane().add(optionsPane, BorderLayout.CENTER);
	}
	
	/**
	 * update game pane UI
	 * @param player the curr
	 * @param drawAll
	 */
	public void updateUI(boolean drawAll) {
		if (currentPane!=null && currentPane==gamePane) {
			gamePane.drawObjects(GameContext.getPlayer().getGameObjects(drawAll));
		}
			
	}
}
