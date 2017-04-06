package bean;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by Administrator on 2017/3/13.
 */

public class UDP
{
    //UDP变量
    public DatagramSocket datagramSocket;
    public DatagramSocket datagramSocket2;//发送命令端口
    public DatagramPacket datagramPacket;
    public String wifiad = "255.255.255.255";
    public String myip;
    public byte[] message = new byte[1024];
    public int server_port = 48899;
    public Integer port = 8899;
    public Boolean tongxingmod = false;
    public WifiManager wifiManager;
    public final WifiInfo wifiInfo;

    //UDP监听  head
    /**
     * 监听UDP接收,需要在子线程里进行
     *
     * @param head 为真时要求内容前4个字符为 "UDP:"
     * @return 返回接收到的内容
     */
    public String UDPreceive(Boolean head)
    {
        String rdata = "";
        try
        {
            // 准备接收数据
            message = new byte[1024];
            datagramPacket = new DatagramPacket(message, message.length);
            if (head)
            {
                datagramSocket.receive(datagramPacket);
                rdata = new String(datagramPacket.getData());
                if (!rdata.contains("UDP:"))
                {
                    return "";
                }
            } else
            {
                datagramSocket2.receive(datagramPacket);
                rdata = new String(datagramPacket.getData());
            }
            Log.d("UDP Demo", rdata);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return rdata;
    }

    /**
     * UDP发送数据
     *
     * @param senddata 需要发送的数据
     * @param leixing  false为发送命令到48899端口
     *                 ture为发送数据到8899端口
     */
    public void UDPsend(final String senddata, final Boolean leixing)
    {
        //发送广播连接设备
        new Thread()
        {
            @Override
            public void run()
            {
                super.run();
                String data = senddata;
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
                if (leixing)
                {
                    //添加包头包尾
                    data = "^" + senddata + "|";
                }
                int msg_length = data.length();
                byte[] messagemessageByte = data.getBytes();
                DatagramPacket p = new DatagramPacket(messagemessageByte, msg_length, local, leixing ? port : server_port);
                try
                {
                    if (leixing)
                    {
                        datagramSocket.send(p);
                        Log.e("datagramSocket",data);
                    } else
                    {
                        datagramSocket2.send(p);
                        Log.e("datagramSocket2",data);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public UDP(Context context)
    {
        //读取本地IP
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        final int ipAddress = wifiInfo.getIpAddress();
        myip = (ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "." + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff);
        Random rand = new Random();
        port = rand.nextInt(8899);
        port += 40000;
        try
        {
            datagramSocket = new DatagramSocket(port);
            datagramSocket2 = new DatagramSocket(port - 1);
            datagramSocket.setSoTimeout(6000);
            datagramSocket2.setSoTimeout(3000);
            datagramPacket = new DatagramPacket(message, message.length);
        }
        catch (SocketException e1)
        {
            e1.printStackTrace();
        }
    }
}
