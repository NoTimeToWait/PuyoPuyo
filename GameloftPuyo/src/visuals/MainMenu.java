package visuals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import engine.GameContext;
import engine.Options;
import engine.Strings;

public class MainMenu extends JFrame{
	
	protected static GameContext gameContext;
	private JPanel btnPane;
	private JPanel optionsPane;
	private JPanel gamePane;
	private JButton continueBtn;
	
	public MainMenu(String title) {
		super(title);
	}
	
	public static JFrame createView(GameContext gameContext, int width, int height, String title) {
		MainMenu menu = new MainMenu(title);
		menu.setSize(width, height);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//menu.setLayout(new BorderLayout());
		menu.gameContext = gameContext;
		menu.setBackground(Color.GRAY);
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
			btnPane.add(startBtn);
			btnPane.add(optionsBtn);
			btnPane.add(exitBtn);
		}
		
		this.getContentPane().add(btnPane, BorderLayout.CENTER);
	}
	
	private void switchToGamePane() {
		if (gamePane==null) {
			gamePane = new FieldView();
			gamePane.setBounds(0, 0, Options.DEFAULT_FIELD_WIDTH*Options.CELL_WIDTH, Options.DEFAULT_FIELD_HEIGHT*Options.CELL_WIDTH);
			gamePane.setBackground(Color.WHITE);
		}
		this.getContentPane().add(gamePane, BorderLayout.CENTER);
	}
	
	private void switchToOptionsPane() {
		if (optionsPane==null) { 
			
		}
		this.getContentPane().add(optionsPane, BorderLayout.CENTER);
	}
}
