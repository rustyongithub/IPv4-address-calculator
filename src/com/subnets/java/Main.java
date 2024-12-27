

package com.subnets.java;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Main {

    // Converts a binary string to its decimal equivalent
    public static int binaryToDecimal(String binaryString) {
        int[] decimalValues = {128, 64, 32, 16, 8, 4, 2, 1};
        String[] binaryArray = binaryString.split("");
        int sum = 0;
        for (int i = 0; i < binaryArray.length; i++) {
            sum += Integer.parseInt(binaryArray[i]) * decimalValues[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println("Starting... Use this program at your own discretion.");

        // Input IPv4 address and subnet mask
        String ipv4Address = JOptionPane.showInputDialog("Enter IPv4 Address:");
        String mask = JOptionPane.showInputDialog("Enter mask in CIDR format (e.g., 24):");

        // Validate IP address and mask
        String[] ipOctets = ipv4Address.split("\\.");
        if (ipOctets.length != 4 || Integer.parseInt(mask) < 8 || Integer.parseInt(mask) > 30) {
            JOptionPane.showMessageDialog(null, "Error: Invalid IP Address or Mask");
            System.exit(0);
        }

        // Convert IP octets to binary format
        StringBuilder networkIpBinary = new StringBuilder();
        for (String octet : ipOctets) {
            String binaryOctet = Integer.toBinaryString(Integer.parseInt(octet));
            networkIpBinary.append("0".repeat(8 - binaryOctet.length())).append(binaryOctet);
        }

        // Calculate the total number of hosts
        int totalHostBits = 32 - Integer.parseInt(mask);
        int totalAvailableHosts = (int) Math.pow(2, totalHostBits) - 2; // Subtract 2 for network and broadcast addresses

        if (totalAvailableHosts < 0) {
            JOptionPane.showMessageDialog(null, "Error: Invalid subnet configuration");
            System.exit(0);
        }

        System.out.println("Running...");

        // Generate all possible host IPs
        List<String> hosts = new ArrayList<>();
        for (int i = 1; i <= totalAvailableHosts; i++) {
            String binaryHost = "0".repeat(totalHostBits - Integer.toBinaryString(i).length()) + Integer.toBinaryString(i);
            String binaryFullIp = networkIpBinary.substring(0, 32 - totalHostBits) + binaryHost;

            StringBuilder hostIp = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                String octetBinary = binaryFullIp.substring(j * 8, (j + 1) * 8);
                hostIp.append(binaryToDecimal(octetBinary));
                if (j < 3) hostIp.append(".");
            }

            hosts.add(hostIp.toString());
        }

        // Display results
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("IP Address: " + ipv4Address));
        panel.add(new JLabel("Subnet Mask: /" + mask));
        panel.add(new JLabel("Total Available Hosts: " + totalAvailableHosts));

        for (String host : hosts) {
            panel.add(new JLabel(host));
        }

        JOptionPane.showMessageDialog(null, panel, "Subnet Calculation Results", JOptionPane.INFORMATION_MESSAGE);
    }
}
