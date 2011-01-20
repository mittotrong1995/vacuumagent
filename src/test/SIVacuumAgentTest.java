package test;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import main.Main;

import org.apache.commons.io.FilenameUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import vacuumAgent.VAAgent;
import vacuumAgent.environment.VAEnvObservable;
import vacuumAgent.environment.VAEnvironment;
import view.principal.DefineAgentActionListener;
import view.principal.OpenFileChooserActionListener;
import view.principal.StartActionListener;
import exception.InvalidValuesException;

/**
 * @author Giovanna,Antonia,Maria
 * 
 *         DA ESTENDERE IN UN PROPRIO TEST ESEMPIO:
 *         {@link:VAVacuumAgentTest.java}
 * 
 */
@RunWith(value = Parameterized.class)
public abstract class SIVacuumAgentTest {
    public static enum VAEnv_Type {
	OBSERVABILE, SEMI_OBSERVABILE, NON_OBSERVABILE;
    }

    private static File TEST_CSV_LOG = null;
    int energy;
    VAEnv_Type type;
    File map;
    private StartActionListener startActionListener;
    OpenFileChooserActionListener openFileChooserActionListener;
    DefineAgentActionListener defineAgentActionListener;
    static FileWriter log;
    static Main main;

    @Parameters
    protected static Collection<Object[]> data() {
	final List<Object[]> data = new ArrayList<Object[]>();
	data.add(new Object[] { 300, VAEnv_Type.NON_OBSERVABILE, new File("Mappe/map10").getPath(), new File("TEST_AGENT.csv") });
	return data;
    }

    public SIVacuumAgentTest(int energy, VAEnv_Type type, File map, File log) {
	this.energy = energy;
	this.type = type;
	this.map = map;
	TEST_CSV_LOG = log;
    }

    @After
    public void close() {
	closeLog();
	startActionListener = null;
	openFileChooserActionListener = null;
	defineAgentActionListener = null;
	main.dispose();
	main = null;

	try {
	    Thread.sleep(1000);
	} catch (final InterruptedException e) {
	    e.printStackTrace();
	}
    }

    public void closeLog() {
	try {
	    if (log != null) {
		log.flush();
		log.close();
	    }
	} catch (final IOException e) {
	    e.printStackTrace();
	}
    }

    public abstract VAAgent defineAgent();

    public void logResultTest(final double performanceMeasure) throws IOException {
	if (log != null) {
	    log.append(FilenameUtils.getBaseName(map.getName()) + "\t" + type.toString() + "\t" + energy + "\t" + performanceMeasure + "\n");
	    log.flush();
	}
    }

    public void openLog() {

	try {
	    if (TEST_CSV_LOG != null) {
		log = new FileWriter(TEST_CSV_LOG, true);
	    }
	} catch (final IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * @throws InvalidValuesException
     * @throws Exception
     */
    @Test
    public void run() throws InvalidValuesException, Exception {

	openFileChooserActionListener.loadMap(map);
	defineAgentActionListener.setAgentAction(energy, type.ordinal());
	startActionListener.run();
	final double performanceMeasure = main.getEnvironment().getPerformanceMeasure();
	logResultTest(performanceMeasure);

    }

    @Before
    public void start() {
	final Point point = new Point(0, 0);
	final VAAgent a = defineAgent();
	final VAEnvironment state = new VAEnvObservable(a, point, null);
	main = new Main(state);
	startActionListener = new StartActionListener(main, 0);
	openFileChooserActionListener = new OpenFileChooserActionListener(main);
	defineAgentActionListener = new DefineAgentActionListener(main);
	openLog();
    }
}
