package TestingHarness;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * Provides all API functions. 
 * THE EXACT FUNCTION PARAMETERS AND RETURN VALUES ARE SUBJECT TO CHANGE
 * @author as2388
 */
@Path("/testerAPI")
@Produces("application/json")
public interface TestServiceInterface {
	
	/**
	 * Starts a new test
	 * @param repoAddress			The address of the git repository to examine for .java files to 
	 * 								analyse
	 * @return						The ID of the test just started, to be used by the caller of this
	 * 								function to access the status and result of the the test at a
	 * 								later time
	 */
	@GET
	@Path("/runNewTest")
	public abstract String runNewTest(@QueryParam("repoAddress") String repoAddress) throws IOException;
	
	/**
	 * Returns the status of the test with ID testID if a test with testID exists, otherwise returns an error code
	 * @param testID					ID of the test to access
	 * @return							The test status of the given ID. Options are: running, complete, TODO
	 * @throws TestIDNotFoundException	
	 */
	@GET
	@Path("/pollStatus")
	public abstract String pollStatus(@QueryParam("testID") String testID) throws TestIDNotFoundException;
	
	/**
	 * Gets the report associated with the testID, or throws an exception if the report couldn't be generated
	 * IMPORTANT SIDE EFFECT: removes report from this servlet
	 * @param testID					ID of the test to access
	 * @return							A report object in JSON format.
	 * @throws TestIDNotFoundException	
	 * @throws CheckstyleException		Something went wrong with CheckStyle, probably due to a bad config file
	 * @throws WrongFileTypeException	A test 
	 */
	@GET
	@Path("/getReport")
	public Report getReport(@QueryParam("testID") String testID) throws TestIDNotFoundException, 
																		CheckstyleException, 
																		WrongFileTypeException, 
																		TestHarnessException;
	
	//TODO: remove
	@GET
	@Path("/test")
	public abstract String test(@QueryParam("testID") String testID) throws TestIDNotFoundException;
}
