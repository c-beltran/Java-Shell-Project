import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.lang.*;
import java.util.*;

public class JavaShell {

	static HistoryFeature hf = new HistoryFeature();
	static StartProcess initiateCMD = new StartProcess();
	static WindowsExclusive WindowsStart = new WindowsExclusive();
	
	public static void main(String[] args) throws IOException {
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String homeDir = System.getProperty("user.dir");
		String cdCommand = null;
		String commandLine;
		String OSName = System.getProperty("os.name");
		Boolean toggle = true;
		
		System.out.println("Your OS is: " + OSName);
		
		List<String> commands = new ArrayList<String>();
		List<String> historyList = new ArrayList<String>();
		String[] OSarr = OSName.split(" ");
		
		//if the OS is windows we start another process
		if(OSarr[0].equals("Windows")){
			WindowsStart.startWindowsCMD();
			toggle = false;
		}
		
		// we break out with <control><C>
		while (toggle)
		{
			// read what the user entered
			System.out.print("jsh>");
			commandLine = console.readLine();
			commandLine.toLowerCase();


				// if the user entered a return, just loop again
			if (commandLine.equals("")) continue;

			else{

				//clear the command list
				commands = new ArrayList<String>();

				//store the history commands onto the list
				historyList.add(commandLine);

				//start of history conditions
				if(commandLine.equals("history")){		
					hf.history(historyList);
					continue;
				}
				else if(commandLine.equals("!!")){
					commandLine = hf.prevCommand(historyList);
				}
				else if(commandLine.matches("[!][0-9]")){
					commandLine = hf.runIthCommand(commandLine, historyList);			
				}//end of history conditions
				
				//parse the input to obtain the command and any parameters
				StringTokenizer getInput = new StringTokenizer(commandLine);
				while(getInput.hasMoreTokens()){
					//add to list
					commands.add(getInput.nextToken());
				}
				
				//need to check for other commands
				if(commandLine.equals("cd")){
					homeDir = System.getProperty("user.dir");
					System.out.println("Your Current Dir. is: " + homeDir);
				}
				else if(commands.get(0).equals("cd") && commands.size() >= 1 ){
					cdCommand = commands.get(1);
					homeDir = cdCommand;
				}
				else{

				//start the process 
					try{
						initiateCMD.initiateProcess(homeDir, commands);
					}
					catch (Exception e){
						System.out.println("Please Enter a Valid Input");
					}
				}
			}
			
			/** The steps are:
			(1) parse the input to obtain the command and any parameters		
			(2) create a ProcessBuilder object
			(3) start the process
			(4) obtain the output stream
			(5) output the contents returned by the command */
		}
	}
}
