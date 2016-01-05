package visuals;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import engine.GameContext;
import engine.NetworkPlayer;
import engine.Options;
import engine.Options.Languages;
import engine.Player;
import engine.Strings;
import game.GameEvent;

public class MainMenu extends JFrame{
	
	protected static GameContext gameContext;
	private JPanel btnPane;
	private JPanel optionsPane;
	private FieldView gamePane;
	private JPanel currentPane;
	private JButton continueBtn;
	private SortedMap<String, Long> actionPerformedMap;
	public boolean languageSwitched = false;
	
	
	public MainMenu(String title) {
		super(title);
		currentPane = new JPanel();
		actionPerformedMap = new TreeMap<String, Long>();
	}
	
	public static MainMenu createView(GameContext gameContext, int width, int height, String title) {
		MainMenu menu = new MainMenu(title);
		menu.setSize(width, height);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.gameContext = gameContext;
		//menu.getContentPane().add(new JPanel(), BorderLayout.WEST);
		//menu.getContentPane().add(new JPanel(), BorderLayout.EAST);
		
		
		mapKeybindings(((JPanel)menu.getContentPane()).getInputMap());
		
		ActionMap actionMap = ((JPanel)menu.getContentPane()).getActionMap();
		actionMap.put("p1_left", getButtonAction(menu,  GameEvent.USERINPUT_LEFT, 0, false));
		actionMap.put("p1_right", getButtonAction(menu,  GameEvent.USERINPUT_RIGHT, 0, false));
		actionMap.put("p1_up", getButtonAction(menu,  GameEvent.USERINPUT_UP, Options.GAME_TICK_TIME/6+30, false));
		actionMap.put("p1_down", getButtonAction(menu,  GameEvent.USERINPUT_DOWN, 30, true));
		actionMap.put("p1_down_released", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menu.actionPerformedMap.put(GameEvent.USERINPUT_DOWN.toString(), -System.currentTimeMillis());
			}	
		});
		actionMap.put("p1_escape", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (menu.currentPane!=menu.btnPane) gameContext.pauseGame();
			}	
		});
		menu.switchToMenuPane();
		return menu;
	}
	
	private static AbstractAction getButtonAction(MainMenu menu, GameEvent event, int timeOffset, boolean releaseRequired) {
		return new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Player player = GameContext.getPlayer();
				long keyPressedTime=0;
				if (menu.actionPerformedMap.containsKey(event.toString()))
					keyPressedTime = menu.actionPerformedMap.get(event.toString());
				boolean keyReleasedOrReady = releaseRequired? keyPressedTime<=0 : System.currentTimeMillis()-keyPressedTime>=Options.GAME_TICK_TIME+timeOffset;
				if (menu.currentPane==menu.gamePane	&& keyReleasedOrReady) { 
					player.dispatchEvent(player, event);
					menu.actionPerformedMap.put(event.toString(), System.currentTimeMillis());
				}
			}
		};
	}
	
	private static void mapKeybindings(InputMap inputMap) {
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER1_ESC_KEY, 0), "p1_escape");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER1_UP_KEY, 0), "p1_up");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER1_DOWN_KEY, 0), "p1_down");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER1_DOWN_KEY, 0, true), "p1_down_released");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER1_LEFT_KEY, 0), "p1_left");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER1_RIGHT_KEY, 0), "p1_right");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER2_UP_KEY, 0), "p2_up");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER2_DOWN_KEY, 0), "p2_down");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER2_DOWN_KEY, 0, true), "p2_down_released");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER2_LEFT_KEY, 0), "p2_left");
		inputMap.put(KeyStroke.getKeyStroke(Options.PLAYER2_RIGHT_KEY, 0), "p2_right");
	}
	
	public void switchToMenuPane() {
		
		
		if (btnPane==null || languageSwitched) {
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
					switchToGamePane();
					gameContext.continueGame();
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
		if (gameContext.getGameSession().isClosed())  
			gamePane = null;
		continueBtn.setVisible(gamePane==null? false : true);
		currentPane = btnPane;
		currentPane.setVisible(true);
		this.getContentPane().add(btnPane, BorderLayout.CENTER);
		btnPane.repaint();
	}
	
	public void switchToGamePane() {
		if (gamePane==null) {
			gamePane = new FieldView();
			gamePane.setPreferredSize(new Dimension(Options.CELL_WIDTH*Options.DEFAULT_FIELD_WIDTH+100, Options.CELL_WIDTH*Options.DEFAULT_FIELD_HEIGHT));
		}
		currentPane.setVisible(false);
		this.getContentPane().remove(currentPane);
		currentPane = gamePane;
		gamePane.switched();
		currentPane.setVisible(true);
		this.getContentPane().add(gamePane);
	}
	
	public void switchToOptionsPane() {
		if (optionsPane==null || languageSwitched) {
			optionsPane = new JPanel();
			optionsPane.setLayout(new BoxLayout(optionsPane, BoxLayout.Y_AXIS));
			JComboBox<String> gameSpeedSelector = new JComboBox<String>(Options.getStrings().getGameSpeedStrings());
			gameSpeedSelector.setSelectedIndex(Options.GAME_TICK_TIME==250? 0:1);
			gameSpeedSelector.setMaximumSize(new Dimension(80, 30));
			gameSpeedSelector.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<String> cb = (JComboBox<String>)e.getSource();
					if (cb.getSelectedItem().equals(Options.getStrings().getGameSpeedStrings()[1])) 
						Options.GAME_TICK_TIME = 450;
					else Options.GAME_TICK_TIME = 250;
				}
			});
			JComboBox<Languages> languages = new JComboBox<Languages>((Languages[])Options.Languages.values());
			languages.setMaximumSize(new Dimension(80, 30));
			for (int i=0; i<Options.Languages.values().length; i++)
				if (Options.LANGUAGE.equals(Options.Languages.values()[i]))
						languages.setSelectedIndex(i);
			languages.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JComboBox<Languages> cb = (JComboBox<Languages>)e.getSource();
			        Options.LANGUAGE = (Languages)cb.getSelectedItem();
			        optionsPane.repaint();
			        languageSwitched = true;
				}
			});
			JButton backBtn = new JButton(Options.getStrings().getBackBtnText());
			backBtn.addActionListener( new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MainMenu.this.switchToMenuPane();
				}
			});
			optionsPane.add(Box.createVerticalGlue());
			optionsPane.add(new JLabel(Options.getStrings().getGameSpeedLblText()));
			optionsPane.add(gameSpeedSelector);
			optionsPane.add(Box.createVerticalGlue());
			optionsPane.add(new JLabel(Options.getStrings().getLanguagesLblText()));
			optionsPane.add(languages);
			optionsPane.add(Box.createVerticalGlue());
			optionsPane.add(backBtn);
			optionsPane.add(Box.createVerticalGlue());
		}
		currentPane.setVisible(false);
		this.getContentPane().remove(currentPane);
		currentPane = optionsPane;
		currentPane.setVisible(true);
		this.getContentPane().add(optionsPane, BorderLayout.CENTER);
	}
	
	/**
	 * update game pane UI
	 * 
	 * @param drawAll
	 */
	public void updateObjects() {
		if (gamePane==null) return;
		for (NetworkPlayer player:GameContext.getGameSession().getPlayers()) 
			gamePane.updateObjects(player);
		gamePane.updateScore();
	}
	
	public void repaintUI(boolean drawAll) {
		if (currentPane!=null && currentPane==gamePane) 
				gamePane.repaint(); //.drawObjects(player);
		
			
	}
}
