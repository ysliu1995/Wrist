package com.ncbci.wrist;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

//Taiwanese TTS
public class TVL extends AsyncTask<String , Void, String>
{
    private static final String TAG = "TaiwaneseVoice";
    private static final String token = "eyJhbGciOiJSUzUxMiIsInR5cCI6IkpXVCJ9.eyJhdWQiOiJ3bW1rcy5jc2llLmVkdS50dyIsInNlcnZpY2VfaWQiOiIxNyIsIm5iZiI6MTU3ODkwMDM2Miwic2NvcGVzIjoiMCIsInVzZXJfaWQiOiI5OCIsImlzcyI6IkpXVCIsInZlciI6MC4xLCJpYXQiOjE1Nzg5MDAzNjIsInN1YiI6IiIsImlkIjoyNTksImV4cCI6MTczNjU4MDM2Mn0.gxd8rJhwjk2OFW84eOcOwaqaXJaddEQM8xaz9_At4pUSmTmsY25rnBhAlf1XUUk1phdkQEPzOu4qys0Vyqnh9lY7IzFRs0sLXyvMF-NYFZpN7zq3Jg97QTPJCNnmavDmQ0MPSx3pyGKOYUNNJptCKL459sIE5pwUrrdt0P3sTcU";
    private String result = null;
    private Context mContext;

    public TVL(Context context) {
        mContext = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG,strings[0]);

        String outmsg = token + "@@@" + strings[0];

        Socket socket = new Socket();
        InetSocketAddress isa = new InetSocketAddress("140.116.245.147", 50006);

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

            // 接收
            BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
            String path =  mContext.getCacheDir() + "/output.wav";//"/storage/emulated/0/DCIM/output.wav";
//            String path = "/storage/emulated/0/DCIM/output.wav";
            FileOutputStream fos = new FileOutputStream(path);
            byte[] b = new byte[1024];
            int count = 0 ;
            while ((count = in.read(b)) > 0)// <=0的話就是結束了
            {
                Log.d("byte length : ", Integer.toString(b.length));
                fos.write(b, 0, count);
            }

            out.close();
            in.close();
            socket.close();
            result = path;
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
            MediaPlayer mp = new MediaPlayer();
            try {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setDataSource(s);
                mp.prepare();
                mp.start();
                while(mp.isPlaying()){}
            } catch (IOException e) {
                e.printStackTrace();
            }
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