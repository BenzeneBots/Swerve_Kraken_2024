package team4384.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj.*;


import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

import team4384.robot.subsystems.*;


import team4384.robot.commands.TeleopSwerve;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    /* Controllers */
    public final Joystick driver = new Joystick(0);

    public final Joystick manip = new Joystick(1);

    private final JoystickButton stow = new JoystickButton(manip, 1);
    private final JoystickButton extend = new JoystickButton(manip, 2);

    private final JoystickButton middle = new JoystickButton(manip, 3);

    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    final JoystickButton SlowMode  = new JoystickButton(driver, 1);
    final JoystickButton zeroOdo = new JoystickButton(driver, 3);
    final JoystickButton resetGyro = new JoystickButton(driver, 4);
    private final int translationAxis = Joystick.kDefaultYChannel;
    private final int strafeAxis = Joystick.kDefaultXChannel;
    private final int rotationAxis = Joystick.kDefaultZChannel;


    SendableChooser<Command> autoChooser = new SendableChooser<Command>();
    public final Swerve s_Swerve = new Swerve();
    public final Pivot s_Pivot = new Pivot();
//    private final StateManager sManager = new StateManager();

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
       configureButtonBindings();

       s_Swerve.setDefaultCommand(
            new TeleopSwerve(
                s_Swerve,
                () -> -driver.getRawAxis(translationAxis),
                () -> -driver.getRawAxis(strafeAxis),
                () -> driver.getRawAxis(rotationAxis),
                    () -> driver.getRawAxis(3),
                    SlowMode,
                    resetGyro
            )
        );

        autoChooser = AutoBuilder.buildAutoChooser();

        SmartDashboard.putData("Auto Chooser", autoChooser);

    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        /* Driver Buttons */
        zeroGyro.onTrue(new InstantCommand(s_Swerve::zeroGyro));

        stow.onTrue(new InstantCommand(s_Pivot::stow));
        extend.onTrue(new InstantCommand(s_Pivot::extend));
        middle.onTrue(new InstantCommand(s_Pivot::middle));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }

}
