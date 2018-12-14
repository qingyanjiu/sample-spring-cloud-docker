package louis.demo.sidecar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Copyright 2015 HP
 * All right reserved.
 * UDP服务类.  采集syslog
 *
 * @version 1.0
 * Creation date: 2015-8-18 - 下午16:32:31
 */
public class UdpServerSocket {
    private byte[] buffer = new byte[1024];

    private DatagramSocket ds = null;

    private DatagramPacket packet = null;

    private InetSocketAddress socketAddress = null;

    private String orgIp;

    private File logFile = null;
    private FileWriter fw = null;
    private BufferedWriter bw = null;

    /**
     * 构造函数，绑定主机和端口.
     *
     * @param host 主机
     * @param port 端口
     * @throws Exception
     */
    public UdpServerSocket(String host, int port) throws Exception {
        socketAddress = new InetSocketAddress(host, port);
        ds = new DatagramSocket(port);
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        logFile = new File("C:\\Users\\louisliu\\Desktop\\poc\\"+date+".log");
        // if file doesnt exists, then create it
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        fw = new FileWriter(logFile.getAbsoluteFile());
        bw = new BufferedWriter(fw);
        System.out.println("--------------service start----------------");
    }

    public final String getOrgIp() {
        return orgIp;
    }

    /**
     * 设置超时时间，该方法必须在bind方法之后使用.
     *
     * @param timeout 超时时间
     * @throws Exception
     */
    public final void setSoTimeout(int timeout) throws Exception {
        ds.setSoTimeout(timeout);
    }

    /**
     * 获得超时时间.
     *
     * @return 返回超时时间.
     * @throws Exception
     */
    public final int getSoTimeout() throws Exception {
        return ds.getSoTimeout();
    }

    /**
     * 绑定监听地址和端口.
     *
     * @param host 主机IP
     * @param port 端口
     * @throws SocketException
     */
    public final void bind(String host, int port) throws SocketException {
        socketAddress = new InetSocketAddress(host, port);
        ds = new DatagramSocket(socketAddress);
    }


    /**
     * 接收数据包，该方法会造成线程阻塞.
     *
     * @return 返回接收的数据串信息
     * @throws IOException
     */
    public final String receive() throws IOException {
        packet = new DatagramPacket(buffer, buffer.length);
        ds.receive(packet);
        orgIp = packet.getAddress().getHostAddress();
        String info = new String(packet.getData(), 0, packet.getLength());
        try {
            bw.write(info);
            bw.flush();
        } catch (Exception e) {
            bw.close();
        } finally {
            bw.flush();
        }
        System.out.println(info);
        //System.out.println("CONTENT="+info+":SOURCE_IP="+packet.getAddress().getHostAddress()+"SOURCE_PORT:"+packet.getPort());
        return info;
    }

    /**
     * 将响应包发送给请求端.
     *
     * @param bytes 回应报文
     * @throws IOException
     */
    public final void response(String info) throws IOException {
        System.out.println("Client IP" + packet.getAddress().getHostAddress()
                + ",Port：" + packet.getPort());
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, packet
                .getAddress(), packet.getPort());
        dp.setData(info.getBytes());
        ds.send(dp);
    }

    /**
     * 设置报文的缓冲长度.
     *
     * @param bufsize 缓冲长度
     */
    public final void setLength(int bufsize) {
        packet.setLength(bufsize);
    }

    /**
     * 获得发送回应的IP地址.
     *
     * @return 返回回应的IP地址
     */
    public final InetAddress getResponseAddress() {
        return packet.getAddress();
    }

    /**
     * 获得回应的主机的端口.
     *
     * @return 返回回应的主机的端口.
     */
    public final int getResponsePort() {
        return packet.getPort();
    }

    /**
     * 关闭udp监听口.
     */
    public final void close() {
        try {
            ds.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 测试方法.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String serverHost = "10.140.200.99";
        int serverPort = 514;
        UdpServerSocket udpServerSocket = new UdpServerSocket(serverHost, serverPort);
        while (true) {
            udpServerSocket.receive();
        }
    }
}