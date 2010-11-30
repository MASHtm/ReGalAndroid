/**
 *  ReGalAndroid, a gallery client for Android, supporting G2, G3, etc...
 *  URLs: https://github.com/anthonydahanne/ReGalAndroid , http://blog.dahanne.net
 *  Copyright (c) 2010 Anthony Dahanne
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.dahanne.android.regalandroid.remote;

import net.dahanne.android.regalandroid.activity.Settings;
import net.dahanne.gallery.commons.remote.RemoteGallery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class RemoteGalleryConnectionFactory {

	private static final int GALLERY2 = 0;
	private static final int GALLERY3 = 1;
	private static final int PIWIGO = 2;
	private static RemoteGallery remoteGallery;
	private static Context context;
	private final static Logger logger = LoggerFactory.getLogger(RemoteGalleryConnectionFactory.class);
	
	private RemoteGalleryConnectionFactory() {

	}

	public static RemoteGallery getInstance() {
		if (remoteGallery == null) {
			logger.debug("choosing galleryType");
			String connectionType = Settings.getGalleryConnectionType(context);
			//if we have "" then let's assume we can go to defaults
			int galleryConnectionType = Settings
					.getGalleryConnectionType(context) .equals("") ? 0 : Integer.valueOf(connectionType);
			switch (galleryConnectionType) {
			case GALLERY2:
				logger.debug("G2 is choosen");
				remoteGallery = new G2Connection(Settings
						.getGalleryUrl(context),Settings
						.getUsername(context),Settings
						.getPassword(context));
				break;
			case GALLERY3:
				logger.debug("G3 is choosen");
				remoteGallery = new G3Connection(Settings
					.getGalleryUrl(context),Settings
					.getUsername(context),Settings
					.getPassword(context));
				break;
			case PIWIGO:
				logger.debug("Piwigo is choosen");
				remoteGallery = new PiwigoConnection(Settings
						.getGalleryUrl(context),Settings
						.getUsername(context),Settings
						.getPassword(context));
				break;

			}
		}
		return remoteGallery;
	}

	public static void setContext(Context context) {
		RemoteGalleryConnectionFactory.context = context;
	}
	
	/**
	 * iif the user changes the gallery type, we reset it
	 */
	public static void resetInstance(){
		remoteGallery = null;
	}
}
