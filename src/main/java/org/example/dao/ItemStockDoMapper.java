package org.example.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.dataobject.ItemStockDo;
import org.example.dataobject.ItemStockDoExample;

public interface ItemStockDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    long countByExample(ItemStockDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int deleteByExample(ItemStockDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int insert(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int insertSelective(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    List<ItemStockDo> selectByExample(ItemStockDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    ItemStockDo selectByPrimaryKey(Integer id);

    ItemStockDo selectByItemId(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int updateByExampleSelective(@Param("record") ItemStockDo record, @Param("example") ItemStockDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int updateByExample(@Param("record") ItemStockDo record, @Param("example") ItemStockDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int updateByPrimaryKeySelective(ItemStockDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table item_stock
     *
     * @mbg.generated Wed Feb 09 23:46:49 CST 2022
     */
    int updateByPrimaryKey(ItemStockDo record);

    int decreaseStock(@Param("itemId") Integer itemId,
                      @Param("amount") Integer amount);
}