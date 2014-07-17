package TestingHarness;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

/**
 * Provides all API functions. 
 * THE EXACT FUNCTION PARAMETERS AND RETURN VALUES ARE SUBJECT TO CHANGE
 * @author as2388
 * @author kls82
 */
@Path("/testerAPI")
@Produces("application/json")
public interface TestServiceInterface {

    /**
     * Starts a new test
     * @param repoAddress				The address of the git repository to examine for .java files to 
     * 									analyse
     * @return							The ID of the test just started, to be used by the caller of this
     * 									function to access the status and result of the the test at a
     * 									later time
     * @throws IOException    			if git team can't get files in given repoAddress
     * @throws WrongFileTypeException   if file in repo is not of java or xml type
     */
    @GET
    @Path("/runNewTest")
    public abstract String runNewTest(@QueryParam("repoAddress") String repoAddress) throws IOException, WrongFileTypeException;

    /**
     * Returns the status of the test with ID testID if a test with testID exists, otherwise returns an error code
     * @param testID					ID of the test to access
     * @return							The test status of the given ID. Options are: 'loading', 'running test k of n', 'complete'
     * 										(note that 'complete' does not imply the test ran successfully)
     * @throws TestIDNotFoundException	No test exists for the given testID
     */
    @GET
    @Path("/pollStatus")
    public abstract String pollStatus(@QueryParam("testID") String testID) throws TestIDNotFoundException;

    /**
     * Gets the report associated with the testID, or throws an exception if the report couldn't be generated
     * IMPORTANT SIDE EFFECT: removes report from this servlet
     * @param testID					ID of the test to access
     * @return							A report object in JSON format.
     * @throws TestIDNotFoundException	No test exists for the given testID
     * @throws CheckstyleException		Something went wrong with CheckStyle, probably due to a bad config file
     * @throws WrongFileTypeException	A given test file is not a .xml or .java file
     * @throws TestHarnessException		TODO Will probably be replaced with a git team exception
     * 									(TestHarnessException is currently only thrown when we fail to find a file on
     * 									 the file system. We ultimately not be looking at the file system)
     */
    @GET
    @Path("/getReport")
    public Report getReport(@QueryParam("testID") String testID) throws TestIDNotFoundException, 
    CheckstyleException, 
    WrongFileTypeException, 
    TestHarnessException;

    /**
     * Test function for serialised exceptions
     * @return	a string saying "Exception not generated by git" if no exception was generated
     */
    @GET
    @Path("/getException")
    public String getException() /*TODO: find out what exception will be thrown by the git team*/;
}
