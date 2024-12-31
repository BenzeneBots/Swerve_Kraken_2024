package team4384.robot.constants;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.ClosedLoopRampsConfigs;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.OpenLoopRampsConfigs;

public final class CTREConfigs {
    public TalonFXConfiguration swerveAngleFXConfig;
    public com.ctre.phoenix6.configs.TalonFXConfiguration swerveDriveFXConfig;
    public CANCoderConfiguration swerveCanCoderConfig;

    public CTREConfigs(){
        swerveAngleFXConfig = new TalonFXConfiguration();
        swerveDriveFXConfig = new com.ctre.phoenix6.configs.TalonFXConfiguration();
        swerveCanCoderConfig = new CANCoderConfiguration();

        /* Swerve Angle Motor Configurations */
        SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
            SwerveMap.angleEnableCurrentLimit,
            SwerveMap.angleContinuousCurrentLimit,
            SwerveMap.anglePeakCurrentLimit, 
            SwerveMap.anglePeakCurrentDuration);

        swerveAngleFXConfig.slot0.kP = SwerveMap.angleKP;
        swerveAngleFXConfig.slot0.kI = SwerveMap.angleKI;
        swerveAngleFXConfig.slot0.kD = SwerveMap.angleKD;
        swerveAngleFXConfig.slot0.kF = SwerveMap.angleKF;
        swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;

        /* Swerve Drive Motor Configuration */
        SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
            SwerveMap.driveEnableCurrentLimit, 
            SwerveMap.driveContinuousCurrentLimit, 
            SwerveMap.drivePeakCurrentLimit, 
            SwerveMap.drivePeakCurrentDuration);

        swerveDriveFXConfig.Slot0.kP = SwerveMap.driveKP;
        swerveDriveFXConfig.Slot0.kI = SwerveMap.driveKI;
        swerveDriveFXConfig.Slot0.kD = SwerveMap.driveKD;
        // swerveDriveFXConfig.Slot0.kF = SwerveMap.driveKF;
        CurrentLimitsConfigs con = new CurrentLimitsConfigs();
        con.StatorCurrentLimit = SwerveMap.drivePeakCurrentLimit;
        con.StatorCurrentLimitEnable = SwerveMap.driveEnableCurrentLimit;
        con.SupplyTimeThreshold = SwerveMap.drivePeakCurrentDuration;
        swerveDriveFXConfig.CurrentLimits = con;

        // OpenLoopRampsConfigs openConfig = new OpenLoopRampsConfigs();
        // ClosedLoopRampsConfigs closedConfig = new ClosedLoopRampsConfigs();
        // swerveDriveFXConfig.OpenLoopRamps = SwerveMap.openLoopRamp;
        // swerveDriveFXConfig.ClosedLoopRamps = SwerveMap.closedLoopRamp;

        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        swerveCanCoderConfig.sensorDirection = SwerveMap.canCoderInvert;
        swerveCanCoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }
}