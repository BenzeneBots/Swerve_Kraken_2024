package team4384.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.GravityTypeValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Pivot extends SubsystemBase {
    private final TalonFX pivot_motor = new TalonFX(60);
    private final MotionMagicVoltage m_request = new MotionMagicVoltage(0);
    public Pivot() {
        var talonFXConfigs = new TalonFXConfiguration();

        var slot0configs = talonFXConfigs.Slot0;
        slot0configs.kS = 0.25;
        slot0configs.kV = 0.12;
        slot0configs.kA = 0.01;
        slot0configs.kP = 1;
        slot0configs.kI = 1;
        slot0configs.kD = 0.4;
        slot0configs.kG = 1.6;
        slot0configs.withGravityType(GravityTypeValue.Arm_Cosine);

        var motionMagicConfigs = talonFXConfigs.MotionMagic;
        motionMagicConfigs.MotionMagicCruiseVelocity = 80;
        motionMagicConfigs.MotionMagicAcceleration = 160;
        motionMagicConfigs.MotionMagicJerk = 1600;

        pivot_motor.getConfigurator().apply(talonFXConfigs);
        pivot_motor.setNeutralMode(NeutralModeValue.Brake);
    }



    public void extend() {
        // Set Position
        pivot_motor.setControl(m_request.withSlot(0).withPosition(28));
    }
    public void stow() {
        // Set Position to home
        pivot_motor.setControl(m_request.withSlot(0).withPosition(7));
    }

    public void middle() {
        pivot_motor.setControl(m_request.withSlot(0).withPosition(18.0));
    }
    @Override
    public void periodic() {
        SmartDashboard.putNumber("Motor Pos", pivot_motor.getPosition().getValue());

    }
}
