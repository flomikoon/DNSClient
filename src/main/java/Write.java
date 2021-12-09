import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Write {

    public final static int QR = 0;
    public final static int AA = 0;
    public final static int TC = 0;
    public final static int RD = 1;
    public final static int[] z = new int[]{0,0,0};
    public final static int[] RCODE = new int[]{0,0,0,0};
    public final static int RA = 0;
    private String domen;
    public String[] args;

    public Write(String domen , String[] args){
        this.domen = domen;
        this.args = args;
    }

    public byte[] getPacket(){
        // ID
        Random random = new Random();
        int ID = random.nextInt(65535);
        byte[] byteID = new byte[]{
                (byte) (ID  >> 8),
                (byte) ID
        };

        //System.out.println(domen + " " + Arrays.toString(byteID));

        // QR = 0 OPCODE = 0000 AA = 0 TC = 0 RD = 1
        int[] OPCODE = new int[]{0 , 0 , 0 , 0};

        int[] b = new int[] {RD, TC , AA , OPCODE[3] , OPCODE[2] , OPCODE[1] , OPCODE[0] , QR};
        byte[] byteThree = new byte[1];
        for (int i = 0 ; i < b.length ; i ++){
            byteThree[0] += (byte) b[i] * Math.pow(2 , i);
        }

        byte[] message = Arrays.copyOf(byteID , byteID.length + byteThree.length);
        System.arraycopy(byteThree, 0, message, byteID.length, byteThree.length);

        //System.out.println(Arrays.toString(message));

        //RA = 0 z = 000 RCODE = 0000
        b = new int[]{RCODE[0] , RCODE[1] ,RCODE[2] ,RCODE[3] , z[0] , z[1] , z[2] , RA};
        byte[] byteFour = new byte[1];
        for (int i = 0 ; i < b.length ; i ++){
            byteFour[0] += (byte) b[i] * Math.pow(2 , i);
        }
        message = Arrays.copyOf(message , message.length + byteFour.length);
        System.arraycopy(byteFour, 0, message, message.length - 1, byteFour.length);

        //System.out.println(Arrays.toString(message));

        //QDCount = 1 ANCount = 0 NSCount = 0 ARCount = 0
        byte[] byteFiveTwelv = new byte[]{0, 1, 0, 0, 0, 0, 0, 0};

        message = Arrays.copyOf(message, message.length + byteFiveTwelv.length);
        System.arraycopy(byteFiveTwelv, 0, message, message.length - byteFiveTwelv.length, byteFiveTwelv.length);

        //System.out.println(Arrays.toString(message));
        ArrayList<String> array = new ArrayList<>();
        while (domen.contains(".")){
            int pos = domen.indexOf(".");
            String substring = domen.substring(0 , pos);
            domen = domen.substring(pos+1);
            array.add(substring);
        }
        array.add(domen);
        //System.out.println(array);

        byte[] question = new byte[0];
        for (String s : array){
            byte[] bytes =  s.getBytes();
            byte[] bytesLen = new byte[]{(byte) bytes.length};
            question = Arrays.copyOf(question , question.length + bytes.length + 1);
            System.arraycopy(bytesLen, 0, question, question.length - bytesLen.length - bytes.length, bytesLen.length);
            System.arraycopy(bytes, 0, question, question.length - bytes.length, bytes.length);
        }

        //System.out.println(Arrays.toString(question));

        message = Arrays.copyOf(message , message.length + question.length);
        System.arraycopy(question, 0, message, message.length - question.length , question.length);


        //System.out.println(Arrays.toString(message));

        byte[] typeClass = new byte[0];

        if (args.length > 0) {
            if (Objects.equals(args[0], "-MX")) {
                typeClass = new byte[]{0, 0, 15, 0, 1};
            } else if (Objects.equals(args[0], "-AAAA")) {
                typeClass = new byte[]{0, 0, 28, 0, 1};
            } else if (Objects.equals(args[0], "-TXT")) {
                typeClass = new byte[]{0, 0, 16, 0, 1};
            }
        } else {
            typeClass = new byte[]{0, 0, 1, 0, 1};
        }

        message = Arrays.copyOf(message , message.length + typeClass.length);
        System.arraycopy(typeClass, 0, message, message.length - typeClass.length , typeClass.length);
        System.out.println(Arrays.toString(message));

        return message;
    }
}
