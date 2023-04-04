package kinostick.stream.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.SocketUtils;

@Service
public class NetworkService {

    @Value("${server.hostname}")
    private String hostname;
    @Value("${server.port}")
    private Integer port;



    public String getHostname()  {
        return hostname;
        /*
        Enumeration<NetworkInterface> n = null;
        try {
            n = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        for (; n.hasMoreElements();)
        {
            NetworkInterface e = n.nextElement();

            Enumeration<InetAddress> a = e.getInetAddresses();
            for (; a.hasMoreElements();)
            {
                InetAddress addr = a.nextElement();
                //System.out.println("  " + addr.getHostAddress());
                String ip = addr.getHostAddress();
                if (ip.substring(0,3).equals("192")) {
                    return ip;
                }
            }
        }

        return "127.0.0.1";
*/
    }

//    public Integer getAvailableTcpPort() {
//        return SocketUtils.findAvailableTcpPort();
//    }

    public Integer getPort() {
        return port;
    }


}
