
public class GameStateManager {
	
	private int gamestate = 0;
	
	void GameStateUpdate(GameState m_curSTATE) {
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
	    	  gamestate = 1;
//	    	  System.out.println(gamestate);
	    	  
	          break;
	       case PAUSE_STATE:
	          //
	    	  //MainMenu.setTeller(2);
	          break;
	    }
	}
	
	public int getState(){
		return gamestate;
	}
}

enum GameState {
    TITLE_STATE,
    MAINGAME_STATE,
    PAUSE_STATE
}
