package edu.brandeis.cs.cs131.pa1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.junit.Test;

import edu.brandeis.cs.cs131.pa1.filter.Message;
import edu.brandeis.cs.cs131.pa1.filter.sequential.SequentialREPL;

public class RedirectionTests extends GenericSequentialTests {

	@Test
	public void testCatRedirected() {
		testInput("cat hello-world.txt > new-hello-world.txt\nexit");
		SequentialREPL.main(null);
		assertFileContentsEquals("new-hello-world.txt", "hello\nworld\n");
		assertOutput(Message.NEWCOMMAND.toString());
		AllSequentialTests.destroyFile("new-hello-world.txt");
	}

	@Test
	public void testComplexRedirection() {
		testInput("cat fizz-buzz-10000.txt | grep F | wc > trial-file.txt\nexit");
		SequentialREPL.main(null);
		assertFileContentsEquals("trial-file.txt", "3334 3334 16004\n");
		assertOutput(Message.NEWCOMMAND.toString());
		AllSequentialTests.destroyFile("trial-file.txt");
	}

	/**
	 * Tests if redirection works when user has cd'd into a directory. This was
	 * rewritten to provide better feedback to students who fail the test (i.e. not
	 * using Set's toString()).
	 * 
	 * @author Chami Lamelas
	 */
	@Test
	public void testDirectoryShiftedRedirection() throws FileNotFoundException {
		testInput("cd dir1\nls > folder-contents.txt\nexit");
		SequentialREPL.main(null);

		File f = new File("dir1/folder-contents.txt");
		Set<String> actual = new HashSet<String>();

		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				actual.add(line);
			}

			assertTrue("The directory dir2 should be listed in folder-contents.txt.", actual.contains("dir2"));
			assertTrue("The file f1.txt should be listed in folder-contents.txt", actual.contains("f1.txt"));
			if (actual.size() > 2) {
				assertTrue(
						"If folder-contents has more than 2 lines, it should have only 3 with the third line being folder-contents.txt.",
						actual.size() == 3 && actual.contains("folder-contents.txt"));
			}

			sc.close();
		} catch (Exception e) {
			throw new FileNotFoundException("The dir1/folder-contents.txt file was not found");
		}

		assertOutput(Message.NEWCOMMAND.toString() + Message.NEWCOMMAND.toString());
		AllSequentialTests.destroyFile("dir1/folder-contents.txt");
	}

	private static void assertFileContentsEquals(String fileName, String expected) {
		File f = new File(fileName);
		try {
			Scanner scan = new Scanner(f);
			String result = "";
			while (scan.hasNextLine()) {
				result += scan.nextLine() + "\n";
			}
			scan.close();
			assertEquals(expected, result);
		} catch (FileNotFoundException e) {
			assertTrue(false);
		}
	}

}
