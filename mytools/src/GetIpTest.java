import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIpTest {
	public static void main(String[] args) throws Exception {
		String host = "192.168.202.101";
		String[] ipStr = host.split("\\.");
        byte[] ipBuf = new byte[4];
        for(int i = 0; i < 4; i++){
            ipBuf[i] = (byte)(Integer.parseInt(ipStr[i])&0xff);
        }
		InetAddress addresses = InetAddress.getByAddress(ipBuf);
		System.out.println(addresses.getHostName());
	}

}
