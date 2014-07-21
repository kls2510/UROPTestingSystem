package testingharness;

import java.util.LinkedList;
import java.util.Map;

/** Used to generate Tester objects.
 * Enables mocking of Tester in TestService.runNewTest()
 * @author as2388
 */
public class TesterFactory {
    public Tester createNewTester(Map<String, LinkedList<String>> arg0, String arg1)
    {
        return new Tester(arg0, arg1);
    }
}
