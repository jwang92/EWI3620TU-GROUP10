package Main;
import UserInput.CursorHandler;

public class GameStateManager {
	
	public int gamestate = 5;
	public boolean sTitle=false,
					sMainGame=false,
					sPause=false,
					sGameOver=false,
					sLogin=false,
					sHighscores=false;
	
	public CursorHandler c = new CursorHandler(MainClass.canvas);
	
	public void GameStateUpdate(GameState m_curSTATE) {
	     // handle update
		
	    switch(m_curSTATE) {
	       case TITLE_STATE:
	          //
	    	   //MainMenu.setTeller(0);
	    	   gamestate = 0;
	          break;
	       case MAINGAME_STATE:
	          //
	    	   //MainMenu.setTeller(1);
	    	   c.setCursor(2);
	    	   gamestate = 1;
	          break;
	       case PAUSE_STATE:
	           //
	    	   gamestate = 2;
	    	   //MainMenu.setTeller(2);
	           break;
	       case STOP_STATE:
	           //
	    	   gamestate = 3;
	    	   //MainMenu.setTeller(3);
	           break;
	       case GAMEOVER_STATE:
	    	   //
	    	   gamestate = 4;
	    	   //MainMenu.setTeller(4);
	    	   break;
	       case LOGIN_STATE:
	    	   //
	    	   gamestate = 5;
	    	   //MainMenu.setTeller(5);
	    	   break;
	       case HIGHSCORES_STATE:
	    	   //
	    	   gamestate = 6;
	    	   //MainMenu.setTeller(6);
	    	   break;
	    }
	}
	
	public int getState(){
		return gamestate;
	}
	
	
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
		return sGameOver;
	}
	
	public boolean getStopHighscores(){
		return sHighscores;
	}
	
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
}




