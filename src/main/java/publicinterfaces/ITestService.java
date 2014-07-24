package publicinterfaces;

import exceptions.*;
import gitapidependencies.RepositoryNotFoundException;
import reportelements.AbstractReport;
import reportelements.Status;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.List;

@Path("/testerAPI/v2/")
@Produces("application/json")
public interface ITestService {

    /**
     * Starts a new test.
     * @param crsId                         Id of user for whom test is to be run
     * @param tickId                        Id of tick for which test is
     * @param repoName                      Name of git repository to access to obtain the student's code
     * @param commitId                      TODO: why do we need this? Can't we obtain this from git? (which is
     *                                      TODO: essentially what the ticking team would have to do on our behalf otherwise)
     * @throws TestStillRunningException    Thrown if the user already has a submission processing for this tick
     * @throws IOException                  Thrown by git API
     * @throws RepositoryNotFoundException  Thrown if the repository allegedly containing the student's code couldn't be
     *                                      found
     * @throws WrongFileTypeException
     * @throws TestIDNotFoundException
     */
    @GET //TODO: change to POST (GET is being used for testing)
    @Path("/{crsId}/{tickId}/{repoName}")
    public void runNewTest(@PathParam("crsId") String crsId, @PathParam("tickId") String tickId,
                           @PathParam("repoName") String repoName, @QueryParam("commitId") String commitId)
            throws TestStillRunningException, IOException, RepositoryNotFoundException, WrongFileTypeException,
            TestIDNotFoundException;

    //TODO: should we also do a GET for this?

    /**
     * TODO: finish JavaDoc
     * Returns the status of the latest test running/ran for the given user's tick
     * @param crsId                Id of user whose test should be polled
     * @param tickId               Id of tick whose latest test should be polled
     * @return                     Current status of test
     * @throws NoSuchTestException Thrown if no test has been started for this user/tick combination
     */
    @GET
    @Path("{crsId}/{tickId}/poll")
    public Status pollStatus(@PathParam("crsId") String crsId, @PathParam("tickId") String tickId)
            throws NoSuchTestException;

    /**
     * TODO: finish JavaDoc
     * Returns the most-recently generated report.
     * Note: this function returns the most recently generated report for a given crsID and tickID, not the report
     * associated with the newest commit, because these are not expected to be different.
     * @param crsId
     * @param tickId
     * @return
     * @throws TickNotInDBException 
     * @throws UserNotInDBException 
     */
    @GET
    @Path("/{crsId}/{tickId}/last")
    public AbstractReport getLastReport(@PathParam("crsId") String crsId, @PathParam("tickId") String tickId)
            throws UserNotInDBException, TickNotInDBException;

    @GET
    @Path("/{crsId}/{tickId}/all")
    public List<AbstractReport> getAllReports(@PathParam("crsId") String crsId, @PathParam("tickId") String tickId)
            throws UserNotInDBException, TickNotInDBException;

    @DELETE
    @Path("/{crsId}")
    public void deleteStudentReportData(@PathParam("crsId") String crsId) throws UserNotInDBException;

    @DELETE
    @Path("/{crsId}/{tickId}")
    public void deleteStudentTick(@PathParam("crsId") String crsId, @PathParam("tickId") String tickId)
            throws TestIDNotFoundException, UserNotInDBException;

    @GET
    @Path("/{tickId}/create")
	public void createNewTest(@PathParam("tickId") String tickId /* , List<XMLTestSettings> checkstyleOpts */)
            throws TestIDAlreadyExistsException;

    //TODO: cancel test?
}
