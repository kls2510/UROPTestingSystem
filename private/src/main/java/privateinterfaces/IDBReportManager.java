package privateinterfaces;

import java.util.LinkedList;
import java.util.List;

import publicinterfaces.Report;
import publicinterfaces.Status;
import publicinterfaces.TestIDNotFoundException;
import publicinterfaces.TickNotInDBException;
import publicinterfaces.UserNotInDBException;

/**
 * Interface for adding, getting, and removing reports from a database
 * @author as2388
 */
public interface IDBReportManager {

    /**
     * Adds a report to the database, and creates a new DBUser and/or DBTick if necessary
     * @param crsId         Id of user to add report to
     * @param tickId        Id of tick of user's tick to add report to
     * @param report        Report to add
     */
    public void addReport(String crsId, String tickId, Report report);

    /**
     * Gets the last report added to a given user's tick
     * @param crsId                 Id of user to get report from
     * @param tickId                Id of tick to get report from
     * @return                      Last added report
     * @throws UserNotInDBException Thrown if user with id crsId was not found in the database
     * @throws TickNotInDBException Thrown if tick with id tickId was not found for the user with id crsId
     */
    public Report getLastReport(String crsId, String tickId) throws UserNotInDBException, TickNotInDBException;

    /**
     * Gets the status of the last report in the given user's tick
     * @param crsId     Id of user whose report is to be looked up
     * @param tickId    Id of user's tick to look up
     * @return          The status of the tick
     */
    public Status getLastStatus(String crsId, String tickId) throws UserNotInDBException, TickNotInDBException;

    /**
     * Gets all reports in a given user's tick
     * @param crsId                 Id of user to get reports from
     * @param tickId                Id of tick to get reports from
     * @return                      List of all reports in user's tick
     * @throws UserNotInDBException Thrown if user with id crsId was not found in the database
     * @throws TickNotInDBException Thrown if tick with id tickId was not found for the user with id crsId
     */
    public List<Report> getAllReports(String crsId, String tickId) throws UserNotInDBException,
            TickNotInDBException;

    /**
     * Removes a user and all his/her reports from the database
     * @param crsId                 Id of user to remove
     * @throws UserNotInDBException Thrown if the user is not in the database
     */
    public void removeUserReports(String crsId) throws UserNotInDBException;

    /**
     * Removes a user's tick and all its associated reports from the database
     * @param crsId                     Id of user whose tick is to be removed
     * @param tickId                    Id of tick to be removed
     * @throws UserNotInDBException     Thrown if the user is not in the database
     * @throws TestIDNotFoundException  Thrown if the tick for the given user is not in the database
     */
    public void removeUserTickReports(String crsId, String tickId) throws UserNotInDBException, TestIDNotFoundException;

}
