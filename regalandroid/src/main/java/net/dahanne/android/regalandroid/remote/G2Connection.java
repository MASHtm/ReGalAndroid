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
package net.dahanne.android.regalandroid.remote;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import net.dahanne.gallery.commons.utils.AlbumUtils;
import net.dahanne.gallery.g2.java.client.business.G2Client;
import net.dahanne.gallery.g2.java.client.model.G2Album;
import net.dahanne.gallery.g2.java.client.model.G2Picture;
import net.dahanne.gallery.g2.java.client.utils.G2ConvertUtils;

public class G2Connection implements RemoteGallery {
	
	private final G2Client client;
	private Album rootAlbum;
	private final String galleryUrl;

	public G2Connection(String galleryUrl, String username, String password) {
		client = new G2Client(galleryUrl,username,password);
		this.galleryUrl = galleryUrl;
	}
	
	
	@Override
	public Collection<Picture> getPicturesFromAlbum(String galleryUrl,
			int albumName) throws GalleryConnectionException {
		
		List<Picture> pictures = new ArrayList<Picture>();
		
		Collection<G2Picture> extractG2PicturesFromProperties = client.extractG2PicturesFromProperties(client.fetchImages(galleryUrl,
				albumName));
		
		for (G2Picture g2Picture : extractG2PicturesFromProperties) {
			pictures.add(G2ConvertUtils.g2PictureToPicture(g2Picture, galleryUrl));
		}
		
		
		return pictures;
	}


	@Override
	public Album getAlbumAndSubAlbumsAndPictures(String galleryUrl, int parentAlbumId)
			throws GalleryConnectionException {
		if(rootAlbum==null){
			//it means we already have the list of albums
			HashMap<String, String> fetchAlbums = fetchAlbums(galleryUrl);
			Map<Integer, G2Album> albumsFromProperties = client.extractAlbumFromProperties(fetchAlbums);
			rootAlbum = client.organizeAlbumsHierarchy(albumsFromProperties);
		}
		//if 0 is specified as the parentAlbumId, it means we have to return the rootAlbum
		if(parentAlbumId==0){
			rootAlbum.getPictures().addAll(getPicturesFromAlbum(galleryUrl, parentAlbumId));
			return rootAlbum;
		}
		Album findAlbumFromAlbumName = AlbumUtils.findAlbumFromAlbumName(rootAlbum, parentAlbumId);
		findAlbumFromAlbumName.getPictures().addAll(getPicturesFromAlbum(galleryUrl, parentAlbumId));
		return  findAlbumFromAlbumName;
	}

	@Override
	public HashMap<String, String> fetchAlbums(String galleryUrl)
			throws GalleryConnectionException {
		return client.fetchAlbums(galleryUrl);
	}

	@Override
	public void loginToGallery(String galleryUrl, String user, String password)
			throws ImpossibleToLoginException {
		client.loginToGallery(galleryUrl, user, password);
		
	}

	@Override
	public int createNewAlbum(String galleryUrl, int parentAlbumName,
			String albumName, String albumTitle, String albumDescription)
			throws GalleryConnectionException {
		return client.createNewAlbum(galleryUrl, parentAlbumName, albumName, albumTitle, albumDescription);
	}

	@Override
	public int sendImageToGallery(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary, String description)
			throws GalleryConnectionException {
		return client.sendImageToGallery(galleryUrl, albumName, imageFile, imageName, summary, description);
	}

	@Override
	public InputStream getInputStreamFromUrl(String imageUrl)
			throws GalleryConnectionException {
		return client.getInputStreamFromUrl(imageUrl);
	}


	@Override
	public Map<Integer, Album> getAllAlbums(String galleryUrl)
			throws GalleryConnectionException {
		return client.getAllAlbums(galleryUrl);
	}

	
	
}
