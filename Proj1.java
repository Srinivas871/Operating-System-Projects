import java.io.*;
import java.util.Scanner;
import java.util.Stack;


public class Proj1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int PC=0, SP=999, IR=0, AC = 0, X=0, Y=0;
		int mode_switch=0; //for switching the mode, User_Mode=0 and Kernel_Mode=1
		int count_instr=0; //counting the no of instructions for timer interrupt
		int instr_limit=Integer.parseInt(args[1]);
		boolean syscalflag=false; //flag for checking for a system call 
		String cmd="";
		//System.out.println(PC);
		
		
		//Communication with the child process starts from here
		try
		{
			Runtime rt = Runtime.getRuntime();
			//Process pr = new ProcessBuilder("java  -classpath E:\\eclipse-standard-kepler-SR1-win32-x86_64\\eclipse\\OperatingSystemProjects\\bin ChildProcess", "sample1.txt", "100").start();
		Process proc = rt.exec("java  -classpath E:\\eclipse-standard-kepler-SR1-win32-x86_64\\eclipse\\OperatingSystemProjects\\bin ChildProcess "+args[0]+" "+args[1]);
		//ProcessBuilder proc= new ProcessBuilder("java  -classpath E:\\eclipse-standard-kepler-SR1-win32-x86_64\\eclipse\\OperatingSystemProjects\\bin", "sample1.txt", "100");
		InputStream is = proc.getInputStream();
		OutputStream os = proc.getOutputStream();
	    PrintWriter pw = new PrintWriter(os);
	    pw.println(PC);
	    pw.flush();
	    
		Scanner sc = new Scanner(is);
		
		String line ="";
		 
		 //proc.waitFor();
		 while(sc.hasNextLine())
		 {
			 line = sc.nextLine();
			 
			 if(line.equals("exit"))
				 break;
			 
			 else
			 {
				 //System.out.println("child: "+line);
			 }
			 
			 if(line.length()<2)
				 cmd=line.substring(0, 1);
			 else
			 cmd=line.substring(0,2);
			 
			 //System.out.println(cmd);
			while(!cmd.equals("50"))
				{
				//Scanner sc=new Scanner(System.in);
				
				
				//storing to the Interesting 
				IR=Integer.parseInt(cmd.trim());
				
				switch(IR)
				{
				
				case 1:
					//Load to the AC
					PC++;
					pw.println(PC);
					pw.flush();
					cmd=cmd_retrieve(sc.nextLine()).trim();
					AC=Integer.parseInt(cmd); //taking the next value
					//System.out.println("The value in AC is " + AC);
					break;
					
				case 2: //Loading the value at the address into AC
					
					PC++;
					pw.println(PC);
					pw.flush();
					cmd=sc.nextLine().trim();
					int addr=Integer.parseInt(cmd); //get the address
					if(mode_switch==0 && addr>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation: accessing system address "+addr+ " in user mode");
					}
					else
					{	
					pw.println(addr); // send the address location
					pw.flush();
					
					cmd=sc.nextLine().trim(); // retrieve the value at address
					AC=value_retrieve(cmd); // store it to AC
					}
					break;
					
				case 3: 
					PC++;
					pw.println(PC);
					pw.flush();
					cmd=sc.nextLine().trim();
					int addr_addr=Integer.parseInt(cmd);
					if(mode_switch==0 && addr_addr>999)
					{
						System.out.println("Memory violation: accessing system address "+addr_addr+ " in user mode");
					}
					else
					{
					pw.println(addr_addr);
					pw.flush();
					cmd=sc.nextLine().trim();
					int addr_main=Integer.parseInt(cmd);
					
					 	if(mode_switch==0 && addr_main>999) //validation for accessing memory only in user space
					 	{
						System.out.println("Memory violation: accessing system address "+addr_main+ " in user mode");
					 	}
					 	else
					 	{
						pw.println(addr_main);
						pw.flush();
						cmd=sc.nextLine().trim();
						AC= Integer.parseInt(cmd);
					 	}
					
					}
					break;
					
				case 4:
					PC++;
					pw.println(PC); // incrementing the value of PC and output the same to get PC
					pw.flush();
					cmd= cmd_retrieve(sc.nextLine().trim());  //Get the cmd
					
					int addr_temp=Integer.parseInt(cmd); //Program Counter in the Cmd
					if(mode_switch==0 && addr_temp>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation: accessing system address "+addr_temp+ " in user mode");
					}
					else
					{
					addr_temp=addr_temp+X; // here we add X
					pw.println(addr_temp);
					pw.flush();
					cmd= cmd_retrieve(sc.nextLine().trim()); //get the instruction to IR
					AC=Integer.parseInt(cmd);
					}
					break;
					
				case 5:
					PC++;
					pw.println(PC); // incrementing the value of PC and output the same to get PC
					pw.flush();
					cmd= cmd_retrieve(sc.nextLine().trim());  //Get the cmd
					
					int addr_temp_2=Integer.parseInt(cmd); //Program Counter in the Cmd
					addr_temp_2=addr_temp_2+Y; // here we add X
					if(mode_switch==0 && addr_temp_2>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation: accessing system address "+addr_temp_2+ " in user mode");
					}
					else
					{
					pw.println(addr_temp_2);
					pw.flush();
					cmd=cmd_retrieve(sc.nextLine()).trim(); //get the instruction to IR
					AC=Integer.parseInt(cmd);
					}
					break;
					
				case 6:
				
					int addr_sp=SP+X+1;
					if(mode_switch==0 && addr_sp>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation: accessing system address "+addr_sp+ " in user mode");
					}
					else
					{
					pw.println(addr_sp);
					pw.flush();
					cmd=cmd_retrieve(sc.nextLine().trim());
					AC=Integer.parseInt(cmd);
					}
					break;
					
				case 7: //Store incomplete pgm ****
					PC++;
					pw.println(PC); //look for the storage address
					pw.flush();
					
					int addr_to_str=Integer.parseInt(sc.nextLine().trim()); //address to be stored
					
					if(mode_switch==0 && addr_to_str>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation: accessing system address "+addr_to_str+ " in user mode");
					}
					else
					{
					pw.println("s"+addr_to_str); //store instruction to memory
					pw.flush();
					
					cmd=sc.nextLine();
					
					pw.println(AC); //storing AC to the value
					pw.flush();
					}
					break;
					
				case 8: //generate a random no and assign it to AC
					int rnd_no= (int) (100 * (Math.random())); //generating a random no
					AC=rnd_no;
					break;
					
				case 9: //Printing case
					PC++;
					pw.println(PC);
					pw.flush();
					cmd=sc.nextLine().substring(0, 1);
					
					if(cmd.equals("1"))
						System.out.print(AC); //print as an integer
					else if(cmd.equals("2"))
						System.out.print((char)AC); //print as a character
					
					break;
					
				case 10: //Add the value in X to AC
					AC=AC+X;
					break;
					
				case 11: //Add the value in Y to AC
					AC=AC+Y;
					break;
					
				case 12: //Subtract X from AC
					AC= AC-X;
					break;
					
				case 13: //Subtract Y from AC
					AC= AC-Y;
					break;
					
				case 14://Copy the value of AC to X
					X=AC;
					break;
					
				case 15://Copy the value of X to AC
					AC=X;
					break;
					
				case 16://Copy the value of AC to Y
					Y=AC;
					break;
					
				case 17://Copy the value of Y to AC
					AC=Y;
					break;
				
				case 18://Copy the value of AC to SP
					SP=AC;
					break;
					
				case 19://Copy the value of SP to AC
					AC=SP;
					break;
					
				case 20: //Jump to the address
					PC++;
					pw.println(PC);
					pw.flush();
					
					cmd=cmd_retrieve(sc.nextLine());
					if(mode_switch==0 && Integer.parseInt(cmd)>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation in Jump: accessing system address "+Integer.parseInt(cmd)+ " in user mode");
					}
					else
					{
					PC=Integer.parseInt(cmd)-1; // decrement for pointing to the before address so that it can correctly further increment.
					}
					break;
					
				case 21: //Jump if AC is equal to 0 to the address which comes next
					if(AC==0)
					{
					PC++;
					pw.println(PC);
					pw.flush();
					
					cmd=cmd_retrieve(sc.nextLine()).trim();
					if(mode_switch==0 && Integer.parseInt(cmd)>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation in Jump if Equal: accessing system address "+Integer.parseInt(cmd)+ " in user mode");
					}
					else
					{
					PC=Integer.parseInt(cmd)-1; // decremeneting for adjusting to the current address in further increment.
					}
					}
					else
						PC++; // skip the next address which is to be read if AC equals 0
					
					break;
					
				case 22: //Jump if AC is not equal to 0 to the address which comes next
					if(AC!=0)
					{
					PC++;
					pw.println(PC);
					pw.flush();
					
					cmd=cmd_retrieve(sc.nextLine()).trim();
					if(mode_switch==0 && Integer.parseInt(cmd)>999) //validation for accessing memory only in user space
					{
						System.out.println("Memory violation in Jump if not equal to: accessing system address "+Integer.parseInt(cmd)+ " in user mode");
					}
					else
					{
					PC=Integer.parseInt(cmd)-1; // decremeneting for adjusting to the current address in further increment.
					}
					}
					else
						PC++; // skip the next address which is to be read if AC equals 0
					
					break;
					
				case 23: // Push PC's current address onto stack
					
					pw.println("s"+SP);
					pw.flush();
					cmd=cmd_retrieve(sc.nextLine());
					
					pw.println(PC);
					pw.flush();
					
					SP--;
					
					PC++;
					
					pw.println(PC);
					pw.flush();
					
					cmd=sc.nextLine().trim(); // read the next address to jump to
					PC=Integer.parseInt(cmd)-1; // decremeneting for adjusting to the current address in further increment.
					break;
					
				case 24: //Pop previously stored value from stack
					SP++;
					pw.println(SP); //SP for location of the memory in stack space   
					pw.flush();
					
					cmd=sc.nextLine().trim();
					PC=Integer.parseInt(cmd)+1; // adjusting to the next instruction in further increment.
					break;
					
				case 25: // Increment X
					X++;
					break;
					
				case 26: // Decrement X
					X--;
					break;
					
				case 27: //push AC onto stack
					pw.println("s"+SP);
					pw.flush();
					
					cmd=sc.nextLine();
					
					//Storing AC to stack location
					
					 pw.println(AC);
					 pw.flush();
					SP--;
				   break;
					
				case 28: //pop AC from stack
				   
					SP++; //increment SP to the current location
					pw.println(SP);
					pw.flush();
					cmd=cmd_retrieve(sc.nextLine()).trim();
					AC=Integer.parseInt(cmd); //Store to AC
					break;
					
					
				case 29: //System Call
					mode_switch=1; //changing the CPU to Kernel mode
					pw.println("s"+1999);
					
					pw.flush();
					
					cmd=sc.nextLine();
					
					//Storing SP to 1999 location
					 pw.println(SP);
					 pw.flush();
					 
					 //store PC to the next location onto stack
					SP=1998;
					pw.println("s"+SP);
					pw.flush();
					
					cmd=sc.nextLine();
					
					//Storing PC to 1998 location
					 pw.println(PC);
					 pw.flush();
					
					 SP--; //decrementing the stack pointer to point to the next freee location
					 PC=1500; //changing the PC to point to interrupt processing at 1500
					 PC--; //decrementing by one so as to let the PC point to 1500 in next increment
					 
					 syscalflag=true;
					break;
					
				case 30: //System call return
					
					SP++; //increment SP to the current location
					pw.println(SP);
					pw.flush();
					cmd=cmd_retrieve(sc.nextLine()).trim();
					PC=Integer.parseInt(cmd); //Store to PC, as we stored it second
					
					//getting SP out of the stack 
					SP++; //increment SP to the current location
					pw.println(SP);
					pw.flush();
					cmd=cmd_retrieve(sc.nextLine()).trim();
					SP=Integer.parseInt(cmd); //Store to PC, as we stored it second
					
					//SP=999; //switch the SP to point to user stack
					mode_switch=0;
					
					
					if(syscalflag==true)
					{
						//leave PC as it is
						syscalflag=false;
					}
					else
					{
						PC--; //to point back to the current instruction
					}
					
					break;
					
				case 50:
					pw.println("exit");
					pw.flush();
					break;
					
				default:
					System.out.println("Some error in child");
					break;
				}
				
				//System.out.println(PC);
				PC++; // incrementing the PC to point to next instruction in memory
				
				if (mode_switch==0) //check if the system is in user mode
				{
				count_instr++; //instructions increment to look for timer interrupt
				}
				
				//check for timer interrupt
				if(count_instr > instr_limit && mode_switch==0)//to check for if the system is in user mode 
				{
					mode_switch=1; //changing the CPU to Kernel mode
					pw.println("s"+1999);
					
					pw.flush();
					
					cmd=sc.nextLine();
					
					//Storing SP to 1999 location
					 pw.println(SP);
					 pw.flush();
					 
					 //store PC to the next location onto stack
					SP=1998;
					pw.println("s"+SP);
					pw.flush();
					
					Scanner sc2=new Scanner(is);
					cmd=sc2.nextLine();
					
					//Storing PC to 1998 location
					 pw.println(PC);
					 pw.flush();
					
					 SP--; //decrementing the stack pointer to point to the next freee location
					 PC=1000; //changing the PC to point to interrupt processing at 1000
					 count_instr=0; //making the counter =0
					
				} //timer interrupt section ends here
				
				 pw.println(PC);
				 pw.flush();
				if(sc.hasNext())
				{
					line=sc.nextLine();
				}	
				if(line.length()<2)
					 cmd=line.substring(0, 1);
				else
				 cmd=line.substring(0,2).trim(); //to take 2-digit value instructions
				
				}   
			
			//setting the parent to terminate
			line="exit";
			pw.println("exit");
			pw.flush();
		 } 
			 
		// System.out.println(line);
			 
		}catch(Throwable t)
		{
			System.out.println("Exception in parent communication process");
			t.printStackTrace();
		}
		
		
	}
	
	public static String cmd_retrieve(String s)
	{
		if(s.length()<3)
			return s;
		else
			return s.substring(0,3).trim();
		
	}
	
	public static int value_retrieve(String s)
	{
		int i=0,value=0;
		while(i<s.length() && s.charAt(i)>=48 && s.charAt(i)<59)
		{
		 	value=10*value + Integer.parseInt(s.substring(i, i+1));
		 	i++;
		}
		
		return value;
	}
	
}
