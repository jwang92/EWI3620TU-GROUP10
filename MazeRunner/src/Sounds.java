import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;


public class Sounds {
	
	private AudioClip walkClip, swingClip, dropSwordClip;
	protected boolean walking = false;
	
	public Sounds(){
		
		URL walkURL = null;
		URL swingURL = null;
		URL dropURL = null;
		try {
			walkURL = new URL("file:sounds/walk3.wav");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			swingURL = new URL("file:sounds/sword_swing.wav");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			dropURL = new URL("file:sounds/drop_sword.wav");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		walkClip = Applet.newAudioClip(walkURL);
		swingClip = Applet.newAudioClip(swingURL);
		dropSwordClip = Applet.newAudioClip(dropURL);
	}
	
	public void walk(){
		
		walkClip.loop();
		walking = true;
				
	}
	
	public void stopWalk(){
		
		walkClip.stop();
		walking = false;
	
	}
	
	public void swing(){
		swingClip.play();
	}

	public void dropSword(){
		dropSwordClip.play();
	}
}
