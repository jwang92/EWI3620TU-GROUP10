
public class GameStateManager {
	
	public enum GameState {
	    TITLE_STATE,
	    MAINGAME_STATE,
	    PAUSE_STATE
	}
	
	//public GameState m_curState = GameState.TITLE_STATE;
	
	void GameStateUpdate(GameState m_curState) {
	     // handle update
	    switch(m_curState) {
	       case TITLE_STATE:
	          //
	          if() {
	              m_curState = GameState.MAINGAME_STATE;
	          }
	          break;
	       case MAINGAME_STATE:
	          //
	          if() {
	              m_curState = GameState.PAUSE_STATE;
	          }
	          break;
	       case PAUSE_STATE:
	           //
	           if() {
	               m_curState = GameState.MAINGAME_STATE;
	           }
	           break;
	    }
	}
	
	
}
