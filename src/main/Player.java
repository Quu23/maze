package main;

public class Player {

    double x,y;

    int direction;/**三角関数の角度を指定する。 */ 

    int eyeSight = 40;

    boolean isCanMove = true;

    double[][] lineLocation = new double[21][3];

    Player(int x,int y,int direction){
        this.x=x;
        this.y=y;
        this.direction=direction;
    }
    public void moveViewPoint(int rad/*右なら+、左なら-*/){
        this.direction += rad;
        if(this.direction > 360){
            this.direction -= 360;
        }else if(this.direction <= 0){
            this.direction += 360;
        }
        
    }
    public int getMovedViewPoint(int rad/*動かす分の角度 */){
        int tempDirection = this.direction;
        tempDirection += rad;
        if(tempDirection > 360){
            tempDirection -= 360;
        }else if(tempDirection <= 0){
            tempDirection += 360;
        }
        return tempDirection;
    }
    public void move(double mx,double my){
            double rad = Math.atan2(my,mx);
            double movedX = this.x + Math.cos(rad)*2;
            double movedY = this.y + Math.sin(rad)*2;
            if(movedX>0&&movedX<240&&movedY>0&&movedY<490){
                this.x = movedX;
                this.y = movedY;
            }
    }
}
