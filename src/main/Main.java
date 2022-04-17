package main;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application{

    static Wall[] walls = {
        //  周りの壁
         new Wall(250, 0  , 251, 500),new Wall(0  , 0  , 1  , 500)
        ,new Wall(0,   0  , 250, 1)  ,new Wall(0  , 500, 250, 500)
        
        ,new Wall(100,100,200,100),new Wall(200, 100, 201, 200)
        ,new Wall(100,100, 101,200),new Wall(101, 200, 200, 200)

    };

    static GraphicsContext g ;
    static Player player;
    //                                 W     S    A     D   ENTER
    static boolean[] pressedKeys = {false,false,false,false,false};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();

        Canvas cvs = new Canvas(500,500);

        root.getChildren().add(cvs);
        
        g = cvs.getGraphicsContext2D();

        Scene scene = new Scene(root,500,500);

        scene.setOnKeyPressed(this::keyPressed);
        scene.setOnKeyReleased(this::keyReleased);

        stage.setTitle("fps");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        player = new Player(125,150,45);


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                draw();
                if(pressedKeys[2])player.moveViewPoint(-2);
                if(pressedKeys[3])player.moveViewPoint(2);  
                if(((pressedKeys[0])&&player.isCanMove)){
                    // 前に進むか後ろに進むか。
                    player.move(player.lineLocation[(player.lineLocation.length-1)/2][0],player.lineLocation[(player.lineLocation.length-1)/2][1]);
                }        
            }            
        };
        timer.start();
    }
    public static void draw(){
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, 500, 500);
        g.setStroke(Color.YELLOW);

        for (int i = 0; i < player.lineLocation.length; i++) {

            // プレイヤーの視点の角度。視線の中心をプレイヤーの向いてる角度としてそこから何度離れているか（中心の場合は0）。
            int lineRad = player.getMovedViewPoint((i-(player.lineLocation.length-1)/2)*5);

            player.lineLocation[i][0] = Math.cos(Math.toRadians(lineRad))*player.eyeSight;
            player.lineLocation[i][1] = Math.sin(Math.toRadians(lineRad))*player.eyeSight;

            player.lineLocation[i][2] = player.lineLocation[i][1]/player.lineLocation[i][0];

            if(pressedKeys[1])g.setFill(Color.YELLOW);
            for (int j = 0; j < walls.length; j++) {
                if(pressedKeys[1]){
                    g.setStroke(Color.WHITE);   
                    g.strokeLine(walls[j].x1,walls[j].y1, walls[j].x2, walls[j].y2);
                }
                if(player.lineLocation[i][2] == walls[j].slope)walls[j].slope+=0.01;

                double[] interscectionLocation = interscection(player.lineLocation[i], walls[j]); 

                if(Math.max(player.x+5, player.lineLocation[i][0]+player.x+5)>interscectionLocation[0]
                &&Math.min(player.x+5, player.lineLocation[i][0]+player.x+5)<interscectionLocation[0]
                &&Math.min(walls[j].x1,walls[j].x2)<interscectionLocation[0]
                &&Math.max(walls[j].x1,walls[j].x2)>interscectionLocation[0]
                ){               
                    if(pressedKeys[1])g.fillOval(interscectionLocation[0]-3, interscectionLocation[1]-3, 6, 6);

                    double distance = Math.sqrt((interscectionLocation[0]-(player.x+5))*(interscectionLocation[0]-(player.x+5))
                                    +(interscectionLocation[1]-(player.y+5))*(interscectionLocation[1]-(player.y+5)));

                    if(distance < 10 && i-(player.lineLocation.length-1)/2==0){
                        player.isCanMove = false;
                    }else if( i-(player.lineLocation.length-1)/2==0){
                        player.isCanMove = true;
                    }
                    
                    distance *= Math.cos(Math.toRadians((i-(player.lineLocation.length-1)/2)*5)); 

                    double wallHeight =  1400/distance;

                    g.setFill(Color.WHITE);
                    g.fillRect(i*5+200, 250-wallHeight/2,4,wallHeight);
                }
            }
            if(pressedKeys[1]){
                // もし真ん中の線（プレイヤーの視点の中心）だったら赤、それ以外は黄色にする。
                g.setStroke(i == (player.lineLocation.length-1)/2 ? Color.RED:Color.YELLOW );
                g.strokeLine(player.x+5, player.y+5, player.lineLocation[i][0]+player.x+5, player.lineLocation[i][1]+player.y+5);
            }
        }
        if(pressedKeys[1]){
            g.setFill(Color.CYAN);
            g.fillOval(player.x, player.y, 10, 10);
        }
    }

    public static double[] interscection(double[] line,Wall w){
        
        double[] interscectionLocation=new double[2];

        interscectionLocation[0] = ((player.x+5+line[0])*line[2] - w.x1*w.slope - (player.y+5+line[1])+w.y1)/(line[2]-w.slope);

        interscectionLocation[1] = line[2]*(interscectionLocation[0]-(player.x+5+line[0])) + (player.y+5+line[1]);

        return interscectionLocation;
    }

    private void keyPressed(KeyEvent e) {

		switch(e.getCode()) {
            case W:
                pressedKeys[0]=true;
                break;
            case S:
                pressedKeys[1]=true;
                break;
		    case A:
                pressedKeys[2]=true;
                break;
		    case D:
			    pressedKeys[3]=true;
                break;
            case ENTER:
                pressedKeys[4]=true;
                break;
		    default:
		    	break;
		}
	}
    private void keyReleased(KeyEvent e) {
 
		switch(e.getCode()) {
            case W:
                pressedKeys[0]=false;
                break;
            case S:
                pressedKeys[1]=false;
                break;
            case A:
                pressedKeys[2]=false;
                break;
            case D:
                pressedKeys[3]=false;
                break;
            case ENTER:
                pressedKeys[4]=false;
                break;
            default:
                break;
		}
	}
}
