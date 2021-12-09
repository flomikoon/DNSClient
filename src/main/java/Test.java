import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

public class Test {
    public static void main(String[] args) throws IOException {
        Random rnd = new Random();
        int r = rnd.nextInt(65535);
        byte[] byR = new byte[]{
                (byte) (r  >> 8),
                (byte) r
        };
        System.out.println(r);
        System.out.println(Arrays.toString(byR));

        int size = 0;
        for (int i = 0; i < 2; i++) { // b => 2
            size = (size << 8) + (byR[i] & 0xff);
        }

        System.out.println(size);

        String s = "yandex";
        System.out.println(Arrays.toString(s.getBytes()));
        s = "ru";
        System.out.println(Arrays.toString(s.getBytes()));

        byte[] ip = new byte[4];
        ip[0] = 8;
        ip[1] = 8;
        ip[2] = 8;
        ip[3] = 8;
        // Получите IP-адрес сервера
        InetAddress IPAddress = InetAddress.getByAddress(ip);

        System.out.println(IPAddress);

        System.out.println(-127 & 0xff);
        System.out.println(-128 & 0xff);
        System.out.println(-64& 0xff);
        System.out.println(12 & 0xff);

        int a1 = 5 & 0xff;
        int a2 = -1 & 0xff;
        int a3 = -1 & 0xff;
        int a4 = 50 & 0xff;
        System.out.println("IP: " + a1 + "." + a2+ "." + a3+ "." + a4);
        a1 = 77 & 0xff;
        a2 = 88 & 0xff;
        a3 = 55 & 0xff;
        a4 = 66 & 0xff;
        System.out.println("IP: " + a1 + "." + a2+ "." + a3+ "." + a4);
        a1 = 77 & 0xff;
        a2 = 88 & 0xff;
        a3 = 55 & 0xff;
        a4 = 50 & 0xff;
        System.out.println("IP: " + a1 + "." + a2+ "." + a3+ "." + a4);
        a1 = 5 & 0xff;
        a2 = -1 & 0xff;
        a3 = -1 & 0xff;
        a4 = 60 & 0xff;
        System.out.println("IP: " + a1 + "." + a2+ "." + a3+ "." + a4);

        int[] b = new int[] {1, 1 , 0 , 1 , 0 , 0 , 0 , 0};
        byte[] byteThree = new byte[1];
        for (int i = 0 ; i < b.length ; i ++){
            byteThree[0] += (byte) b[i] * Math.pow(2 , i);
            System.out.println(Arrays.toString(byteThree));
        }

        String test = "datatracker.ietf.org";
        int aa = test.indexOf('.');
        System.out.println(aa);
        String ss = test.substring(0 , aa);
        String test2 = test.substring(aa+1);
        System.out.println(ss);
        System.out.println(test2);
        aa = test2.indexOf(".");
        String sss = test2.substring(0 , aa);
        String test3 = test2.substring(aa+1);
        System.out.println(sss);
        System.out.println(test3);

        System.out.println(-125 & 0xff);

        byte[] mass = new byte[]{79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 13, 10, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 13, 10, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 13, 10, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 13, 10, 78, 69, 32, 86, 73, 67, 84, 79, 82, 89, 32, 71, 82, 73, 69, 83, 32, 70, 79, 82, 69, 88, 32, 68, 69, 65, 68, 76, 73, 78, 69, 32, 86, 73, 67, 84};

        System.out.println(new String(mass , 0 , mass.length));
    }
}
