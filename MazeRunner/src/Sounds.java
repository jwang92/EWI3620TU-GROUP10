import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;


public class Sounds {
	
	private AudioClip walkClip;
	protected boolean walking = false;
	
	public Sounds(){
		
		URL walkURL = null;
		try {
			walkURL = new URL("file:sounds/walk3.wav");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		walkClip = Applet.newAudioClip(walkURL);
		
	}
	
	public void walk(){
		
		walkClip.loop();
		walking = true;
				
	}
	
	public void stopWalk(){
		
		walkClip.stop();
		walking = false;
	
	}

}
