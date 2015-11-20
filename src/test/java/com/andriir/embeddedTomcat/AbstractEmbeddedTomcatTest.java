/**
 * 
 */
package com.andriir.embeddedTomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.core.AprLifecycleListener;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import java.io.File;

/**
 * This abstract class prepares a tomcat server to do integration testing.
 * 
 * @author hostettler
 */
public abstract class AbstractEmbeddedTomcatTest {

	/** The tomcat instance. */
	private Tomcat siteTomcat;
	private Tomcat manageTomcat;
	/** The temporary directory in which Tomcat and the app are deployed. */
	private String siteWorkingDir = System.getProperty("java.io.tmpdir") + "site\\";
	private String manageWorkingDir = System.getProperty("java.io.tmpdir") + "manage\\";
	/** The class logger. */
	private static final Logger LOGGER = Logger.getLogger(AbstractEmbeddedTomcatTest.class);

	/**
	 * Stops the tomcat server.
	 * 
	 * @throws Throwable
	 *             if anything goes wrong.
	 */
	@Before
	public final void setup() throws Throwable {

        // Site Tomcat
		siteTomcat = initTomcat(siteWorkingDir, 8080);
		LOGGER.info("Start Site Tomcat ...");
        siteTomcat.start();

        // Manage Tomcat
        manageTomcat = initTomcat(manageWorkingDir, 8081);
        LOGGER.info("Start Manage Tomcat ...");
        manageTomcat.start();
        siteTomcat.getServer().await();
	}

    private Tomcat initTomcat(String workingDir, int portNumber) {
        LOGGER.info(String.format("Creates Tomcat: Port=%d, Working Dir=%s", portNumber, workingDir));
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(portNumber);
        tomcat.setBaseDir(workingDir);
//		tomcat.getHost().setName("localhost");
        tomcat.getHost().setAppBase(workingDir);
        tomcat.getHost().setAutoDeploy(true);
        tomcat.getHost().setDeployOnStartup(true);

        // Add AprLifecycleListener
        StandardServer server = (StandardServer) tomcat.getServer();
        AprLifecycleListener listener = new AprLifecycleListener();
        server.addLifecycleListener(listener);

        LOGGER.info("Prepares and adds the web app");
        String contextPath = "/" + getApplicationId();
        File webApp = new File(workingDir, getApplicationId());
//		File oldWebApp = new File(webApp.getAbsolutePath());
//		FileUtils.deleteDirectory(oldWebApp);
//		new ZipExporterImpl(createWebArchive()).exportTo(new File(siteWorkingDir + "/" + getApplicationId() + ".war"),
//                true);

        tomcat.addWebapp(null, "", webApp.getAbsolutePath());
//        tomcat.addWebapp(contextPath, appBase);
//        Context webContext = tomcat.addWebapp(contextPath, webApp.getAbsolutePath());
//        Context webContext = tomcat.addWebapp(tomcat.getHost(), contextPath, webApp.getAbsolutePath());
//        webContext.getServletContext().setAttribute(Globals. ALT_DD_ATTR, siteWorkingDir + "web.xml");

        LOGGER.info("Init users and roles");
//		tomcat.addUser("admin", "admin");
//		tomcat.addUser("user", "user");
//		tomcat.addRole("admin", "admin");
//		tomcat.addRole("admin", "user");
//		tomcat.addRole("user", "user");

        return tomcat;
    }

    //    @Before
    public final void setup2() throws Throwable {
        LOGGER.info(String.format("Tomcat's base directory : %s", siteWorkingDir));

//        LOGGER.info("Creates a new server...");
//        tomcat = new Tomcat();
//        tomcat.setPort(8080);
//
//        tomcat.setBaseDir(siteWorkingDir);
//        tomcat.getHost().setName("localhost");
//        tomcat.getHost().setAppBase(siteWorkingDir);
//        tomcat.getHost().setAutoDeploy(true);
//        tomcat.getHost().setDeployOnStartup(true);
//
//        LOGGER.info("Prepares and adds the web app");
//        String contextPath = "/" + getApplicationId();
//        File webApp = new File(siteWorkingDir, getApplicationId());
//		File oldWebApp = new File(webApp.getAbsolutePath());
//		FileUtils.deleteDirectory(oldWebApp);
//        tomcat.addWebapp(tomcat.getHost(), contextPath, webApp.getAbsolutePath());
//
//        tomcat = new Tomcat();
//        tomcat.setPort(8080);
//
//        String webappDirLocation = siteWorkingDir;
//        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
//        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());
//
//        // Declare an alternative location for your "WEB-INF/classes" dir
//        // Servlet 3.0 annotation will work
//        File additionWebInfClasses = new File("target/classes");
//        WebResourceRoot resources = new StandardRoot(ctx);
//        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
//                additionWebInfClasses.getAbsolutePath(), "/"));
//        ctx.setResources(resources);
//
//        LOGGER.info("Init users and roles");
//        tomcat.addUser("admin", "admin");
//        tomcat.addUser("user", "user");
//        tomcat.addRole("admin", "admin");
//        tomcat.addRole("admin", "user");
//        tomcat.addRole("user", "user");
//
//        tomcat.start();
//        tomcat.getServer().await();
    }

	/**
	 * Stops the tomcat server.
	 * 
	 * @throws Throwable
	 *             if anything goes wrong.
	 */
	@After
	public final void teardown() throws Throwable {
        stopTomcat(siteTomcat);
        stopTomcat(manageTomcat);
    }

    private void stopTomcat(Tomcat tomcat) {
        LOGGER.info(String.format("Stopping Tomcat %s", tomcat));
        try {
            if (tomcat.getServer() != null && tomcat.getServer().getState() != LifecycleState.DESTROYED) {
                if (tomcat.getServer().getState() != LifecycleState.STOPPED) {
                    tomcat.stop();
                }
                tomcat.destroy();
            }
        } catch (LifecycleException e) {
            e.printStackTrace();
            LOGGER.error("Failed to stop tomcat: " + e);
        }
    }

//	/**
//	 * @return the port tomcat is running on
//	 */
//	protected int getTomcatPort() {
//		return tomcat.getConnector().getLocalPort();
//	}

//	/**
//	 * @return the URL the app is running on
//	 */
//	protected String getAppBaseURL() {
//		return "http://localhost:" + getTomcatPort() + "/" + getApplicationId();
//	}

//	/**
//	 * @return a web archive that will be deployed on the embedded tomcat.
//	 */
//	protected abstract WebArchive createWebArchive();

	/**
	 * @return the name of the application to test.
	 */
	protected abstract String getApplicationId();
}
