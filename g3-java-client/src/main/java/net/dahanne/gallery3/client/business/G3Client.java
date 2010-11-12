/**
 *  Gallery3-java-client
 *  URLs: http://github.com/anthonydahanne/g3-java-client , http://blog.dahanne.net
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
package net.dahanne.gallery3.client.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery3.client.business.exceptions.G3BadRequestException;
import net.dahanne.gallery3.client.business.exceptions.G3ForbiddenException;
import net.dahanne.gallery3.client.business.exceptions.G3GalleryException;
import net.dahanne.gallery3.client.business.exceptions.G3ItemNotFoundException;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.ItemUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class G3Client implements IG3Client {

	private static final String X_GALLERY_REQUEST_KEY = "X-Gallery-Request-Key";
	private static final String X_GALLERY_REQUEST_METHOD = "X-Gallery-Request-Method";
	private static final String GET = "get";
	private static final String DELETE = "delete";
	private static final String POST = "post";
	private static final String PUT = "put";

	private static final String INDEX_PHP_REST_ITEM = "index.php/rest/item/";
	private static final String INDEX_PHP_REST_ITEMS = "index.php/rest/items";

	private final String galleryItemUrl;
	private String existingApiKey;

	private String password;
	private String username;

	public G3Client(String galleryUrl) {
		if(!galleryUrl.endsWith("/")){
			this.galleryItemUrl = galleryUrl + "/";
		}else{
			this.galleryItemUrl = galleryUrl;
		}
		
	}

	public Item getItem(int itemId) throws G3GalleryException {
		Item item = null;
		String stringResult = sendHttpRequest(INDEX_PHP_REST_ITEM + itemId,
				new ArrayList<NameValuePair>(), GET, null);
		try {
			JSONObject jsonResult = (JSONObject) new JSONTokener(stringResult)
					.nextValue();
			item = ItemUtils.parseJSONToItem(jsonResult);
		} catch (JSONException e) {
			throw new G3GalleryException(e.getMessage());
		}
		return item;
	}

	public int createItem(Entity entity, File file) throws G3GalleryException {
		String resultUrl;
		NameValuePair nameValuePair;
		try {
			if (file == null) {
				nameValuePair = new BasicNameValuePair("entity",
						ItemUtils.convertAlbumEntityToJSON(entity));
			} else {
				nameValuePair = new BasicNameValuePair("entity",
						ItemUtils.convertPhotoEntityToJSON(entity));
			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(nameValuePair);
			resultUrl = sendHttpRequest(
					INDEX_PHP_REST_ITEM + entity.getParent(), nameValuePairs,
					POST, file);

			resultUrl = ItemUtils.convertJsonStringToUrl(resultUrl);
		}

		catch (JSONException e) {
			throw new G3GalleryException(e.getMessage());
		}
		return ItemUtils.getItemIdFromUrl(resultUrl);

	}

	public void updateItem(Entity entity) throws G3GalleryException {
		NameValuePair nameValuePair;
		try {
			nameValuePair = new BasicNameValuePair("entity",
					ItemUtils.convertAlbumEntityToJSON(entity));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(nameValuePair);
			sendHttpRequest(INDEX_PHP_REST_ITEM + entity.getId(),
					nameValuePairs, PUT, null);
		} catch (JSONException e) {
			throw new G3GalleryException(e.getMessage());
		}

	}

	public void deleteItem(int itemId) throws G3GalleryException {
		sendHttpRequest(INDEX_PHP_REST_ITEM + itemId,
				new ArrayList<NameValuePair>(), DELETE, null);

	}

	/**
	 * Send the HTTP request to the gallery
	 * 
	 * @param appendToGalleryUrl
	 * @param nameValuePairs
	 * @param useExistingApiKey
	 * @param requestMethod
	 * @param file
	 * @return the response from the HTTP server
	 * @throws G3GalleryException
	 */
	private String sendHttpRequest(String appendToGalleryUrl,
			List<NameValuePair> nameValuePairs, String requestMethod, File file)
			throws G3GalleryException {

		String result;
		

		// do we need to login ? do we have the apikey ?
		if (username != null && existingApiKey == null) {
			// we are inside a call of getApiKey
			if (nameValuePairs != null && nameValuePairs.size() != 0) {
				if (nameValuePairs.get(0).getName().equals("user")) {

				} else {
					existingApiKey = getApiKey(username, password);
				}
			} else {
				existingApiKey = getApiKey(username, password);
			}
		}

		try {
			HttpEntity responseEntity = extracted(appendToGalleryUrl,
					nameValuePairs, requestMethod, file);
			
			
			
			
			
			
			
			
			responseEntity = new BufferedHttpEntity(responseEntity);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					responseEntity.getContent()));
			String line;
			StringBuilder sb = new StringBuilder();
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
			} finally {
				reader.close();
			}

			result = sb.toString();

		} catch (ClientProtocolException e) {
			throw new G3GalleryException(e.getMessage());
		} catch (IOException e) {
			throw new G3GalleryException(e.getMessage());
		}
		return result;
	}

	private HttpEntity extracted(String appendToGalleryUrl,
			List<NameValuePair> nameValuePairs, String requestMethod,
			File file)
			throws UnsupportedEncodingException, IOException,
			ClientProtocolException, G3GalleryException {
		HttpClient defaultHttpClient = new DefaultHttpClient();
		HttpRequestBase httpMethod;
		if (POST.equals(requestMethod)) {
			httpMethod = new HttpPost(galleryItemUrl + appendToGalleryUrl);
			httpMethod.setHeader(X_GALLERY_REQUEST_METHOD, requestMethod);
			if (file != null) {
				MultipartEntity multipartEntity = new MultipartEntity();

				String string = nameValuePairs.toString();
				// dirty fix to remove the enclosing entity{}
				String substring = string.substring(string.indexOf("{"),
						string.lastIndexOf("}") + 1);

				StringBody contentBody = new StringBody(substring,
						Charset.forName("UTF-8"));
				// System.out.println(substring);
				multipartEntity.addPart("entity", contentBody);
				FileBody fileBody = new FileBody(file);
				multipartEntity.addPart("file", fileBody);
				((HttpPost) httpMethod).setEntity(multipartEntity);
			} else {
				((HttpPost) httpMethod).setEntity(new UrlEncodedFormEntity(
						nameValuePairs));
			}
		} else if (PUT.equals(requestMethod)) {
			httpMethod = new HttpPost(galleryItemUrl + appendToGalleryUrl);
			httpMethod.setHeader(X_GALLERY_REQUEST_METHOD, requestMethod);
			((HttpPost) httpMethod).setEntity(new UrlEncodedFormEntity(
					nameValuePairs));
		} else if (DELETE.equals(requestMethod)) {
			httpMethod = new HttpGet(galleryItemUrl + appendToGalleryUrl);
			httpMethod.setHeader(X_GALLERY_REQUEST_METHOD, requestMethod);
		} else {
			httpMethod = new HttpGet(galleryItemUrl + appendToGalleryUrl);
		}
		if (existingApiKey != null) {
			httpMethod.setHeader(X_GALLERY_REQUEST_KEY, existingApiKey);
		}
		HttpResponse response = null;

		String[] patternsArray = new String[3];
		patternsArray[0] = "EEE, dd MMM-yyyy-HH:mm:ss z";
		patternsArray[1] = "EEE, dd MMM yyyy HH:mm:ss z";
		patternsArray[2] = "EEE, dd-MMM-yyyy HH:mm:ss z";
		try {
			// be extremely careful here, android httpclient needs it to be
			// an
			// array of string, not an arraylist
			defaultHttpClient.getParams().setParameter(
					CookieSpecPNames.DATE_PATTERNS, patternsArray);
			response = defaultHttpClient.execute(httpMethod);
		} catch (ClassCastException e) {
			List<String> patternsList = Arrays.asList(patternsArray);
			defaultHttpClient.getParams().setParameter(
					CookieSpecPNames.DATE_PATTERNS, patternsList);
			response = defaultHttpClient.execute(httpMethod);
		}

		int responseStatusCode = response.getStatusLine().getStatusCode();

		switch (responseStatusCode) {
		case HttpURLConnection.HTTP_CREATED:
			break;
		case HttpURLConnection.HTTP_OK:
			break;
		case HttpURLConnection.HTTP_BAD_REQUEST:
			throw new G3BadRequestException();
		case HttpURLConnection.HTTP_FORBIDDEN:
			throw new G3ForbiddenException();
		case HttpURLConnection.HTTP_NOT_FOUND:
			throw new G3ItemNotFoundException();
		default:
			throw new G3GalleryException("HTTP code " + responseStatusCode);
		}

		HttpEntity responseEntity = response.getEntity();
		return responseEntity;
	}

	public String getApiKey(String username, String password)
			throws G3GalleryException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("user", username));
		nameValuePairs.add(new BasicNameValuePair("password", password));

		String jsonResult = sendHttpRequest("index.php/rest", nameValuePairs,
				POST, null);

		String key = ItemUtils.convertJsonResultToApiKey(jsonResult);

		return key;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setExistingApiKey(String existingApiKey) {
		this.existingApiKey = existingApiKey;
	}

	public String getExistingApiKey() {
		return existingApiKey;
	}

	/**
	 * get the entire item representing the AlbumId + all its album sub items
	 */
	public List<Item> getAlbumAndSubAlbums(int albumId)
			throws G3GalleryException {
		List<Item> items = new ArrayList<Item>();
		Item item = getItems(albumId, items, "album");
		// we add to the list the parent album
		items.add(0, item);
		return items;
	}

	/**
	 * Get the photos of the album albumId
	 * 
	 * @param albumId
	 * @return
	 * @throws G3GalleryException
	 */
	public List<Item> getPictures(int albumId) throws G3GalleryException {
		List<Item> items = new ArrayList<Item>();
		getItems(albumId, items, "photo");
		return items;
	}

	private Item getItems(int albumId, List<Item> items, String type)
			throws G3GalleryException {
		Item item = this.getItem(albumId);
		Collection<String> members = item.getMembers();
		JSONArray urls = new JSONArray(members);
		try {
			String encodedUrls;
			encodedUrls = URLEncoder.encode(urls.toString(), "UTF-8");
			StringBuilder requestToAppend = new StringBuilder();
			requestToAppend.append(INDEX_PHP_REST_ITEMS);
			requestToAppend.append("?urls=");
			requestToAppend.append(encodedUrls);
			requestToAppend.append("&type=");
			requestToAppend.append(type);
			String sendHttpRequest = sendHttpRequest(
					requestToAppend.toString(), new ArrayList<NameValuePair>(),
					GET, null);

			JSONTokener jsonTokener = new JSONTokener(sendHttpRequest);
			JSONArray jsonResult = (JSONArray) jsonTokener.nextValue();
			for (int i = 0; i < jsonResult.length(); i++) {
				items.add(ItemUtils.parseJSONToItem((JSONObject) jsonResult
						.get(i)));
			}
		} catch (UnsupportedEncodingException e) {
			throw new G3GalleryException(e);
		} catch (JSONException e) {
			throw new G3GalleryException(e);
		}
		return item;
	}

	public InputStream getPhotoInputStream(String url)
			throws G3GalleryException {
		InputStream content = null;
		String appendToGalleryUrl =  url.substring(url.indexOf(galleryItemUrl)+galleryItemUrl.length());
		try {
			content = extracted(appendToGalleryUrl, null, GET, null).getContent();
		} catch (IllegalStateException e) {
			throw new G3GalleryException(e);
		} catch (UnsupportedEncodingException e) {
			throw new G3GalleryException(e);
		} catch (ClientProtocolException e) {
			throw new G3GalleryException(e);
		} catch (IOException e) {
			throw new G3GalleryException(e);
		}
		return content;
	}

}
