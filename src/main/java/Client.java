import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    public final static int SERVICE_PORT = 53;

    public static void main(String[] args) throws IOException {
        DatagramSocket clientSocket = new DatagramSocket();
        byte[] ip = new byte[]{ 8 , 8 , 8 , 8};
        // Получите IP-адрес сервера
        InetAddress IPAddress = InetAddress.getByAddress(ip);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String domen = reader.readLine();

        byte[] message = new Write(domen , args).getPacket();

        DatagramPacket sendingPacket = new DatagramPacket(message ,message .length,IPAddress, SERVICE_PORT);

        // Отправьте UDP-пакет серверу
        clientSocket.send(sendingPacket);

        byte[] receivingDataBuffer = new byte[200];
        // Получите ответ от сервера, т.е. предложение из заглавных букв
        DatagramPacket receivingPacket = new DatagramPacket(receivingDataBuffer,receivingDataBuffer.length);
        clientSocket.receive(receivingPacket);


        System.out.println("READ");

        new Read(receivingPacket.getData()).getMessage();

        // Закройте соединение с сервером через сокет
        clientSocket.close();
    }
}
