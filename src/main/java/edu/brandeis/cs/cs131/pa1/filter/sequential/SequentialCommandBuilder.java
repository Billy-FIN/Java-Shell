package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.util.List;
import java.util.LinkedList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
 * This class manages the parsing and execution of a command. It splits the raw
 * input into separated subcommands, creates subcommand filters, and links them
 * into a list.
 * 
 * 9/18/2023
 * COSI 131A PA1
 */
public class SequentialCommandBuilder {

	/**
	 * Stores filters corresponding to user's command
	 */
	private static List<SequentialFilter> commandList = new LinkedList<SequentialFilter>();
	
	/**
	 * Stores every command segment passed by the user
	 */
	private static String[] parsedCommand;
	
	/**
	 * Utility class, cannot instantiate explicitly
	 */
	private SequentialCommandBuilder() {
	}
	
	/**
	 * Creates filters from the command and then link all the filters
	 * 
	 * @param command 			String					the line of input by user
	 * @return commandList 		List<SequentialFilter>	a list of all the filters
	 * @throws FileNotFoundException
	 */
	public static List<SequentialFilter> createFiltersFromCommand(String command) throws FileNotFoundException {
		// split the command line by " | "
		command = command.trim();
		parsedCommand = command.split(" \\| ");
		int num = 0;
		for(String cmd: parsedCommand) {	
			// create a filer based on its command
			SequentialFilter cmdFilter = constructFilterFromSubCommand(cmd.trim(), num);								
			commandList.add(cmdFilter);
			num++;
		}
		// link all the commands and processes them
		linkFilters(commandList);
		return commandList;
	}
	
