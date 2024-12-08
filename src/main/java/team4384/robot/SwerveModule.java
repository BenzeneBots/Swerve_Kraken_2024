package team4384.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionDutyCycle;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import team4384.lib.math.Conversions;
import team4384.lib.util.CTREModuleState;
import team4384.lib.util.SwerveModuleConstants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.sensors.CANCoder;
import team4384.robot.constants.SwerveMap;

import static team4384.robot.constants.SwerveMap.*;

public class SwerveModule {
    public int moduleNumber;
    public String moduleName;
    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private com.ctre.phoenix6.hardware.TalonFX mAngleMotor;
    private com.ctre.phoenix6.hardware.TalonFX mDriveMotor;
    private com.ctre.phoenix6.hardware.CANcoder angleEncoder;

    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(driveKS, driveKV, driveKA);

    public SwerveModule(int moduleNumber, String moduleName, SwerveModuleConstants moduleConstants){
        this.moduleNumber = moduleNumber;
        this.moduleName = moduleName;
        this.angleOffset = moduleConstants.angleOffset;
        
        /* Angle Encoder Config */
        angleEncoder = new CANcoder(moduleConstants.cancoderID);
        configAngleEncoder();

        /* Angle Motor Config */
        mAngleMotor = new TalonFX(moduleConstants.angleMotorID);
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new com.ctre.phoenix6.hardware.TalonFX(moduleConstants.driveMotorID);
        configDriveMotor();

        lastAngle = getState().angle;
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop){
        /* This is a custom optimize function, since default WPILib optimize assumes continuous controller which CTRE and Rev onboard is not */
        desiredState = CTREModuleState.optimize(desiredState, getState().angle); 
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop){
            double percentOutput = desiredState.speedMetersPerSecond / SwerveMap.maxSpeed;
            mDriveMotor.set(percentOutput);
//        else {
//            double velocity = Conversions.MPSToFalcon(desiredState.speedMetersPerSecond, SwerveMap.wheelCircumference, SwerveMap.driveGearRatio);
//            mDriveMotor.set(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward, feedforward.calculate(desiredState.speedMetersPerSecond));
//        }
    }

    private void setAngle(SwerveModuleState desiredState){
        Rotation2d angle = (Math.abs(desiredState.speedMetersPerSecond) <= (SwerveMap.maxSpeed * 0.01)) ? lastAngle : desiredState.angle; //Prevent rotating module if speed is less then 1%. Prevents Jittering.

        PositionDutyCycle posControl = new PositionDutyCycle(0.0);
        mAngleMotor.setControl(posControl.withPosition(Conversions.degreesToFalcon(angle.getDegrees(), angleGearRatio)));
        lastAngle = angle;
    }

    private Rotation2d getAngle(){
        return Rotation2d.fromDegrees(Conversions.falconToDegrees(mAngleMotor.getPosition().getValue(), SwerveMap.angleGearRatio));
    }

    public Rotation2d getCanCoder(){
        return Rotation2d.fromDegrees(angleEncoder.getPosition().getValue());
    }

    public void resetToAbsolute(){
        double absolutePosition = Conversions.degreesToFalcon(getCanCoder().getDegrees() - angleOffset.getDegrees(), SwerveMap.angleGearRatio);
        PositionDutyCycle posControl = new PositionDutyCycle(0.0);
        mAngleMotor.setControl(posControl.withPosition(absolutePosition));
    }

    private void configAngleEncoder(){
        angleEncoder.getConfigurator().apply(Robot.ctreConfigs.swerveCanCoderConfig);
        angleEncoder.configFactoryDefault();
        angleEncoder.configAllSettings(Robot.ctreConfigs.swerveCanCoderConfig);
    }

    private void configAngleMotor(){
//        mAngleMotor.configFactoryDefault();
//        mAngleMotor.configAllSettings(Robot.ctreConfigs.swerveAngleFXConfig);
//        mAngleMotor.setInverted(SwerveMap.angleMotorInvert);
//        mAngleMotor.setNeutralMode(SwerveMap.angleNeutralMode);
//        resetToAbsolute();
        TalonFXConfiguration configs = new TalonFXConfiguration();
        mAngleMotor.getConfigurator().apply(configs);
        mAngleMotor.getConfigurator().apply(Robot.ctreConfigs.swerveDriveFXConfig);
        mAngleMotor.setInverted(SwerveMap.driveMotorInvert);
        mAngleMotor.setNeutralMode(NeutralModeValue.Coast);
        mAngleMotor.setPosition(0);
    }

    private void configDriveMotor(){
        TalonFXConfiguration configs = new TalonFXConfiguration();
        mDriveMotor.getConfigurator().apply(configs);
        mDriveMotor.getConfigurator().apply(Robot.ctreConfigs.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveMap.driveMotorInvert);
        mDriveMotor.setNeutralMode(NeutralModeValue.Coast);
        mDriveMotor.setPosition(0);
    }

    public SwerveModuleState getState(){
        return new SwerveModuleState(
            Conversions.falconToMPS(mDriveMotor.getVelocity().getValue(), SwerveMap.wheelCircumference, SwerveMap.driveGearRatio),
            getAngle()
        ); 
    }

    public SwerveModulePosition getPosition(){
        return new SwerveModulePosition(
            Conversions.falconToMeters(mDriveMotor.getPosition().getValue(), SwerveMap.wheelCircumference, SwerveMap.driveGearRatio),
            getAngle()
        );
    }
}