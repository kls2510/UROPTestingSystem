package publicinterfaces;

import exceptions.TestIDNotFoundException;
import exceptions.TestStillRunningException;
import exceptions.WrongFileTypeException;
import gitapidependencies.HereIsYourException;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import reportelements.Report;
import reportelements.Status;

import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
/**
 * Provides all API functions. 
 * @author as2388
 * @author kls82
 */
@Path("/testerAPI")
@Produces("application/json")
public interface TestServiceInterface {

    /**
     * Starts a new test
     * @param repoName  				The name of the git repository to examine for .java files to 
     * 									analyse
     * @return							The ID of the test just started, to be used by the caller of this
     * 									function to access the status and result of the the test at a
     * 									later time
     * @throws IOException    			if git team can't get files in given repoName
     * @throws WrongFileTypeException   if file in repo is not of java or xml type
     */
    @GET
    @Path("/runNewTest")
    public abstract String runNewTest(@QueryParam("repoName") String repoName) throws IOException, WrongFileTypeException;

    /**
     * Returns the status of the test with ID testID if a test with testID exists, otherwise returns an error code
     * @param testID					ID of the test to access
     * @return							The test status of the given ID. Options are: 'loading', 'running test k of n', 'complete'
     * 										(note that 'complete' does not imply the test ran successfully)
     * @throws TestIDNotFoundException	No test exists for the given testID
     */
    @GET
    @Path("/pollStatus")
    public abstract Status pollStatus(@QueryParam("testID") String testID) throws TestIDNotFoundException;

    /**
     * Gets the report associated with the testID, or throws an exception if the report couldn't be generated
     * IMPORTANT SIDE EFFECT: Unless TestStillRunningException is thrown, removes report from this servlet
     * @param testID					    ID of the test to access
     * @return							    A report object in JSON format.
     * @throws TestIDNotFoundException      No test exists for the given testID
     * @throws CheckstyleException		    Something went wrong with CheckStyle, probably due to a bad config file, but
     *                                      could be a missing file; check the exception's message
     * @throws WrongFileTypeException	    A given test file is not a .xml or .java file
     * @throws TestStillRunningException    The test isn't complete, so there is no report to remove
     * @throws IOException                  Git API threw IOException when getting a file or TestService couldn't create and
     *                                      use temporary files
     */
    @GET
    @Path("/getReport")
    public Report getReport(@QueryParam("testID") String testID) throws TestIDNotFoundException, 
                                                                        CheckstyleException, 
                                                                        WrongFileTypeException, 
                                                                        TestStillRunningException,
                                                                        IOException;

    /**
     * Test function for serialised exceptions
     * @return	a string saying "Exception not generated by git" if no exception was generated
     */
    @GET
    @Path("/getException")
    public String getException() throws HereIsYourException;
    
    
    /**Used by interface authors for occasional manual testing. May change behaviour seemingly at random, do not use */
    @Deprecated
    @GET
    @Path("/test")
    public String test(@QueryParam("testID") String testID) throws TestIDNotFoundException, HereIsYourException;
}