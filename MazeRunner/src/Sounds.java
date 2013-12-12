import java.applet.Applet;
import java.applet.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;


public class Sounds {
	
	private AudioClip walkClip, swingClip, dropSwordClip, photonClip;
	protected boolean walking = false;
	
	public Sounds(){
		
		URL walkURL = null;
		URL swingURL = null;
		URL dropURL = null;
		URL photonURL = null;
		try {
			walkURL = new URL("file:sounds/walk3.wav");
			swingURL = new URL("file:sounds/sword_swing.wav");
			dropURL = new URL("file:sounds/drop_sword.wav");
			photonURL = new URL("file:sounds/photon.wav");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		walkClip = Applet.newAudioClip(walkURL);
		swingClip = Applet.newAudioClip(swingURL);
		dropSwordClip = Applet.newAudioClip(dropURL);
		photonClip = Applet.newAudioClip(photonURL);
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
	
	public void photon(){
		photonClip.play();
	}
}
