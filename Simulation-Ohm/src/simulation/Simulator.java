package simulation;

import com.team1389.util.Loopable;
import com.team1389.util.Timer;

import edu.wpi.first.wpilibj.HLUsageReporting;
import edu.wpi.first.wpilibj.HLUsageReporting.Interface;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public final class Simulator {
	static Timer timer;

	private Simulator() {
		throw new AssertionError();
	};

	/**
	 * initializes WPILib and runs the given loopable as a simulation
	 * <p>
	 * hangs thread while simulating
	 * 
	 * @throws InterruptedException
	 *             if the simulation is interrupted
	 */
	public static void simulate(Loopable loopable, double updateRate) throws InterruptedException {
		initWPILib();
		timer = new Timer();
		loopable.init();
		while (true) {
			timer.mark();
			loopable.update();
			double waitTime = 1000 / updateRate - 1000 * timer.getSinceMark();
			if (waitTime > 0) {
				Thread.sleep((long) waitTime);
			}
		}
	}

	public static void simulate(Loopable loopable) throws InterruptedException {
		simulate(loopable, 50);
	}

	public static void initWPILib() {
		edu.wpi.first.wpilibj.Timer.SetImplementation(new SimulationTimer());
		initNetworkTablesAsRobot();
		HLUsageReporting.SetImplementation(new Interface() {
			@Override
			public void reportSmartDashboard() {
			}

			@Override
			public void reportScheduler() {
			}

			@Override
			public void reportPIDController(int num) {
			}
		});
	}

	private static void initNetworkTablesAsRobot() {
		NetworkTable.setServerMode();
		NetworkTable.initialize();
		NetworkTable.globalDeleteAll();
	}

}
