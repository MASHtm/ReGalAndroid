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

package net.dahanne.android.regalandroid.tasks;

import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.activity.ShowAlbums;
import net.dahanne.android.regalandroid.activity.ShowGallery;
import net.dahanne.android.regalandroid.remote.RemoteGalleryConnectionFactory;
import net.dahanne.android.regalandroid.utils.ShowUtils;
import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * @author Anthony Dahanne
 * 
 */
public class FetchAlbumAndSubAlbumsAndPicturesTask extends
		AsyncTask<Object, Void, Album> {
	String exceptionMessage = null;
	ShowAlbums activity;
	private String galleryUrl;
	private final ProgressDialog progressDialog;
	private final boolean refreshView;

	public FetchAlbumAndSubAlbumsAndPicturesTask(ShowAlbums context,
			ProgressDialog progressDialog, boolean refreshView) {
		super();
		this.refreshView=refreshView;
		activity = context;
		this.progressDialog = progressDialog;
	}

	@Override
	protected Album doInBackground(Object... parameters) {
		galleryUrl = (String) parameters[0];
		int albumId = (Integer) parameters[1];

		Album albumAndSubAlbums;
		try {
			albumAndSubAlbums = RemoteGalleryConnectionFactory.getInstance().getAlbumAndSubAlbumsAndPictures(albumId);
		} catch (GalleryConnectionException e) {
			albumAndSubAlbums = null;
			exceptionMessage = e.getMessage();
		}
		return albumAndSubAlbums;
	}

	@Override
	protected void onPostExecute(Album albumAndSubAlbums) {

		if (albumAndSubAlbums != null) {
			if(refreshView==true){
				((RegalAndroidApplication) activity.getApplication()).setCurrentAlbum(albumAndSubAlbums);
				activity.setUpView();
			}else{
				
				RegalAndroidApplication application = (RegalAndroidApplication) activity
				.getApplication();
				Intent intent;
				if (albumAndSubAlbums != null
						&& albumAndSubAlbums.getSubAlbums().size() != 0
						&& albumAndSubAlbums.getName() != application
						.getCurrentAlbum().getName()) {
					intent = new Intent(activity, ShowAlbums.class);
				} else {
					// the user wants to see the pictures
					intent = new Intent(activity, ShowGallery.class);
				}
				// set the parent
				albumAndSubAlbums.setParent(application.getCurrentAlbum());
				
				application.setCurrentAlbum(albumAndSubAlbums);
				activity.startActivity(intent);
			}
			
			
		} else {
			// something went wrong
			ShowUtils.getInstance().alertConnectionProblem(exceptionMessage,
					galleryUrl, activity);
		}
		progressDialog.dismiss();
	}

}
