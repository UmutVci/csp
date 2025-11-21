# Distributed Cigarette Smokers Problem (gRPC + Spring Boot)

This project implements the classical **Cigarette Smokers Synchronization Problem** in a *distributed* setting.  
Instead of solving the problem using threads and shared memory, each role is implemented as a **separate node communicating via gRPC**.

## System Architecture

The system consists of **five independent processes**, each running as its own Spring Boot application:

| Node | Responsibility |
|------|---------------|
| **Dealer** | Chooses two random ingredients and places them on the table. Waits for smoker signal to continue. |
| **Table** | Stores current ingredients & exposes RPC methods (`putIngredient`, `takeIngredient`, `checkIngredients`). |
| **Smoker 1** | Has infinite *paper*. Takes missing ingredients and smokes. |
| **Smoker 2** | Has infinite *tobacco*. Takes missing ingredients and smokes. |
| **Smoker 3** | Has infinite *matches*. Takes missing ingredients and smokes. |

All communication happens **only through RPC calls**, never shared memory or threads.


## Communication (gRPC)

All nodes share the same `.proto` file:

```proto
service TableService {
  rpc putIngredient(IngredientRequest) returns (PutResponse);
  rpc checkIngredients(CheckRequest) returns (CheckResponse);
  rpc takeIngredient(TakeRequest) returns (TakeResponse);
}

service DealerService {
  rpc requestContinue(ContinueRequest) returns (ContinueResponse);
}

```

How to Run

Ensure you have Java 21+ and Maven installed.

## 1. Build all nodes
```
mvn clean package -DskipTests
```

This generates runnable JAR files inside each module’s target/ directory.

## 2. Start the Table Node (gRPC Server)
```
cd csp-table
java -jar target/table-node-0.0.1-SNAPSHOT.jar
```

The Table node exposes its gRPC service at:

```localhost:9090```

## 3. Start the Dealer Node
```cd csp-dealer
java -jar target/dealer-node-0.0.1-SNAPSHOT.jar
```

Dealer connects to the Table node and randomly places ingredients.

## 4. Start Smoker Nodes (run 3 instances)

Each smoker role runs in a separate terminal instance:

```cd csp-smoker```
```java -jar target/smoker-node-0.0.1-SNAPSHOT.jar --smoker.role=paper```

```java -jar target/smoker-node-0.0.1-SNAPSHOT.jar --smoker.role=tobacco```

```java -jar target/smoker-node-0.0.1-SNAPSHOT.jar --smoker.role=matches```


Alternatively, roles can be configured in application.properties:

```smoker.role=paper```

## 5. Runtime Behavior

Once all nodes are running:

Dealer places 2 random ingredients on the table

Only the smoker holding the missing ingredient smokes

Smoker notifies Dealer via gRPC

Dealer proceeds with next cycle

Sample logs:
```
Dealer puts ingredients: tobacco + paper
Smoker(matches): takes ingredients and smokes...
Dealer waits for smoker...
```
## 6. Shut Down

Stop nodes manually via:
```
CTRL + C
```

Each node can be stopped independently.

Ingredients are represented as integers:
0 = paper
1 = tobacco
2 = matches
