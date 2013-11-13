package com.cloud.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		context.registerService(serviceimp.class.getName(),new serviceimpl(),null);
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}
	
	//Implement the Service
	
	private static class serviceimpl implements serviceimp
	{

		 static final String database = "jdbc:mysql://127.0.0.1:3306/eventrecommendation";  

		  
		   static final String username = "root";
		   static final String password = "root";
		   static Connection conn = null;

				
		@Override
		public String showdata(String data)  {
			// TODO Auto-generated method stub
			
			String abc="";
			String r = data;
		      
		      String[] sp=r.split("%");
		      
		      
		      for (int x = 0; x < sp.length ; x++) {
		    	  
					System.out.println("string " + x + " " + sp[x]);
				}
		      
		    if (sp[0].equals("Search"))
		    {
		    	
				try {
					abc = searching(sp);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	System.out.println("called searching and got-----------*" + abc);
		    }
	    else if (sp[0].equals("Report"))
	    {
	    	try {
				reporting(sp);
				abc="Event%has%been%reported";
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	    	
	    }
	
		return abc;
		
		}
		
		
		
		public double shortdist(double lat1, double lon1, double lat2, double lon2 )
		   {
			   double dLat = Math.toRadians(lat2 - lat1);
		        double dLon = Math.toRadians(lon2 - lon1);
		        lat1 = Math.toRadians(lat1);
		        lat2 = Math.toRadians(lat2);
		        final double R = 6372.8;
		 
		        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
		        double c = 2 * Math.asin(Math.sqrt(a));
		        return R * c;
			   
		   }
		
		public String searching (String[] sr) throws ClassNotFoundException
		   {
			   String sendSt=null;
			   
			   Statement stmt = null;
			   Statement stmt1=null;
			   
			   ResultSet resultSet = null;
			   ResultSet rs = null;
			   
			   String[] srch=  sr;
			   
			   try{
				      
				   
				   
				      Class.forName("com.mysql.jdbc.Driver");  //registering the jdbc driver

				      //Open a connection
				      System.out.println("Connecting");
				      conn = DriverManager.getConnection(database, username, password);
				      System.out.println("Success");
				   
				      stmt=conn.createStatement();
				      ArrayList<String> locIds = new ArrayList<String>();
				      ArrayList<Double> lat = new ArrayList<Double>();
				      ArrayList<Double> longi = new ArrayList<Double>();
				      ArrayList<String> locName = new ArrayList<String>();
				      ArrayList<Double> dis = new ArrayList<Double>();
				      
				      resultSet=stmt.executeQuery("select * from location");
				     
				      //int i=0;
				      //writeResultSet(resultSet);
				      while(resultSet.next())
				      {
				    	  lat.add(resultSet.getDouble("latitude"));
				    	  locIds.add(resultSet.getString("locationID"));
				    	  longi.add(resultSet.getDouble("longitude"));
				    	  locName.add(resultSet.getString("locationname"));
				    	  
				    	  
				      }
				      
				      
						for (int i = 0; (i < lat.size() && i < longi.size()); i++) {
					    	  
								dis.add(shortdist(33.4272395,-111.9307812,lat.get(i),longi.get(i)));
								
							}
					      double shortest = Collections.min(dis);
					      int index=dis.indexOf (Collections.min(dis));
					      System.out.println("the shotest dis" + shortest);
					      System.out.println("index is" + index);
					      
				     
					      stmt1=conn.createStatement();
					      
					      ArrayList<String> eventID = new ArrayList<String>();
					      ArrayList<String> eventnames = new ArrayList<String>();
					      ArrayList<String> eventDesc = new ArrayList<String>();
					      ArrayList<String> eventTime = new ArrayList<String>();
					      ArrayList<String> eventlocID = new ArrayList<String>();
					      //ArrayList<Integer> eventRate = new ArrayList<Integer>();
					      ArrayList<String> eventCat = new ArrayList<String>();
					      
					      String L = null;
					      if(index==0)
					      {
					    	  L="Memorial Union";
					    	  
					      }
					      else if(index==1)
					      {
					    	  L="Student Recreation Centre";
					      }
					      else if(index==2)
					      {
					    	  L="Nobel Science and Engineering Library";
					      }
					      else if(index==3)
					      {
					    	  L="Brickyard on Mill Av";
					      }
					      else if(index==4)
					      {
					    	  L="Engineering centre, G wing";
					      }
					      
					      System.out.println("the vlaue of location " + L);
				      
				      String q= "select * from events where time=" + "\"" + srch[4] + "\"" + " AND category=" + "\"" +srch[1] + "\""+ " AND eventlocationID = (select locationID from location where locationname=" + "\"" + L + "\""+ ")";
				      System.out.println("here is the query" + q) ;
				      
				      rs=stmt1.executeQuery(q);
				      while(rs.next())
				      {
				    	  eventID.add(rs.getString("eventID"));
				    	  eventnames.add(rs.getString("eventname"));
				    	  eventDesc.add(rs.getString("description"));
				    	  eventTime.add(rs.getString("time"));
				    	  eventlocID.add(rs.getString("eventlocationID"));
				    	  //eventRate.add(rs.getInt("rating"));
				    	  eventCat.add(rs.getString("category"));
				    	  
				    	  
				      }
				       String locsend=null;
				      
				      for(int j=0; j<eventlocID.size(); j++)
				      {
				    	  if(eventlocID.get(j).equals("l1"))
				    	  {
				    		  locsend= "Memorial Union";
				    	  }
				    	  else if(eventlocID.get(j).equals("l2"))
				    	  {
				    		  locsend= "Student Recreation Centre";
				    	  }
				    	  else if(eventlocID.get(j).equals("l3"))
				    	  {
				    		  locsend= "Nobel Science and Engineering Library";
				    	  }
				    	  else if(eventlocID.get(j).equals("l4"))
				    	  {
				    		  locsend= "Brickyard on Mill Av";
				    	  }
				    	  else if(eventlocID.get(j).equals("l5"))
				    	  {
				    		  locsend= "Engineering centre, G wing";
				    	  }
				    	  System.out.println("Locsend here is----" + locsend);
				    	  
				      }
				      
				      //ArrayList<String> sendSt = new ArrayList<String>();
				      ;
				      
				      for(int z=0; z<eventID.size(); z++)
				      {
				    	   //sendSt.add(z,eventID.get(z)+"%"+eventnames.get(z)+"%"+eventDesc.get(z)+"%"+eventTime.get(z)+"%"+locsend+"%"+eventCat.get(z));			      }
			   			
				    	  sendSt=eventID.get(z)+"%"+eventnames.get(z)+"%"+eventDesc.get(z)+"%"+eventTime.get(z)+"%"+locsend+"%"+eventCat.get(z);
				      }
			   			
				      System.out.println("the concatinated string is-----" + sendSt);
				      System.out.println("/n everything"+ eventID + eventnames + eventDesc + eventTime + eventlocID +  eventCat);
				      
				      
//				      System.out.println("resutl set" + lat);
//				      System.out.println("index stuff" + lat.get(1));
//				      System.out.println("resutl set" + longi);
//				      System.out.println("resutl set" + locIds);
//				      System.out.println("resutl set" + locName);
//				      System.out.println("resutl set" + dis);
				      
				      //System.out.println("quesry"+ rs);
				      
				     
				      //return sendSt;
				      
				      //System.out.println("split done" + sp.toString());
				      
				      
				   }catch(SQLException se){      
				      se.printStackTrace();
				   }finally{
				      try{
				         if(conn!=null)
				            conn.close();
				        
				         if (resultSet != null) {
				             resultSet.close();
				           }

				      }catch(SQLException se){
				         se.printStackTrace();
				      }
				   }
			return sendSt;
			   
		   }

		  public void reporting (String[] sr) throws ClassNotFoundException, SQLException
		   {
			   Class.forName("com.mysql.jdbc.Driver");  //registering the jdbc driver

			      //Open a connection
			      System.out.println("Connecting");
			      conn = DriverManager.getConnection(database, username, password);
			      System.out.println("Success");
			      Statement stmt = null;
			      stmt=conn.createStatement();
			      String[] srch=  sr;
			      String rand = null;
			      
		      Random gen = new Random();
		      for (int idx = 1; idx <= 100; ++idx){
		          int randomInt = gen.nextInt(1000);
		          rand= Integer.toString(randomInt);
		          //System.out.println("random integer is  " + randomInt);
		      }
			          
			          String L = null;
				      if(srch[5].equals("Memorial Union"))
				      {
				    	  L="l1";
				    	  
				      }
				      else if(srch[5].equals("Student Recreation Centre"))
				      {
				    	  L="l2";
				      }
				      else if(srch[5].equals("Nobel Science and Engineering Library"))
				      {
				    	  L="l3";
				      }
				      else if(srch[5].equals("Brickyard on Mill Av"))
				      {
				    	  L="l4";
				      }
				      else if(srch[5].equals("Engineering centre, G wing"))
				      {
				    	  L="l5";
				      }
			          
			      //ResultSet resultSet = stmt.executeQuery("select * from location");
			      PreparedStatement preparedStatement = conn.prepareStatement("insert into events values(?,?,?,?,?,?)");
			      preparedStatement.setString(1, rand);
			      preparedStatement.setString(2, srch[1]);
			      preparedStatement.setString(3, srch[2]);
			      preparedStatement.setString(4, srch[3]);
			      preparedStatement.setString(5, L);
			      preparedStatement.setString(6, srch[5]);
			     
			      int resSet = preparedStatement.executeUpdate();

			      
		   }
		   
	}
				}
		






