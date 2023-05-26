package hellofx;

import java.util.Stack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Control{
    @FXML
    private TextField Disk_count_txtfield;
    
    //Global Variables 
    private int Moves_count=0;
    private int Disk_count;
    private int Charge=0;//When the Pole is Selected, it changes the Charge value to 1,2,3 respectively
    private int From=0;//disk selected "From" which Pole
    private int To=0;//disk placed "To" which Pole
    private int Initial_layoutY=609;//Position according to the canvas (AnchorPane)
    private Rectangle[] Rectangle_array;//Array of Rectangles (Disks)
    //Stacks Declaration
    Stack<Integer> stack1=new Stack<>();//stack1 for Pole#1
    Stack<Integer> stack2=new Stack<>();//stack2 for Pole#2
    Stack<Integer> stack3=new Stack<>();//Stack3 for Pole#3

    @FXML
    private AnchorPane anchorpane;//fx:id of the canvas (AnchorPane)
    @FXML
    private Button Pol1_button;

    @FXML
    private Button Pol2_button;

    @FXML
    private Button Pol3_button;

    @FXML
    private Text Warning_label;
    
    @FXML
    private Text Moves;
    
    @FXML
    private Text MinMoves_Label;

    //Start Button Method (Starts the Game)
    @FXML
    void Start(ActionEvent event) {
        Warning_label.setFill(Color.CORAL);
        Disk_count=MovesCalculator();
        Rectangle_array=new Rectangle[Disk_count];
        for(int i=0;i<Disk_count;i++){
            //Creating a Disk till "Disk_count"
            Rectangle r=new Rectangle();
            //Positioning and Styling of Disks
            r.setWidth(290-(15*i));
            r.setHeight(30);
            r.setArcHeight(30);
            r.setArcWidth(30);
            r.setLayoutX(75+(7*i));
            r.setLayoutY(Initial_layoutY-(30*i));
            r.setFill(Color.CORAL);
            r.setStroke(Color.BLACK);
            r.setStrokeWidth(1);
            //Populating the array of disks
            Rectangle_array[i]=r;
            anchorpane.getChildren().add(Rectangle_array[i]);
            //pushing each disk's integar number into the stack
            stack1.push(Disk_count-i);
        }
    }
    public int MovesCalculator(){
        int x=Integer.parseInt(Disk_count_txtfield.getText());
        int y=((int)Math.pow(2, x))-1;
        MinMoves_Label.setText(Integer.toString(y));
        return x;
    }
    @FXML
    void End(ActionEvent event) {
        System.exit(0);
    }

    //Method that decides where the selected disk be placed, based on the values of "From" pole and "To" pole
    public double Position_Decider(Rectangle r){
        if(From==1&&To==2){
            return r.getLayoutX()+380;
        }else if(From==1&&To==3){
            return r.getLayoutX()+760;
        }else if(From==2&&To==1){
            return r.getLayoutX()-380;
        }else if(From==2&&To==3){
            return r.getLayoutX()+380;
        }else if(From==3&&To==1){
            return r.getLayoutX()-760;
        }else if(From==3&&To==2){
            return r.getLayoutX()-380;
        }else{
            return 0;
        }
    }

    //Method that Changes the Position of Selected Disk
    public void Position_Changer(Rectangle r, Stack<Integer> x, Stack<Integer> y) throws InterruptedException{
        double layout=0;
        if(y.isEmpty()){
            layout=Position_Decider(r);
            r.setFill(Color.CORAL);
            r.setLayoutX(layout);
            r.setLayoutY(Initial_layoutY);
            //updating |Charge back to Zero| to get ready for the next Move
            Charge=0;
            Moves_count++;
            Moves.setText(Integer.toString(Moves_count));
            ButtonDefaulter();
            y.push(x.pop());
        }else{
            int result=StackChecker(y,x);
            if(result!=0){
                layout=Position_Decider(r);
                r.setFill(Color.CORAL);
                r.setLayoutX(layout);
                r.setLayoutY(Initial_layoutY-result);
                //updating |Charge back to Zero| to get ready for the next Move
                Charge=0;
                Moves_count++;
                Moves.setText(Integer.toString(Moves_count));
                ButtonDefaulter();
                y.push(x.pop());
            }else{
                //if a bigger disk is placed on smaller disk then the selected disk remains at its original position and WRONG MOVE IS quoted, this is checked by "StackChecker Method"
                r.setFill(Color.CORAL);
                Warning_label.setText("Wrong Move");
                Warning_label.setFill(Color.WHITE);
                Charge=0;
                ButtonDefaulter();
            }
        }
    }

    //Method that checks that whether the pole, where the selected disk is to be placed, already contains a disk, this is done by checking the stack of the respective pole
    public int StackChecker(Stack<Integer> x,Stack<Integer> y){
        int count=1;
        for(int i=0;i<Disk_count;i++){
            if(x.get(i)==x.peek()){
                //if big disk is placed on small one, method returns Zero which is Used to quote WRONG MOVE
                if(x.peek()<y.peek()){
                    return 0;
                }else{
                    break;
                }
            }else{
                count++;
            }
        }
        return 30*count;
    }
    public void ButtonDefaulter(){
        Pol1_button.setText("Select");
        Pol1_button.setStyle("-fx-background-color: coral;-fx-background-radius: 100px;");
        Pol2_button.setText("Select");
        Pol2_button.setStyle("-fx-background-color: coral;-fx-background-radius: 100px;");
        Pol3_button.setText("Select");
        Pol3_button.setStyle("-fx-background-color: coral;-fx-background-radius: 100px;");
    }
    public void ButtonStyler(){
        if(From==1){
            Pol2_button.setText("Place");
            Pol2_button.setStyle("-fx-background-color: black;-fx-background-radius: 100px;");
            Pol3_button.setText("Place");
            Pol3_button.setStyle("-fx-background-color: black;-fx-background-radius: 100px;");
        }else if(From==2){
            Pol1_button.setText("Place");
            Pol1_button.setStyle("-fx-background-color: black;-fx-background-radius: 100px;");
            Pol3_button.setText("Place");
            Pol3_button.setStyle("-fx-background-color: black;-fx-background-radius: 100px;");
        }else{
            Pol2_button.setText("Place");
            Pol2_button.setStyle("-fx-background-color: black;-fx-background-radius: 100px;");
            Pol1_button.setText("Place");
            Pol1_button.setStyle("-fx-background-color: black;-fx-background-radius: 100px;");
        }
    }

    //POLE # 1
    @FXML
    void Pol1(ActionEvent event) throws InterruptedException {
        Warning_label.setFill(Color.CORAL);
        try {
            if(Charge!=0){
                To=1;//for placing a disk
                if(From==2){
                    Position_Changer(Rectangle_array[Disk_count-stack2.peek()], stack2, stack1);
                }else if(From==3){
                    Position_Changer(Rectangle_array[Disk_count-stack3.peek()], stack3, stack1);
                }
    
            }else{//for selecting a disk
                From=1;
                Rectangle_array[Disk_count-stack1.peek()].setFill(Color.YELLOW);
                ButtonStyler();
                Charge=1;
            }
        } catch (Exception e) {
            Warning_label.setText("Empty Stack");
            Warning_label.setFill(Color.WHITE);
        }
    }

    //POLE # 2
    @FXML
    void Pol2(ActionEvent event) throws InterruptedException {
        Warning_label.setFill(Color.CORAL);
        try {
            if(Charge!=0){//for placing a disk
                To=2;
                if(From==1){
                    Position_Changer(Rectangle_array[Disk_count-stack1.peek()], stack1, stack2);
                }else if(From==3){
                    Position_Changer(Rectangle_array[Disk_count-stack3.peek()], stack3, stack2);
                }
            }else{//for selecting a disk
                From=2;
                Rectangle_array[Disk_count-stack2.peek()].setFill(Color.YELLOW);
                ButtonStyler();
                Charge=2;
            }
        } catch (Exception e) {
            Warning_label.setText("Empty Stack");
            Warning_label.setFill(Color.WHITE);
        }
    }

    //POLE # 3
    @FXML
    void Pol3(ActionEvent event) throws InterruptedException {
        Warning_label.setFill(Color.CORAL);
        try {
            if(Charge!=0){//for placing a disk
                To=3;
                if(From==1){
                    Position_Changer(Rectangle_array[Disk_count-stack1.peek()], stack1, stack3);
                }else if(From==2){
                    Position_Changer(Rectangle_array[Disk_count-stack2.peek()], stack2, stack3);
                }
            }else{//for selecting a disk
                From=3;
                Rectangle_array[Disk_count-stack3.peek()].setFill(Color.YELLOW);
                ButtonStyler();
                Charge=3;
            }
        } catch (Exception e) {
            Warning_label.setText("Empty Stack");
            Warning_label.setFill(Color.WHITE);
        }
    }
}
