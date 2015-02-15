/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.CvSeq;
import static org.bytedeco.javacpp.opencv_imgproc.cvBoundingRect;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class TargetServer implements Server<Map.Entry<ImageObject, CvSeq>> {

    private static volatile TargetServer server;
    private ServerSocket sock;

    private TargetServer() {
    }

    public static synchronized TargetServer getServer() {
        if (server == null) {
            server = new TargetServer();
        }

        return server;
    }

    @Override
    public Socket accept() throws IOException {
        sock = new ServerSocket(9000);
        sock.setReuseAddress(true);
        return sock.accept();
    }

    @Override
    public byte[] packData(Map.Entry<ImageObject, CvSeq> data) throws IOException {
        byte[] snd = new byte[6];
        ImageObject object = data.getKey();
        CvSeq contour = data.getValue();
        snd[0] = (byte) object.getID();
        snd[1] = (!contour.isNull()) ? (byte) 1 : (byte) 0;
        if (!contour.isNull() && contour.elem_size() > 0) {
            System.out.println(contour);
            CvRect bounds = cvBoundingRect(contour, 0);
            int x = bounds.x() + (bounds.width() / 2);
            int y = bounds.y() + (bounds.height() / 2);
            snd[2] = (byte) (x & 0x0000FF00 >> 8);
            snd[3] = (byte) (x & 0x000000FF);
            snd[4] = (byte) (y & 0x0000FF00 >> 8);
            snd[5] = (byte) (x & 0x000000FF);
            return snd;
        } else {
            return new byte[]{0, 0, 0, 0, 0, 0};
        }
    }

    @Override
    public void close(Socket client) throws IOException {
        client.close();
    }

    @Override
    public void flushQueue() throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
