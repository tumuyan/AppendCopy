package com.github.xiaofei_dev.appendcopy.backstage;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.xiaofei_dev.appendcopy.R;


public final class CopyService extends Service {

    /**
     * 控制悬浮图标
     */
    private int i=0;
private String p_desc,p_html,p_text,q_desc,q_html,q_text;
private CharSequence p_label,q_label;
    private Boolean q_is_html,p_is_html;
    private TextView append_textview;
    private TextView float_text_0;
    private final int ADD_VIEW = 0;
    private final int ADD_APPEND_VIEW = 1;

    private LinearLayout iconFloatView;
    private LinearLayout iconFloatAppendView;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;

  //   private String text = "" ;
    private boolean isAddView;
    private final Handler mHandler = new Handler();
    private Runnable mAutoRemoveView;
    private int flag = 0;
  //  private StringBuilder mStringBuilder = new StringBuilder();
//    private StringBuilder tStringBuilder = new StringBuilder();
  //  public ClipData qClipData;
    public ClipData xClipData;
   // private ClipData  pClipData;
    private boolean isFrist = true;
    private static final String TAG = "CopyService";

    private ClipboardManager cmb;

    private int INFORM_STYLE=1;
    // %2==1 skip原因显示在悬浮球上
    //  < 1显示toast信息


    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager)(getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
        initView();
        initLayoutParams();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand: " + "flag: " + flag +"   "+"isAddView: "+isAddView);

//        if(!isFrist){
//            isFrist = true;
//            return super.onStartCommand(intent, flags, startId);
//        }
        if(intent != null){

            p_label= intent.getStringExtra("LABEL");
            p_desc= intent.getStringExtra("DESC");
            p_text= intent.getStringExtra("TEXT");
            p_is_html= Boolean.valueOf(intent.getStringExtra("is_html"));
            p_html="";
            if(p_is_html){
                {   //格式清理
                    String text = intent.getStringExtra("HTML");
                    text = text.replaceAll("\\sstyle=\"[^\"<>]+\"", "");
                    text = text.replaceAll("\\s*<span[^<>]*>\\s*</span>\\s*", "");
                    text = text.replaceAll("\\s*<p[^<>]*>\\s*</p>\\s*", "");
                    text = text.replaceAll("\\s*<div[^<>]*>\\s*</div>\\s*", "");
                    p_html = text;
                    Log.d("p_html", p_html);
                }
            }

        }else {
            return super.onStartCommand(intent, flags, startId);
        }
        if(flag == ADD_APPEND_VIEW){
            // tm  扩展剪贴板的部分；

            boolean clip_plus= float_p( );
            Log.w("Length0",""+q_text.length());

            if (clip_plus){

          //xtm      mStringBuilder.append(text);
                i=i+1;
                if (i<10){
                    append_textview.setText("+"+i);
                }else{
                    append_textview.setText(""+i);
                }
                if(INFORM_STYLE<1) {
                    Toast.makeText(getApplicationContext(), R.string.clip_plus, Toast.LENGTH_SHORT).show();
                }
                if(INFORM_STYLE%2==1){
                    append_textview.setBackgroundResource(R.drawable.s1);
                }

            }else{

//                Toast.makeText(getApplicationContext(),R.string.clip_skip,Toast.LENGTH_SHORT).show();

            }


        }else if(flag == ADD_VIEW && isAddView && isFrist){
            removeView();
            //已主动调用移除悬浮窗的情况下，取消自动延时移除悬浮窗
            mHandler.removeCallbacks(mAutoRemoveView);
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getApplication().startService(intent);
                }
            },510);
        }else if(flag == ADD_VIEW && !isAddView && isFrist){
            addView();
        }else {
            //由提示悬浮窗将内容复制至剪贴板的情况下不做任何操作，只是重置 isFrist 的值为 true
            isFrist = true;

            return super.onStartCommand(intent, flags, startId);
        }
        {  //移除了清空剪贴板的代码。始终觉得这个block是没用的，事实证明就是有作用。可见逻辑没盘清。我觉得原作者也没用盘清。
       //     cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
         //   ClipData data = ClipData.newPlainText("content", "");
    //        cmb.setPrimaryClip(data);
            return super.onStartCommand(intent, flags, startId);

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private synchronized void addView(){
        if(!isAddView){
            iconFloatView.clearAnimation();
            iconFloatView.setAlpha(0);
            iconFloatView.setVisibility(View.VISIBLE);
            iconFloatView.animate().alpha(1).setDuration(500)
                    .start();
            mWindowManager.addView(iconFloatView,mLayoutParams);
            isAddView = true;
            mHandler.postDelayed(mAutoRemoveView = new Runnable() {
                @Override
                public void run() {
                    if(isAddView){
                        removeView();
                    }
                }
            },2500);
        }

    }

    private synchronized void removeView(){
        if(isAddView){
//            iconFloatView.clearAnimation();
//            iconFloatView.setAlpha(1);
//            iconFloatView.setVisibility(View.VISIBLE);
//            iconFloatView.animate().alpha(0).setDuration(500)
//                    .start();
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if(isAddView){
//                        mWindowManager.removeView(iconFloatView);
//                        isAddView = false;
//                    }
//                }
//            },500);
            mWindowManager.removeView(iconFloatView);
            isAddView = false;
        }
    }

    /**
     *desc：添加拼接剪贴板内容提示悬浮窗
     */
    private void addAppendView(){
        mWindowManager.addView(iconFloatAppendView,mLayoutParams);
        flag = ADD_APPEND_VIEW;
        isFrist = false;
        if(INFORM_STYLE<1){
            Toast.makeText(getApplicationContext(),R.string.begin_listening_clip,Toast.LENGTH_SHORT).show();
        }

    }

    /**
     *desc：移除拼接剪贴板内容提示悬浮窗
     */
    private void removeAppendView(){
//        iconFloatAppendView.clearAnimation();
//        iconFloatAppendView.setAlpha(1);
//        iconFloatAppendView.setVisibility(View.VISIBLE);
//        iconFloatAppendView.animate().alpha(0).setDuration(500)
//                .start();
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mWindowManager.removeView(iconFloatAppendView);
//            }
//        },500);
        mWindowManager.removeView(iconFloatAppendView);
        flag = ADD_VIEW;
//        isFrist = true;
        i=0;
        //  qClipData=null;
        q_is_html=null;
        q_html=null;
        q_text=null;
        q_desc=null;
        q_label=null;
        xClipData=null;
    //xtm    mStringBuilder = mStringBuilder.delete(0,mStringBuilder.length());
//        stopSelf();
    }



/**
 *desc：初始化两个悬浮窗并添加点击事件
 */
    private void initView(){

        iconFloatView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.floating_icon_start,null);
        iconFloatAppendView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.floating_icon_append,null);
        append_textview=   (TextView)  iconFloatAppendView.findViewById(R.id.floating_icon_append_textView);
        float_text_0=   (TextView)  iconFloatView.findViewById(R.id.floating_icon_append_textView);


        iconFloatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeView();
                //已主动调用移除悬浮窗方法的情况下，取消自动延时移除悬浮窗
                mHandler.removeCallbacks(mAutoRemoveView);
                i=1;
                append_textview.setText("+1");
               // qClipData=pClipData;


                if(p_is_html){
                    q_is_html=true;
                    q_html=p_html;
                }else{
                    q_is_html=false;
                }
                    q_desc = p_desc;
                    q_label = p_label;
                    q_text = p_text;
                    addAppendView();
            }
        });

        iconFloatAppendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d(TAG, "onClick: " + mStringBuilder.toString());
                //将拼接好的内容复制至剪贴板
                cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //xtm  ClipData data = ClipData.newPlainText("content", mStringBuilder.toString());cmb.setPrimaryClip(data);
               //xxtm cmb.setPrimaryClip(qClipData);
                Log.w("Length1",""+q_text.length());

                if(q_is_html){

                    xClipData=ClipData.newHtmlText(q_label,q_text,q_html);
                }else{
                    xClipData = ClipData.newPlainText(q_label,q_text);
                }
                Log.d("end cmb",xClipData.toString());
                cmb.setPrimaryClip(xClipData);
            //    if(INFORM_STYLE<1) {
                    Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
              //  }
                removeAppendView();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stopSelf();
                    }
                },500);
            }
        });
    }

    /**
     *desc：初始化 mLayoutParams 设置其各字段的值
     */
    private void initLayoutParams(){
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        int screenHeight = point.y;

        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.gravity = Gravity.TOP|Gravity.END;
        /*if (Build.VERSION.SDK_INT > 24) {
            //7.0 以上的系统限制了 TOAST 类型悬浮窗的使用
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            mLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        }*/
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mLayoutParams.format = PixelFormat.TRANSLUCENT;
        mLayoutParams.y = screenHeight/3;
        mLayoutParams.width = iconFloatView.findViewById(R.id.floating_icon_append_textView).getLayoutParams().width;
        mLayoutParams.height = iconFloatView.findViewById(R.id.floating_icon_append_textView).getLayoutParams().height;
    }


    public boolean float_p( ){
            CharSequence label= p_label;
      if (q_desc.equals(p_desc) &  (p_desc.contains("text"))){
                    int p_length=p_text.length();
                    int q_length=q_text.length();
                    // 如果存在包含关系，直接skip；
                    if(p_length>0){
                        if (q_text.contains(p_text)){
                            if(INFORM_STYLE<1) {
                                Toast.makeText(getApplicationContext(), R.string.clip_skip1, Toast.LENGTH_SHORT).show();
                            }
                            if(INFORM_STYLE%2==1){
                                append_textview.setBackgroundResource(R.drawable.s2);
                                append_textview.setText("重");
                            }
                            return  false;
                        }else if (p_text.contains(q_text)){
                            q_text=p_text;
                            q_html=p_html;
           //                 Toast.makeText(getApplicationContext(),R.string.clip_skip2,Toast.LENGTH_SHORT).show();
                            return  true;
                        }else{
                            int get_length=p_length;
                            if (q_length<p_length){
                                get_length=q_length;
                            }

                            //  剪贴板去重合并
                            for(int i=1;i<get_length;i++){
                                String match_text=p_text.substring(0,i);
                                int match_index=q_text.lastIndexOf(match_text);

                                if (q_length-match_index==i){
                                   q_text=q_text+p_text.substring(i);

                                   if(q_is_html){
                                       //需要增加HTML部分的去重

                                       Boolean exit_html_branch=false;
                                        int p_length_html=p_html.length();
                                        int q_length_html=q_html.length();
                                       // 如果存在包含关系，直接skip；
                                       if(p_length_html>0){
                                           if (q_html.contains(p_html)){

                                           }else if (p_html.contains(q_html)){
                                                q_html=p_html;

                                           }else{



                                               {//简易使用match_text做一次粗糙的去重
                                                   int match_index_html_q=q_html.lastIndexOf(match_text);
                                                   int match_index_html_p=p_html.indexOf(match_text);

                                                   if((match_index_html_p>-1) & (match_index_html_q>-1)){
                                                       q_html=q_html.substring(0,match_index_html_q)+p_html.substring(match_index_html_p);
                                                   }else{
                                                       q_html=q_html+p_html;
                                                   }


                                               }


                                               /*
                                               int get_length_html=p_length_html;
                                               if (q_length_html<p_length_html){
                                                   get_length_html=q_length_html;
                                               }

                                               int j;
                                       for(j=1;j<get_length_html;j++){
                                           String match_html=p_html.substring(0,j);
                                           int match_index2=q_html.lastIndexOf(match_html);
                                           Log.d("new match loop","j,q_length_html,match_index,match_html:"+j+" "+q_length_html+""+match_html);
                                           if (q_length_html-match_index2==j){
                                               q_html=q_html+p_html.substring(j-1);
                                               j=get_length_html+1;
                                           }
                                       }  
                                       
                                       if(j>get_length_html){
                                       q_html=q_html+p_html;
                                       }
                                       */
                                           }
                                           }

                                   }


                                    return true;
                                }
                            }

                            //  如果去重失败，直接拼接；这里存在一个问题，HTML类型使用前边的loop是无法正确拼接的。
                            {
                               q_text=q_text+" \n"+p_text;
                                if(q_is_html){
                                    q_html=q_html+p_html;
                                }
                              //xtm  mClipboard.setPrimaryClip(mClipData);
                                return true;
                            }

                        }
                    }
                }else{
          if(INFORM_STYLE<1) {
              Toast.makeText(getApplicationContext(), R.string.clip_skip3, Toast.LENGTH_SHORT).show();
          }
          if(INFORM_STYLE%2==1){
              append_textview.setBackgroundResource(R.drawable.s2);
              append_textview.setText("异");
          }
      }


        if(INFORM_STYLE<1) {
            Toast.makeText(getApplicationContext(), R.string.clip_skip, Toast.LENGTH_SHORT).show();
        }
        return  false;
    }



}
