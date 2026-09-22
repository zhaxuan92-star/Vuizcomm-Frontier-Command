package com.vuizcomm.frontier;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import java.text.NumberFormat;
import java.util.Locale;

public final class FrontierView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GameState state;
    private final MainActivity activity;
    private final NumberFormat nf = NumberFormat.getIntegerInstance(Locale.US);
    private float downX, downY;

    public FrontierView(MainActivity a) {
        super(a);
        activity=a;
        state=SaveManager.load(a);
        p.setTypeface(Typeface.create("sans", Typeface.NORMAL));
        setBackgroundColor(Color.rgb(5,10,18));
    }

    void save(){ SaveManager.save(activity,state); }

    @Override protected void onDraw(Canvas c) {
        super.onDraw(c);
        int w=getWidth(), h=getHeight();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(8,17,31)); c.drawRect(0,0,w,h,p);

        text(c,"VUIZCOMM",24,34,34,Color.WHITE,true);
        text(c,"FRONTIER COMMAND",24,68,17,Color.rgb(92,190,255),true);
        text(c,state.nation+"  •  "+state.region,w-24,48,15,Color.LTGRAY,false,Paint.Align.RIGHT);

        p.setColor(Color.rgb(18,35,55)); c.drawRoundRect(18,92,w-18,220,20,20,p);
        text(c,"NATIONAL STATUS",34,122,14,Color.rgb(120,210,255),true);
        text(c,"Population",34,153,14,Color.LTGRAY,false);
        text(c,nf.format(state.population),34,181,25,Color.WHITE,true);
        text(c,"Treasury",w/2,153,14,Color.LTGRAY,false);
        text(c,String.format(Locale.US,"%,.0f",state.treasury),w/2,181,25,Color.WHITE,true);

        stat(c,18,238,w/2-8,"Influence",state.influence);
        stat(c,w/2+8,238,w-18,"Stability",state.stability);
        stat(c,18,310,w/2-8,"Industry",state.industry);
        stat(c,w/2+8,310,w-18,"Diplomacy",state.diplomacy);

        p.setColor(Color.rgb(18,35,55)); c.drawRoundRect(18,382,w-18,h-112,20,20,p);
        text(c,"VUIZCOMM MAP",34,412,14,Color.rgb(120,210,255),true);
        drawMap(c,34,432,w-34,Math.min(h-150,650));

        button(c,18,h-92,w/2-8,h-22,"ADVANCE",Color.rgb(25,125,190));
        button(c,w/2+8,h-92,w-18,h-22,"DIPLOMACY",Color.rgb(40,120,75));

        if(happeningsVisible(c,h)) {}
    }

    private boolean happeningsVisible(Canvas c,int h){
        return false;
    }

    private void drawMap(Canvas c,float l,float t,float r,float b){
        p.setColor(Color.rgb(7,37,63)); c.drawRect(l,t,r,b,p);
        Path red=new Path(); red.moveTo(l+18,t+35); red.lineTo(l+145,t+15); red.lineTo(l+230,t+65);
        red.lineTo(l+195,t+140); red.lineTo(l+95,t+165); red.lineTo(l+25,t+125); red.close();
        p.setColor(Color.rgb(225,35,45)); c.drawPath(red,p);
        Path purple=new Path(); purple.moveTo(l+25,t+190); purple.lineTo(l+120,t+155); purple.lineTo(l+205,t+210);
        purple.lineTo(l+175,t+315); purple.lineTo(l+65,t+340); purple.lineTo(l+20,t+275); purple.close();
        p.setColor(Color.rgb(86,78,145)); c.drawPath(purple,p);
        p.setColor(Color.rgb(78,190,225)); c.drawCircle(r-85,t+85,72,p);
        p.setColor(Color.rgb(42,112,45)); c.drawCircle(r-95,b-80,85,p);
        text(c,"VZCOMM",l+72,t+94,15,Color.WHITE,true);
        text(c,"SUB-VZ",l+78,t+265,14,Color.WHITE,true);
    }

    private void stat(Canvas c,int l,int top,int r,String label,int value){
        p.setColor(Color.rgb(18,35,55)); c.drawRoundRect(l,top,r,top+58,14,14,p);
        text(c,label,l+14,top+23,13,Color.LTGRAY,false);
        text(c,value+"%",r-14,top+38,20,Color.WHITE,true,Paint.Align.RIGHT);
    }

    private void button(Canvas c,int l,int t,int r,int b,String label,int color){
        p.setColor(color); c.drawRoundRect(l,t,r,b,16,16,p);
        text(c,label,(l+r)/2,t+42,15,Color.WHITE,true,Paint.Align.CENTER);
    }

    private void text(Canvas c,String s,float x,float y,float size,int color,boolean bold){
        text(c,s,x,y,size,color,bold,Paint.Align.LEFT);
    }
    private void text(Canvas c,String s,float x,float y,float size,int color,boolean bold,Paint.Align align){
        p.setColor(color); p.setTextSize(size); p.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));
        p.setTextAlign(align); c.drawText(s,x,y,p);
    }

    @Override public boolean onTouchEvent(MotionEvent e){
        if(e.getAction()==MotionEvent.ACTION_DOWN){downX=e.getX();downY=e.getY();return true;}
        if(e.getAction()==MotionEvent.ACTION_UP){
            float y=e.getY();
            if(y>getHeight()-105 && y<getHeight()-10){
                if(e.getX()<getWidth()/2){
                    state.tick(); save(); activity.notice("Frontier advanced."); invalidate();
                } else {
                    state.diplomacy=Math.min(100,state.diplomacy+5);
                    state.influence=Math.min(100,state.influence+2);
                    state.happenings.add("A new diplomatic channel opened.");
                    save(); activity.notice("Diplomacy improved."); invalidate();
                }
            }
            return true;
        }
        return true;
    }
}
