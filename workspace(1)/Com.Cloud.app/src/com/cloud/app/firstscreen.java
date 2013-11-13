package com.cloud.app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public final class firstscreen extends Fragment {
	
	
	private LinearLayout layout;
	private Button searchbutton;
	private Button reportbutton;
	private Spinner category;
	private Spinner places;
	private Button gpsbutton;
	private Spinner times;
	private Button finalbutton;
	private GPSTracker gps;
	private Button gobackbutton;
	private TextView columsView1;
	private TextView columsView2;
	private TextView columsView3;
	private TextView columsView4;
	
	private String searchselect;
	private String reportselect;
	private String selectcat;
	private double longitude;
	private double latitude;
	private String selecttime;
	private String selectplace;
	private String tosend;
	private String description;
	private EditText desc_text;
	private int slength;
	private StringBuilder stri;
	private String[] cols;
	private String intermediate="";
	private String str;
	
	private Socket socket;
    private static final int SERVERPORT = 4499;
    private static final String SERVER_IP = "192.168.0.31";


	
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
		{
		
		searchselect="";
		reportselect="";
			
			final Handler datarec=new Handler(){
				@Override
				 public void handleMessage(Message msg) {
	                  // process incoming messages here
					System.out.println("Received a message = "+msg.what);
					switch(msg.what){
					case 0:{
						
						 searchbutton.setVisibility(LinearLayout.INVISIBLE);
		    	         reportbutton.setVisibility(LinearLayout.INVISIBLE);
		    	         category.setVisibility(LinearLayout.INVISIBLE);
		    	         times.setVisibility(LinearLayout.INVISIBLE);
		    	         gpsbutton.setVisibility(LinearLayout.INVISIBLE);
		    	         finalbutton.setVisibility(LinearLayout.INVISIBLE);
		    	         places.setVisibility(LinearLayout.INVISIBLE);
		    	         columsView1.setVisibility(LinearLayout.VISIBLE);
		    	         columsView2.setVisibility(LinearLayout.VISIBLE);
		    	         columsView3.setVisibility(LinearLayout.VISIBLE);
		    	         columsView4.setVisibility(LinearLayout.VISIBLE);
		    	         gobackbutton.setVisibility(LinearLayout.VISIBLE);
		    	         desc_text.setVisibility(LinearLayout.INVISIBLE);
						intermediate=msg.getData().getString("events");
						Toast.makeText(container.getContext(), msg.getData().getString("events"), Toast.LENGTH_LONG).show();
						
		    	        slength=intermediate.length();
		    	        cols=intermediate.split("%");
		    	        
		      	        columsView1.setText(cols[0]);
		      	      columsView2.setText(cols[1]);
		      	    columsView3.setText(cols[2]);
		      	  columsView4.setText(cols[3]);
		    			        
		    	        
		    	        
		    	         		
		    	        //List <TextView> text=new ArrayList<TextView>();
		    	        
		    	          //for(int i=0; i<cols.length; i++)
		    	          //{
		    	        //	  String x = cols[i];
		    	        	  
		    	       // 	  columsView = new TextView(container.getContext());
		    	        //	  columsView.setText(x);	                                
		    	        	 
		    	          //}
		    	          //columsView.setText(x);
		    	          //columsView = new TextView(container.getContext());
		    	        
					}break;
					}
	              }
	          };
				
				
	          
			
			
		
		
		
		
		
			

			final Context context = container.getContext();
			layout=new LinearLayout(context);
			layout.setOrientation(LinearLayout.VERTICAL);
			
			searchbutton=new Button(context);
			searchbutton.setWidth(5);
			searchbutton.setText("SEARCH");
			//What the search button does
			searchbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					searchselect="Search";
					gpsbutton.setClickable(true);
					gpsbutton.setVisibility(LinearLayout.VISIBLE);
					places.setVisibility(LinearLayout.INVISIBLE);
					places.setClickable(false);
					gobackbutton.setVisibility(LinearLayout.INVISIBLE);
					desc_text.setVisibility(LinearLayout.INVISIBLE);
					columsView1.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView2.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView3.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView4.setVisibility(LinearLayout.INVISIBLE);
				}
			});
			
			reportbutton=new Button(context);
			reportbutton.setWidth(5);
			reportbutton.setText("REPORT");
			
			//What the report button does
			reportbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					reportselect="Report";
					places.setClickable(true);
					gpsbutton.setVisibility(LinearLayout.VISIBLE);
					places.setVisibility(LinearLayout.VISIBLE);
					columsView1.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView2.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView3.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView4.setVisibility(LinearLayout.INVISIBLE);
	
					gpsbutton.setClickable(true);
					
					desc_text.setVisibility(LinearLayout.VISIBLE);
					gobackbutton.setVisibility(LinearLayout.INVISIBLE);
					desc_text.setVisibility(LinearLayout.VISIBLE);
					
					
					description="";
					StringBuilder stri=new StringBuilder(desc_text.getText());
					description+=stri.toString();
					System.out.println("Event Description is "+description);
				
				}
			});
			
			
			category=new Spinner(context);
			String[] categ={"<Select Category>","Recreation","Health","Career","Educational","Religious"};
		    
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,categ);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        category.setAdapter(adapter);
		    
		    category.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View w,
						int pos, long d) {
					// TODO Auto-generated method stub
					if(pos!=0)
					Toast.makeText(container.getContext(), "Selection@pos = "+pos, Toast.LENGTH_LONG).show();
					selectcat=parent.getItemAtPosition(pos).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
		    	
		    	
		    });
		    
		    gpsbutton=new Button(context);
		    
		    gpsbutton.setText("Click to get GPS Location");
		    
		    //what gps button does
		    
		    gpsbutton.setOnClickListener(new OnClickListener()
		    {

				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					gps=new GPSTracker(container.getContext());
					
					if(gps.canGetLocation())
					{
	                    
	                    latitude = gps.getLatitude();
	                    longitude = gps.getLongitude();
	                     
	                    // \n is for new line
	                    Toast.makeText(container.getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();   
	                }
					
				}
		    	
		    });
		    
		    gobackbutton=new Button(context);
		    gobackbutton.setText("Go Back");
		    
		    gobackbutton.setOnClickListener(new OnClickListener()
		    {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					 searchbutton.setVisibility(LinearLayout.VISIBLE);
	    	         reportbutton.setVisibility(LinearLayout.VISIBLE);
	    	         category.setVisibility(LinearLayout.VISIBLE);
	    	         times.setVisibility(LinearLayout.VISIBLE);
	    	         gpsbutton.setVisibility(LinearLayout.VISIBLE);
	    	         finalbutton.setVisibility(LinearLayout.VISIBLE);
	    	         gobackbutton.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView1.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView2.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView3.setVisibility(LinearLayout.INVISIBLE);
	    	         columsView4.setVisibility(LinearLayout.INVISIBLE);
	 
				}
		    	
		    }
		    		);
		    
		    	    
		    
		    
		    times=new Spinner(context);
			String[] timer ={"<Select Time>","20:00:00","10:00:00","11:00:00","15:00:00","6:00:00","13:00:00","16:00:00"};
		   
			ArrayAdapter<String> timeadapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,timer);
			timeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        times.setAdapter(timeadapter);
		    
		    times.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View w,
						int pos, long d) {
					// TODO Auto-generated method stub
					if(pos!=0)
					Toast.makeText(container.getContext(), "Time Selection done = "+pos, Toast.LENGTH_LONG).show();
					selecttime=parent.getItemAtPosition(pos).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
		    	
		    	
		    });
		    
		    class ClientThread implements Runnable 
		    {
		    	    Handler handler;
		    		public ClientThread(Handler h)
		    		{
		    			System.out.println("###########################");
		    			handler = h;
		    			
		    		}
		    	@Override
		    	    public void run() 
		    	    {
		    	    			    	    			// TODO Auto-generated method stub
		    	    			
		    	    			try 
		    	    	    	{
		    	    	            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
		    	    	            socket = new Socket(serverAddr, SERVERPORT);
		    	    	            if(searchselect.equals("Search"))
		    			    		{
		    			    			tosend=searchselect+"%"+selectcat+"%"+longitude+"%"+latitude+"%"+selecttime;
		    			    			searchselect="";
		    			    		}
		    	    	            else if(reportselect.equals("Report"))
		    			    		{
		    			    			System.out.println("The description of event is "+description);
		    			    			System.out.println(latitude+"   "+longitude);
		    	    	            	tosend=reportselect+"%"+selectcat+"%"+longitude+"%"+latitude+"%"+selectplace+"%"+description;
		    			    			reportselect="";	
		    			    		
		    			    		}else{
		    			    			System.out.println("INVALID SELECTION");
		    			    		}
		    		    			
 	    	           
		    		    			
		    		    			OutputStream outToServer = socket.getOutputStream();
		    		    	         DataOutputStream out = new DataOutputStream(outToServer);

		    		    	         out.writeUTF(tosend);
		    		    	         
		    		    	         System.out.println("data sent server");
		    		    	         		    		    	        
		    		    	         InputStream inFromServer = socket.getInputStream();
		    		    	         DataInputStream in =new DataInputStream(inFromServer);
		    		    	    	 System.out.println("read from server");	         
		    		    	         String str = in.readUTF();
		    		    	         
		    		    	         	         
		    		    	           		    	         
		    		    	         	    		    	         
		    		    	         
		    		    	         System.out.println("send data to handler = "+str);
		    		    	         Message msg = new Message();
		    		    	         Bundle bundle = new Bundle();
		    		    	         bundle.putString("events", str);
		    		    	         msg.what = 0;
		    		    	         msg.setData(bundle);
		    		    	         handler.sendMessage(msg);
		    		    	         
		    		    	         socket.close();
		    	    	        } 
		    	    	    	catch (UnknownHostException e1) 
		    	    	    	{
		    	    	            e1.printStackTrace();

		    	    	        } 
		    	    	    	catch (IOException e1) 
		    	    	    	{
		    	    	            e1.printStackTrace();
		    	    	        }
		    	    }
		    }
		    
		    
		    
		    
		   
		    
		    
		    finalbutton=new Button(context);
		    finalbutton.setText("Submit");
		    		    finalbutton.setOnClickListener(new OnClickListener()
		    {
		    	
		    		    	
		    		    	
		    		    	@Override
				public void onClick(View arg0) 
				{
		    		try
		    		{
		    			Thread x= new Thread(new ClientThread(datarec));
		    			x.start();
	    			
		    		}
		    		
		    		catch (Exception e) 
		    		{
		    		    e.printStackTrace();
		    	    }
   		
		    	}
		    });
		    		    
		   		 

		    
		    
		    places=new Spinner(context);
			String[] place={"<Select Location>","Nobel Science and Engineering Library","Student Recreation Center","Memorial Union","Brickyard on Mill Av","Engineering centre, G wing"};
		    
			ArrayAdapter<String> placeadapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,place);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        places.setAdapter(placeadapter);
		    
		    places.setOnItemSelectedListener(new OnItemSelectedListener(){

				@Override
				public void onItemSelected(AdapterView<?> parent, View w,
						int pos, long d) {
					// TODO Auto-generated method stub
					if(pos!=0)
					Toast.makeText(container.getContext(), "Selection done = "+pos, Toast.LENGTH_LONG).show();
					selectplace=parent.getItemAtPosition(pos).toString();
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub
					
				}
		    	
		    	
		    });
		    
		    desc_text= new EditText(context);
		    desc_text.setLines(1);
            
		    
		    
		    columsView1= new TextView(container.getContext());
		    columsView2= new TextView(container.getContext());
		    columsView3= new TextView(container.getContext());
		    columsView4= new TextView(container.getContext());
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		    
		   
		   layout.addView(searchbutton);
		   layout.addView(reportbutton);
		   layout.addView(category);
		   layout.addView(gpsbutton);
		   layout.addView(places);
		   layout.addView(times);
		   layout.addView(finalbutton);
		   layout.addView(columsView1);
		   layout.addView(columsView2);
		   layout.addView(columsView3);
		   layout.addView(columsView4);
		   layout.addView(desc_text);
		   layout.addView(gobackbutton);
		   		   
		   layout.setBackgroundColor(Color.WHITE);
		   
		   layout.setVisibility(View.VISIBLE);
		   
		   
		   return layout;
		    
			
			
			
		}
	}
	
