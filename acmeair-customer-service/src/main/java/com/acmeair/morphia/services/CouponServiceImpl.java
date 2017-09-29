/*
 *   Copyright 2017 Huawei Technologies Co., Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.acmeair.morphia.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acmeair.morphia.entities.CouponImpl;
import com.acmeair.morphia.repositories.CouponRepository;
import com.acmeair.service.CouponService;
import com.acmeair.service.SeckillService;
import com.acmeair.web.dto.CouponInfo;

@Service
public class CouponServiceImpl implements CouponService {

  @Autowired
  private CouponRepository couponRepository;

  @Autowired
  private SeckillService seckillService;

  @Override
  public void syncCoupons(String username) {
    CouponImpl latestCoupon = couponRepository.findTopByCustomerIdOrderByIdDesc(username);
    int latestCouponId = latestCoupon == null ? 0 : latestCoupon.getId();
    List<CouponInfo> seckillCoupons = seckillService.getCoupons(username);
    for (CouponInfo coupon : seckillCoupons) {
      if (coupon.getId() > latestCouponId) {
        //couponRepository.save((CouponImpl) coupon);
        couponRepository.save(new CouponImpl(
            coupon.getId(),
            coupon.getPromotionId(),
            coupon.getTime(),
            coupon.getDiscount(),
            coupon.getCustomerId(),
            false));
      }
    }
  }

  @Override
  public List<CouponImpl> getCoupons(String username) {
    return couponRepository.findByCustomerIdAndIsUsed(username, false);
  }

  @Override
  public void useCoupon(int couponId) {
    CouponImpl coupon = couponRepository.findOne(couponId);
    if (coupon != null) {
      coupon.setUsed(true);
      couponRepository.save(coupon);
    }
  }
}
