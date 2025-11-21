package com.umutavci.cspsmoker.service;

import com.umutavci.proto.DealerServiceGrpc;
import com.umutavci.proto.TableServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientsConfig {

    @GrpcClient("table-service")
    private TableServiceGrpc.TableServiceBlockingStub tableStub;

    @GrpcClient("dealer-service")
    private DealerServiceGrpc.DealerServiceBlockingStub dealerStub;

    @Bean
    public TableServiceGrpc.TableServiceBlockingStub tableStub() {
        return tableStub;
    }

    @Bean
    public DealerServiceGrpc.DealerServiceBlockingStub dealerStub() {
        return dealerStub;
    }

}
