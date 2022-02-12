package org.example.service;

import org.example.service.model.PromoModel;

public interface PromoService {
    //获取即将进行或正在进行的商品活动
    PromoModel getPromoByItemId(Integer itemId);
}
