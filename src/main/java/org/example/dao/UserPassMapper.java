package org.example.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.dataobject.UserPass;
import org.example.dataobject.UserPassExample;

public interface UserPassMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    long countByExample(UserPassExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int deleteByExample(UserPassExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int insert(UserPass record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int insertSelective(UserPass record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    List<UserPass> selectByExample(UserPassExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    UserPass selectByPrimaryKey(Integer id);

    UserPass selectByUserId(Integer userId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int updateByExampleSelective(@Param("record") UserPass record, @Param("example") UserPassExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int updateByExample(@Param("record") UserPass record, @Param("example") UserPassExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int updateByPrimaryKeySelective(UserPass record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Tue Feb 08 16:55:30 CST 2022
     */
    int updateByPrimaryKey(UserPass record);
}