/**
 * The MIT License (MIT)

Copyright (c) 2013 Chute

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.getchute.android.photopickerplus.ui.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.getchute.android.photopickerplus.R;
import com.getchute.android.photopickerplus.config.PhotoPicker;
import com.getchute.android.photopickerplus.dao.MediaDAO;
import com.getchute.android.photopickerplus.models.DeliverMediaModel;
import com.getchute.android.photopickerplus.models.enums.MediaType;
import com.getchute.android.photopickerplus.ui.activity.AssetActivity;
import com.getchute.android.photopickerplus.ui.activity.ServicesActivity;
import com.getchute.android.photopickerplus.ui.listener.ListenerFilesCursor;
import com.getchute.android.photopickerplus.ui.listener.ListenerItemCount;
import com.getchute.android.photopickerplus.ui.listener.ListenerVideoSelection;
import com.getchute.android.photopickerplus.util.AssetUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class CursorAdapterVideos extends BaseRecyclerCursorAdapter implements
  ListenerVideoSelection {

	private ListenerFilesCursor filesCursorListener;
	private ListenerItemCount itemCountListener;
	private Context context;
	private int position;
	private int count = 0;

	public CursorAdapterVideos(Context context, Cursor c,
			ListenerFilesCursor filesCursorListener, ListenerItemCount itemCountListener) {
		super(context, c);
		this.context = context;
		this.filesCursorListener = filesCursorListener;
		this.itemCountListener = itemCountListener;
		if (context.getResources().getBoolean(R.bool.has_two_panes)) {
			((ServicesActivity) context).setVideosSelectListener(this);
		} else {
			((AssetActivity) context).setVideosSelectListener(this);
		}
	}

	@Override
	public List<Integer> getCursorVideosSelection() {
		final List<Integer> selectedItemPositions = new ArrayList<Integer>();
		final Iterator<Integer> iterator = tick.keySet().iterator();
		while (iterator.hasNext()) {
			selectedItemPositions.add(iterator.next());
		}
		return selectedItemPositions;

	}

	@Override
	public int getDataIndex(Cursor cursor) {
		if (cursor == null) {
			return 0;
		} else {
			return cursor.getColumnIndex(MediaStore.Video.Media.DATA);
		}
	}

	@Override
	public void setViewClickListener(View view, String path, int position) {
		view.setOnClickListener(new VideoClickListener(path, position));

	}

	@Override
	public void setPlayButtonVisibility(ImageView imageView) {
		imageView.setVisibility(View.VISIBLE);

	}

	@Override
	public void loadImageView(ImageView imageView, Cursor cursor) {
		imageView.setImageBitmap(MediaDAO.getVideoThumbnail(context, cursor));

	}

	private final class VideoClickListener implements OnClickListener {

		private String path;
		private int itemPosition;

		private VideoClickListener(String path, int itemPosition) {
			this.path = path;
			this.itemPosition = itemPosition;
		}

		@Override
		public void onClick(View v) {
			position = itemPosition;
			if (PhotoPicker.getInstance().isMultiPicker()) {
				toggleTick(position);
			} else {
        Uri uri = null;
        if (getCursor().moveToPosition(position)) {
          int id = getCursor().getInt(getCursor()
            .getColumnIndex(MediaStore.Video.Media._ID));
          uri = ContentUris.withAppendedId(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, id);
        }
				String thumb = MediaDAO.getVideoThumbnailFromCursor(context,
						getCursor(), position);
				filesCursorListener.onCursorAssetsSelect(AssetUtil
						.getMediaModel(createMediaResultModel(thumb, path, uri)));
			}

		}

	}

	public List<DeliverMediaModel> getSelectedFilePaths() {
		final List<DeliverMediaModel> deliverList = new ArrayList<DeliverMediaModel>();
		Iterator<Entry<Integer, String>> iterator = tick.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, String> pairs = iterator.next();
			String path = pairs.getValue();
			int position = pairs.getKey();
      Uri uri = null;
      if (getCursor().moveToPosition(position)) {
        int id = getCursor().getInt(getCursor()
          .getColumnIndex(MediaStore.Video.Media._ID));
        uri = ContentUris.withAppendedId(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, id);
      }
			String thumbnail = MediaDAO.getVideoThumbnailFromCursor(context,
					getCursor(), position);
			deliverList.add(createMediaResultModel(thumbnail, path, uri));
		}
		return deliverList;
	}

	public void toggleTick(int positionSelected) {
		if (tick.containsKey(positionSelected)) {
			tick.remove(positionSelected);
			count--;
		} else {
			tick.put(positionSelected, getItem(positionSelected));
			count++;
		}
		itemCountListener.onSelectedVideosCount(count);
		notifyDataSetChanged();
	}

	private DeliverMediaModel createMediaResultModel(String thumb,
                                                   String videoUrl, Uri uri) {
		DeliverMediaModel model = new DeliverMediaModel();
    model.setLocalMediaUri(uri);
		model.setVideoUrl(videoUrl);
		model.setMediaType(MediaType.VIDEO);
		model.setImageUrl(thumb);
		model.setThumbnail(thumb);
		return model;
	}

}
