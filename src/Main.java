import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class Main extends Application {

    private int wordCount = 0;
    private Label[] labels;
    private Label wordLabel;
    private Label countLabel;
    private Label wpmLabel;
    private String theWord = "Test";
    private char typeChar;
    private int position = 0;
    private Timer timer;
    private BorderPane borderPane;
    @Override
    public void start(Stage window) throws Exception{
        window.setTitle("Speedtyper");
        //WPM Label
        wpmLabel = new Label("Current WPM: ");

        //Word Label
        wordLabel = new Label(theWord);

        //Count label
        countLabel = new Label("Word typed: " + String.valueOf(wordCount));

        //Bottom HBOX
        HBox bottomHbox = new HBox();
        bottomHbox.getChildren().add(countLabel);
        bottomHbox.setAlignment(Pos.CENTER);

        //Borderpane
        borderPane = new BorderPane();
        borderPane.setCenter(generateNewHbox(charLabels(randomizeWordFromFile())));
        borderPane.setBottom(bottomHbox);
        borderPane.setTop(wpmLabel);

        //Scene
        Scene scene = new Scene(borderPane, 300, 275);
        scene.setOnKeyPressed(ke -> {
            checkCharTyped(ke.getText());
            createTimer();
        });

        scene.getStylesheets().add("stylesheet.css");

        //Windows
        window.setScene(scene);
        window.show();
    }
    //Creates a timer for calculating the WPM that ticks once every 1 second.
    public void createTimer(){
        if(timer == null){
            timer = new Timer();
            timer.schedule(updateTimerAndWpmLabel, 1000l, 1000l);
        }

    }
    //TimerTask handling the WPM counter.
    TimerTask updateTimerAndWpmLabel = new TimerTask()
    {
        int i = 0;
        double d;
        int wpm = 0;

        public void run()
        {
            if(countLabel.getText() == null){
                d = 1;
            }
            else
            {
                d = wordCount;
            }
            if(i < 60){
                wpm = (int)(d / i * 60);
            }
            else{
                wpm = (int)d / i;
            }
            //The task you want to do
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    i++;

                    wpmLabel.setText("Current WPM: " + String.valueOf(wpm));
                }
            });
        }
    };
    //Check if the character typed matches the next character. If not, the character will turn red, if it does, it will turn green.
    public void checkCharTyped(String s){
        if(s.equals(labels[position].getText())){
            labels[position].setStyle("-fx-text-fill: green");
            position++;
        }
        else{
            labels[position].setStyle("-fx-text-fill: red");
        }
        if(position == labels.length){
            try{
                borderPane.setCenter(generateNewHbox(charLabels(randomizeWordFromFile())));
                position = 0;
                wordCount++;
                countLabel.setText("Word typed: " + String.valueOf(wordCount));
            }
            catch (Exception ex){
                System.out.println(ex);
            }
        }
    }
    //Returns a randomized string read from a .txt-file.
    public static String randomizeWordFromFile() throws IOException
    {
        Scanner sc = new Scanner(new File("C:\\Projects\\Java\\Speedtyper\\wordlist.txt"));
        List<String> lines = new ArrayList<String>();
        while(sc.hasNextLine())
        {
            lines.add(sc.nextLine());
        }
        String[] arr = lines.toArray(new String[0]);
        int random = (int)(Math.random() * arr.length);
        return arr[random];
    }
    //Takes the randomized word and splits is into one label per character in the word.
    public Label[] charLabels(String word){
        int wordLength = word.length();
        labels = new Label[word.length()];
        for(int i = 0; i < word.length(); i++){
            labels[i] = new Label(word.substring(i,i+1));
            //labels[i].setStyle("-fx-font-size: 20 px;");
        }
        return labels;
    }
    //Returns a new Hbox containing all of the new character labels.
    public HBox generateNewHbox(Label[] charLabels){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        for(Label l : charLabels){
            hBox.getChildren().add(l);
        }
        return hBox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
