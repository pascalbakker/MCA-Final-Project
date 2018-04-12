
package GUI;

import java.util.ArrayList;
import java.util.List;

import Scoreboard.Instruction;
import Scoreboard.Scoreboard;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {

	private static final int INSTRUCTION_FONT_SIZE = 20;

	private static final double INSTRUCTION_PREF_WIDTH = App.INSTRUCTION_FONT_SIZE * 4;

	public static final void main(final String[] args) {

		Application.launch(args);

	}

	private final List<Instruction> instructions = new ArrayList<>();

	private VBox instructionVBox;

	private boolean stateStopped = true;

	private VBox controlVBox;

	private Scoreboard scoreboard;

	private Label label;

	private final void clearInstructionsUI() {

		this.instructionVBox.getChildren().clear();

	}

	private final void displayLabel() {

	}

	private final void makeControls() {

		final Button startButton = new Button();

		startButton.setOnAction((t) -> {

			if (this.stateStopped) {

				this.makeInstructions();

				this.scoreboard = new Scoreboard(this.instructions, true);

				this.clearInstructionsUI();

				this.makeInstructionsText();

				this.stateStopped = false;

			}

		});

		startButton.setText("Start");

		this.controlVBox.getChildren().add(startButton);

		final Button stopButton = new Button();

		stopButton.setOnAction((t) -> {

			if (!this.stateStopped) {

				this.label.setText("");

				this.clearInstructionsUI();

				this.makeInstructionsTextFields();

				this.stateStopped = true;

			}

		});

		stopButton.setText("Stop");

		this.controlVBox.getChildren().add(stopButton);

		final Button stepButton = new Button();

		stepButton.setOnAction((t) -> {

			if (!this.stateStopped) {

				this.scoreboard.oneClock();

				this.label.setText(this.scoreboard.queueToString() + "\n" + this.scoreboard.printRegisters() + "\n" + this.scoreboard.toString());

			}

		});

		stepButton.setText("Step");

		this.controlVBox.getChildren().add(stepButton);

	}

	private final void makeDefaultInstructions() {

		this.instructions.clear();
		this.instructions.add(new Instruction("LD", "R6", "36", "0", 1));
		this.instructions.add(new Instruction("LD", "R2", "45", "0", 2));
		this.instructions.add(new Instruction("LD", "R4", "45", "0", 3));
		this.instructions.add(new Instruction("MULT", "R0", "R2", "R4", 4));
		this.instructions.add(new Instruction("SUB", "R8", "R6", "R2", 5));
		this.instructions.add(new Instruction("DIV", "R10", "R0", "R6", 6));
		this.instructions.add(new Instruction("ADD", "R6", "R8", "R2", 7));

	}

	private final void makeInstructions() {

		this.instructions.clear();

		int i = 0;

		for (final Node node : this.instructionVBox.getChildren()) {

			i++;

			if (node instanceof HBox) {

				final HBox hBox = (HBox) node;

				final String command = ((TextField) hBox.getChildren().get(0)).getText();

				final String r0 = ((TextField) hBox.getChildren().get(1)).getText();

				final String r1 = ((TextField) hBox.getChildren().get(2)).getText();

				final String r2 = ((TextField) hBox.getChildren().get(3)).getText();

				this.instructions.add(new Instruction(command, r0, r1, r2, i));

			}

		}

	}

	private final void makeInstructionsText() {

		for (final Instruction instruction : this.instructions) {

			final Font font = new Font(App.INSTRUCTION_FONT_SIZE);

			final HBox instructionHBox = new HBox(2);

			// Make Command Box
			{

				final Text text = new Text(instruction.getCommand());

				text.setFont(font);

				instructionHBox.getChildren().add(text);

			}

			// Make r0 Box
			{

				final Text text = new Text(instruction.getFirstR());

				text.setFont(font);

				instructionHBox.getChildren().add(text);

			}

			// Make r1 Box
			{

				final Text text = new Text(instruction.getSecondR());

				text.setFont(font);

				instructionHBox.getChildren().add(text);

			}

			// Make r2 Box
			{

				final Text text = new Text(instruction.getThirdR());

				text.setFont(font);

				instructionHBox.getChildren().add(text);

			}

			this.instructionVBox.getChildren().add(instructionHBox);

		}

	}

	private final void makeInstructionsTextFields() {

		for (final Instruction instruction : this.instructions) {

			final Font font = new Font(App.INSTRUCTION_FONT_SIZE);

			final HBox instructionHBox = new HBox(2);

			// Make Command Box
			{

				final TextField text = new TextField(instruction.getCommand());

				text.setFont(font);

				text.setPrefWidth(App.INSTRUCTION_PREF_WIDTH);

				instructionHBox.getChildren().add(text);

			}

			// Make r0 Box
			{

				final TextField text = new TextField(instruction.getFirstR());

				text.setFont(font);

				text.setPrefWidth(App.INSTRUCTION_PREF_WIDTH);

				instructionHBox.getChildren().add(text);

			}

			// Make r1 Box
			{

				final TextField text = new TextField(instruction.getSecondR());

				text.setFont(font);

				text.setPrefWidth(App.INSTRUCTION_PREF_WIDTH);

				instructionHBox.getChildren().add(text);

			}

			// Make r2 Box
			{

				final TextField text = new TextField(instruction.getThirdR());

				text.setFont(font);

				text.setPrefWidth(App.INSTRUCTION_PREF_WIDTH);

				instructionHBox.getChildren().add(text);

			}

			this.instructionVBox.getChildren().add(instructionHBox);

		}

	}

	@Override
	public void start(final Stage primaryStage) {
		final VBox mainVBox = new VBox(10);
		
		ImageView tutorial = new ImageView();
		Image image = new Image("mcafinalimagesave.png");
		tutorial.setImage(image);

		this.makeDefaultInstructions();

		primaryStage.setTitle("Scoreboard Example");

		final HBox mainHBox = new HBox(8);

		this.controlVBox = new VBox(8);

		this.makeControls();

		mainHBox.getChildren().add(this.controlVBox);
		

		this.instructionVBox = new VBox(8);

		this.makeInstructionsTextFields();

		mainHBox.getChildren().add(this.instructionVBox);
		//mainHBox.getChildren().add(tutorial);

		this.label = new Label();

		mainHBox.getChildren().add(this.label);
		mainHBox.getChildren().add(tutorial);

		mainVBox.getChildren().add(mainHBox);
		mainVBox.getChildren().add(tutorial);
		final Scene scene = new Scene(mainVBox, 1600, 900);

		primaryStage.setScene(scene);

		primaryStage.show();

	}

}
