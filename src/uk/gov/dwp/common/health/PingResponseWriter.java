package uk.gov.dwp.common.health;

import java.io.PrintWriter;

public class PingResponseWriter {

    public void write(PrintWriter writer) {
        writer.write("pong");
    }
}
