package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.File;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;
import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
* This class provides the implementation of a ls filter.
* It can list the files in the current working directory.
* A ls filter cannot have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class ListFilter extends SequentialFilter {
	
	/**
	 * Used for creating a File object with current working directory
	 */
	private File workingDirectory;
	
	/**
	 * Used for redirect filter to redirect the output to this <file>
	 */
	private String redirectFileName;
	
	/**
	 * The boolean flag to determine whether this filter needs
	 * to have a redirect filter
	 */
	private boolean flag;
	
	/**
	 * Constructs a new ls filter
	 * 
	 */
	public ListFilter() {
		workingDirectory = new File(CurrentWorkingDirectory.get());
		output = new Pipe();
	}
	
	/**
	 * Constructs a new ls filter that needs to be redirected
	 * 
	 * @param flag				boolean	the flag determine whether this filter 
	 *                          		needs to have a redirect filter
	 * @param redirectFileName	String	the <file> that the redirect filter writes
	 *                          		its output	
	 */
	public ListFilter(Boolean flag, String redirectFileName) {
		workingDirectory = new File(CurrentWorkingDirectory.get());
		output = new Pipe();
		this.flag = flag;
		this.redirectFileName = redirectFileName.substring(2);			
	}
	
	/**
	 * Obtains and writes all the names of directories and files
	 * within current working directory to the output pipe
	 */
	public void process() {	
		// cannot have input
		if(input != null) {
			throw new IllegalArgumentException("ls");
		}
		for(String file: workingDirectory.list()) {		// get all the names of directories and files within current working directory		
			output.write(file);
		}
		// creates a redirect filter and link it if flag == true
		if(flag == true) {
			SequentialFilter redirect = new RedirectFilter(redirectFileName);
			this.setNextFilter(redirect);
			redirect.process();
			output = null;
		}
	}
	
	/**
	 * Does nothing. Implemented here due to abstraction of parent class
	 * 
	 * @param line String not used
	 * @return ""
	 */
	public String processLine(String line) {
		return "";
	}
	
}
