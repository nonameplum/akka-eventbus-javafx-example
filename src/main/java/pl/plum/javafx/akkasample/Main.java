package pl.plum.javafx.akkasample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pl.plum.javafx.AkkaEvent;
import pl.plum.javafx.AkkaEventBus;

import java.util.function.BiConsumer;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new AkkaEventBus.SingletonBuilder(primaryStage).build();

        primaryStage.setTitle("Akka event bus example");

        StackPane root = new StackPane();
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();

        // Subscribe the two actors to the two different channels
        String CHANNEL1 = "channel1";
        String CHANNEL2 = "channel2";

        BiConsumer<String, AkkaEvent<String>> printAkkEvent = (name, event) -> {
            System.out.print(name + ": ");
            System.out.println(event);
            System.out.println("Message :" + event.getMessage());
            System.out.println("//----------------------");
        };

        AkkaEventBus.subscribeEventHandler(CHANNEL1, (AkkaEvent<String> event) -> {
            printAkkEvent.accept("Lambda 1", event);
        });

        AkkaEventBus.subscribeEventHandler(CHANNEL2, (AkkaEvent<String> event) -> {
            printAkkEvent.accept("Lambda 2", event);
        });

        AkkaEventBus.subscribeEventHandler(CHANNEL1, (AkkaEvent<String> event) -> {
            printAkkEvent.accept("Lambda 3", event);
        });

        AkkaEventBus.subscribeEventHandler(CHANNEL2, (AkkaEvent<String> event) -> {
            printAkkEvent.accept("Lambda 4", event);
        });

        // Publish a couple of events to the two channels.
        // Publish to CHANNEL1
        AkkaEventBus.publish(new AkkaEvent<String>(CHANNEL1, "test message from " + CHANNEL1));

        // Publish to CHANNEL2
        AkkaEventBus.publish(new AkkaEvent<String>(CHANNEL2, "test message from " + CHANNEL2));

        Button btn = new Button();
        btn.setText("Akka publish");
        btn.setOnAction(event -> {
            // Publish to CHANNEL1
            AkkaEventBus.publish(new AkkaEvent<String>(CHANNEL1, "ButtonClick message from " + CHANNEL1));
            // Publish to CHANNEL2
            AkkaEventBus.publish(new AkkaEvent<String>(CHANNEL2, "ButtonClick message from " + CHANNEL2));
        });
        root.getChildren().add(btn);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
