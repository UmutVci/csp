package com.umutavci.cspdealer.logic;


import com.umutavci.cspdealer.service.DealerServiceImpl;
import com.umutavci.proto.IngredientRequest;
import com.umutavci.proto.TableServiceGrpc;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DealerLoop {

    private static final Logger log = LoggerFactory.getLogger(DealerLoop.class);

    private final TableServiceGrpc.TableServiceBlockingStub tableStub;
    private final DealerServiceImpl dealerService;
    private final Random random = new Random();

    public DealerLoop(DealerServiceImpl dealerService,
                      TableServiceGrpc.TableServiceBlockingStub tableServiceStub) {
        this.tableStub = tableServiceStub;
        this.dealerService = dealerService;
    }

    @PostConstruct
    public void start() {
        new Thread(this::runLoop).start();
    }

    private void runLoop() {
        while (true) {
            int ing1 = random.nextInt(3);
            int ing2;
            do { ing2 = random.nextInt(3); } while (ing2 == ing1);

            log.info("Dealer puts ingredients: {} and {}", ing1, ing2);
            tableStub.putIngredient(IngredientRequest.newBuilder().setIngredient(ing1).build());
            tableStub.putIngredient(IngredientRequest.newBuilder().setIngredient(ing2).build());

            log.info("Dealer waits for smoker...");
            dealerService.waitUntilContinue();
        }
    }
}