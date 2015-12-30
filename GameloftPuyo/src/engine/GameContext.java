package engine;

import java.util.Timer;
import java.util.TimerTask;

import visuals.MainMenu;

public class GameContext {
	
	private Timer timer;
	private static MainMenu menu;
	private static GameSession gameSession;
	private static Player player;

	public static void main(String[] args) {
		GameContext context = new GameContext();
		menu = MainMenu.createView(context, Options.WINDOW_WIDTH, Options.WINDOW_HEIGHT, Options.getStrings().getTitle());
		menu.setVisible(true);
		//TODO: add nickname feature
		player = new Player();
	}
	
	public void startGame() {
		// gameSession is created by the host and shared with other players on a local computer or over the LAN or WAN
		// since the game currently does not support multiplayer, we create game session when the player hits "NEW GAME" button
		gameSession = new GameSession();
		player.setReady();
		gameSession.addPlayer(player);
		//here we can also add a Runnable to query gameSession if all players are ready
		//and don't start the game before all players are ready
		TimerTask tickDispatchTask = new TimerTask() {
			@Override
			public void run() {
				for (Player player:gameSession.getPlayers())
					player.dispatchTick();
			}
		};
		timer.schedule(tickDispatchTask, Options.GAME_TICK_TIME, Options.GAME_TICK_TIME);
	}

	public void continueGame() {
		
	}
}
