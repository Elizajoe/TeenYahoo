package com.example.gridimagesearch.other;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.Toast;

public class CustomOnItemSelectedListener implements OnItemSelectedListener {
	
	public CustomOnItemSelectedListener() {
		super();
		
	}



	private static String value = "zzzzzz";

	

	

	public static void setValue(String value) {
		CustomOnItemSelectedListener.value = value;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		
		Toast.makeText(parent.getContext(), 
				"OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),Toast.LENGTH_SHORT).show();
		// value = parent.getItemAtPosition(position).toString();
		setValue(parent.getItemAtPosition(position).toString());
		
		System.out.println("value inside is "+ value);
		
		
		
	}
	


	public static String getValue() {
		return value;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
		
	}

}
