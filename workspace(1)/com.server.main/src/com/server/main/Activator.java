package com.server.main;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


import java.sql.SQLException;
import org.osgi.framework.ServiceReference;
import firstservice.serviceimp;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	
	int serverport=4499;
	Thread t;
	public void start(BundleContext bundleContext) throws Exception {
		
		Activator.context = bundleContext;
		t = new StdServer(serverport,this);
		t.start();
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		t.stop();
		Activator.context = null;
	}

	public String a(String gett) throws ClassNotFoundException, SQLException
	{
		
		System.out.println("Entered function a");
		String s = null;
		BundleContext context = this.getContext();
		ServiceReference<?> refs = context.getServiceReference(serviceimp.class.getName());
		try {
			if(refs!=null)
			{
				serviceimp serv=(serviceimp)context.getService(refs);
				System.out.println("Service Reference Object Obtained");
				s=serv.showdata(gett);
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return s;

	}
}
