package org.example.service;

import org.example.error.BussinessException;
import org.example.service.model.ItemModel;

import java.util.List;

public interface ItemService {
    //创建商品
    ItemModel createItem(ItemModel itemModel) throws BussinessException;
    //列表浏览
    List<ItemModel> listItem();
    //详情浏览
    ItemModel getItemById(Integer id);
    //库存扣减
    boolean decreaseStock(Integer itemId, Integer amount);
    //增加销量
    void increaseSales(Integer itemId, Integer amount);
}
