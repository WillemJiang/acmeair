package com.acmeair.entities;

public enum SeckillResultCode {
  //成功
  Success(1),
  //最后一次成功（最后一张票）
  Finish(2),
  //失败
  Failed(3);

  private int index = 0;
  SeckillResultCode(int index) {
    this.index = index;
  }
}
