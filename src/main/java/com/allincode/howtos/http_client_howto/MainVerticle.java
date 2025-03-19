package com.allincode.howtos.http_client_howto;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;


/**
 * 定时任务每3秒从服务器获取数据
 * curl -H "Accept: application/json" https://icanhazdadjoke.com/
 * {"id":"IJBAsrrzPmb","joke":"What do you call a cow with two legs? Lean beef.","status":200}
 */
public class MainVerticle extends AbstractVerticle {
  private HttpRequest<JsonObject> request;

  @Override
  public void start() {
    request = WebClient.create(vertx)
      .get(443, "icanhazdadjoke.com", "/")
      .ssl(true)
      .putHeader("Accept", "application/json")
      .as(BodyCodec.jsonObject());
    vertx.setPeriodic(3000, handler -> {
      request.send(ar -> {
        if (ar.succeeded()) {
          System.out.println(ar.result().body().getString("joke"));
          System.out.println("=================================");
        } else {
          System.out.println("Something went wrong " + ar.cause().getMessage());
        }
      });
    });
  }
}
