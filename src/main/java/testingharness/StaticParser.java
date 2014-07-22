package testingharness;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import gitapidependencies.RepositoryNotFoundException;
import gitapidependencies.WebInterface;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reportelements.StaticReportItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/* import uk.ac.cam.cl.git.public_interfaces.WebInterface; */

/**
 * Provides function for running Checkstyle with a given config file and .java file
 * @author kls82
 *
 */
public class StaticParser {
    static Logger log = LoggerFactory.getLogger(StaticParser.class);

    /**
     * Runs Checkstyle with a given config file and .java file, and puts results into a linked list of static report items
     * @param test                      Path to Checkstyle configuration file to use
     * @param file                      Path to .java file on which to run Checkstyle
     * @param sReport                   Reference to LinkedList of StaticReportItems into which to insert found problems
     * @param repoName                  Name of git repository in which {@code file} is located
     * @throws CheckstyleException      
     * @throws IOException              
     */
    public static void test(String test, String file, List<StaticReportItem> sReport, String repoName) throws CheckstyleException, IOException, RepositoryNotFoundException{
        //must be in list for .process to work
        LinkedList<File> fileList = new LinkedList<>();

        //read contents of file from git and store in a temporary file
        ResteasyClient rc = new ResteasyClientBuilder().build();
        		
        ResteasyWebTarget t = rc.target(configuration.ConfigurationLoader.getConfig().getGitAPIPath());
        WebInterface proxy = t.proxy(WebInterface.class);
        String contents = proxy.getFile(file, repoName);

        File javaFile = File.createTempFile(file.substring(0,file.lastIndexOf(".")),".java"); 
        log.info("file temporarily stored at: " + javaFile.getAbsolutePath());

        //write string to temp file
        log.info("writing data to " + javaFile.getAbsolutePath());
        FileOutputStream output = new FileOutputStream(javaFile.getAbsolutePath());
        byte[] bytes = contents.getBytes();
        output.write(bytes);
        output.flush();
        output.close();

        if (javaFile.exists()){
            fileList.add(javaFile);
        }
        else {
            throw new IOException("Could not find file: " + file);
        }

        //test the java file and use the listener to add each line with an error
        //in it to the linked list of static report items
        try {
            log.info("Testing: " + javaFile.getAbsolutePath());
            Configuration config = ConfigurationLoader.loadConfiguration(
                    configuration.ConfigurationLoader.getConfig().getGitAPIPath() 
                    + "git/" + repoName + ".git/" + test, 
                    new PropertiesExpander(System.getProperties()));
            AuditListener listener = new StaticLogger(sReport,file);
            Checker c = createChecker(config, listener); 
            c.process(fileList); 
            c.destroy();
            log.info("Finished");
        }
        finally {
            //try to delete the temp file which was created
            if( javaFile.delete()) {
                log.info("Deleted temp file: " + javaFile.getAbsolutePath());
            }
            else {
                log.error("Failed to delete temp file: " + javaFile.getAbsoluteFile());
            }

            //Close the rest easy client
            rc.close();
        }
    }

    private static Checker createChecker(Configuration config, AuditListener listener) throws CheckstyleException {
        Checker c = null; 

        try {
            c = new Checker();
        } 
        catch (CheckstyleException e) {
            System.out.println(e.getMessage());
        } 
        final ClassLoader moduleClassLoader = Checker.class.getClassLoader(); 
        c.setModuleClassLoader(moduleClassLoader); 
        c.configure(config);
        c.addListener(listener); 

        return c; 
    }
} 