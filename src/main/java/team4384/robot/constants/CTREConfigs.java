package team4384.robot.constants;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CANcoderConfigurator;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

public final class CTREConfigs {
    public com.ctre.phoenix6.configs.TalonFXConfiguration swerveAngleFXConfig;
    public com.ctre.phoenix6.configs.TalonFXConfiguration swerveDriveFXConfig;
    public com.ctre.phoenix6.configs.CANcoderConfigurator swerveCanCoderConfig;

    public CTREConfigs(){
        swerveAngleFXConfig = new TalonFXConfiguration();
        swerveDriveFXConfig = new com.ctre.phoenix6.configs.TalonFXConfiguration();
        swerveCanCoderConfig = new CANcoderConfigurator();

        /* Swerve Angle Motor Configurations */
        SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
            SwerveMap.angleEnableCurrentLimit,
            SwerveMap.angleContinuousCurrentLimit,
            SwerveMap.anglePeakCurrentLimit, 
            SwerveMap.anglePeakCurrentDuration);

        swerveAngleFXConfig.Slot0.kP = SwerveMap.angleKP;
        swerveAngleFXConfig.Slot0.kI = SwerveMap.angleKI;
        swerveAngleFXConfig.Slot0.kD = SwerveMap.angleKD;
        swerveAngleFXConfig.Slot0.kF = SwerveMap.angleKF;
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
//        swerveDriveFXConfig.slot0.kF = SwerveMap.driveKF;
//        swerveDriveFXConfig.openloopRamp = SwerveMap.openLoopRamp;
//        swerveDriveFXConfig.closedloopRamp = SwerveMap.closedLoopRamp;

        /* Swerve CANCoder Configuration */
        swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        swerveCanCoderConfig.sensorDirection = SwerveMap.canCoderInvert;
        swerveCanCoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
    }
}