package filetransfer;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class FileClient {
    public static final String RECEIVED_PATH = "receivedfile";
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Scanner sc;

    public FileClient() {
        try {
            new File(RECEIVED_PATH).mkdirs();

            socket = new Socket("127.0.0.1",FileServer.PORT);
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            sc = new Scanner(System.in);

            getFile();  
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getFile() throws IOException {
        String filelen = in.readUTF();
        int maxFiles = Integer.parseInt(filelen);
        String menu = in.readUTF();
        System.out.println(menu);

        int userSelection = 0;
        boolean isSelectionCorrect = false;
        while (!isSelectionCorrect) {
            System.out.print("Select a file number: ");
            userSelection = sc.nextInt();
            isSelectionCorrect = userSelection > 0 && userSelection <= maxFiles;
        }
        out.writeUTF(""+userSelection);

        String fileName = in.readUTF();
        String fileContent = in.readUTF();

        File savedFile = new File(RECEIVED_PATH,fileName);
        try (FileWriter writer = new FileWriter(savedFile)) {
            writer.write(fileContent);
        }

        System.out.println("-----File START-----");
        System.out.println(fileContent);
        System.out.println("-----FILE END-----");
        System.out.println("\nFile saved to: "+savedFile);
    }
    public static void main(String[] args) {
        new FileClient();
    }
}
