package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
 * The main implementation of the REPL loop (read-eval-print loop). It reads
 * commands from the user, parses them, executes them and displays the result.
 * 
 * 9/18/2023
 * COSI 131A PA1
 */
public class SequentialREPL {

	/**
	 * The main method that will execute the REPL loop
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {

		Scanner consoleReader = new Scanner(System.in);
		System.out.print(Message.WELCOME);

		while (true) {
			System.out.print(Message.NEWCOMMAND);

			// read user command, if its just whitespace, skip to next command
			String cmd = consoleReader.nextLine();
			if (cmd.trim().isEmpty()) {
				continue;
			}

			// exit the REPL if user specifies it
			if (cmd.trim().equals("exit")) {
				break;
			}
			
			// REST OF REPL	
			try {
				SequentialCommandBuilder.createFiltersFromCommand(cmd);
			} catch(IllegalArgumentException e1) {
				if(e1.getMessage().length() > 5 && e1.getMessage().substring(0, 5).equals("param")) {		// error message example: paramcd
					System.out.print(Message.REQUIRES_PARAMETER.with_parameter(e1.getMessage().substring(5)));
				} else if(e1.getMessage().length() > 5 && e1.getMessage().substring(0, 5).equals("input")) {	// error message example: inputwc
					System.out.print(Message.REQUIRES_INPUT.with_parameter(e1.getMessage().substring(5)));
				} else if(e1.getMessage().length() > 6 && e1.getMessage().substring(0, 6).equals("output")) {	// error message example: outputcd dir1
					System.out.print(Message.CANNOT_HAVE_OUTPUT.with_parameter(e1.getMessage().substring(6)));
				} else if(e1.getMessage().length() > 3 && e1.getMessage().substring(0, 3).equals("div")){		// error message example: divcd dir1
					System.out.print(Message.DIRECTORY_NOT_FOUND.with_parameter(e1.getMessage().substring(3)));
				} else {
					System.out.print(Message.CANNOT_HAVE_INPUT.with_parameter(e1.getMessage()));				// error message example: pwd					
				}
			} catch(UnsupportedOperationException e2) {
				System.out.print(Message.COMMAND_NOT_FOUND.with_parameter(e2.getMessage()));		// error message example: cda
			} catch(StringIndexOutOfBoundsException e3) {
				System.out.print(Message.REQUIRES_PARAMETER.with_parameter(e3.getMessage()));		// error message example: >
			} catch(FileNotFoundException e4) {
				System.out.print(Message.FILE_NOT_FOUND.with_parameter(e4.getMessage()));			// error message example: cat sdf.txt
			}
			//rest the SequentialCommandBuilder to ready for next line of commands
			SequentialCommandBuilder.reset();
		}
		System.out.print(Message.GOODBYE);
		consoleReader.close();

	}

}
