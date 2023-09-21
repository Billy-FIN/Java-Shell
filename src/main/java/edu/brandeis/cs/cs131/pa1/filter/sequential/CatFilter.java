package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;
import edu.brandeis.cs.cs131.pa1.filter.Filter;
import edu.brandeis.cs.cs131.pa1.filter.Message;

/**
* This class provides the implementation of a cat filter. It can read
* a file's content. A cat filter cannot have input.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class CatFilter extends SequentialFilter {
	
	/**
	 * The Scanner instance to read the file
	 */
	private Scanner fileContent;
	
	/**
	 * The String instance to store the <file> parameter
	 */
	private String fileName;
	
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
	 * Constructs a new cat filter that reads a certain file
	 * 
	 * @param fileName	String	the name of the file needed to be read
	 * @throws FileNotFoundException
	 */
	public CatFilter(String fileName) throws FileNotFoundException {
		output = new Pipe();
		this.fileName = fileName;
		try {
			// absolute path - will not have FileNotFoundExceptions after cd 
			fileContent = new Scanner(new File(CurrentWorkingDirectory.get() + CurrentWorkingDirectory.getPathSeparator() + fileName));			
		} catch (FileNotFoundException e) {
			// don't handle the exception here and throw it out
			throw new FileNotFoundException("cat " + fileName);
		}
	}
	
	/**
	 * Constructs a new cat filter that needs to be redirected
	 * 
	 * @param fileName			String	the name of the file needed to be read
	 * @param flag				boolean	the flag determine whether this filter 
	 *                                  needs to have a redirect filter
	 * @param redirectFileName	String	the <file> that the redirect filter writes
	 *                                  its output
	 * @throws FileNotFoundException
	 */
	public CatFilter(Boolean flag, String fileName, String redirectFileName) throws FileNotFoundException {
		this.flag = flag;
		this.fileName = fileName;
		this.redirectFileName = redirectFileName;
		output = new Pipe();
		try {
			// absolute path - will not have FileNotFoundExceptions after cd 
			fileContent = new Scanner(new File(CurrentWorkingDirectory.get() + CurrentWorkingDirectory.getPathSeparator() + fileName));			
		} catch (FileNotFoundException e) {
			// don't handle the exception here and throw it out
			throw new FileNotFoundException("cat " + fileName);
		}
	}
	
	/**
	 * Processes the parameter and passes the result to the output pipe.
	 * 
	 */
	public void process() {
		processLine(fileName);
		// creates a redirect filter and link it if flag == true
		if(flag == true) {
			SequentialFilter redirect = new RedirectFilter(redirectFileName);
			this.setNextFilter(redirect);
			// execute the work of redirect filter
			redirect.process();
			output = null;
		}
	}
	
	/**
	 * Reads the file content line by line and then write into
	 * the output pipe.
	 * 
	 * @param line String
	 * @return null
	 */
	public String processLine(String line) {
		while(fileContent.hasNextLine()) {
			String text = fileContent.nextLine();
			output.write(text);				
		}
		fileContent.close();
		return null;
	}
}
