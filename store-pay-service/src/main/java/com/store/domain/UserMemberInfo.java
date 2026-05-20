package com.store.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 会员信息表
 * @TableName user_member_info
 */
@TableName(value ="user_member_info")
@Data
public class UserMemberInfo {
    /**
     * ID（自增主键）
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 关联用户ID（对应用户表id）
     */
    private Integer userId;

    /**
     * 关联会员等级ID（对应idnice_member_level.id，标记购买的会员等级）
     */
    private Integer levelId;

    /**
     * 会员生效时间
     */
    private Date startTime;

    /**
     * 会员失效时间
     */
    private Date endTime;

    /**
     * 会员状态（unused=未生效，effective=生效中，expired=已过期）
     */
    private String memberStatus;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 记录创建时间
     */
    private Date createTime;

    /**
     * 记录更新时间
     */
    private Date updateTime;
}