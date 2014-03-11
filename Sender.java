/*
	STOP AND WAIT---------SENDER

	both sent frames and ACK's can be corrupted by noise
*/


import java.io.*;
import java.net.*;
import java.util.*;	//for Random function

class DataOfSender
{
	static InputStreamReader r;
	static BufferedReader br;
	static BufferedWriter writer;
	static DataInputStream input;
 	static DataOutputStream output;

	public static ServerSocket s;
	Socket s1=new Socket("localhost",1023);



	DataOfSender()throws IOException,InterruptedException
	{


	
	r=new InputStreamReader(System.in);
	br=new BufferedReader(r);
	writer = new BufferedWriter(new OutputStreamWriter(s1.getOutputStream()));
	input= new DataInputStream(s1.getInputStream());
	output= new DataOutputStream(s1.getOutputStream());
	
		start(); 
	}

	void start()throws IOException,InterruptedException
	{
	String toSend=new String();
	boolean flag=true;
	int send_no;
	int sentFrame;
	Random noise=new Random();

	send_no=0;

	

		while(true)
		{
		System.out.println("Sending frame with sequence no "+send_no); //Debugging purposes
		
			sentFrame=send_no;
			
							/*sentFrame^=noise.nextInt(2);[can be used instead of next line]
									
								to add noise...noise is XORed since 1+1=2 but 1^1=0
								and since we want 1 to 0 and 0 to 1 only

								This can be done at receiver instead of sender
								or at both the places

								Since noise is generated only between two values 
								in noise.nextInt(2) there is a 50% chance that         										the value will be changed.

								Therefore we can increase the range of the nos 
								generated...eg sentFrame^=noise.nextInt(100);
								and make sure that only 1 or 0 is XORed by 
								applying modulo 2 division so that only 1 or 0
								are the nos which are generated
							*/

		sentFrame^=noise.nextInt(100)%2;


		System.out.println("Sequence no after adding noise "+sentFrame); //Debugging purposes	

			toSend=Integer.toString(sentFrame);

		System.out.println("Press 1 to send frame"); //Debugging purposes	
		int choice=Integer.parseInt(br.readLine());  //Debugging purposes	

			writer.write(toSend);
			writer.newLine();
			writer.flush();

			waitForACK((send_no+1)%2);

			send_no=(send_no+1)%2;
			
		}
	}

	synchronized void waitTime()throws InterruptedException
	{
		wait(1000);
		return;
	}

	void waitForACK(int ack_no)throws IOException
	{
	int flag;
	String toReceive=new String();

		while(true)
		{
		toReceive=input.readLine();
		flag=Integer.parseInt(toReceive);
			
			if(flag==ack_no)
			{
			System.out.println("ACK RECEIVED");
			return;
			}
		}
	}
}

class Sender
{
	public static void main(String args[])throws IOException,InterruptedException
	{
	DataOfSender d=new DataOfSender();
	}
}