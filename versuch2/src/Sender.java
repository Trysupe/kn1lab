import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.Scanner;   //Import the Scanner class
/**
 * Die "Klasse" Sender liest einen String von der Konsole und zerlegt ihn in einzelne Worte. Jedes Wort wird in ein
 * einzelnes {@link Packet} verpackt und an das Medium verschickt. Erst nach dem Erhalt eines entsprechenden
 * ACKs wird das nächste {@link Packet} verschickt. Erhält der Sender nach einem Timeout von 5 Sekunden kein ACK,
 * überträgt er das {@link Packet} erneut.
 */
public class Sender {
    /**
     * Hauptmethode, erzeugt Instanz des {@link Sender} und führt {@link #send()} aus.
     * @param args Argumente, werden nicht verwendet.
     */
    public static void main(String[] args) {
        Sender sender = new Sender();
        try {
            sender.send();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Erzeugt neuen Socket. Liest Text von Konsole ein und zerlegt diesen. Packt einzelne Worte in {@link Packet}
     * und schickt diese an Medium. Nutzt {@link SocketTimeoutException}, um 5 Sekunden auf ACK zu
     * warten und das {@link Packet} ggf. nochmals zu versenden.
     * @throws IOException Wird geworfen falls Sockets nicht erzeugt werden können.
     */
    private void send() throws IOException {
   	//Text einlesen und in Worte zerlegen
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter String");
        String input = myScanner.nextLine();
        String[] woerter = input.split(" ");
        InetAddress address = InetAddress.getLocalHost();
        DatagramPacket packetOut;


        // Socket erzeugen auf Port 9998 und Timeout auf 5 Sekunden setzen
             DatagramSocket socket = new DatagramSocket(9998);
             socket.setSoTimeout(5000);
        // Iteration über den Konsolentext
               int sequenz = 1;
               int ack = 1;
        for (int i = 0; i != woerter.length;) {
                Packet payload = new Packet(sequenz, ack, false , woerter[i].getBytes());
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                ObjectOutputStream o = new ObjectOutputStream(b);
                o.writeObject(payload);
                byte[] buf = b.toByteArray();

                packetOut = new DatagramPacket(buf, buf.length, address, 9997);
        	// Paket an Port 9997 senden

                try {
                    socket.send(packetOut);
                    // Auf ACK warten und erst dann Schleifenzähler inkrementieren
                    byte[] buffer = new byte[256];
                    DatagramPacket rcvPacketRaw = new DatagramPacket(buffer, buffer.length, address, 9998);
                    socket.receive(rcvPacketRaw);
                    ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(rcvPacketRaw.getData()));
                    Packet packetIn = (Packet) is.readObject();

                    if (packetIn.isAckFlag()) {
                        ack = packetIn.getAckNum();
                    }

                    int nextSequenz = sequenz + woerter[i].length();
                    if (nextSequenz == ack) {
                        sequenz = nextSequenz;
                        i++;
                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    System.out.println("Receive timed out, retrying...");
                }

            }
        String eot ="EOT";

        Packet payload = new Packet(sequenz, ack, false , eot.getBytes());
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(payload);
        byte[] buf = b.toByteArray();

            DatagramPacket eotPacket = new DatagramPacket(buf, buf.length, address, 9997);
            socket.send(eotPacket);
        
        // Wenn alle Packete versendet und von der Gegenseite bestätigt sind, Programm beenden
        socket.close();
        
        if(System.getProperty("os.name").equals("Linux")) {
            socket.disconnect();
        }

        System.exit(0);
    }
}
