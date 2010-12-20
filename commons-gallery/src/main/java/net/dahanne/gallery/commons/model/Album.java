/**
 *  commons-gallery, a common API module for ReGalAndroid
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

package net.dahanne.gallery.commons.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Album implements Serializable {

	private int id;
	private int name;
	private String title;
	private int parentName;
	private String summary;
	private boolean add;
	private boolean write;
	private boolean deleteAlbum;
	private boolean createSubAlbum;
	private boolean fakeAlbum;
	private String extrafields;
	private String albumUrl;
	private Album parent;
	private final Set<Picture> pictures = new LinkedHashSet<Picture>();
	private final List<Album> subAlbums = new ArrayList<Album>();

	public Album() {
		super();
	}

	private static final long serialVersionUID = -671355798682957050L;

	public Album(int id, int name, String title, String summary, int parent,
			boolean add, boolean write, boolean deleteAlbum,
			boolean createSubAlbum, String extrafields) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.summary = summary;
		parentName = parent;
		this.add = add;
		this.write = write;
		this.deleteAlbum = deleteAlbum;
		this.createSubAlbum = createSubAlbum;
		this.extrafields = extrafields;
	}

	public Album(int id) {
		this.id = id;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getParentName() {
		return parentName;
	}

	public void setParentName(int parentName) {
		this.parentName = parentName;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isDeleteAlbum() {
		return deleteAlbum;
	}

	public void setDeleteAlbum(boolean deleteAlbum) {
		this.deleteAlbum = deleteAlbum;
	}

	public boolean isCreateSubAlbum() {
		return createSubAlbum;
	}

	public void setCreateSubAlbum(boolean createSubAlbum) {
		this.createSubAlbum = createSubAlbum;
	}

	public String getExtrafields() {
		return extrafields;
	}

	public void setExtrafields(String extrafields) {
		this.extrafields = extrafields;
	}

	public void setAlbumUrl(String albumUrl) {
		this.albumUrl = albumUrl;
	}

	public String getAlbumUrl() {
		return albumUrl;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Album getParent() {
		return parent;
	}

	public void setParent(Album parent) {
		this.parent = parent;
	}

	public List<Album> getSubAlbums() {
		return subAlbums;
	}

	public Set<Picture> getPictures() {
		return pictures;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("id : ").append(id)
				.append(" name : ").append(name).append(" title : ")
				.append(title).append(" parentName : ").append(parentName)
				.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (fakeAlbum ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + name;
		result = prime * result + parentName;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Album other = (Album) obj;
		if (fakeAlbum != other.fakeAlbum)
			return false;
		if (id != other.id)
			return false;
		if (name != other.name)
			return false;
		if (parentName != other.parentName)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	public void setFakeAlbum(boolean fakeAlbum) {
		this.fakeAlbum = fakeAlbum;
	}

	public boolean isFakeAlbum() {
		return fakeAlbum;
	}

	

}
