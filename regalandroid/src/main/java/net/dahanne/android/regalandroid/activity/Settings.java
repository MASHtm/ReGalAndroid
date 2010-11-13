/*
 * G2Android
 * Copyright (c) 2009 Anthony Dahanne
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.dahanne.android.regalandroid.activity;

import java.io.File;

import net.dahanne.android.regalandroid.R;
import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class Settings extends PreferenceActivity {

	// Option keys and default values
	// TODO : it is too bad I can't load getString from the context : there must
	// be a way not to repeat the key values already in strings.xml
	private static String OPT_GALLERY_URL_KEY = "galleryUrl";
	private static String OPT_USERNAME_KEY = "username";
	private static String OPT_PASSWORD_KEY = "password";
	private static String OPT_G2ANDROID_PATH = "g2android_path";
	private static String OPT_G2ANDROID_CACHE_PATH = "g2android_cache_path";
	private static String OPT_CLEAR_CACHE_EVERY_SESSION = "clear_cache";
	private static String OPT_GALLERY_URL_DEF = "http://g2.dahanne.net";
	private static String OPT_G2ANDROID_PATH_DEF = "g2android";
	private static String OPT_G2ANDROID_CACHE_PATH_DEF = "tmp";
	private static String OPT_DEFAULT_SUMMARY_KEY = "DefaultSummary";
	private static String OPT_DEFAULT_DESCRIPTION_KEY = "DefaultDescription";
	private static String OPT_GALLERY_CONNECTION_TYPE = "GalleryType";
	private static String OPT_GALLERY_CONNECTION_TYPE_DEF = "0";

	
	private static final String TAG = "Settings";
	private static String galleryUrl;
	private static RemoteGallery remoteGallery = RemoteGalleryConnectionFactory
			.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
	}

	/** Get the current value of the galleryUrl option */
	public static String getGalleryUrl(Context context) {
		galleryUrl = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_GALLERY_URL_KEY, OPT_GALLERY_URL_DEF);
		return galleryUrl;
	}

	

	/** Get the current value of the username option */
	public static String getUsername(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_USERNAME_KEY, "");
	}

	/** Get the current value of the password option */
	public static String getPassword(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_PASSWORD_KEY, "");
	}

	/** Get the current value of the g2Android path option */
	public static String getG2AndroidPath(Context context) {
		File externalStorageDirectory = Environment
				.getExternalStorageDirectory();
		return new StringBuilder()
				.append(externalStorageDirectory.getAbsolutePath())
				.append("/")
				.append(PreferenceManager.getDefaultSharedPreferences(context)
						.getString(OPT_G2ANDROID_PATH, OPT_G2ANDROID_PATH_DEF))
				.toString();
	}

	/** Get the current value of the g2android cache option */
	public static String getG2AndroidCachePath(Context context) {
		String cachePath = PreferenceManager.getDefaultSharedPreferences(
				context).getString(OPT_G2ANDROID_CACHE_PATH,
				OPT_G2ANDROID_CACHE_PATH_DEF);
		return new StringBuilder().append(getG2AndroidPath(context))
				.append("/").append(cachePath).append("/").toString();
	}

	/** Get the current value of the clear session option */
	public static boolean isCacheClearedEverySession(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_CLEAR_CACHE_EVERY_SESSION, false);
	}

	/** Get the current value of the username option */
	public static String getDefaultSummary(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_DEFAULT_SUMMARY_KEY, "");
	}

	/** Get the current value of the username option */
	public static String getDefaultDescription(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_DEFAULT_DESCRIPTION_KEY, "");
	}

	/**
	 * Get the gallery connection type, it is used by the
	 * RemoteGalleryConnectionFactory class to know what type of gallery the
	 * user is trying to connect to
	 */
	public static String getGalleryConnectionType(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context).getString(
				OPT_GALLERY_CONNECTION_TYPE, OPT_GALLERY_CONNECTION_TYPE_DEF);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//we are about to leave the settings activity; let's clean things up
		RemoteGalleryConnectionFactory.resetInstance();
		((RegalAndroidApplication) this.getApplication()).setCurrentAlbum(null);
		((RegalAndroidApplication) this.getApplication()).setCurrentPosition(0);
		
	}

}
