package edu.brandeis.cs.cs131.pa1.filter.sequential;

import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

import edu.brandeis.cs.cs131.pa1.filter.CurrentWorkingDirectory;

/**
* This class provides the implementation of a cd filter. It can change
* the working directory. A cd filter cannot have input or output.
*
* @author Qiuyang Wang 
* qiuyangwang@brandeis.edu 
* 9/18/2023
* COSI 131A PA1
*/
public class ChangeDirectoryFilter extends SequentialFilter {
	
	/**
	 * Used for creating a File object with current working directory
	 */
	private File workingDirectory;
	
	/**
	 * Used for creating a String object with targeted working directory
	 */
	private String targetWorkingDirectory;
	
	/**
	 * Stores the regular expression of "\"
	 */
	private String fileSeparator = CurrentWorkingDirectory.getPathSeparator(true);
	
	/**
	 * Stores sub-directories in a queue for chaining of directory movement
	 */
	private Queue<String> subDirectories = new LinkedList<String>();
	
	/**
	 * Used to check whether the specified dir exists
	 */
	private boolean flag;
	
	/**
	 * Constructs a new cd filter
	 * 
	 * @param param String example: dir1/dir2/dir3
	 */
	public ChangeDirectoryFilter(String param) {
		flag = true;
		this.targetWorkingDirectory = param;
		workingDirectory = new File(CurrentWorkingDirectory.get());
		String[] directories = param.split(fileSeparator);
		for(String directory: directories) {
			subDirectories.add(directory);
		}
	}
	
	/**
	 * Processes the parameter and then cd into the specified working directory
	 * (cd . and cd .. supported)
	 * 
	 */
	public void process() {
		// cannot have input
		if(input != null) {
			throw new IllegalArgumentException("cd");
		}
		while(!subDirectories.isEmpty()) {
			flag = true;
			for(String file: workingDirectory.list()) {			
				String dir = subDirectories.peek();
				// cd . (cd into current dir)
				if(dir.equals(".")) {
					flag = false;
					subDirectories.poll();
					break;
				} else if(dir.equals("..")) {		// cd .. (cd into parent dir)
					// get the index number of last "\"
					int last = CurrentWorkingDirectory.get().lastIndexOf(CurrentWorkingDirectory.getPathSeparator());
					// set to parent dir
					CurrentWorkingDirectory.setTo(CurrentWorkingDirectory.get().substring(0, last));
					// update current working directory
					workingDirectory = new File(CurrentWorkingDirectory.get());
					flag = false;
					subDirectories.poll();
					break;
				}
				// locates the targeted dir
				if(dir.equals(file)) {
					processLine(subDirectories.poll());
					flag = false;
					break;
				}
			}
			// if flag == true, it means specified dir is not existed
			if(flag) {
				throw new IllegalArgumentException("divcd " + subDirectories.peek());				
			}
		}
		
	}
	
	/**
	 * Changes working directory to the child dir and then update current working directory
	 * 
	 * @param line String the child dir within the current working directory
	 * @return ""
	 */
	public String processLine(String line) {
		// uses absolute path
		// set to targeted dir
		CurrentWorkingDirectory.setTo(CurrentWorkingDirectory.get() + CurrentWorkingDirectory.getPathSeparator() + line);
		// update current working directory
		workingDirectory = new File(CurrentWorkingDirectory.get());
		return "";
	}
	
}
