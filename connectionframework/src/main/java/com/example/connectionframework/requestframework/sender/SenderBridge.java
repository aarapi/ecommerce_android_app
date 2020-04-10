package com.example.connectionframework.requestframework.sender;

import android.app.Activity;
import android.content.Context;

import com.example.connectionframework.BuildConfig;
import com.example.connectionframework.requestframework.components.CustomAlertBox;
import com.example.connectionframework.requestframework.constants.Constants;
import com.example.connectionframework.requestframework.constants.MessagingFrameworkConstant;
import com.example.connectionframework.requestframework.httpconnection.HttpConnectionException;
import com.example.connectionframework.requestframework.httpconnection.HttpUtil;
import com.example.connectionframework.requestframework.json.JsonWrapper;
import com.example.connectionframework.requestframework.json.Serializer;
import com.example.connectionframework.requestframework.receiver.ReceiverActivity;
import com.example.connectionframework.requestframework.receiver.ReceiverBridgeInterface;
import com.example.connectionframework.requestframework.tasks.SendRequest;
import com.rahman.dialog.Activity.SmartDialog;
import com.rahman.dialog.ListenerCallBack.SmartDialogClickListener;

import java.util.ArrayList;
import java.util.List;


public class SenderBridge {
    private String urlConnection = BuildConfig.HOST_URL;
    private Context context;
    public SenderBridge(Context context) {
        this.context = context;
    }

    public List<Object> sendMessage(Request request){
        String jsonFormated = Serializer.toJson(request);
        String jsonResponse = null;

        try {
             jsonResponse = HttpUtil.getResponse(this.urlConnection,
                    jsonFormated);
        }catch (HttpConnectionException e){
            if (e.getMessage() != null && e.getMessage().equals(Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE)){
                jsonResponse = Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE;
            }else if (e.getMessage() != null && e.getMessage().equals(Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE)){
                jsonResponse = Constants.Application.CONNECTION_OTHER_EXCEPTION;
            }else if (e.getMessage() != null && e.getMessage().equals(Constants.Application.CONNECTION_OTHER_EXCEPTION)){
                jsonResponse = Constants.Application.CONNECTION_OTHER_EXCEPTION;
            }
            Repository.newInstance().setMessageError(jsonResponse);
            return null;
        }finally {
            Message message = returnMessage(jsonResponse);



            return message.getData();
        }
    }


    private Message returnMessage(String response){
        Message message = new Message();

        if(response.equals(Constants.Application.CONNECTION_TIMED_OUT_ERROR_MESSAGE)){
            message.setStatusCode(MessagingFrameworkConstant.STATUS_CODES.ConnectionTimedOut);
            Repository.newInstance().setMessageError(response);
        }else if(response.equals(Constants.Application.CONNECTION_OTHER_EXCEPTION)){
            message.setStatusCode(MessagingFrameworkConstant.STATUS_CODES.ConnectionFailed);
            Repository.newInstance().setMessageError(response);
        }
        else {
            message = JsonWrapper.getobject(response);
        }

        if (message.getData() != null) {
            if (message.getData().size() == 0) {
                Repository.newInstance().setMessageError("Nuk u gjet asnje produkt");
                message.setData(null);
            }
        }if(message.getStatusCode() != MessagingFrameworkConstant.STATUS_CODES.Success){
            if (message.getStatusCode() == MessagingFrameworkConstant.STATUS_CODES.ConnectionFailed){
                Repository.newInstance().setMessageError("Kontrolloni Lidhjen me Rrjetin");
            }else if (message.getStatusCode() == MessagingFrameworkConstant.STATUS_CODES.Error){
                Repository.newInstance().setMessageError("Problem ne server!");
            }else if (message.getStatusCode() == MessagingFrameworkConstant.STATUS_CODES.ConnectionTimedOut){
                Repository.newInstance().setMessageError("Lidhja u nderpre!");
            }

            message.setData(null);
            message.setAction(Repository.newInstance().getAction());
        }

        Repository.newInstance().setStatusCode(message.getStatusCode());

        return message;
    }


    public void sendMessageAssync(Request request, final Context context){

        ReceiverBridgeInterface receiverBridgeInterface = new ReceiverBridgeInterface() {
            @Override
            public void onDataReceive(String jsonrRsponse) {
                ReceiverActivity receiverBridgeInterfaceActivity = (ReceiverActivity) context;
                Message message = returnMessage(jsonrRsponse);
                receiverBridgeInterfaceActivity.onDataReceive(message.getAction(),message.getData());
            }
        };
        SendRequest sendRequest = new SendRequest(urlConnection, receiverBridgeInterface);

        Repository.newInstance().setAction(request.getAction());

        sendRequest.execute(Serializer.toJson(request));

    }
}
