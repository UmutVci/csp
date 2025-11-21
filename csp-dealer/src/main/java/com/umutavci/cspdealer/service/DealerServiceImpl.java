package com.umutavci.cspdealer.service;

import com.umutavci.proto.ContinueRequest;
import com.umutavci.proto.ContinueResponse;
import com.umutavci.proto.DealerServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class DealerServiceImpl extends DealerServiceGrpc.DealerServiceImplBase {

    private volatile boolean continueSignal = false;

    @Override
    public void requestContinue(ContinueRequest request, StreamObserver<ContinueResponse> responseObserver) {
        continueSignal = true;
        responseObserver.onNext(ContinueResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    public void waitUntilContinue() {
        while (!continueSignal) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {}
        }
        continueSignal = false;
    }
}
