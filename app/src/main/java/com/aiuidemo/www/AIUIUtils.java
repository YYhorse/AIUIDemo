package com.aiuidemo.www;

import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;

import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by yy on 2018/1/8.
 */
public class AIUIUtils {
    public static AIUIAgent mAIUIAgent;
    public static final String ACTION_AIUIRECORD = "action.aiuirecord";
    /***********************************************************************************************
     * * 函数名称: void SpeechInit()
     * * 功能说明：语音初始化
     **********************************************************************************************/
    public static void SpeechInit() {
        SpeechUtility.createUtility(BaseApplication.getInstance(), SpeechConstant.APPID + "=5a532ffc");         //将“12345678”替换成您申请的APPID，申请地址：http://open.voicecloud.cn
        //创建AIUIAgent
        mAIUIAgent = AIUIAgent.createAgent(BaseApplication.getInstance(), getAIUIParams(), mAIUIListener);
        //发送`CMD_START`消息，使AIUI处于工作状态
        AIUIMessage startMsg = new AIUIMessage(AIUIConstant.CMD_START, 0, 0, null, null);
        mAIUIAgent.sendMessage(startMsg);
    }

    public static String getAIUIParams() {
        String params = "";
        AssetManager assetManager = BaseApplication.getInstance().getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static AIUIListener mAIUIListener = new AIUIListener() {
        @Override
        public void onEvent(AIUIEvent event) {
            switch (event.eventType) {
                //唤醒事件
                case AIUIConstant.EVENT_WAKEUP: {
                    Log.e("YY", "唤醒事件");
                    break;
                }
                //结果事件（包含听写，语义，离线语法结果）
                case AIUIConstant.EVENT_RESULT: {
//                    Log.e("YY","结果="+event.info);
                    //解析结果
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));
                            String sub = params.optString("sub");
                            if ("nlp".equals(sub)) {
                                // 解析得到语义结果
                                String resultStr = cntJson.optString("intent");
                                //-----发送广播通知UI-------//
                                final Intent intent = new Intent();
                                intent.setAction(ACTION_AIUIRECORD);
                                intent.putExtra("AIUIinfo", resultStr);
                                BaseApplication.getInstance().sendBroadcast(intent);
                                //------------------------//

                                Log.e("AIUI返回：", resultStr);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    break;
                }
                //休眠事件
                case AIUIConstant.EVENT_SLEEP: {
                    Log.e("YY", "休眠事件");
                    break;
                }
                case AIUIConstant.EVENT_START_RECORD: {
                    Log.e("YY", "开始录音");
                }
                break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    Log.e("YY", "停止录音");
                }
                break;
                //错误事件
                case AIUIConstant.EVENT_ERROR: {
                    //-----发送广播通知UI-------//
                    final Intent intent = new Intent();
                    intent.setAction(ACTION_AIUIRECORD);
                    intent.putExtra("AIUIinfo", "错误事件" + event.info);
                    BaseApplication.getInstance().sendBroadcast(intent);
                    Log.e("YY", "错误事件=" + event.info);
                    break;
                }
            }
        }
    };
}
