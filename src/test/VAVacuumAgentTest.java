package test;

import static test.SIVacuumAgentTest.VAEnv_Type.NON_OBSERVABILE;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import vacuumAgent.VAAction;
import vacuumAgent.VAAction.VAActionType;
import vacuumAgent.VAAgent;
import vacuumAgent.VAPercept;
import vacuumAgent.VATile.VATileStatus;
import framework.Action;
import framework.Percept;

/**
 * @author Giovanna,Antonia,Maria
 * 
 */
@RunWith(value = Parameterized.class)
public class VAVacuumAgentTest extends SIVacuumAgentTest {
    @Parameters
    public static Collection<Object[]> data() {

	final int energy = 300;
	final VAEnv_Type type_env = NON_OBSERVABILE;
	final File fileMap = new File("Mappe/map");

	// Se non si vuole il log basta metterlo a null
	final File fileLog = new File("log.csv");

	final List<Object[]> data = new ArrayList<Object[]>();
	data.add(new Object[] { energy, type_env, fileMap, fileLog });
	return data;
    }

    public VAVacuumAgentTest(int energy, VAEnv_Type type, File map, File log) {
	super(energy, type, map, log);
    }

    @Override
    public VAAgent defineAgent() {
	return new VAAgent(energy) {
	    @Override
	    public Action execute(Percept percept) {
		final VAPercept p = (VAPercept) percept;
		final VATileStatus status = p.getCurrentTileStatus();

		VAAction a = new VAAction(VAActionType.SUCK);
		if (status == VATileStatus.DIRTY) {
		    a = new VAAction(VAActionType.SUCK);
		} else {
		    final Random r = new Random();
		    final int i = r.nextInt(4);

		    switch (i) {
		    case 0:
			if (p.getNeighborhood().eastIsFree()) {
			    a = new VAAction(VAActionType.MOVEEAST);
			}
			break;
		    case 1:
			if (p.getNeighborhood().northIsFree()) {
			    a = new VAAction(VAActionType.MOVENORTH);
			}
			break;
		    case 2:
			if (p.getNeighborhood().southIsFree()) {
			    a = new VAAction(VAActionType.MOVESOUTH);
			}
			break;
		    case 3:
			if (p.getNeighborhood().westIsFree()) {
			    a = new VAAction(VAActionType.MOVEWEST);
			}
			break;
		    default:
			break;
		    }
		}
		return a;
	    }
	};
    }

}
