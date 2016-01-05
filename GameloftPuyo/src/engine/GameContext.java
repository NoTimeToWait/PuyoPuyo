package engine;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import visuals.FieldView;
import visuals.MainMenu;

public class GameContext {
	
	private Timer gameTimer;
	private Timer uiTimer;
	private static MainMenu menu;
	private static GameSession gameSession;
	private static Player player;
	private static boolean paused = false;

	public static void main(String[] args) {
		GameContext context = new GameContext();
		menu = MainMenu.createView(context, Options.WINDOW_WIDTH, Options.WINDOW_HEIGHT, Options.getStrings().getTitle());
		menu.setVisible(true);
		
	}
	
	public void pauseGame() {
		paused = true;
		menu.switchToMenuPane();
	}
	
	public void startGame() {
		// gameSession is created by the host and shared with other players on a local computer or over the LAN or WAN
		// since the game currently does not support multiplayer, we create game session when the player hits "NEW GAME" button
		//here we can also add a Runnable to query gameSession if all players are ready
		//and don't start the game before all players are ready
		paused = false;
		gameSession = new GameSession();
		//TODO: add nickname feature
		player = new Player();
		player.setReady();
		gameSession.addPlayer(player);
		if (gameSession.isHost(player)&&gameTimer==null) {
			gameTimer = new Timer();
			gameTimer.schedule(getTickDispatchTask(), Options.GAME_TICK_TIME, Options.GAME_TICK_TIME);
		}
		if (uiTimer==null) {
			uiTimer = new Timer();
			uiTimer.scheduleAtFixedRate(getUIUpdateTask(), Options.GAME_TICK_TIME/Options.getFramesPerTick(), Options.GAME_TICK_TIME/Options.getFramesPerTick());
		}
	}

	public void continueGame() {
		paused = false;
	}
	
	public static void gameOver(Player p) {
		paused = true;
		gameSession.close(p);
		JOptionPane.showMessageDialog(menu, Options.getStrings().getGameOverMsg()+p.getScore());
		menu.switchToMenuPane();
	}
	
	/**
	 * 
	 * @return
	 */
	public static GameSession getGameSession() {
		//right now just returns a new object, 
		//though this method should implement fetching a GameSession from the host over the network
		//when in multiplayer mode (currently not implemented)
		if (gameSession==null) gameSession = new GameSession();
		return gameSession;
	}
	
	/**
	 * get current player
	 */
	public static Player getPlayer() {
		//TODO: player selection could be implemented here
		if (player==null) player = new Player();
		return player;
	}
	
	private TimerTask getTickDispatchTask() {
		return new TimerTask() {
			@Override
			public void run() {
				if (menu==null) return;
				if (paused) return;
				//host should update all players elapsed game time
				for (NetworkPlayer player:gameSession.getPlayers())
					player.dispatchTick(GameContext.this.player);
				menu.updateObjects();
			}
		};
	}
	
	private TimerTask getUIUpdateTask() {
		return new TimerTask() {
			@Override
				public void run() {
				if (menu==null) return;
				if (paused) return;
				menu.repaintUI(true);
			}
		};
	}
		
	//public static void updateMenu() {
	//	menu.updateObjects();
	//}
	
}
