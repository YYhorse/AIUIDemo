package com.aiuidemo.www;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIMessage;

public class MainActivity extends AppCompatActivity {
    public TextView mNlpText,result_txt;
    public static final String ACTION_AIUIRECORD = "action.aiuirecord";
    AIUIBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RegisterBroad();
        mNlpText = (TextView) findViewById(R.id.mNlpText);
        result_txt = (TextView) findViewById(R.id.result_txt);
        mNlpText.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void refreshAlarmView(TextView textView,String msg){
        textView.append(msg);
        int offset=textView.getLineCount()*textView.getLineHeight();
        if(offset>(textView.getHeight()-textView.getLineHeight()-20)){
            textView.scrollTo(0,offset-textView.getHeight()+textView.getLineHeight()+20);
        }
    }
    private void RegisterBroad() {
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_AIUIRECORD);
        broadcastReceiver = new AIUIBroadcastReceiver();
        registerReceiver(broadcastReceiver, filter);
    }

    public void ClickTest3Method(View view){
        AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
        AIUIUtils.mAIUIAgent.sendMessage(wakeupMsg);
        refreshAlarmView(mNlpText,"人工智能已唤醒");
    }

//    public void ClickTest2Method(View view){
//        AIUIMessage msg = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, "data_type=text", "今天天气怎么样".getBytes());
//        AIUIUtils.mAIUIAgent.sendMessage(msg);
//    }

    public void ClickCleanMethod(View viwe){
        mNlpText.setText("");
    }
    public void ClickRecordMethod(View view){
        // 打开AIUI内部录音机，开始录音
        String params = "sample_rate=16000,data_type=audio";
        AIUIMessage writeMsg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, params, null );
        AIUIUtils.mAIUIAgent.sendMessage(writeMsg);
        refreshAlarmView(mNlpText, "人工智能工作");
    }

    /*
        AIUI广播事件
     */
    private class AIUIBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("AIUIinfo").length()>4) {
                if (intent.getStringExtra("AIUIinfo").substring(0, 4).compareTo("错误事件") == 0) {
                    Toast.makeText(MainActivity.this, intent.getStringExtra("AIUIinfo"), Toast.LENGTH_LONG).show();
                }
                refreshAlarmView(mNlpText, intent.getStringExtra("AIUIinfo"));
                //---------解析数据------------//
                Gson gson = new Gson();
                AiUiJson aiuiJson = gson.fromJson(intent.getStringExtra("AIUIinfo"), AiUiJson.class);
                result_txt.setText("");
                result_txt.append("询问事情："+aiuiJson.getText());
                result_txt.append("\n给出回答:"+aiuiJson.getAnswer().getText());
                result_txt.append("\n相关信息如下");
//                for(int i=0;i<aiuiJson.getData().getResult().size();i++){
//                    result_txt.append("【"+aiuiJson.getData().getResult().get(i).getName()+"】");
//                    result_txt.append(aiuiJson.getData().getResult().get(i).getContent());
//                    result_txt.append(aiuiJson.getData().getResult().get(i).getUrl());
//                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        super.onDestroy();
        // 注销广播
        unregisterReceiver(broadcastReceiver);
    }
}
