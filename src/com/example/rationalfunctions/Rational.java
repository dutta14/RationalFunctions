package com.example.rationalfunctions;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.androidplot.series.XYSeries;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;

public class Rational extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rational);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rational, menu);
		return true;
	}
	
	XYSeries make(Number[] a, String na) {
		return new SimpleXYSeries(Arrays.asList(a),SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED,na);
		//returns the XYSeries with the list, format: odd indices:y, even indices: x.
	}
	
	LineAndPointFormatter make(int line, int pt) {
		return new LineAndPointFormatter(line, pt, null,(PointLabelFormatter)null);
	}
	
	public void seegraph(View v) {	
		
		Mulpoly pf; //polynomial which is input
		String st,fx;		
	    Iterator<Integer> it; 
	    XYPlot graph = (XYPlot) findViewById(R.id.graph);
		graph.clear();	//clearing the graph
		graph.redraw();	 //redrawing the lines
		try {
			fx=((EditText)findViewById(R.id.fx)).getText().toString().replace("x)", "x+0)".replace("(x","(1x"));
			pf = new Mulpoly(fx); //making a mulpoly object from given input text's modification
			double m;
			try {m=Double.parseDouble(fx.substring(0,fx.indexOf('(')));} 
			catch(Exception e) {
				m=1;
			}
			if(m<1) m=1;
			Enumeration<String> e = pf.keys();
			//if option is to plot the graphs
			if((((EditText)findViewById(R.id.plot)).getText().toString()).equalsIgnoreCase("P"))    
		 		while (e.hasMoreElements()) {
		 			st=e.nextElement();
		 			it=pf.get(st).iterator(); 
		 			while (it.hasNext())
		 				graph.addSeries(make(pf.getGraph(st,it.next()),"y="+st), make(Color.rgb(0,0,0), Color.rgb(0,0,0)));		
		 		}  
		 	// Turn the above arrays into XYSeries:
			Number[] zps[] = pf.getArr(), yaxis={0,10,0,-10}, xaxis={-50,0,71,0}; // Create a couple arrays of y-values to plot	
			XYSeries arr[]=new XYSeries[4];	
		 	arr[0]=make( yaxis,"Y-axis");
		 	arr[1]=make(xaxis ,"X-axis"); 
		 	arr[2]=make(zps[2],"Zeroes");
		 	arr[3]=make(zps[0],"Poles");
		 	LineAndPointFormatter larr[]=new LineAndPointFormatter[4];	 	
		 	larr[0]=make(Color.rgb(220,220,220), Color.rgb(220,220,220));
	        larr[1]=make(Color.rgb(220,220,220), Color.rgb(220,220,220));
	        larr[2]=make(Color.rgb(0,0,0), Color.rgb(200,0,0));
		 	larr[3]=make(Color.rgb(0,0,0), Color.rgb(0,0,200));	// line color, point color		
	        for(int i=0; i<4;graph.addSeries(arr[i], larr[i]),i++);	   
	        graph.setRangeBoundaries(-10/m, 10/m, BoundaryMode.FIXED);
		 } 
		catch(Exception e) {
			Toast.makeText(getApplicationContext(), "Enter correctly.", Toast.LENGTH_SHORT).show();
		}
		
	}
}
