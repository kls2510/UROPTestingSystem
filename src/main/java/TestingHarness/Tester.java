package TestingHarness;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * Runs all the static and dynamic analysis tests for a given tick, and produces a report,
 * stored in memory until the object is destroyed
 * 
 * @author as2388
 * @author kls2510
 *
 */
public class Tester {
	static Logger log = Logger.getLogger(Tester.class.getName());		//initialise log4j logger
	
	private List<sReportItem> sReport = new LinkedList<sReportItem>();	//list of static report items
	private List<dReportItem> dReport = new LinkedList<dReportItem>();  //list of dynamic report items
	private Report report;												//Report object into which all the report items will ultimately go
	private final String dir = System.getProperty("user.dir");			//TODO: remove hard-coded path
	
	//instantiated in constructor when joined with other project
	private String crsid = "eg1";										//TODO: find out why we need this
	
	//Maps a test (either static or dynamic) to a list of files on which that test should be run
	private Map<String, LinkedList<String>> testingQueue = new HashMap<String, LinkedList<String>>();
	
	//temporary
	public static void main(String[] args) {
		new Tester();
	}
	
	/**
	 * Creates a new Tester, then immediately starts running hard-coded tests
	 */
	public Tester(/* some class */) {
		/*
		 * WHEN WE RECIEVE CLASS:
		 * this.crsid = class.getCrsid;
		 * this.testingQueue = class.getTests();
		 */
		
		runTester();
	}
	
	/**
	 * Runs all tests required by the tick on all files required to be tested by the tick
	 */
	private void runTester()
	{
		log.info("Tick analysis started");
		
		//temporary files used to test this works
		LinkedList<String> ll = new LinkedList<String>();
		// may need: /src/main/resources/
		//ll.add("C:\\ResourcesForUROP/TestResource.java");
		//ll.add("C:\\ResourcesForUROP/TestResource2.java");
		
		ll.add(dir + "\\src\\test\\resources\\PackedLong.java");
		testingQueue.put(dir + "\\src\\main\\resources\\CheckstyleFormat.xml", ll);
		//testingQueue.put("C:\\ResourcesForUROP/CheckstyleFormat.xml",ll);
		
		try {
			//loop through each test, decide what type of test it is and run it, adding the result to outputs
			for (Map.Entry<String, LinkedList<String>> e : testingQueue.entrySet()) {
				String testFileName = e.getKey();
				LinkedList<String> fileNames = e.getValue();
				
				String ext = testFileName.substring(testFileName.lastIndexOf('.') + 1, testFileName.length());
				
				if ("xml".equals(ext)) {
					//run static analysis tests
					runStaticAnalysis(testFileName, fileNames);
				}
				else if ("java".equals(ext)) {
					//TODO: run dynamic analysis tests
				}
				else { 
					throw new WrongFileTypeException(); //TODO: maybe change this to unexpected file type exception. Do even need to throw an exception?
				} 
			} 
			//Once the for loop is complete, all tests to be run have finished
			log.info("Tick analysis finished successfully");
			
			//build the final report from the static and dynamic results
			this.report = new Report(sReport, dReport);
			
			//TODO: remove this. For now, print result to the console
			printReport();
		}		
		//TODO: change report status and return it such that the error encountered is obvious
		catch (CheckstyleException err){
			Report report = new Report(err.getMessage());
			log.error("Tick analysis failed. CheckstyleaException message: " + err.getMessage());
			this.report = report;
		}
		catch (WrongFileTypeException err) {
			Report report = new Report(err.getMessage());
			log.error("Tick analysis failed. WrongFileTypeException message: " + err.getMessage());
			this.report = report;
		} 
		catch (TestHarnessException err) {
			System.out.println();
			Report report = new Report(err.getMessage());
			log.error("Tick analysis failed. TestHarnessException message: " + err.getMessage());
			this.report = report;
		} 
	}
	
	private void printReport()
	{
		//print the overall test result
		System.out.println("Your result: " + report.getResult());
		System.out.println();
		
		//Print each error
		for(sReportItem i : report.getStaticResults()){
			System.out.print(i.getSeverity() + ": file " + i.getFileName() + "  at line(s) "); 
			for(int l : i.getLineNumbers()) {
				System.out.print(l + ", ");		//print the line numbers of ass the instances on which this particular error was found
			}
			System.out.println(i.getMessage());
		}		
	}
	
	/**
	 * Run CheckStyle, set up with the given config file, on all the files to which it should be applied
	 * 
	 * @param configFileName		Path to the config file needed by CheckStyle
	 * @param fileNames				A list of paths to the files on which the static analyses tests are to be performed
	 * @throws CheckstyleException	
	 * @throws TestHarnessException	
	 */
	public void runStaticAnalysis(String configFileName, List<String> fileNames) throws CheckstyleException, TestHarnessException {
		for (String file : fileNames) {
			StaticParser.test(configFileName, file, this.sReport);
		}
	}
	
	//temporary to get data to html file
	public static Report getReport() {
		Tester t = new Tester();
		return t.report;
	}
}
