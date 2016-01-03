package engine;

import java.util.Timer;
import java.util.TimerTask;

import visuals.MainMenu;

public class GameContext {
	
	private Timer gameTimer = new Timer();
	private Timer uiTimer = new Timer();
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
		//here we can also add a Runnable to query gameSession if all players are ready
		//and don't start the game before all players are ready
		gameSession = getGameSession();
		player.setReady();
		gameSession.addPlayer(player);
		if (gameSession.isHost(player)) {
			TimerTask tickDispatchTask = new TimerTask() {
				@Override
				public void run() {
					//host should update all players elapsed game time
					for (NetworkPlayer player:gameSession.getPlayers())
						player.dispatchTick(GameContext.this.player);
				}
			};
			gameTimer.schedule(tickDispatchTask, Options.GAME_TICK_TIME, Options.GAME_TICK_TIME);
		}
		TimerTask uiUdateTask = new TimerTask() {
			@Override
				public void run() {
				menu.updateUI(true);
			}
		};
		uiTimer.scheduleAtFixedRate(uiUdateTask, 1000/Options.FRAMERATE, 1000/Options.FRAMERATE);
	}

	public void continueGame() {
		
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
}
