package com.vuizcomm.frontier;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import java.text.NumberFormat;
import java.util.Locale;
public final class FrontierView extends View {
    private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    private final GameState state;
    private final MainActivity activity;
    private final NumberFormat numbers=NumberFormat.getIntegerInstance(Locale.US);
    public FrontierView(MainActivity a){super(a);activity=a;state=SaveManager.load(a);setBackgroundColor(Color.rgb(5,10,18));}
    public void save(){SaveManager.save(activity,state);}
    @Override protected void onDraw(Canvas c){
        int w=getWidth(),h=getHeight();
        paint.setColor(Color.rgb(5,10,18));c.drawRect(0,0,w,h,paint);
        text(c,"VUIZCOMM",24,38,30,Color.WHITE,true,Paint.Align.LEFT);
        text(c,"FRONTIER COMMAND",24,66,15,Color.rgb(92,190,255),true,Paint.Align.LEFT);
        text(c,state.nation+" • "+state.region,w-24,48,14,Color.LTGRAY,false,Paint.Align.RIGHT);
        panel(c,18,90,w-18,214);
        text(c,"NATIONAL STATUS",34,119,13,Color.rgb(120,210,255),true,Paint.Align.LEFT);
        text(c,"Population",34,151,13,Color.LTGRAY,false,Paint.Align.LEFT);
        text(c,numbers.format(state.population),34,181,24,Color.WHITE,true,Paint.Align.LEFT);
        text(c,"Treasury",w/2f,151,13,Color.LTGRAY,false,Paint.Align.LEFT);
        text(c,String.format(Locale.US,"%,.0f",state.treasury),w/2f,181,24,Color.WHITE,true,Paint.Align.LEFT);
        stat(c,18,232,w/2-8,"Influence",state.influence); stat(c,w/2+8,232,w-18,"Stability",state.stability);
        stat(c,18,300,w/2-8,"Industry",state.industry); stat(c,w/2+8,300,w-18,"Diplomacy",state.diplomacy);
        panel(c,18,372,w-18,Math.max(610,h-112));
        text(c,"VUIZCOMM MAP",34,401,13,Color.rgb(120,210,255),true,Paint.Align.LEFT);
        drawMap(c,34,420,w-34,Math.min(h-145,660));
        button(c,18,h-88,w/2-8,h-18,"ADVANCE",Color.rgb(25,125,190));
        button(c,w/2+8,h-88,w-18,h-18,"DIPLOMACY",Color.rgb(40,120,75));
    }
    private void panel(Canvas c,float l,float t,float r,float b){paint.setColor(Color.rgb(18,35,55));c.drawRoundRect(l,t,r,b,20,20,paint);}
    private void drawMap(Canvas c,float l,float t,float r,float b){
        paint.setColor(Color.rgb(7,37,63));c.drawRect(l,t,r,b,paint);
        Path red=new Path();red.moveTo(l+18,t+35);red.lineTo(l+145,t+15);red.lineTo(l+230,t+65);red.lineTo(l+195,t+140);red.lineTo(l+95,t+165);red.lineTo(l+25,t+125);red.close();
        paint.setColor(Color.rgb(225,35,45));c.drawPath(red,paint);
        Path purple=new Path();purple.moveTo(l+25,t+190);purple.lineTo(l+120,t+155);purple.lineTo(l+205,t+210);purple.lineTo(l+175,t+315);purple.lineTo(l+65,t+340);purple.lineTo(l+20,t+275);purple.close();
        paint.setColor(Color.rgb(86,78,145));c.drawPath(purple,paint);
        paint.setColor(Color.rgb(78,190,225));c.drawCircle(r-90,t+82,72,paint);
        paint.setColor(Color.rgb(42,112,45));c.drawCircle(r-95,b-80,85,paint);
        text(c,"VZCOMM",l+72,t+94,15,Color.WHITE,true,Paint.Align.LEFT);
        text(c,"SUB-VZ",l+78,t+265,14,Color.WHITE,true,Paint.Align.LEFT);
    }
    private void stat(Canvas c,int l,int t,int r,String label,int value){paint.setColor(Color.rgb(18,35,55));c.drawRoundRect(l,t,r,t+56,14,14,paint);text(c,label,l+14,t+22,13,Color.LTGRAY,false,Paint.Align.LEFT);text(c,value+"%",r-14,t+38,19,Color.WHITE,true,Paint.Align.RIGHT);}
    private void button(Canvas c,int l,int t,int r,int b,String label,int color){paint.setColor(color);c.drawRoundRect(l,t,r,b,16,16,paint);text(c,label,(l+r)/2f,t+43,15,Color.WHITE,true,Paint.Align.CENTER);}
    private void text(Canvas c,String s,float x,float y,float size,int color,boolean bold,Paint.Align align){paint.setColor(color);paint.setTextSize(size);paint.setTextAlign(align);paint.setTypeface(Typeface.create("sans",bold?Typeface.BOLD:Typeface.NORMAL));c.drawText(s,x,y,paint);}
    @Override public boolean onTouchEvent(MotionEvent e){
        if(e.getAction()==MotionEvent.ACTION_UP && e.getY()>=getHeight()-105){
            if(e.getX()<getWidth()/2f){state.tick();save();activity.notice("Frontier advanced.");}
            else{state.diplomacy=Math.min(100,state.diplomacy+5);state.influence=Math.min(100,state.influence+2);if(state.happenings.size()>=12)state.happenings.remove(0);state.happenings.add("A new diplomatic channel opened.");save();activity.notice("Diplomacy improved.");}
            invalidate();return true;
        }
        return true;
    }
}
