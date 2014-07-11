package TestingHarness;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Provides all API functions. 
 * THE EXACT FUNCTION PARAMETERS AND RETURN VALUES ARE SUBJECT TO CHANGE
 * @author as2388
 */
@Path("/API") //full path to here is /tester/API/
public class TestService
{
	/* Maps the ID of a test to in-progress tests. 
	 * TestService is responsible for generating unique IDs
	 * Class user's are responsible for remembering the ID so that they can poll its status and get its report when done */
	private static Map<String, Tester> ticksInProgress;	//TODO: should we be keeping these in a DB instead?
	private int notFoundCode = 404;						//TODO: investigate whether this is the best status code to be returning
	
	private TestService()
	{
		if (ticksInProgress == null)
		{
			ticksInProgress = new HashMap<String, Tester>();
		}
	}
	
	/**
	 * Starts a new test
	 * @param serializedTestData	A map containing paths to unit tests/test configs, 
	 * 								and paths to the files on which to run the tests,
	 * 								which we probably have to deserialize from JSON
	 * @return						The ID of the test just started, to be used by the caller of this
	 * 								function to access the status and result of the the test at a
	 * 								later time
	 */
	@POST
	@Path("/runNewTest")
	public Response runNewTest(@QueryParam("testData") String serializedTestData)
	{
		//TODO: deserialise the parameter to a Map.
		//for now:
		Map<String, LinkedList<String>> tests = new HashMap<String, LinkedList<String>>();
		
		//create a new Tester object
		Tester tester = new Tester(tests);
		
		//TODO: generate unique ID. For now, all tests have ID 0
		String id="0";
		
		//add the object to the list of in-progress tests
		ticksInProgress.put(id, tester);
		
		//start the test
		tester.runTests();
		
		//return status ok and the id of the 	
		return Response.status(200).entity(id).build();
	}
	
	
	/**
	 * Returns the status of the test with ID testID if a test with testID exists, otherwise returns an error code
	 * @param testID	ID of the test to access
	 * @return			If the test was found: HTTP status code 200, and a string containing the status,
	 * 										     either TODO e.g. waiting, started, completed, error
	 * 					  Else: 			   HTTP status code 404
	 */
	@POST
	@Path("/pollStatus")
	@Produces("text/plain")
	public Response pollStatus(@QueryParam("testID") String testID)
	{
		if (ticksInProgress.containsKey(testID))
		{
			return Response.status(200).entity(ticksInProgress.get(testID).getReport().getStatus()).build();
		}
		else
		{
			return Response.status(notFoundCode).build();
		}
	}
	
	/**
	 * Gets the report associated with the testID.
	 * @param testID	ID of the test to access
	 * @return			A report object in JSON format
	 */
	@POST
	@Path("/getReport")
	@Produces("application/json")
	public Response getReport(@QueryParam("testID") String testID)
	{
		if (ticksInProgress.containsKey(testID))
		{
			Report toReturn = ticksInProgress.get(testID).getReport();
			//Assuming we're not responsible for storing tests, we should remove the test at this point
			ticksInProgress.remove(testID);
			return Response.status(200).entity(toReturn).build();
		}
		else
		{
			return Response.status(notFoundCode).build();
		}	
	}
}