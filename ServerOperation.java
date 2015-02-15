/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bytedeco.javacpp.opencv_core.CvSeq;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public class ServerOperation {

    Runnable serverop = new Runnable() {

        @Override
        public void run() {
            for (;;) {
                try {
                    byte[] arg = new byte[1];
                    int i = 0;
                    try {
                        System.out.print("Reading...");
                        client.getInputStream().read(arg);
                        System.out.println(arg[0]);
                        System.out.println("done");
                    } catch (IOException ex) {
                        Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    switch (arg[0]) {
                        case 7:
                            try {
                                Runtime.getRuntime().exec("shutdown -P now");
                            } catch (IOException ex) {
                                Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;

                        case 5:
                            if (searches.isEmpty()) {
                                client.getOutputStream().write(new byte[]{0, 0, 0, 0, 0, 0,});
                                client.getOutputStream().flush();
                                searches.clear();
                                break;
                            }
                            ArrayList<ImageObject> templates = new ArrayList<>();
                            for (Integer val : searches) {
                                for (ImageObject object : objects) {
                                    if (object.getID() == val) {
                                        templates.add(object);
                                    }
                                }
                            }
                            searches.clear();
                            filter.setTemplates(templates.toArray(new ImageObject[templates.size()]));
                            System.out.println(filter.getNumTemplates());

                            System.out.println(templates.size());
                            try {
                                Map<ImageObject, ArrayList<CvSeq>> map = filter.getScores();
                                System.out.println("keys:" + map.keySet().size());
                                if (map.keySet().isEmpty()) {
                                    client.getOutputStream().write(new byte[]{0, 0, 0, 0, 0, 0,});
                                    client.getOutputStream().flush();
                                    searches.clear();
                                    break;
                                }
                                for (ImageObject key : map.keySet()) {
                                    System.out.println("here");
                                    if (searches.contains(key.getID())) {
                                        for (CvSeq seq : map.get(key)) {
                                            byte[] write = server.packData(new HashMap.SimpleEntry<>(key, seq));
                                            System.out.println(write[0]);
                                            System.out.println("writing");
                                            client.getOutputStream().write(write);
                                            System.out.println("done");
                                        }
                                    }
                                }
                                System.out.println("flushing");
                                client.getOutputStream().flush();
                                System.out.println("done");
                            } catch (IOException ex) {
                                Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;

                        case 64:
                            break;

                        default:
                            System.out.println("set" + arg[0]);
                            Integer search = (int) arg[0];
                            if (!searches.contains(search)) {
                                searches.add(search);
                            }
                            System.out.println("Num searches: " + searches.size());
                            break;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ServerOperation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    };

    ScoreFilter filter;
    Socket client;
    ArrayList<ImageObject> objects;
    ArrayList<Integer> searches;
    TargetServer server;

    public ServerOperation(TargetServer server, Socket client, ScoreFilter filter) {
        objects = new ArrayList<>();
        searches = new ArrayList<>();
        this.filter = filter;
        this.server = server;
        this.client = client;
    }

    public void scanObjects(String filepath) {
        File dir = new File(filepath);
        for (File file : dir.listFiles()) {
            System.out.println(file);
            ImageObject object = ImageObject.fromFile(file);
            objects.add(object);
            System.out.println("added " + object);
        }
    }

    public Runnable getServerOperation() {
        return this.serverop;
    }
}
