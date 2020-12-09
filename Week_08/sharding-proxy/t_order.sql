CREATE TABLE `t_order` (
  `order_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `order_status` tinyint(4) NOT NULL COMMENT '状态',
  `goods_amount` decimal(12,2) NOT NULL COMMENT '商品原价',
  `express_amount` decimal(12,2) NOT NULL COMMENT '快递金额',
  `total_amount` decimal(12,2) NOT NULL COMMENT '总价',
  `actual_amount` decimal(12,2) NOT NULL COMMENT '实付金额',
  `discount_amount` decimal(12,2) NOT NULL COMMENT '优惠金额',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB;

insert into `t_order`(`user_id`, `order_status`, `goods_amount`, `express_amount`, `total_amount`, `actual_amount`, `discount_amount`, `update_time`, `create_time`) values (1, 10, 10.00, 1.00, 1.00, 1.00, 1.00, now(), now());
select * from t_order;
select * from t_order limit 10,10;
select * from t_order where order_id = 543394997140434945;
select * from t_order where user_id = 3 and order_id = 543394997140434945;
update t_order set order_status = 20 where order_id = 543394997140434945;