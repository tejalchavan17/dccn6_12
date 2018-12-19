import java.io.*;
import java.net.*;
import java.util.*;
public class Sender
{
	Socket sender;
	DataOutputStream out;
	DataInputStream in;
	int n,i=0,j,t,seqno=0,nextseqno,flag=0,ackno=-1;
	long timer,timer1;
	String st;
    int data[]=new int[50];
	Sender(){}
	public void run()
	{
	 	try
		{
			Scanner sc=new Scanner(System.in);
			System.out.println("Waiting for Connection....");
 			sender = new Socket("localhost",2005);
			out=new DataOutputStream(sender.getOutputStream());
 			in=new DataInputStream(sender.getInputStream());
			st=in.readUTF();
			System.out.println(st);
			System.out.println("Enter data size:");
			n=sc.nextInt();
			out.writeInt(n);
			System.out.println("Enter bits:");
			for(t=0;t<n;t++)
	        {
				data[t]=sc.nextInt();	
         	}
			do
			{
				try
				{

					out.writeInt(data[i]);
					out.writeInt(seqno);
					System.out.println("Data send:"+data[i]+" and frame no.:"+seqno);
					seqno=(seqno==0)?1:0;
					i++;
					timer=System.currentTimeMillis();
					do
					{
						
						ackno=in.readInt();
						timer1 = System.currentTimeMillis();
						if(ackno != -1)
						{ 
							flag=1;
							break;
						}
					}while(timer1-timer<80);
					if(flag==1)
					{
						if(seqno==ackno)
						{
							System.out.println("Acknowledge received");
							System.out.println("Next sequence no. to send:"+ackno);
						}
						flag=0;
					}
					else
					{
						System.out.println("Acknowledge not received");
						System.out.println("Resending data");
						i--;
						seqno=(seqno==1)?0:1;
					}	
				}
				catch(Exception e){}
			}
			while(i<n);
			System.out.println("All data sent. exiting.");
		}
		catch(Exception e){}
		finally
		{
			try
			{
				in.close();
				out.close();
				sender.close();
			}
			catch(Exception e){}
		}
	}
	public static void main(String args[])
	{
		Sender s=new Sender();
		s.run();
	}
}


import java.io.*;
import java.net.*;
import java.util.*;
public class Receiver
{
	ServerSocket reciever;
	Socket connection=null;
	DataOutputStream out;
	DataInputStream in;
	int i=0,ackno,data,n;
	String str="hi";
	Receiver(){}
	public void run()
	{
		try
		{
			Scanner sc=new Scanner(System.in);
			reciever = new ServerSocket(2005);
			System.out.println("waiting for connection...");
			connection=reciever.accept();
			System.out.println("Connection established   :");
			out=new DataOutputStream(connection.getOutputStream());
			in=new DataInputStream(connection.getInputStream());
			out.writeUTF("connected    .");
			n=in.readInt();
			do
			{
				try
				{
					data=in.readInt();
					ackno=in.readInt();
					i++;
					if(i!=n)
					{
						System.out.println("Data received:"+data+" and frame no.:"+ackno);
						ackno=(ackno==0)?1:0;
					}
					if(i==n)
					{
						ackno=-1;
					}
					out.writeInt(ackno);
				}
				catch(Exception e){}
			}
			while(i<n+1);
			out.writeUTF("connection ended    .");
		}
		catch(Exception e){}
		finally
		{
			try
			{
				in.close();
				out.close();
				reciever.close();
			}
			catch(Exception e){}
		}
	}

	public static void main(String args[])
	{
		Receiver s=new Receiver();
			s.run();
	}
}

/*Output:
tejal@ubuntu:~/Desktop$ javac Sender.java
tejal@ubuntu:~/Desktop$ java Sender
Waiting for Connection....
connected    .
Enter data size:
4 
Enter bits:
1 2 3 4
Data send:1 and frame no.:0
Acknowledge received
Next sequence no. to send:1
Data send:2 and frame no.:1
Acknowledge received
Next sequence no. to send:0
Data send:3 and frame no.:0
Acknowledge received
Next sequence no. to send:1
Data send:4 and frame no.:1
Acknowledge not received
Resending data
Data send:4 and frame no.:1
Acknowledge received
Next sequence no. to send:0
All data sent. exiting.
tejal@ubuntu:~/Desktop$ 

tejal@ubuntu:~/Desktop$ javac Receiver.java
tejal@ubuntu:~/Desktop$ java Receiver
waiting for connection...
Connection established   :
Data received:1 and frame no.:0
Data received:2 and frame no.:1
Data received:3 and frame no.:0
Data received:4 and frame no.:1
tejal@ubuntu:~/Desktop$ 
*/

