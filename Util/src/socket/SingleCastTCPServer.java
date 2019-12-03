package socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class SingleCastTCPServer {
	public static void main(String[] args) throws Exception {
		System.out.println("启动单播TCP服务端");
		// 构造ServerSocket实例，指定监听的本地端口为6666
		try (ServerSocket serverSocket = new ServerSocket(6666)) {
			// 设置超时时间为10秒
			//serverSocket.setSoTimeout(10000);
			while (true) {
				// 侦听并接收到此套接字的连接，此方法在连接传入之前一直阻塞。
				Socket socket = serverSocket.accept();
				TCPServerHandleThread tcpServerHandleThread = new TCPServerHandleThread(socket);
				Thread thread = new Thread(tcpServerHandleThread);
				thread.start();
			}
		}
	}
}

/**
 * 为每个连接进来的请求单独起一个处理线程
 */
class TCPServerHandleThread implements Runnable {
	private Socket socket;

	public TCPServerHandleThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			try (BufferedInputStream inputStream = new BufferedInputStream(socket.getInputStream());
					BufferedOutputStream outputStream = new BufferedOutputStream(socket.getOutputStream())) {
				// 每次读取的最大字节数，为了演示特地写小一点
				byte[] bytes = new byte[8];
				// 每次读取的有效字节数
				int count;
				// 结果数组
				byte[] result = new byte[0];
				while ((count = inputStream.read(bytes)) != -1) {
					// 本次读取的有效字节数组
					byte[] temp1 = Arrays.copyOfRange(bytes, 0, count);
					// 复制结果数组到新数组，新数组长度为结果数组的长度加上本次读取的有效字节数，用0填充
					byte[] temp2 = Arrays.copyOf(result, result.length + count);
					// 将本次读取的有效字节数组放到新数组里
					System.arraycopy(temp1, 0, temp2, result.length, count);
					result = temp2;
				}
				InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
				System.out.println("客户端的ip和端口：" + inetSocketAddress.getAddress() + " " + inetSocketAddress.getPort());
				InetSocketAddress inetSocketAddressL = (InetSocketAddress) socket.getLocalSocketAddress();
				System.out
						.println("本地绑定的ip和端口：" + inetSocketAddressL.getAddress() + " " + inetSocketAddressL.getPort());
				System.out.println("接收到的数据长度：" + result.length);
				System.out.println("接收到的数据：" + new String(result, "UTF-8"));
				// 关闭此socket输入流
				socket.shutdownInput();
				outputStream.write("接收完毕".getBytes("UTF-8"));
				outputStream.flush();
				// 关闭此socket输出流
				socket.shutdownOutput();
			} finally {
				socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
