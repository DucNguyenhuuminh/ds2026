package filetransfer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.List;

public class ClientConnection {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public ClientConnection(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void sendFile() {
        try {
            sendMenu();
            int index = getSelectedFileIndex();
            sendSelectedFile(index);
        }catch(IOException e) {
            e.printStackTrace();
        } 
    }

    private void sendSelectedFile(int index) throws IOException {
        File[] filelist = new File(FileServer.FILES_PATH).listFiles();
        File selectedFile = filelist[index];
        out.writeUTF(selectedFile.getName());
        List<String> filelines = Files.readAllLines(selectedFile.toPath());
        String fileContent = String.join("\n",filelines);
        out.writeUTF(fileContent);
    }

    private int getSelectedFileIndex() throws IOException {
        String input = in.readUTF();
        return Integer.parseInt(input)-1;
    }

    private void sendMenu() throws IOException {
        String menu = "-----MENU FILES-----\n";
        File[] filelist = new File(FileServer.FILES_PATH).listFiles();
        out.writeUTF(""+filelist.length);

        for (int i = 0; i < filelist.length; i++) {
            menu += String.format(" %d - %s\n",i+1,filelist[i].getName());
        }
        out.writeUTF(menu);
    }
}
