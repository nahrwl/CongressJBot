package io.github.techietotoro;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    	XMLParser parser = new XMLParser("http://www.govtrack.us/api/v2/bill?&order_by=-introduced_date&current_status__in=introduced%7Creferred&format=xml&limit=10");
        parser.parse();
    	assertTrue( true );
    }
}
