package com.umutavci.cspdealer.config;

import com.umutavci.proto.TableServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientsConfig {

    @GrpcClient("table-service")
    private TableServiceGrpc.TableServiceBlockingStub tableStub;

    @Bean
    public TableServiceGrpc.TableServiceBlockingStub tableServiceStub() {
        return tableStub;
    }
}
