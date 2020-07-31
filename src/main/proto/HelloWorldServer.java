/**
 * @author cuibq <cuibingqi@kuaishou.com>
 * Created on 2019-09-20
 */

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

/**
 * @Author cuibq <cuibingqi@kuaishou.com>
 * @Description Created on 2019-09-20
 */
public class HelloWorldServer {
    private static void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        Server server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build();
        server.start();
    }

    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
