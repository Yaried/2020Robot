/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.chargerrobotics.subsystems;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.chargerrobotics.Constants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimelightSubsystem extends SubsystemBase {
	/**
	 * Creates a new LimelightSubsystem.
	 */
	private NetworkTable table;
	private NetworkTableEntry tx, ty, tv, leds, camMode;
	private boolean isRunning;
	private static LimelightSubsystem instance;

	private double x, y, v;

	public LimelightSubsystem() {
		table = NetworkTableInstance.getDefault().getTable("limelight");
		tx = table.getEntry("tx");
		ty = table.getEntry("ty");
		tv = table.getEntry("tv");
		leds = table.getEntry("ledMode");
		camMode = table.getEntry("camMode");
	}

	public static LimelightSubsystem getInstance() {
		if (instance == null) {
			instance = new LimelightSubsystem();
			CommandScheduler.getInstance().registerSubsystem(instance);
		}
		return instance;
	}

	public void setLEDStatus(boolean enabled) {
		leds.setDouble(enabled ? 0.0 : 1.0);
		camMode.setNumber(enabled ? 0 : 1);
	}

	public double getX() {
		if (v == 0) {
			return 0.0;
		} else {
			return x;
		}
	}

	public double getY() {
		return y;
	}

	public double getV() {
		return v;
	}

	// distance in inches
	public double distance() {
		if (v == 0) {
			return -1.0;
		} else {
			return ((Constants.targetHeight - Constants.cameraHeight)/Math.tan(((Constants.cameraAngle + y)*Math.PI)/180));
		}
	}

	@Override
	public void periodic() {
		super.periodic();
		if (isRunning) {
			x = tx.getDouble(0.0);
			y = ty.getDouble(0.0);
			v = tv.getDouble(0.0);
			SmartDashboard.putNumber("LimelightX", x);
			SmartDashboard.putNumber("LimelightY", y);
		}
	}
}
