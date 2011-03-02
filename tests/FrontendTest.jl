import java.util.StringTokenizer;
import java.util.NoSuchElementException;

public class FrontendTest {
    Object labelMe;
    public void giveMeABeginLabel(String x) { }
    public Object[] arrayField = null;
    public void warnMeBecauseImBad() {
	Class c = FrontendTest.class;
    }
    public void alsoMightHaveSomeProblems() {
	StringTokenizer s = new StringTokenizer("whatevs");
	try {
	    s.nextToken();
	} catch (NoSuchElementException e) {
	}
    }
}
