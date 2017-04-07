package diyikeji.homefarm;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 2017/2/13.
 */

public class Zhilian extends Activity
{

    private byte[] message;
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 0:
                    Toast.makeText(getApplicationContext(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;
    private String wifiad = "";
    private String myip;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhilian);

        //读取本地IP
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        myip = (ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff);

        Integer port = 8898;
        // 接收的字节大小，客户端发送的数据不能超过这个大小
        message = new byte[1024];
        try
        {
            // 建立Socket连接
            datagramSocket = new DatagramSocket(port);
            datagramPacket = new DatagramPacket(message, message.length);
            new Thread()
            {
                @Override
                public void run()
                {
                    super.run();
                    try
                    {
                        while (true)
                        {
                            // 准备接收数据
                            message = new byte[1024];
                            datagramPacket = new DatagramPacket(message, message.length);
                            datagramSocket.receive(datagramPacket);
                            String a = new String(datagramPacket.getData());
                            if (wifiad.equals(""))
                            {
                                wifiad = a.substring(0, a.indexOf(","));
                            }
                            Log.d("UDP Demo", a);
                            Message msg = Message.obtain();
                            msg.what = 0;
                            msg.obj = a;
                            handler.sendMessage(msg);
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
        catch (SocketException e)
        {
            e.printStackTrace();
        }

        Button fasong = (Button) findViewById(R.id.fasong);
        fasong.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        super.run();
                        String senddata = "csncat@yzr";
                        int server_port = 48899;
                        InetAddress local = null;
                        try
                        {
                            // 换成服务器端IP
                            local = InetAddress.getByName("255.255.255.255");
                        }
                        catch (UnknownHostException e)
                        {
                            e.printStackTrace();
                        }
                        int msg_length = senddata.length();
                        byte[] messagemessageByte = senddata.getBytes();
                        DatagramPacket p = new DatagramPacket(messagemessageByte, msg_length, local, server_port);
                        try
                        {
                            datagramSocket.send(p);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        Button fasong2 = (Button) findViewById(R.id.fasong2);
        fasong2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        super.run();
                        String senddata = "AT+NETP=UDP,SERVER,8898," + myip + "\r";
                        int server_port = 48899;
                        InetAddress local = null;
                        try
                        {
                            // 换成服务器端IP
                            local = InetAddress.getByName(wifiad);
                        }
                        catch (UnknownHostException e)
                        {
                            e.printStackTrace();
                        }
                        int msg_length = senddata.length();
                        byte[] messagemessageByte = senddata.getBytes();
                        DatagramPacket p = new DatagramPacket(messagemessageByte, msg_length, local, server_port);
                        try
                        {
                            datagramSocket.send(p);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        Button fasong3 = (Button) findViewById(R.id.fasong3);
        fasong3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        super.run();
                        String senddata = "AT+TMODE=throughput\r";
                        int server_port = 48899;
                        InetAddress local = null;
                        try
                        {
                            // 换成服务器端IP
                            local = InetAddress.getByName(wifiad);
                        }
                        catch (UnknownHostException e)
                        {
                            e.printStackTrace();
                        }
                        int msg_length = senddata.length();
                        byte[] messagemessageByte = senddata.getBytes();
                        DatagramPacket p = new DatagramPacket(messagemessageByte, msg_length, local, server_port);
                        try
                        {
                            datagramSocket.send(p);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        final EditText fasongneirong = (EditText) findViewById(R.id.fasongneirong);
        Button fasong4 = (Button) findViewById(R.id.fasong4);
        fasong4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        super.run();
                        String senddata = "^UDP:" + fasongneirong.getText()+"|";
                        int server_port = 8898;
                        InetAddress local = null;
                        try
                        {
                            // 换成服务器端IP
                            local = InetAddress.getByName(wifiad);
                        }
                        catch (UnknownHostException e)
                        {
                            e.printStackTrace();
                        }
                        int msg_length = senddata.length();
                        byte[] messagemessageByte = senddata.getBytes();
                        DatagramPacket p = new DatagramPacket(messagemessageByte, msg_length, local, server_port);
                        try
                        {
                            datagramSocket.send(p);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

        Button fasong5 = (Button) findViewById(R.id.fasong5);
        fasong5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        super.run();
                        String senddata = "AT+Z\r";
                        int server_port = 48899;
                        InetAddress local = null;
                        try
                        {
                            // 换成服务器端IP
                            local = InetAddress.getByName(wifiad);
                        }
                        catch (UnknownHostException e)
                        {
                            e.printStackTrace();
                        }
                        int msg_length = senddata.length();
                        byte[] messagemessageByte = senddata.getBytes();
                        DatagramPacket p = new DatagramPacket(messagemessageByte, msg_length, local, server_port);
                        try
                        {
                            datagramSocket.send(p);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });
    }
}
