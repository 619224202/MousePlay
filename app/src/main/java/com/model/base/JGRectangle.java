package com.model.base;


/** Minimal replacement of java.awt.Rectangle. */
public class JGRectangle {

	public int x=0,y=0,width=0,height=0;

	public JGRectangle () {}

	public JGRectangle (int x,int y,int width,int height) {
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
	}

	public JGRectangle(JGRectangle rec) {
		this.x = rec.x;
		this.y = rec.y;
		this.width = rec.width;
		this.height= rec.height;
	}

	/** Copy contents of source rectangle into this rectangle. */
	public void copyFrom(JGRectangle src) {
		x=src.x;
		y=src.y;
		width=src.width;
		height=src.height;
	}

	public boolean intersects(JGRectangle rec) {
		return    rec.x            < x+width
		       && rec.x+rec.width  > x
		       && rec.y            < y+height
		       && rec.y+rec.height > y;
	}
	public String toString() {
		return "JGRectangle("+x+","+y+","+width+","+height+")";
	}

	public boolean equals(JGRectangle rect) {
		// TODO Auto-generated method stub
		return (rect.x==x&&rect.y==y
				&&rect.width==width&&rect.height==height);
	}
	
	public void setRect(int x,int y,int width,int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public JGRectangle intersection(int rX, int rY, int rW, int rH) {
        int tx1 = this.x;
        int ty1 = this.y;
        int rx1 = rX;
        int ry1 = rY;
        long tx2 = tx1; tx2 += this.width;
        long ty2 = ty1; ty2 += this.height;
        long rx2 = rx1; rx2 += rW;
        long ry2 = ry1; ry2 += rH;
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) {
            tx2 = Integer.MIN_VALUE;
        }
        if (ty2 < Integer.MIN_VALUE) {
            ty2 = Integer.MIN_VALUE;
        }
        if(tx2<0){
        	tx2=0;
        }
        if(ty2<0){
        	ty2 = 0;
        }
        return new JGRectangle(tx1, ty1, (int) tx2, (int) ty2);
    }
	
	public JGRectangle intersection(JGRectangle rect){
		return intersection(rect.x, rect.y, rect.width, rect.height);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
}
