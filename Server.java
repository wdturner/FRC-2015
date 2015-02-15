/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg2014vision;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Jared "Jär Bär" Gentner
 */
public interface Server<E> {

    Socket accept() throws IOException;

    byte[] packData(E data) throws IOException;

    void flushQueue() throws IOException;

    void close(Socket sock) throws IOException;
}