import java.io.*;
import java.net.*;
import java.util.*;
public class Sender
{
	Socket sender;
	DataOutputStream out;
	DataInputStream in;
	int m,tseqno,wsize,i,freceived,j,fno,f=0,c=0;
	String st;
	Sender(){}
	public void run()
	{
	 	try
		{
			Scanner sc=new Scanner(System.in);
			System.out.println("Waiting for Connection....");
 			sender = new Socket("localhost",2005);
			out=new DataOutputStream(sender.getOutputStream());
 			in=new DataInputStream(sender.getInputStream());
			st=in.readUTF();
			System.out.println(st);
			System.out.println("Enter value of m:");
			m=sc.nextInt();
			tseqno=(int)Math.pow(2,m);
			wsize=tseqno-1;
			out.writeInt(wsize);
			for(i=0;i<wsize;i++)
			{
				System.out.println("Frame "+i+" send");
				out.writeInt(i);
			}
			for(i=0;i<wsize;i++)
			{
				freceived=in.readInt();
				if(freceived!=-1)
				{
					System.out.println("Acknowledge of frame "+freceived+" received");
				}
				else
				{
					j=i;
					fno=i;
					f=1;
				}
				if(f==1)
				{
					c++;
				}
			}
			
			out.writeInt(c);
			out.writeInt(fno);			
			for(i=j;i<wsize;i++)
			{
				System.out.println("Resending Frame "+i);
				out.writeInt(i);						
			}
			for(j=0;j<c;j++)
			{
				freceived=in.readInt();
				System.out.println("Acknowledge of frame "+freceived+" received");
			}
		}
		catch(Exception e){}
		finally
		{
			try
			{
				in.close();
				out.close();
				sender.close();
			}
			catch(Exception e){}
		}
	}
	public static void main(String args[])
	{
		Sender s=new Sender();
		s.run();
	}
}

				
import java.io.*;
import java.net.*;
import java.util.*;
public class Receiver
{
	ServerSocket reciever;
	Socket connection=null;
	DataOutputStream out;
	DataInputStream in;
	int wsize,frame,j,t,fno,c,framer;
	Receiver(){}
	public void run()
	{
		try
		{
			Scanner sc=new Scanner(System.in);
			reciever = new ServerSocket(2005);
			System.out.println("waiting for connection...");
			connection=reciever.accept();
			System.out.println("Connection established   :");
			out=new DataOutputStream(connection.getOutputStream());
			in=new DataInputStream(connection.getInputStream());
			out.writeUTF("connected    .");
			wsize=in.readInt();
			for(j=0;j<wsize;j++)
			{
				frame=in.readInt();
				System.out.println("frame "+frame+" received");
				System.out.println("Acknowledge send");
			}
			for(j=0;j<wsize;j++)
			{
				if(j==2)
					out.writeInt(-1);
				else
					out.writeInt(j);
			}		
			c=in.readInt();
			fno=in.readInt();
			for(j=0;j<c;j++)
			{
				framer=in.readInt();
				System.out.println("frame "+framer+" received and discard");
				System.out.println("Acknowledge send");
			}
			for(j=fno;j<wsize;j++)
			{
				out.writeInt(j);
			}
			out.writeUTF("connection ended    .");
		}
		catch(Exception e){}
		finally
		{
			try
			{
				in.close();
				out.close();
				reciever.close();
			}
			catch(Exception e){}
		}
	}

	public static void main(String args[])
	{
		Receiver s=new Receiver();
			s.run();
	}
}

/*Output:
tejal@ubuntu:~/Desktop$ javac Sender.java
tejal@ubuntu:~/Desktop$ java Sender
Waiting for Connection....
connected    .
Enter value of m:
3
Frame 0 send
Frame 1 send
Frame 2 send
Frame 3 send
Frame 4 send
Frame 5 send
Frame 6 send
Acknowledge of frame 0 received
Acknowledge of frame 1 received
Acknowledge of frame 3 received
Acknowledge of frame 4 received
Acknowledge of frame 5 received
Acknowledge of frame 6 received
Resending Frame 2
Resending Frame 3
Resending Frame 4
Resending Frame 5
Resending Frame 6
Acknowledge of frame 2 received
Acknowledge of frame 3 received
Acknowledge of frame 4 received
Acknowledge of frame 5 received
Acknowledge of frame 6 received


tejal@ubuntu:~/Desktop$ javac Receiver.java
tejal@ubuntu:~/Desktop$ java Receiver
waiting for connection...
Connection established   :
frame 0 received
Acknowledge send
frame 1 received
Acknowledge send
frame 2 received
Acknowledge send
frame 3 received
Acknowledge send
frame 4 received
Acknowledge send
frame 5 received
Acknowledge send
frame 6 received
Acknowledge send
frame 2 received and discard
Acknowledge send
frame 3 received and discard
Acknowledge send
frame 4 received and discard
Acknowledge send
frame 5 received and discard
Acknowledge send
frame 6 received and discard
Acknowledge send
tejal@ubuntu:~/Desktop$ 


*/




