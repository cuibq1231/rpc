import com.cbq.learn.rpc.java.GreeterGrpc;
import com.cbq.learn.rpc.java.GreeterImpl.HelloReply;
import com.cbq.learn.rpc.java.GreeterImpl.HelloRequest;

import io.grpc.stub.StreamObserver;

/**
 * @author cuibingqi
 */
public class GreeterImpl extends GreeterGrpc.GreeterImplBase {
    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        HelloReply reply = HelloReply.newBuilder().setMessage("Hello " + req.getName()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}