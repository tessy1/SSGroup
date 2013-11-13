package com.cloud.service;

import java.sql.SQLException;

public interface serviceimp {
	
	//Sample Method
	public String showdata(String data) throws ClassNotFoundException, SQLException;
	public double shortdist(double lat1, double lon1, double lat2, double lon2 );
	public String searching (String[] sr) throws ClassNotFoundException;
	public void reporting (String[] sr) throws ClassNotFoundException, SQLException;

}
