package io.github.fairyspace.coolutils.ip;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌道阻且长，行则将至🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌
 * 🍁 Program: coolutils
 * 🍁 Description:
 * 🍁 @author: xuquanru
 * 🍁 Create: 2023/1/5
 * 🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌行而不辍，未来可期🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌🐌
 **/
public class IpUtil {
    public static List<InetAddress> resolveLocalAddresses() {
        List<InetAddress> addrs = new ArrayList<>();
        Enumeration<NetworkInterface> ns = null;
        try {
            ns = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            // ignored...
        }
        while (ns != null && ns.hasMoreElements()) {
            NetworkInterface n = ns.nextElement();
            Enumeration<InetAddress> is = n.getInetAddresses();
            while (is.hasMoreElements()) {
                InetAddress i = is.nextElement();
                if (!i.isLoopbackAddress() && !i.isLinkLocalAddress() &&
                        !i.isMulticastAddress() && !isSpecialIp(i.getHostAddress())
                    /*&& i.isSiteLocalAddress()*/) {
                    addrs.add(i);
                }
            }
        }
        return addrs;
    }

    public static List<String> resolveLocalIps() {
        List<InetAddress> addrs = resolveLocalAddresses();
        List<String> ret = new ArrayList<>();
        for (InetAddress addr : addrs)
            ret.add(addr.getHostAddress());
        return ret;
    }

    /**
     *
     */
    public static InetAddress resolveLocalAddress() {
        List<InetAddress> addrs = resolveLocalAddresses();
        if (addrs == null || addrs.isEmpty())
            return null;
        return addrs.iterator().next();
    }

    public static String getLocalHostName() {
        String hostName;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostName = addr.getHostName();
        } catch (Exception e) {
            hostName = "";
        }
        return hostName;
    }

    private static boolean isSpecialIp(String ip) {
        if (ip.contains(":")) return true;
        if (ip.startsWith("127.")) return true;
        if (ip.startsWith("169.254.")) return true;
        if (ip.equals("255.255.255.255")) return true;
        return false;
    }


    private boolean isOnline(String host) {
        String port = host.contains(":") ? host.substring(host.indexOf(':') + 1) : "80";
        String ip = host.contains(":") ? host.substring(0, host.indexOf(':')) : host;
        Socket socket = null;
        try {
            SocketAddress sockaddr = new InetSocketAddress(ip, new Integer(port));
            socket = new Socket();
            socket.connect(sockaddr, 5000);
        }
        catch (NumberFormatException | IOException e) {
            return false;
        }
        finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            }
            catch (IOException ex) {
            }
        }
        return true;
    }
}
