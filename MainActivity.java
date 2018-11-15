package com.example.pi_test1.myapplication4;

import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

public static class Sleep {
    public static void main(String[] args ) {
        try {
            Thread.sleep(10000); //10초 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


// 웹사이트 주소를 저장할 변수
String classBoardUrl = "http://www.classboard.co.kr/d/";
Handler handler = new Handler(); // 화면에 그려주기 위한 객체

private WebView mWebView;
private WebSettings mWebSettings;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mWebView = (WebView)findViewById(R.id.webview_login);
    mWebView.setWebViewClient(new WebViewClient());
    mWebSettings = mWebView.getSettings();
    mWebSettings.setJavaScriptEnabled(true);

    String readkey="";
    readkey=MakeFile();

    StringBuffer dkey = makeDeviceKey();
    String clientUrl = classBoardUrl + dkey;
    loadHtml(clientUrl); // 웹에서 html 읽어오기

    mWebView.loadUrl("http://www.classboard.co.kr/d/"+readkey);


} // end of onCreate

public void loadHtml(String clientUrl) { // 웹에서 html 읽어오기
    String aa;

    final String buff = clientUrl + "/info" ;

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            final StringBuffer sb = new StringBuffer();
            try {
                URL url = new URL(buff);
                System.out.println(buff);
                HttpURLConnection conn =
                        (HttpURLConnection)url.openConnection();// 접속
                if (conn != null) {
                    conn.setConnectTimeout(2000);
                    conn.setUseCaches(false);
                    if (conn.getResponseCode()
                            ==HttpURLConnection.HTTP_OK){
                        //    데이터 읽기
                        BufferedReader br
                                = new BufferedReader(new InputStreamReader
                                (conn.getInputStream(),"euc-kr"));//"utf-8"
                        while(true) {
                            String line = br.readLine();
                            if (line == null) break;
                            sb.append(line+"\n");
                        }
                        br.close(); // 스트림 해제
                    }
                    conn.disconnect(); // 연결 끊기
                }

                JSONObject config = new JSONObject(sb.toString());
                System.out.println(config);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    });
    t.start(); // 쓰레드 시작
}

public String MakeFile() {
            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            sdPath += "/MyDir";

            File file = new File(sdPath);
            file.mkdirs();

            sdPath += "test2.txt";
            file = new File(sdPath);

    try { //디렉토리 생성
        file.createNewFile();
    } catch (IOException e) {
        e.printStackTrace();
    }

    String readStr = "";
    String str=null;

    try { //파일이 있을떄
        BufferedReader br1 = new BufferedReader(new FileReader(getFilesDir() + "test2.txt"));
    } catch (FileNotFoundException e) { // 파일이 없을때
        e.printStackTrace();

        try { //txt file 저장
            StringBuffer dkey1 = makeDeviceKey();
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(getFilesDir() + "test2.txt", true));
            bw1.write(String.valueOf(dkey1));
            bw1.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    try {
        BufferedReader br1 = new BufferedReader(
                new FileReader(getFilesDir() + "test2.txt"));
        while((str = br1.readLine()) != null)
            readStr += str +"\n";
        br1.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
            return readStr;
}

public StringBuffer makeDeviceKey(){
    StringBuffer temp = new StringBuffer();
    Random rnd = new Random();
    for (int i = 0; i < 12; i++) {
        int rIndex = rnd.nextInt(2);
        switch (rIndex) {
            case 0:
                // A-Z
                temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                break;
            case 1:
                // 0-9
                temp.append((rnd.nextInt(10)));
                break;
        }
    }
    return temp;
}
}
