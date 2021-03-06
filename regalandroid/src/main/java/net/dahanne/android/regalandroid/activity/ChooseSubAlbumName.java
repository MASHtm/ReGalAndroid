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

package net.dahanne.android.regalandroid.activity;

import net.dahanne.android.regalandroid.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ChooseSubAlbumName extends Activity implements OnClickListener {
	static final String SUBALBUM_NAME = "subalbumName";
	private EditText subalbumEditText;
	private Button okButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_subalbum);
		setTitle(R.string.create_album_label);
		subalbumEditText = (EditText) findViewById(R.id.subalbum_id);
		okButton = (Button) findViewById(R.id.ok_id);
		okButton.setOnClickListener(this);

	}

	public void onClick(View v) {
		Intent data = new Intent();
		data.putExtra(SUBALBUM_NAME, subalbumEditText.getText().toString());
		setResult(RESULT_OK, data);
		finish();

	}
}
