package com.example.gridimagesearch.activities;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.gridimagesearch.R;
import com.example.gridimagesearch.adapters.ImageResultsAdapter;
import com.example.gridimagesearch.models.ImageResult;
import com.example.gridimagesearch.other.EndlessScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class SearchActivity extends Activity implements OnScrollListener,OnItemSelectedListener{

	private EditText etQuery;
	private GridView gvResults;
	private ArrayList<ImageResult> imageResults;
	private ImageResultsAdapter aImageResults;
	private static String filterQuery ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		SpinnerAdapter adapter ;
		setContentView(R.layout.activity_search);
		// gvResults =(GridView) findViewById(R.id.gvResults);
		setUpViews();

		// Creates the data source for list
		imageResults = new ArrayList<ImageResult>();

		// Create the adapter
		aImageResults = new ImageResultsAdapter(this, imageResults);
		// link adapter to the view
		gvResults.setAdapter(aImageResults);
		gvResults.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int i, int totalItemCount) {

				customLoadMoreDataFromApi(i);
			}
		});
	}

	public String getQuery() {
		String query = etQuery.getText().toString() + filterQuery;
		return query;
	}

	// ********************************
	public void customLoadMoreDataFromApi(int offset) {

		String query = getQuery();
		String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q="
				+ query + "&v=1.0&rsz=8" + "&start=" + offset;
		System.out.println("*******************************" + searchUrl);

		AsyncHttpClient client = new AsyncHttpClient();
		client.get(searchUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
				Log.d("DEBUG", response.toString());

				JSONArray imageResultsJSON = null;

				try {
					imageResultsJSON = response.getJSONObject("responseData")
							.getJSONArray("results");
					// imageResults.clear(); //clear the existing images from
					// the array (in case it is a new search)

					// When you make changes to the adapter, it can modify the
					// underlying data.
					aImageResults.addAll(ImageResult
							.fromJSONArray(imageResultsJSON));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Log.i("INFO", imageResults.toString());

			}
		});

		gvResults.setAdapter(aImageResults);

	}

	// ********************************

	private void setUpViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);

		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Launch the imgae display activity
				// first create an intent
				Intent i = new Intent(SearchActivity.this,
						ImageDisplayActivity.class);
				// get image results to display
				ImageResult result = imageResults.get(position);
				// pass image results into intent
				i.putExtra("result", result); // need to be serializable or
												// parcelable
				// launch the intent
				startActivity(i);
			}

		});
	}

	// fired when button is pressed
	public void onImageSearch(View v) {
		// String query = etQuery.getText().toString();
		String query = getQuery();

		Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
		// happens in background
		AsyncHttpClient client = new AsyncHttpClient();

		String searchUrl = "https://ajax.googleapis.com/ajax/services/search/images?q="
				+ query + "&v=1.0&rsz=8";
		System.out.println("*******************************" + searchUrl);

		client.get(searchUrl, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				Log.d("DEBUG", response.toString());

				JSONArray imageResultsJSON = null;

				try {
					imageResultsJSON = response.getJSONObject("responseData")
							.getJSONArray("results");
					imageResults.clear(); // clear the existing images from the
											// array (in case it is a new
											// search)

					// When you make changes to the adapter, it can modify the
					// underlying data.
					aImageResults.addAll(ImageResult
							.fromJSONArray(imageResultsJSON));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				Log.i("INFO", imageResults.toString());

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*
		 * if (id == R.id.action_settings) { return true; }
		 */

		//Spinner spinner = (Spinner) findViewById(R.id.spSize);
		List<String> list = new ArrayList<String>();
		list.add("any");
		list.add("small");
		list.add("medium");
		list.add("large");
		list.add("xlarge");
		
		
		//Spinner spinner1 = (Spinner) findViewById(R.id.spColor);
		List<String> listColor = new ArrayList<String>();
		listColor.add("any");
		listColor.add("black");
		listColor.add("blue");
		listColor.add("pink");
		listColor.add("red");
		listColor.add("yellow");
		listColor.add("white");
		
		

		//Spinner spinner2 = (Spinner) findViewById(R.id.spColor);
		List<String> listType = new ArrayList<String>();
		listType.add("any");
		listType.add("face");
		listType.add("photo");
		listType.add("clipart");
		listType.add("lineart");
		
		

		

		if (id == R.id.miFilter) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			//LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout_spinners =inflater.inflate(R.layout.advanced_search_filter,null);
			
			
			final Spinner spSize = (Spinner) layout_spinners.findViewById(R.id.spSize);
			final Spinner spColor = (Spinner) layout_spinners.findViewById(R.id.spColor);
			final Spinner spType = (Spinner) layout_spinners.findViewById(R.id.spType);
			final EditText etSite = (EditText) layout_spinners.findViewById(R.id.site);
	
			
			builder.setView(layout_spinners)
					.setPositiveButton("Save",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int id) {
									
									filterQuery="";
									filterQuery=filterQuery+"&imgsz="+String.valueOf(spSize.getSelectedItem())+
											   "&imgcolor="+String.valueOf(spColor.getSelectedItem())+
											  "&imgtype="+String.valueOf(spType.getSelectedItem())+"&as_sitesearch="+etSite.getText();
									System.out.println("filter query is "+filterQuery);
									
									String size1 =spSize.getSelectedItem().toString();
									/*System.out.println("the size selected by user is :"+size1 );
									
									SpinnerAdapter adapter = spSize.getAdapter();
									spSize.setAdapter(adapter);*/
									setSpinnerToValue(spSize,size1);
									String color1 =spColor.getSelectedItem().toString();
									setSpinnerToValue(spColor,color1);
									String type1 =spType.getSelectedItem().toString();
									setSpinnerToValue(spType,type1);
									String site = etSite.getText().toString();
									etSite.setText(site);
									
									
								}

								
								
								
							})
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									onResume();
								}
							});
			
			

			builder.setMessage("Filter your search here").setTitle("Advanced Search");
			
			ArrayAdapter<String> aSpSize = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list );
			aSpSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spSize.setAdapter(aSpSize);
			//String size1 =spSize.getSelectedItem().toString();
			//spSize.setAdapter(aSpSize);
			//int index = setSpinnerToValue(spSize,size1);
			
			//spSize.setSelection(index,true);
			//spSize.setOnItemSelectedListener(this);
			
			
			ArrayAdapter<String> aSpColor = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listColor );
			aSpColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spColor.setAdapter(aSpColor);
			
			ArrayAdapter<String> aSpType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,listType );
			aSpType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spType.setAdapter(aSpType);
			
			
			
			
			AlertDialog dialog = builder.create();
			dialog.show();

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	
/*	
	public int pos =0;
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		
		 pos = parent.getSelectedItemPosition();
		
		
			Spinner spSize = (Spinner) findViewById(R.id.spSize);	
			spSize.setSelection(pos);
     (parent.getItemAtPosition(position).toString());

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}*/

	private void  setSpinnerToValue(Spinner spinner, String value ) {
		int index = 0;
		SpinnerAdapter adapter = spinner.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			if (adapter.getItem(i).equals(value)) {
				System.out.println("Adapter item is "+ adapter.getItem(i));
				System.out.println("the given value is "+value);
				index = i;
				
				System.out.println("the index decided is " +index);
				break; // terminate loop
			}
		}
		
		//adapter = ArrayAdapter.CreateFromResource(this, Resource.Array.text_array, Android.Resource.Layout.SpinnerItem);
		//spinner.setAdapter(adapter);
		 spinner.setSelection(index);
		
		
	}
	

}
