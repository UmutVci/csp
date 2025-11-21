package com.umutavci.cspsmoker.logic;

import com.umutavci.proto.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SmokerLoop {

    private final TableServiceGrpc.TableServiceBlockingStub tableStub;
    private final DealerServiceGrpc.DealerServiceBlockingStub dealerStub;

    @Value("${smoker.role}")
    private String role;

    public SmokerLoop(TableServiceGrpc.TableServiceBlockingStub tableStub,
                      DealerServiceGrpc.DealerServiceBlockingStub dealerStub) {
        this.tableStub = tableStub;
        this.dealerStub = dealerStub;
    }

    @PostConstruct
    public void start() {
        new Thread(this::runLoop).start();
    }

    private void runLoop() {
        int myIngredient = mapRoleToId(role);

        while (true) {
            List<Integer> ingredients = tableStub.checkIngredients(CheckRequest.newBuilder().build())
                    .getIngredientsList();

            if (ingredients.size() == 2 && !ingredients.contains(myIngredient)) {
                // take both
                for (Integer ing : ingredients) {
                    tableStub.takeIngredient(TakeRequest.newBuilder().setIngredient(ing).build());
                }

                System.out.println(role + " smoker is smoking...");
                sleep(2000);

                dealerStub.requestContinue(ContinueRequest.newBuilder().build());
            }

            sleep(500);
        }
    }

    private int mapRoleToId(String role) {
        return switch (role.toLowerCase()) {
            case "paper" -> 0;
            case "tobacco" -> 1;
            case "matches" -> 2;
            default -> throw new IllegalArgumentException("Unknown role: " + role);
        };
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}

