package main;

public class Wall {
    int x1,y1,x2,y2;
    double slope; //壁の傾き

    Wall(int x1,int y1,int x2,int y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        if(x2!=x1){
            this.slope = (y2-y1)/(x2-x1);
        }else{
            throw new IllegalArgumentException("傾きが０になってしまいます"+x1+":"+x2);
        }
    }
}
