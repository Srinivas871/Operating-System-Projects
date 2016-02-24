import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class ChildProcess {

	public static void main(String[] args)
	{
	
	String[] arr= new String[2000];
	//int intercount=0;
	int addrscount=0;
	
	
	
	try
	{
	String fname=args[0];
	//String option=args[1];
	int getopt=Integer.parseInt(args[1]);
	
		Scanner in=new Scanner(new File(fname));
		String str;
	while(in.hasNextLine())
	{
		
		//int i=0;
		//System.out.println(in.nextLine());
		str=in.nextLine();
		try
		{
		
			if(str.equals("") || str.equals(null))
			{
			  continue;	
			}
			
			
		if(str.substring(0, 1).equals("."))
		{
			addrscount=Integer.parseInt(str.substring(1,str.length()));
			continue;
		} 
		
		arr[addrscount]=str;
		if(!str.substring(0, 1).equals(" "))
		addrscount++;
		}catch(NoSuchElementException ex)
		{
			System.out.println("File Loaded");
		}
	}
	
	
	/*for (String i: arr)
	{
		System.out.println(i);
	} */ 
	
	//System.out.println("1000: " +arr[1000]);
	
	}catch(Throwable t)
	{
		t.printStackTrace();
	}
 
	try
	{
		 Scanner parentscan = new Scanner(System.in);
		 String line= parentscan.nextLine();
		 int index=0;
		 
		 while(!line.equals("exit"))
		{
			 ///if(line.equals("skip"))
				// continue;
			 
			 if(line.substring(0, 1).equals("s"))
			 {
				 index=Integer.parseInt(line.substring(1));
				 
				 //functionality to check whethwr the place is empty, thereafter decided this is not required, so removed
				 
					 System.out.println("empty");
				 
				 arr[index]=parentscan.nextLine();
				 line=" ";
			 }
			 else
			 {
				 if(line.equals(" "))
				 {
					 line=parentscan.nextLine();
					 continue;
				 } 
				index=Integer.parseInt(line);
				 System.out.println(arr[index]);
				 line=parentscan.nextLine(); // next line scan
			 }
	 
		}	 
	}catch(Exception e)
	{
		e.printStackTrace();
		System.out.println("Exception in Communication with Child");
	} 
	} 
}
