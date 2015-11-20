/**
 * 
 */
package com.andriir.embeddedTomcat;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Tests the page in which the student list is displayed.
 * 
 * @author hostettler
 */
public class RunAccloudTest extends AbstractEmbeddedTomcatTest {

    /** The class logger. */
    private static final Logger LOGGER = Logger.getLogger(AbstractEmbeddedTomcatTest.class);

	/**
	 * Test a simple workflow. It verifies that the student list does return something useful.
	 *
	 * @throws Exception
	 *             if anything goes wrong
	 */
	@Test
	public void testListStudents() throws Exception {
        LOGGER.info("HELLO from testListStudents test!!!");
//		WebDriver driver = new HtmlUnitDriver();
//		driver.get(getAppBaseURL());
//
//		Assert.assertTrue(driver.getPageSource().contains("Steve Hostettler"));
//		WebElement element = driver.findElement(By.xpath("//button[contains(@id,'register')]"));
//		Assert.assertNotNull(element);
//		element.click();
//
//		Assert.assertTrue(driver.getPageSource().contains("Enregistrer un "));
	}


	/** Where the web sources are. */
	private static final String WEBAPP_SRC = "src/main/webapp";

    @Override
	protected String getApplicationId() {
		return "root";
	}
}
