/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.diwan;

import java.io.PrintWriter;

/**
 *
 * @author adilmbpro
 */
public class Util {
    public final static boolean DEBUG = true;
    public static PrintWriter printer = null;

    public static void log (String message) {
        if (DEBUG && printer != null) {
            printer.println(message);
            printer.println("<br/>");
            printer.flush();
        }
        System.out.println(message);
    }

    public static void logError (String message) {
        if (DEBUG && printer != null) {
            printer.println("Error: " + message);
            printer.println("<br/>");
            printer.flush();
        }
        System.err.println(message);
    }

    public static void log(int code) {
        log(Integer.toString(code));
    }
}
