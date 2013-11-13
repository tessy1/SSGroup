package com.server.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.sql.SQLException;

public class StdServer extends Thread
{
	private ServerSocket serverSocket;
	private Activator act;
	private String op;
	private String dummy;
	
	public StdServer(int port,Activator activator) throws IOException
	{
		serverSocket=new ServerSocket(port);
		serverSocket.setSoTimeout(1000000);
		act = activator;
	}
	
	public void run()
	{
		System.out.println("Waiting for Client on port "+serverSocket.getLocalPort());
		while(true)
		{
			try
			{
				Socket server = serverSocket.accept();
				System.out.println("Connected to port "+server.getRemoteSocketAddress());
				//while
				DataInputStream in =new DataInputStream(server.getInputStream());
				//System.out.println(in.readUTF());
				
				try {
					dummy=in.readUTF();
					System.out.println(dummy);
					
					op=act.a(dummy);
					System.out.println(op);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
								
				DataOutputStream out=new DataOutputStream(server.getOutputStream());
				System.out.println(op);
				out.writeUTF(op);
				
				//end
				server.close();
			}
			catch(SocketTimeoutException s)
			{
				System.out.println("Socket Timed out");
				break;
			}
			catch(IOException e)
			{
				e.printStackTrace();
				break;
			}
		}
	}
}