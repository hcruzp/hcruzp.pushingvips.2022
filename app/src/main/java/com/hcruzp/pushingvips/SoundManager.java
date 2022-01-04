package com.hcruzp.pushingvips;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	/** enumeration of existing sounds **/
	public enum SoundFX {
		POP_CORK, BUBBLE_POP, FONDO
	};

	private int soundsamount = 6;
	/** soundpool for playing sounds **/
	private SoundPool soundPool;
	/** hashmap with all available sounds **/
	private HashMap<SoundFX, Integer> soundMap;

	/** flag if sound is enabled/disabled **/
	private boolean soundOn = true;
	int streamVolume = 0;

	/**
	 * create a new SoundManager
	 * 
	 * @param context
	 */
	public SoundManager(Context context) {
		soundPool = new SoundPool(soundsamount, AudioManager.STREAM_MUSIC, 100);
		initSoundMap(context);
/*		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);*/
		SharedPreferences audiosettings = context
				.getSharedPreferences("UNO", 0);
		soundOn = audiosettings
				.getBoolean("DOS", true);
	}

	/**
	 * init the soundmap with soundeffects
	 * 
	 * @param context
	 */
	private void initSoundMap(Context context) {
		soundMap = new HashMap<SoundFX, Integer>();
		soundMap.put(SoundFX.POP_CORK, soundPool.load(context, R.raw.popcork, 1));
		soundMap.put(SoundFX.BUBBLE_POP, soundPool.load(context, R.raw.bubble_pop, 1));
		soundMap.put(SoundFX.FONDO, soundPool.load(context, R.raw.fish, 1));
	}

	/**
	 * SoundPool.play (int soundID, float leftVolume, float rightVolume, int
	 * priority, int loop, float rate) soundID a soundID returned by the load()
	 * function leftVolume left volume value (range = 0.0 to 1.0) rightVolume
	 * right volume value (range = 0.0 to 1.0) priority stream priority (0 =
	 * lowest priority) loop loop mode (0 = no loop, -1 = loop forever) rate
	 * playback rate (1.0 = normal playback, range 0.5 to 2.0)
	 **/

	/**
	 * soundID a soundID returned by the load() function leftVolume left volume
	 * value (range = 0.0 to 1.0) rightVolume right volume value (range = 0.0 to
	 * 1.0) priority stream priority (0 = lowest priority) loop loop mode (0 =
	 * no loop, -1 = loop forever) rate playback rate (1.0 = normal playback,
	 * range 0.5 to 2.0)
	 */

	/**
	 * play available sounds
	 * 
	 * @param sound
	 *            available sound of type SoundFX
	 * @param leftVolume
	 *            range = 0.0 to 1.0
	 * @param rightVolume
	 *            range = 0.0 to 1.0
	 * @param loop
	 *            0 = no loop, -1 = loop forever
	 */
	public int playSound(SoundFX sound, float leftVolume, float rightVolume, int loop) {
//		if (soundOn) {
		System.out.println("soundMap.get(sound) = " + soundMap.get(sound));
			return soundPool.play(soundMap.get(sound), leftVolume, rightVolume, 1, loop, 1.0f);
/*		}
		return 0;*/
	}

	public int playTilinSound(int resId, float leftVolume, float rightVolume, int loop, Context cntx) {
		int soundID = soundPool.load(cntx, resId, 1);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				soundPool.play(soundID, leftVolume, rightVolume, 1, loop, 1.0f);
			}
		}, 100L);
		return 1;
	}

	public void stopSound(int streamId) {
//		if (!soundOn) {
			soundPool.stop(streamId);
//			soundOn = true;
//		}
	}
	
	public void pauseSound(int streamId) {
//		if (soundOn) {
			soundPool.pause(streamId);
//			soundOn = false;
//		}
	}
	
	public void resumeSound(int streamId) {
//		if (!soundOn) {
			soundPool.resume(streamId);
/*			soundOn = true;
		}*/
	}

	public HashMap<SoundFX, Integer> getSoundMap() {
		return soundMap;
	}

	public void setSoundMap(HashMap<SoundFX, Integer> soundMap) {
		this.soundMap = soundMap;
	}

	public SoundPool getSoundPool() {
		return soundPool;
	}

	public void setSoundPool(SoundPool soundPool) {
		this.soundPool = soundPool;
	}
}