package com.umutavci.csptable.service;

import com.umutavci.csptable.TableState;
import com.umutavci.proto.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class TableServiceImpl extends TableServiceGrpc.TableServiceImplBase {
    private final TableState tableState = new TableState();

    @Override
    public void putIngredient(IngredientRequest request,
                              StreamObserver<PutResponse> responseObserver) {
        try {
            tableState.putIngredient(request.getIngredient());
            PutResponse response = PutResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalStateException e) {
            responseObserver.onError(
                    Status.FAILED_PRECONDITION
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void checkIngredients(CheckRequest request,
                                 StreamObserver<CheckResponse> responseObserver) {
        var current = tableState.getCurrentIngredients();

        CheckResponse.Builder builder = CheckResponse.newBuilder();
        builder.addAllIngredients(current);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void takeIngredient(TakeRequest request,
                               StreamObserver<TakeResponse> responseObserver) {
        boolean success = tableState.takeIngredient(request.getIngredient());

        if (!success) {
            responseObserver.onError(
                    Status.FAILED_PRECONDITION
                            .withDescription("Requested ingredient not present on table")
                            .asRuntimeException()
            );
            return;
        }

        TakeResponse response = TakeResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
