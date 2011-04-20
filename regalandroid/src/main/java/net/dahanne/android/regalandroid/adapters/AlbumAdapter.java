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

package net.dahanne.android.regalandroid.adapters;

import java.util.List;

import net.dahanne.android.regalandroid.R;
import net.dahanne.gallery.commons.model.Album;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumAdapter extends ArrayAdapter<Album> {

	private final List<Album> items;
	private final Context context;

	public AlbumAdapter(Context context, int textViewResourceId,
			List<Album> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.show_albums_row, null);
		}
		Album album = items.get(position);
		if (album != null) {
			TextView tt = (TextView) v.findViewById(R.id.first_line);
			TextView bt = (TextView) v.findViewById(R.id.second_line);
			if (tt != null) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(album.getTitle());
				if(album.getPictures().size()>0){
					stringBuilder.append(" (");
					stringBuilder.append(album.getPictures().size());
					stringBuilder.append(")");
				}
				tt.setText(stringBuilder.toString());
			}
			if (bt != null) {
				String summary = album.getSummary();
				if (summary == null || summary.equals("")) {
					summary = "";
				}
				bt.setText(summary);
			}
			;
			ImageView iv = (ImageView) v.findViewById(R.id.icon);
			if (iv != null) {
				if (context.getString(R.string.view_album_pictures).equals(
						album.getTitle())) {
					// display another icon when fake album
					iv.setImageResource(R.drawable.ic_launcher_gallery);
				} else {
					iv.setImageResource(R.drawable.folder_images_icon);
				}
			}

		}
		return v;
	}
}
