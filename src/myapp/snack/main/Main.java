package myapp.snack.main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	private static final int WIDTH = 500;
	private static final int HEIGHT = WIDTH;
	private static final int ROWS = 15;
	private static final int COLLUMNS = ROWS;
	private static final int SQUARE_SIZE = WIDTH / ROWS;
	private static final String[] FOODS_IMAGE = new String[] { "/myapp/snack/img/rocket-white.png",
			"/myapp/snack/img/warning-rocket.png" };

	private static final int RIGHT = 0;
	private static final int LEFT = 1;
	private static final int UP = 2;
	private static final int DOWN = 3;

	private GraphicsContext gc;

	private List<Point> snackBody = new ArrayList<>();
	private Point snackHead;
	private Image foodImage;
	private int foodX;
	private int foodY;
	private boolean gameOver;
	private int currentDirection;

	private int score = 0;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Serpent");
		Group root = new Group();
		Canvas canva = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canva);

		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		gc = canva.getGraphicsContext2D();

		scene.setOnKeyPressed((e) -> {
			KeyCode code = e.getCode();
			if (code == KeyCode.LEFT || code == KeyCode.O) {
				if (currentDirection != RIGHT) {
					currentDirection = LEFT;
				}
			} else if (code == KeyCode.RIGHT || code == KeyCode.A) {
				if (currentDirection != LEFT) {
					currentDirection = RIGHT;
				}
			}
			if (code == KeyCode.UP || code == KeyCode.U) {
				if (currentDirection != DOWN) {
					currentDirection = UP;
				}
			}
			if (code == KeyCode.DOWN || code == KeyCode.D) {
				if (currentDirection != UP) {
					currentDirection = DOWN;
				}
			}

		});

		for (int i = 0; i < 3; i++) {
			snackBody.add(new Point(5, ROWS / 2));
		}

		snackHead = snackBody.get(0);
		generateFood();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(230), e -> run(gc)));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();

	}

	private void run(GraphicsContext gc) {
		if (gameOver) {
			gc.setFill(Color.RED);
			gc.setFont(new Font("Arial", 50));
			gc.fillText("GAME OVER", WIDTH / 3.5, HEIGHT / 2);
			return;
		}
		drawBackgroung(gc);
		drawFood(gc);
		drawSnack(gc);
		drawScore();
		for (int i = snackBody.size() - 1; i >= 1; i--) {
			snackBody.get(i).x = snackBody.get(i - 1).x;
			snackBody.get(i).y = snackBody.get(i - 1).y;

		}
		switch (currentDirection) {
		case RIGHT:
			moveRight();
			break;

		case LEFT:
			moveLeft();
			break;

		case UP:
			moveUp();
			break;

		case DOWN:
			moveDawn();
			break;

		}
		gameOver();
		eatFood();
	}

	private void drawFood(GraphicsContext gc) {
		gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
	}

	private void drawBackgroung(GraphicsContext gc) {
		for (int i = 0; i < ROWS; i++) {
			for (int j = 0; j < COLLUMNS; j++) {
				if ((i + j) % 2 == 0) {
					gc.setFill(Color.web("#FFA3FF"));
				} else {
					gc.setFill(Color.web("#FFB3FF"));
				}
				gc.fillRect(i * SQUARE_SIZE, j * SQUARE_SIZE, WIDTH, HEIGHT);
			}
		}
	}

	private void generateFood() {
		start: while (true) {
			foodX = (int) (Math.random() * ROWS);
			foodY = (int) (Math.random() * COLLUMNS);
			for (Point snack : snackBody) {
				if (snack.getX() == foodX && snack.getY() == foodY) {
					continue start;
				}
			}

			foodImage = new Image(FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)]);
			break;
		}
	}

	private void drawSnack(GraphicsContext gc) {
		gc.setFill(Color.web("ffee77"));
		gc.fillRoundRect(snackHead.getX() * SQUARE_SIZE, snackHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
				SQUARE_SIZE - 1, 35, 35);
		for (int i = 1; i < snackBody.size(); i++) {
			gc.fillRoundRect(snackBody.get(i).getX() * SQUARE_SIZE, snackBody.get(i).getY() * SQUARE_SIZE,
					SQUARE_SIZE - 1, SQUARE_SIZE - 1, 20, 20);
		}
	}

	private void moveRight() {
		snackHead.x++;
	}

	private void moveLeft() {
		snackHead.x--;
	}

	private void moveUp() {
		snackHead.y--;
	}

	private void moveDawn() {
		snackHead.y++;
	}

	private void gameOver() {
		if (snackHead.x < 0 || snackHead.y < 0 || snackHead.x * SQUARE_SIZE >= WIDTH
				|| snackHead.y * SQUARE_SIZE >= HEIGHT) {
			gameOver = true;
		}

		for (int i = 1; i < snackBody.size(); i++) {
			if (snackHead.x == snackBody.get(i).getX() && snackHead.y == snackBody.get(i).getY()) {
				gameOver = true;
				break;
			}
		}

	}

	private void eatFood() {
		if (snackHead.getX() == foodX && snackHead.getY() == foodY) {
			snackBody.add(new Point(-1, -1));
			generateFood();
			score += 5;
		}
	}

	private void drawScore() {
		gc.setFill(Color.GREEN);
		gc.setFont(new Font("Arial", 20));
		gc.fillText("SCORE : " + score, 10, 35);
		return;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
