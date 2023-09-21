package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.FileNotFoundException;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;

/**
* This class provides the implementation of a working 
* directory filter. It can get the current working directory.
* A working directory filter cannot have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class WorkingDirectoryFilter extends SequentialFilter {
	
	/**
	 * The String instance to store the current working directory
	 */
	private String workingDirectory;
	
	/**
	 * The boolean flag to determine whether this filter needs
	 * to have a redirect filter
	 */
	private boolean flag;
	
	/**
	 * Used for redirect filter to redirect the output to this <file>
	 */
	private String fileName;
	
	/**
	 * Constructs a new working directory filter that needs to be redirected
	 * 
	 * @param flag		boolean	the flag determine whether this filter 
	 *                          needs to have a redirect filter
	 * @param fileName	String	the <file> that the redirect filter writes
	 *                          its output	
	 */
	public WorkingDirectoryFilter(Boolean flag, String fileName) {
		workingDirectory = CurrentWorkingDirectory.get();
		output = new Pipe();
		this.flag = flag;
		// uses substring because, for example, the param is "> test.txt"
		this.fileName = fileName.substring(2);
	}
	
	/**
	 * Constructs a new working directory filter
	 * 
	 */
	public WorkingDirectoryFilter() {
		workingDirectory = CurrentWorkingDirectory.get();
		output = new Pipe();
	}
	
	/**
	 * Passes the result to the output pipe
	 */
	public void process() {
		if(input != null) {
			throw new IllegalArgumentException("pwd");
		}
		output.write(workingDirectory);
		// creates a redirect filter and link it if flag == true
		if(flag == true) {
			SequentialFilter redirect = new RedirectFilter(fileName);
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
