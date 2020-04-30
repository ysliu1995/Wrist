package com.ncbci.wrist;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

public class C2TL extends AsyncTask<String , Void, String>
{
    private static final String TAG = "Chinese2TaiL";
    private static final String token = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJ3bW1rcy5jc2llLmVkdS50dyIsInNlcnZpY2VfaWQiOiIxNyIsIm5iZiI6MTU3ODE5MzUwNSwic2NvcGVzIjoiMCIsInVzZXJfaWQiOiI4OCIsImlzcyI6IkpXVCIsInZlciI6MC4xLCJpYXQiOjE1NzgxOTM1MDUsInN1YiI6IiIsImlkIjoyNTUsImV4cCI6MTczNTg3MzUwNX0.r2bOx3KpZ2JhFq-QAMnKncMSIjOVRjF4vHF8VlIVx6S4jGgHqnW9075xBFNC-Cl6P7xhnVxdzgME9mSB6G3iUy_DfsdjUXUTUpxaNfgWmVIEpBz3r0_glUGccxEd154-zuFNffqs8oSEMCdoivYMzYG2v_lNjMjXwryHU3JrV5g";
    private String result=null;
    private Context mContext;

    public C2TL(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG,strings[0]);

        String outmsg = token + "@@@" + strings[0];

        Socket socket = new Socket();
        InetSocketAddress isa = new InetSocketAddress("140.116.245.149", 27005);

        try {
            //將outmsg轉成byte[]
            byte[] token_et_s = outmsg.getBytes();
            //用於計算outmsg的byte數
            byte[] g = new byte[4];

            g[0] = (byte) ((token_et_s.length & 0xff000000) >>> 24);
            g[1] = (byte) ((token_et_s.length & 0x00ff0000) >>> 16);
            g[2] = (byte) ((token_et_s.length & 0x0000ff00) >>> 8);
            g[3] = (byte) ((token_et_s.length & 0x000000ff));

            socket.connect(isa, 10000);

            BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
            // 送出字串
            out.write(byteconcate(g, token_et_s));
            out.flush();

            // 接收字串
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            byte[] b = new byte[1024];
            while (in.read(b) > 0)// <=0的話就是結束了
                result = new String(b, Charset.forName("UTF-8"));
            out.close();
            in.close();
            socket.close();
            return result;

        } catch (IOException ex) {
            Log.e(TAG, "doInBackground: request failed", ex);
            return ex.getMessage();
        } catch (NullPointerException ex) {
            Log.e(TAG, "doInBackground: received empty response", ex);
            return ex.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(s!=null)
        {
            Log.d(TAG,Integer.toString(s.length()));
            Log.d(TAG,s);
            String[] convert = s.split("\u0000");   //切除亂碼
            Log.d(TAG,convert[0]);
//            resultText.setText(convert[0]);
            new TVL(mContext).execute(convert[0]);
            return;
        }
    }

    private byte[] byteconcate(byte[] a, byte[] b)
    {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}