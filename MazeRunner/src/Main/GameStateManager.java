package Main;
import UserInput.CursorHandler;

public class GameStateManager {
	
	//start in login state
	public int gamestate = 5;
	
	//booleans: takes care that init-methode is used only once when entering a new gamestate
	public boolean sTitle=false,
					sMainGame=false,
					sPause=false,
					sGameOver=false,
					sLogin=false,
					sHighscores=false,
					sFinish=false;
	
	public CursorHandler c;
	
	public GameStateManager(MainClass mClass) {
		c = new CursorHandler(mClass.canvas);
	}

	public void GameStateUpdate(GameState m_curSTATE) {
	     // handle update
		
	    switch(m_curSTATE) {
	       case TITLE_STATE:
	    	   gamestate = 0;
	          break;
	       case MAINGAME_STATE:
	    	   gamestate = 1;
	          break;
	       case PAUSE_STATE:
	    	   gamestate = 2;
	           break;
	       case STOP_STATE:
	    	   gamestate = 3;
	           break;
	       case GAMEOVER_STATE:
	    	   gamestate = 4;
	    	   break;
	       case LOGIN_STATE:
	    	   gamestate = 5;
	    	   break;
	       case HIGHSCORES_STATE:
	    	   gamestate = 6;
	    	   break;
	       case FINISH_STATE:
	    	   gamestate = 7;
	    	   break;
	    }
	}
	
	public int getState(){
		return gamestate;
	}
	
	/*
	 * Getters
	 */
	
	public boolean getStopTitle(){
		return sTitle;
	}
	
	public boolean getStopMainGame(){
		return sMainGame;
	}
	
	public boolean getStopPause(){
		return sPause;
	}
	
	public boolean getStopGameOver(){
		return sGameOver;
	}
	
	public boolean getStopLogin(){
		return sLogin;
	}
	
	public boolean getStopHighscores(){
		return sHighscores;
	}
	
	public boolean getStopFinish(){
		return sFinish;
	}
	
	/*
	 * Setters
	 */
	
	public void setStopTitle(boolean s){
		sTitle=s;
	}
	
	public void setStopMainGame(boolean s){
		sMainGame=s;
	}
	
	public void setStopPause(boolean s){
		sPause=s;
	}
	
	public void setStopGameOver(boolean s){
		sGameOver=s;
	}
	
	public void setStopLogin(boolean s){
		sLogin=s;
	}
	
	public void setStopHighscores(boolean s){
		sHighscores=s;
	}
	
	public void setStopFinish(boolean s){
		sFinish=s;
	}
}




