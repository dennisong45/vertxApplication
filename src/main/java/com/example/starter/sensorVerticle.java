package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import java.util.Random;
import java.util.UUID;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class sensorVerticle extends AbstractVerticle {



  private final String uuid = UUID.randomUUID().toString();
  private double temperature = 21.0;
  private final Random random = new Random();

  @Override
  public void start(Promise<Void> startPromise){

    vertx.setPeriodic(2000, this::updateTemperature);
    Router router = Router.router(vertx);
    router.get("/data").handler(this::getData);
    vertx.createHttpServer()
      .requestHandler(router)
      .listen(8080)
      .onSuccess(ok -> {
        System.out.println("START" + getClass().getName());
        startPromise.complete();
      }).onFailure(startPromise::fail);
  }

  private void getData(RoutingContext routingContext) {
    JsonObject payload = new JsonObject()
      .put("uuid", uuid)
      .put("temperature", temperature)
      .put("timestamp", System.currentTimeMillis());
    routingContext.response()
      .putHeader("Content-Type","application/json")
      .setStatusCode(200)
      .end(payload.encode());
  }

  private void updateTemperature(Long id){
    temperature = temperature + (random.nextGaussian() / 2.0d);
    System.out.println("Temperature = " + temperature);
  }
}
