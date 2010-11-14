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
import net.dahanne.gallery.commons.remote.GalleryOperationNotYetSupportedException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import net.dahanne.gallery3.client.business.G3Client;
import net.dahanne.gallery3.client.business.exceptions.G3GalleryException;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.G3ConvertUtils;
import net.dahanne.gallery3.client.utils.ItemUtils;

import org.apache.http.cookie.Cookie;

public class G3Connection implements RemoteGallery {

	private G3Client client;

	public G3Connection(String galleryUrl, String username, String password) {
		client = new G3Client(galleryUrl);
		client.setUsername(username);
		client.setPassword(password);
		

	}

	public int createNewAlbum(String arg0, int arg1, String arg2, String arg3,
			String arg4) throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available in G3 yet");
	}

	public HashMap<String, String> fetchAlbums(String arg0)
			throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available in G3 yet");
	}


	public Map<Integer, Album> getAllAlbums(String arg0)
			throws GalleryConnectionException {
		
		
//		try {
//			Item item = client.getItem(1);
//			for (String member : item.getMembers()) {
//				Integer itemIdFromUrl = ItemUtils.getItemIdFromUrl(member);
//				Item item2 = client.getItem(itemIdFromUrl);
//			}
//		} catch (G3GalleryException e) {
//			throw new GalleryConnectionException(e);
//		}

		return null;
	}

	public InputStream getInputStreamFromUrl(String arg0)
			throws GalleryConnectionException {
		try {
			return client.getPhotoInputStream(arg0);
		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
	}

	public Collection<Picture> getPicturesFromAlbum(String galleryUrl, int albumName)
			throws GalleryConnectionException {
		Collection<Picture> pictures = new ArrayList<Picture>();
		try {
			List<Item> picturesAsItems = client.getPictures(albumName);
			for (Item item : picturesAsItems) {
				pictures.add(G3ConvertUtils.itemToPicture(item));
			}
			
		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
		return pictures;
	}

	public List<Cookie> getSessionCookies() {
		// TODO Auto-generated method stub
		return null;
	}


	public void loginToGallery(String galleryUrl, String username,
			String password) throws ImpossibleToLoginException {
		try {
			client.getApiKey(username, password);
		} catch (G3GalleryException e) {
			throw new ImpossibleToLoginException(e);
		}

	}

	public Album retrieveRootAlbumAndItsHierarchy(String arg0)
			throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available in G3 yet");
	}

	public int sendImageToGallery(String arg0, int arg1, File arg2,
			String arg3, String arg4, String arg5)
			throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available in G3 yet");
	}

	public Album getAlbumAndSubAlbumsAndPictures(String galleryUrl, int parentAlbumId)
			throws GalleryConnectionException {
		
		//dirty hack to load the root album, id 1 in G3
		if(parentAlbumId==0){
			parentAlbumId=1;
		}
		
		Album parentAlbum=null;
		try {
			List<Item> albumAndSubAlbums = client.getAlbumAndSubAlbumsAndPictures(parentAlbumId);
			//the first is item is an album
			parentAlbum = G3ConvertUtils.itemToAlbum(albumAndSubAlbums.get(0));
			for (Item item : albumAndSubAlbums) {
				if(item==albumAndSubAlbums.get(0)){
					//no need to add the first one, it is the parent
					continue;
				}
				if(item.getEntity().getType().equals("album")){
					Album itemToAlbum = G3ConvertUtils.itemToAlbum(item);
					itemToAlbum.setParentName(parentAlbum.getName());
					itemToAlbum.setParent(parentAlbum);
					parentAlbum.getSubAlbums().add(itemToAlbum);
				}else{
					parentAlbum.getPictures().add(G3ConvertUtils.itemToPicture(item));
				}
			}
			
		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
		return parentAlbum;
	}

}
