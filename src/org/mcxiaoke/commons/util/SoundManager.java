package org.mcxiaoke.commons.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseIntArray;

/**
 * @author mcxiaoke
 * @version 1.0 2011.11.16
 * @version 1.1 2011.12.09
 * @version 1.2 2011.12.26
 * @see http
 *      ://www.droidnova.com/creating-sound-effects-in-android-part-2,695.html
 * 
 */
public final class SoundManager {
	private static SoundManager _instance;
	private static SoundPool mSoundPool;
	private static SparseIntArray mSoundPoolMap;
	private static AudioManager mAudioManager;
	private static Activity mContext;

	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param Index
	 *            - The Sound Index for Retrieval
	 * @param SoundID
	 *            - The Android ID for the Sound asset.
	 */
	public static void addSound(int Index, int SoundID) {
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}

	/**
	 * Deallocates the resources and Instance of SoundManager
	 */
	public static void cleanup() {
		mSoundPool.release();
		mSoundPool = null;
		mSoundPoolMap.clear();
		_instance = null;
	}

	/**
	 * Requests the instance of the Sound Manager and creates it if it does not
	 * exist.
	 * 
	 * @return Returns the single instance of the SoundManager
	 */
	public static synchronized SoundManager getInstance() {
		if (_instance == null)
			_instance = new SoundManager();
		return _instance;
	}

	/**
	 * Initialises the storage for the sounds
	 * 
	 * @param theContext
	 *            The Application context
	 */
	public static void initSounds(Activity context) {
		mContext = context;
		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		mSoundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
	}

	/**
	 * Loads the various sound assets Currently hardcoded but could easily be
	 * changed to be flexible.
	 */
	public static void loadSounds() {
		// mSoundPoolMap.put(1, mSoundPool.load(mContext, R.raw.pop, 1));
	}

	/**
	 * Plays a Sound
	 * 
	 * @param index
	 *            - The Index of the Sound to be played
	 * @param speed
	 *            - The Speed to play not, not currently used but included for
	 *            compatibility
	 */
	public static void playSound(int index, float speed) {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		try {
			mSoundPool.play(mSoundPoolMap.get(index), streamVolume,
					streamVolume, 1, 0, speed);
		} catch (RuntimeException e) {
			Log.e("SoundManager",
					"playSound: index " + index + " error:" + e.getMessage());
		}
	}

	/**
	 * Stop a Sound
	 * 
	 * @param index
	 *            - index of the sound to be stopped
	 */
	public static void stopSound(int index) {
		mSoundPool.stop(mSoundPoolMap.get(index));
	}

	private SoundManager() {
		mSoundPoolMap = new SparseIntArray();
	}
}
