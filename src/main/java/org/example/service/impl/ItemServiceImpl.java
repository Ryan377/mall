package org.example.service.impl;

import org.example.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.dao.*;
import org.example.dao.ItemStockDoMapper;
import org.example.dataobject.ItemDo;
import org.example.dataobject.ItemStockDo;
import org.example.error.BussinessException;
import org.example.error.EnumBussinessError;
import org.example.service.ItemService;
import org.example.service.PromoService;
import org.example.service.model.ItemModel;
import org.example.service.model.PromoModel;
import org.example.validator.ValidationResult;
import org.example.validator.ValidatorImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ValidatorImpl validator;
    @Autowired(required = false)
    private ItemDoMapper itemDOMapper;
    @Autowired(required = false)
    private ItemStockDoMapper itemStockDOMapper;
    @Autowired
    private PromoService promoService;

    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) throws BussinessException {
        //校验入参
        ValidationResult result=validator.validate(itemModel);
        if(result.isHasErrors()){
            throw new BussinessException(EnumBussinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        //转化ItemModel变成DataObject
        ItemDo itemDO = this.convertItemDOFromItemModel(itemModel);
        //写入数据库
        itemDOMapper.insertSelective(itemDO);
        //得到生成的主键，并将主键一并写入到itemStock表
        itemModel.setId(itemDO.getId());
        ItemStockDo itemStockDO=this.convertItemStockDOFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);
        //返回创建完成的对象
        return this.getItemById(itemModel.getId());
    }

    @Override
    public List<ItemModel> listItem() {
        List<ItemDo> list = itemDOMapper.listItem();
        List<ItemModel> itemModelList= list.stream().map(itemDO -> {
            ItemStockDo itemStockDO=itemStockDOMapper.selectByItemId(itemDO.getId());
            return this.convertModelFromDataObject(itemDO,itemStockDO);
        }).collect(Collectors.toList());
        return itemModelList;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDo itemDO = itemDOMapper.selectByPrimaryKey(id);
        if(itemDO == null) return null;
        //操作获得库存数量
        ItemStockDo itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        //将dataObj转换成Model
        ItemModel itemModel=convertModelFromDataObject(itemDO,itemStockDO);
        //获取商品的活动信息
        PromoModel promoModel= promoService.getPromoByItemId(itemModel.getId());
        if(promoModel!=null&&promoModel.getStatus()!=3){
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int affectedRow=itemStockDOMapper.decreaseStock(itemId,amount);
        return (affectedRow>0);
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDOMapper.increaseSales(itemId,amount);
    }

    private ItemDo convertItemDOFromItemModel(ItemModel itemModel){
        if(itemModel == null) return null;
        ItemDo itemDO = new ItemDo();
        BeanUtils.copyProperties(itemModel,itemDO);
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    private ItemStockDo convertItemStockDOFromItemModel(ItemModel itemModel){
        if(itemModel == null) return null;
        ItemStockDo itemStockDO = new ItemStockDo();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }

    private ItemModel convertModelFromDataObject(ItemDo itemDO,ItemStockDo itemStockDO){
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO,itemModel);
        itemModel.setPrice(new BigDecimal(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
