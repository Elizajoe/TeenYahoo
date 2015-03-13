package com.example.gridimagesearch.adapters;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gridimagesearch.R;
import com.example.gridimagesearch.models.ImageResult;
import com.squareup.picasso.Picasso;

public class ImageResultsAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultsAdapter(Context context,List<ImageResult> images) {
		super(context, R.layout.item_image_result , images);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageResult imageInfo = getItem(position);
		if(convertView==null){ //not a recycled view
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_image_result, parent,false);
		}
		
		ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
		TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
		
		//if Recycled view, then clear out image from last time
		ivImage.setImageResource(0);
		//Populate title and remote download the image url
		
		tvTitle.setText(Html.fromHtml(imageInfo.title));
		//Remotely download image data in the background // use picasso
		//Picasso.with(getContext()).load(imageInfo.thumbUrl).into(ivImage);
		
		return convertView;
		
	}
	
	
	
	

}
