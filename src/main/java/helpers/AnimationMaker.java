package helpers;

import enums.AnimationDirection;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class AnimationMaker {
    private AnimationMaker() {

    }

    public static void makeAnimation(StackPane stackPane, AnchorPane anchorPane, String fxmlPath, AnimationDirection animationDirection) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(AnimationMaker.class.getResource(fxmlPath)));
        Scene scene = anchorPane.getScene();
        if (animationDirection == AnimationDirection.FROM_RIGHT) {
            root.translateXProperty().set(scene.getWidth());

        } else {
            root.translateXProperty().set(-1 * scene.getWidth());

        }
        stackPane.getChildren().add(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
        timeline.getKeyFrames().add(kf);
        timeline.setOnFinished(t -> stackPane.getChildren().remove(anchorPane));
        timeline.play();
    }

}