	/**
	 * link all the commands and processes them one by one
	 * 
	 * @param subCommand String 		the one of parts of command, may come with parameters
	 * @param num 		 int    		the position of this sub command in the LinkedList
	 * @return filter SequentialFilter	a respective SequentialFilter created just now
	 * @throws FileNotFoundException
	 */
	private static SequentialFilter constructFilterFromSubCommand(String subCommand, int num) throws FileNotFoundException {
		// split the subcommand by the whitespace
		String[] parsedSubCommand = subCommand.split(" ");
		// get the filter name in order to match a switch-case then 
		String filterName = parsedSubCommand[0];
		String param = null;
		// check whether cmd has input 
		if(parsedSubCommand.length > 1) {
			int count = parsedSubCommand.length;
			param = "";
			for(int i = 1; i < count - 1; i++) {
				// concat param by a whitespace
				param = param + parsedSubCommand[i] + " ";
			}
			// manually add the last one in order to have no whitespace after it
			param = param + parsedSubCommand[count - 1];
		}
		// create a filter instance based on the command
		switch(filterName){
		case "pwd":
			if(param != null && param.indexOf(">") != 0) {			// cannot have parameter
				throw new IllegalArgumentException(filterName);
			} else if (param != null && param.indexOf(">") == 0) {		// needs to redirect
				if(param.length() == 1) {								// > needs a parameter
					throw new StringIndexOutOfBoundsException(">");
				}
				WorkingDirectoryFilter pwdFilter = new WorkingDirectoryFilter(true, param);
				return pwdFilter;
			} else {
				WorkingDirectoryFilter pwdFilter = new WorkingDirectoryFilter();
				return pwdFilter;				
			}
		case "ls":
			if(param != null && param.indexOf(">") != 0) {			// cannot have parameter
				throw new IllegalArgumentException(filterName);
			} else if (param != null && param.indexOf(">") == 0) {	// needs to redirect
				if(param.length() == 1) {							// > needs a parameter
					throw new StringIndexOutOfBoundsException(">");
				}
				ListFilter lsFilter = new ListFilter(true, param);
				return lsFilter;
			} else {
				ListFilter lsFilter = new ListFilter();
				return lsFilter;			
			}
		case "cd":
			if(param == null || param.indexOf(">") == 0) {			// needs a parameter
				throw new IllegalArgumentException("param" + filterName);
			} else if(num == 0 && parsedCommand.length > 1) {		// cannot have output
				throw new IllegalArgumentException("output" + filterName + " " + param);
			} else if(num != 0) {									// cannot have input
				throw new IllegalArgumentException(filterName + " " + param);
			}
			ChangeDirectoryFilter cdFilter = new ChangeDirectoryFilter(param);
			return cdFilter;
		case "cat":
			if(num > 0) {										// cannot have input
				throw new IllegalArgumentException(subCommand);
			}
			if(param == null || param.indexOf(">") == 0) {			// needs a parameter
				throw new IllegalArgumentException("param" + filterName);
			} else if (param.indexOf(">") != -1) {		// needs to redirect
				if(param.split(" ").length == 2) {				// > needs a parameter
					throw new StringIndexOutOfBoundsException(">");
				} else {
					CatFilter catFilter;
					try {
						// example: test.txt > 1.txt -> [0] = test.txt, [2] = 1.txt
						catFilter = new CatFilter(true, param.split(" ")[0], param.split(" ")[2]);
					} catch (FileNotFoundException e) {
						// throw this not, not handle it right now
						throw new FileNotFoundException(e.getMessage());
					}
					return catFilter;					
				}
			} else {
				CatFilter catFilter;
				try {
					catFilter = new CatFilter(param);					
				} catch (FileNotFoundException e) {
					// throw this not, not handle it right now
					throw new FileNotFoundException(e.getMessage());
				}
				return catFilter;			
			}
		case "head":
			if(param != null && param.indexOf(">") != 0) {			// cannot have a parameter
				throw new IllegalArgumentException(filterName);
			} else if (param != null && param.indexOf(">") == 0) {	// needs to redirect
				HeadFilter headFilter = new HeadFilter(true, param.substring(2));
				return headFilter;
			} else {				
				HeadFilter headFilter = new HeadFilter();
				return headFilter;			
			}
		case "tail":
			if(param != null && param.indexOf(">") != 0) {			// cannot have a parameter
				throw new IllegalArgumentException(filterName);
			} else if (param != null && param.indexOf(">") == 0) {	// needs to redirect
				TailFilter tailFilter = new TailFilter(true, param.substring(2));
				return tailFilter;
			} else {								
				TailFilter tailFilter = new TailFilter();
				return tailFilter;			
			}
		case "wc":
			if(param != null && param.indexOf(">") != 0) {			// cannot have a parameter
				throw new IllegalArgumentException(filterName);
			} else if (param != null && param.indexOf(">") == 0) {	// needs to redirect
				WordCountFilter wcFilter = new WordCountFilter(true, param.substring(2));
				return wcFilter;
			} else {								
				WordCountFilter wcFilter = new WordCountFilter();
				return wcFilter;			
			}
		case "grep":
			if(param == null || param.indexOf(">") == 0) {			// must have a parameter
				throw new IllegalArgumentException("param" + filterName);
			} else if (param != null && param.indexOf(">") > 0) {	// needs to redirect
				// example: test.txt > 1.txt -> [0] = test.txt, [2] = 1.txt
				GrepFilter grepFilter = new GrepFilter(true, param.split(" ")[0], param.split(" ")[2]);
				return grepFilter;
			} else {								
				GrepFilter grepFilter = new GrepFilter(param);
				return grepFilter;			
			}
		case "uniq":
			if(param != null && param.indexOf(">") != 0) {			// cannot have a parameter
				throw new IllegalArgumentException(filterName);
			} else if (param != null && param.indexOf(">") == 0) {	// needs to redirect
				UniqFilter uniqFilter = new UniqFilter(true, param.substring(2));
				return uniqFilter;
			} else {								
				UniqFilter uniqFilter = new UniqFilter();
				return uniqFilter;			
			}
		case ">":
			// > needs input
			throw new IllegalArgumentException("input" + subCommand);
		default:
			if(param != null) {
				throw new UnsupportedOperationException(filterName + " " + param);				
			} else {
				throw new UnsupportedOperationException(filterName);
			}
		}
	}

	/**
	 * link all the commands and processes them one by one
	 * 
	 * @param filters List<SequentialFilter> a list of all the filters
	 */
	private static void linkFilters(List<SequentialFilter> filters) {
		Iterator<SequentialFilter> it = filters.iterator();
		SequentialFilter prevFilter = null;
		while(it.hasNext()) {
		    SequentialFilter filter = it.next();
		    if(prevFilter != null) {
		    	// link
		    	filter.setPrevFilter(prevFilter);
		    }
		    // execute
		    filter.process();	
		    // update prevFilter to become the current one
		    prevFilter = filter;
		}
		SequentialFilter lastFilter = prevFilter;
		// if it has an output pipe
		if(lastFilter.output != null) {
			// sets and links it with a print filter to handle output
			PrintFilter finalOutput = new PrintFilter();
			lastFilter.setNextFilter(finalOutput);
			finalOutput.process();
		}
	}
	
	/**
	 * Clears the commandList and be ready to take the next line of command
	 * 
	 */
	public static void reset() {
		commandList = new LinkedList<SequentialFilter>();
	}
}
