package team4384.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.wpilibj.*;

import com.pathplanner.lib.path.PathPlannerPath;
import com.pathplanner.lib.util.HolonomicPathFollowerConfig;
import com.pathplanner.lib.util.PIDConstants;
import com.pathplanner.lib.util.ReplanningConfig;

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

    private final JoystickButton zeroGyro = new JoystickButton(driver, XboxController.Button.kY.value);
    final JoystickButton SlowMode  = new JoystickButton(driver, 1);
    final JoystickButton zeroOdo = new JoystickButton(driver, 3);
    final JoystickButton resetGyro = new JoystickButton(driver, 4);
    private final int translationAxis = Joystick.kDefaultYChannel;
    private final int strafeAxis = Joystick.kDefaultXChannel;
    private final int rotationAxis = Joystick.kDefaultZChannel;


    SendableChooser<Command> autoChooser = new SendableChooser<Command>();
    public final Swerve s_Swerve = new Swerve();
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

       AutoBuilder.configureHolonomic(
            s_Swerve::getPose,
            s_Swerve::resetOdometry,
            s_Swerve::getModuleStates,
            s_Swerve::autoDrive,
            new HolonomicPathFollowerConfig(
                    new PIDConstants(1.0, 0, 0),
                    new PIDConstants(1.0, 0, 0),
                    5.0,
                    0.38,
                    new ReplanningConfig()
            ),
            () -> {
                var alliance = DriverStation.getAlliance();
                return alliance.filter(value -> value == DriverStation.Alliance.Red).isPresent();
            },
            s_Swerve);

//        Command auto1 = AutoBuilder.buildAuto("Auto 1");
//        Command auto2 = AutoBuilder.buildAuto("Auto 2");
//        Command auto3 = AutoBuilder.buildAuto("Auto 3");
//
//        autoChooser.setDefaultOption("Auto 1", auto1);
//        autoChooser.addOption("Auto 2", auto2);
//        autoChooser.addOption("Auto 3", auto3);
//        SmartDashboard.putData("Auto Choices", autoChooser);
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
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
//    public Command getAutonomousCommand() {
//        return new Command() {
//            private Timer timer = new Timer();
//            @Override
//            public void initialize(){
//                timer.start();
//            }
//            @Override
//            public void execute() {
//                if(timer.get() >1){
//                    mShooter.index();
//                }
//                mShooter.shoot();
//            }
//        };
//    }

  
  // ToDo - Disable this function during competition. Required only for development and testing purpose.

}