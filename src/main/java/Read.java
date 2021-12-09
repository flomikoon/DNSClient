import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Read {

    public byte[] readData;

    public Read(byte[] readData) {
        this.readData = readData;
    }

    public void getMessage() {
        System.out.println(Arrays.toString(readData));

        byte[] threeFour = new byte[]{readData[2], readData[3]};

        ArrayList<Integer> bits = byteInBit(threeFour);

        //QR , OPCODE (4) , AA , TC , RD , RA , z(3) , RCODE(4)
        //System.out.println(bits);

        if (bits.get(bits.size() - 1) == 1 && bits.get(bits.size() - 2) == 1) {
            System.out.println("Доменное имя, указанное в запросе, не существует");
            System.exit(1);
        } else if (bits.get(bits.size() - 1) == 1 && bits.get(bits.size() - 2) == 0) {
            System.out.println("Серверу имен не удалось обработать этот запрос из-за проблемы с сервером имен");
            System.exit(1);
        } else if (bits.get(bits.size() - 3) == 1) {
            System.out.println("Сервер имен не поддерживает запрошенный тип запроса");
            System.exit(1);
        } else if (bits.get(bits.size() - 3) == 1 && bits.get(bits.size() - 1) == 1) {
            System.out.println("Сервер имен отказывается выполнять указанную операцию по соображениям политики");
            System.exit(1);
        } else if (bits.get(bits.size() - 1) == 1 && bits.get(bits.size() - 2) == 0 && bits.get(bits.size() - 3) == 0) {
            System.out.println("Cерверу имен не удалось интерпретировать запрос");
            System.exit(1);
        }

        byte[] fiveTwelv = new byte[]{readData[4], readData[5], readData[6], readData[7],
                readData[8], readData[9], readData[10], readData[11],};

        bits = byteInBit(fiveTwelv);

        // QDCOUNT(16) , ANCOUNT(16) , NSCOUNT(16) , ARCOUNT(16)
        //System.out.println(bits);
        int[] len = new int[]{0, 0, 0, 0};
        int m = 0;
        for (int j = 0; j < len.length; j++) {
            for (int i = m; i < 16 + m; i++) {
                len[j] = len[j] + (int) (bits.get(i) * Math.pow(2, 15 * (j + 1) + j - i));
            }
            m += 16;
        }

        //QDCOUNT , ANCOUNT , NSCOUNT , ARCOUNT
        //System.out.println(Arrays.toString(len));
        int QDCOUNT = len[0];
        int ANCOUNT = len[1];
        int NSCOUNT = len[2];
        int ARCOUNT = len[3];

        int mark = 12;

        ArrayList<byte[]> domen = new ArrayList<>();

        while (readData[mark] != 0) {
            int length = readData[mark];
            mark++;
            byte[] d = new byte[length];
            System.arraycopy(readData, mark, d, 0, length);
            domen.add(d);
            mark += length;
        }

        //Запрашиваемый домен
        StringBuilder domenString = new StringBuilder();
        for (byte[] doman : domen) {
            domenString.append(new String(doman, 0, doman.length, StandardCharsets.UTF_8));
            domenString.append(".");
        }
        domenString.deleteCharAt(domenString.length() - 1);
        //System.out.println(domenString);


        //Секция ответа
        mark += 2;

        int byteType = readData[mark];
        mark += 3;
        if (ANCOUNT == 0) {
            ANCOUNT = NSCOUNT;
        }
        for (int l = 0; l < ANCOUNT; l++) {
            byte[] lenFirstByte = new byte[]{readData[mark]};
            bits = byteInBit(lenFirstByte);

            if (bits.get(0) == 1 && bits.get(1) == 1) {
                mark++;

                bits.addAll(byteInBit(new byte[]{readData[mark]}));

                int newMark = bitInInt(bits, 15, 2); //Переходим на место на которое указывает ссылка

                domen = getDomen(newMark);


                domenString = new StringBuilder();
                for (byte[] doman : domen) {
                    domenString.append(new String(doman, 0, doman.length, StandardCharsets.UTF_8));
                    domenString.append(".");
                }

                domenString.deleteCharAt(domenString.length() - 1);
                //System.out.println(domenString);

                mark += 9; // вычисляем длинну данных

                int length = bitInInt(byteInBit(new byte[]{readData[mark], readData[mark + 1]}), 15, 0);

                mark += 2;

                if (byteType == 1) {
                    domenString.append(" ");
                    for (int i = 0; i < length; i++) {
                        domenString.append(readData[mark] & 0xff);
                        domenString.append(".");
                        mark += 1;
                    }
                    domenString.deleteCharAt(domenString.length() - 1);
                    System.out.println(domenString); // не удалять
                } else if (byteType == 28) {
                    domenString.append(" ");
                    for (int j = 0; j < 8; j++) {
                        bits = byteInBit(new byte[]{readData[mark], readData[mark + 1]});
                        mark++;
                        for (int i = 0; i < bits.size(); i += 4) {
                            int sum = bits.get(i) * 8 + bits.get(i + 1) * 4 + bits.get(i + 2) * 2 + bits.get(i + 3);
                            if (sum == 10) {
                                domenString.append("a");
                            } else if (sum == 11) {
                                domenString.append("b");
                            } else if (sum == 12) {
                                domenString.append("c");
                            } else if (sum == 13) {
                                domenString.append("d");
                            } else if (sum == 14) {
                                domenString.append("e");
                            } else if (sum == 15) {
                                domenString.append("f");
                            } else {
                                domenString.append(sum);
                            }
                        }
                        domenString.append(":");
                        mark++;
                    }
                    domenString.deleteCharAt(domenString.length() - 1);
                    System.out.println(domenString); // не удалять
                } else if (byteType == 15) {
                    mark--;
                    int sizeOne = readData[mark] & 0xff;
                    mark++;
                    domenString.append(" " + "MX preference = ");

                    int pref = bitInInt(byteInBit(new byte[]{readData[mark], readData[mark + 1]}), 15, 0);
                    domenString.append(pref);

                    mark++;

                    // exchange

                    domenString.append(" " + "mail exchanger = ");

                    int lastByte = mark + sizeOne - 3;

                    mark++;
                    while (lastByte != mark) {
                        int lengthMail = readData[mark] & 0xff;
                        byte[] mx = new byte[lengthMail];
                        for (int i = 0; i < lengthMail; i++) {
                            mark++;
                            mx[i] = readData[mark];
                        }
                        domenString.append(new String(mx, 0, mx.length)).append(".");
                        mark++;
                    }

                    //
                    bits = byteInBit(new byte[]{readData[mark]});

                    if (bits.get(0) == 1 && bits.get(1) == 1) {
                        mark++;

                        bits.addAll(byteInBit(new byte[]{readData[mark]}));

                        //домен
                        newMark = 0;
                        for (int i = 2; i < bits.size(); i++) {
                            newMark += bits.get(i) * Math.pow(2, 15 - i);
                        }

                        domen = new ArrayList<>();
                        for (int i = 0; i < QDCOUNT; i++) {
                            while (readData[newMark] != 0) {
                                length = readData[newMark + i];
                                newMark++;
                                if (length < 0) {

                                    newMark -= 1;

                                    bits = byteInBit(new byte[]{readData[newMark]});

                                    if (bits.get(0) == 1 && bits.get(1) == 1) {
                                        newMark++;
                                        bits.addAll(byteInBit(new byte[]{readData[newMark]}));
                                    }

                                    newMark = bitInInt(bits, 15, 2);

                                    domen.addAll(getDomen(newMark));

                                    break;
                                }
                                byte[] d = new byte[length];
                                for (int j = 0; j < length; j++) {
                                    d[j] = readData[newMark + i + j];
                                }
                                domen.add(d);
                                newMark += length;
                            }
                        }
                        for (byte[] doman : domen) {
                            domenString.append(new String(doman, 0, doman.length, StandardCharsets.UTF_8));
                            domenString.append(".");
                        }
                    }
                    domenString.deleteCharAt(domenString.length() - 1);
                    System.out.println(domenString);
                    mark++;
                } else if (byteType == 16) {
                    mark--;
                    domenString.append(" ");

                    ArrayList<StringBuilder> txt = new ArrayList<>();
                    int nameSize = mark + readData[mark] - 20;

                    while (nameSize != mark) {
                        mark++;
                        domen = new ArrayList<>();
                        while (readData[mark] != 0) {
                            int lenOne = readData[mark];
                            if (lenOne < 0) {
                                newMark = mark;
                                bits = byteInBit(new byte[]{readData[newMark]});

                                if (bits.get(0) == 1 && bits.get(1) == 1) {
                                    newMark++;
                                    bits.addAll(byteInBit(new byte[]{readData[newMark]}));
                                }

                                newMark = bitInInt(bits, 15, 2);

                                domen.addAll(getDomen(newMark));

                                mark++;
                                break;
                            }
                            byte[] by = new byte[lenOne];
                            for (int i = 0; i < lenOne; i++) {
                                mark++;
                                by[i] = readData[mark];
                            }
                            mark++;
                            domen.add(by);
                        }

                        domenString = new StringBuilder();
                        for (byte[] doman : domen) {
                            domenString.append(new String(doman, 0, doman.length, StandardCharsets.UTF_8));
                            domenString.append(".");
                        }

                        domenString.deleteCharAt(domenString.length() - 1);
                        txt.add(domenString);
                    }

                    mark++;
                    byte[] serial = new byte[]{readData[mark], readData[mark + 1], readData[mark + 2], readData[mark + 3]};
                    mark += 4;
                    byte[] refresh = new byte[]{readData[mark], readData[mark + 1], readData[mark + 2], readData[mark + 3]};
                    mark += 4;
                    byte[] retry = new byte[]{readData[mark], readData[mark + 1], readData[mark + 2], readData[mark + 3]};
                    mark += 4;
                    byte[] expire = new byte[]{readData[mark], readData[mark + 1], readData[mark + 2], readData[mark + 3]};
                    mark += 4;
                    byte[] minimum = new byte[]{readData[mark], readData[mark + 1], readData[mark + 2], readData[mark + 3]};
                    mark += 4;

                    System.out.println("Name server: " + txt.get(0));
                    System.out.println("Mail addr: " + txt.get(1));
                    System.out.println("Serial: " + bitInInt(byteInBit(serial), 31, 0));
                    System.out.println("Refresh: " + bitInInt(byteInBit(refresh), 31, 0));
                    System.out.println("Retry: " + bitInInt(byteInBit(retry), 31, 0));
                    System.out.println("Expire: " + bitInInt(byteInBit(expire), 31, 0));
                    System.out.println("Minimum: " + bitInInt(byteInBit(minimum), 31, 0));
                }
            }
        }
    }

    private ArrayList<Integer> byteInBit(byte[] bytes) {
        ArrayList<Integer> bits = new ArrayList<>();

        for (byte aByte : bytes) {
            int res = aByte & 0xff;
            for (int j = 0; j < 8; j++) {
                int ch = (int) (res / Math.pow(2, 7 - j));
                if (ch == 1) {
                    res -= Math.pow(2, 7 - j);
                    bits.add(1);
                } else {
                    bits.add(0);
                }
            }
        }
        return bits;
    }

    private int bitInInt(ArrayList<Integer> bits, int size, int start) {
        int sum = 0;
        for (int i = start; i < bits.size(); i++) {
            sum += bits.get(i) * Math.pow(2, size - i);
        }
        return sum;
    }

    private ArrayList<byte[]> getDomen(int mark) {
        ArrayList<byte[]> result = new ArrayList<>();
        while (readData[mark] != 0) {
            int length = readData[mark];
            mark++;
            byte[] d = new byte[length];
            System.arraycopy(readData, mark, d, 0, length);
            result.add(d);
            mark += length;
        }

        return result;
    }
}

