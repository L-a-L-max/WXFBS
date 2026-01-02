-- ----------------------------
-- 1、Gitee绑定表
-- ----------------------------
drop table if exists gitee_bind;
create table gitee_bind (
  bind_id           bigint(20)      not null auto_increment    comment '绑定ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  gitee_user_id     varchar(64)     not null                   comment 'Gitee用户ID',
  gitee_username    varchar(100)    not null                   comment 'Gitee用户名',
  gitee_avatar      varchar(255)    default ''                 comment 'Gitee头像',
  bind_time         datetime                                   comment '绑定时间',
  primary key (bind_id),
  unique key uk_gitee_bind_user (user_id),
  unique key uk_gitee_bind_gitee (gitee_user_id),
  key idx_gitee_bind_user (user_id),
  constraint fk_gitee_bind_user foreign key (user_id) references sys_user (user_id) on delete cascade
) engine=innodb comment = 'Gitee绑定表';

-- 删除用户时同步清理Gitee绑定（软删场景）
drop trigger if exists trg_sys_user_gitee_bind_cleanup;
delimiter $$
create trigger trg_sys_user_gitee_bind_cleanup
after update on sys_user
for each row
begin
  if old.del_flag <> '2' and new.del_flag = '2' then
    delete from gitee_bind where user_id = new.user_id;
  end if;
end$$
delimiter ;

-- ----------------------------
-- 2、Gitee评测报告表
-- ----------------------------
drop table if exists gitee_analysis_report;
create table gitee_analysis_report (
  report_id         bigint(20)      not null auto_increment    comment '报告ID',
  user_id           bigint(20)      not null                   comment '用户ID',
  profile_score     int(10)         default null               comment '形象评分',
  profile_level     varchar(10)     default ''                 comment '形象等级',
  community_score   int(10)         default null               comment '社区评分',
  community_level   varchar(10)     default ''                 comment '社区等级',
  tech_score        int(10)         default null               comment '技术评分',
  tech_level        varchar(10)     default ''                 comment '技术等级',
  total_score       int(10)         default null               comment '综合评分',
  total_level       varchar(10)     default ''                 comment '综合等级',
  report_time       datetime                                   comment '评测时间',
  primary key (report_id),
  key idx_gitee_report_user (user_id),
  constraint fk_gitee_report_user foreign key (user_id) references sys_user (user_id)
) engine=innodb comment = 'Gitee评测报告表';

-- ----------------------------
-- 3、Gitee模块使用统计报表
-- ----------------------------
drop table if exists gitee_usage_report;
create table gitee_usage_report (
  report_id               bigint(20)      not null auto_increment    comment '报表ID',
  report_date             date            not null                   comment '统计日期',
  new_bind_count          int(10)         default 0                  comment '当日新增绑定用户数',
  daily_evaluation_count  int(10)         default 0                  comment '当日评测总次数',
  daily_active_user_count int(10)         default 0                  comment '当日活跃评测用户数',
  total_bind_count        int(10)         default 0                  comment '累计绑定用户数',
  score_distribution      text                                      comment '评分区间分布(JSON)',
  create_time             datetime                                  comment '创建时间',
  update_time             datetime                                  comment '更新时间',
  primary key (report_id),
  unique key uk_gitee_usage_date (report_date),
  key idx_gitee_usage_date (report_date)
) engine=innodb comment = 'Gitee模块使用统计报表';

-- ----------------------------
-- 4、菜单权限表
-- ----------------------------
-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 菜单ID规划说明（统一规划，便于扩展和维护）：
-- ┌─────────────┬──────────┬─────────────────────────────────────┐
-- │ 菜单层级    │ ID范围   │ 说明                                │
-- ├─────────────┼──────────┼─────────────────────────────────────┤
-- │ 一级菜单    │ 1-99     │ 顶级目录（如：内容管理、系统管理）  │
-- │ 二级菜单    │ 100-499  │ 功能页面（系统100-117，业务118起）  │
-- │ 三级菜单    │ 500-999  │ 子页面/子功能                       │
-- │ 按钮权限    │ 1000+    │ 按钮级操作（系统1000-1060，业务1061起）│
-- └─────────────┴──────────┴─────────────────────────────────────┘
--
-- 业务模块ID分配：
--   二级菜单：118=日更助手, 119=发布记录, 120-199预留
--   按钮权限：1061-1080=日更助手, 1081-1100=发布记录, 1101+预留
--
-- 权限标识命名规范：模块:功能:操作
--   业务模块：business:daily:*, business:publish:*, business:wechat:*
--   系统模块：system:*, monitor:*, tool:*
-- ----------------------------
-- 一级菜单（ID: 1-99）
insert into sys_menu values('8', 'gitee管理', '0', '8', 'gitee', null, '', '', 1, 0, 'M', '0', '0', '', 'gitee', 'admin', sysdate(), '', null, 'gitee管理目录');
-- 二级菜单（ID范围：100-499）
-- 内容管理子菜单（parent_id=1，业务功能从118开始）
insert into sys_menu values('127',  '使用统计', '8',   '1', 'usage-report', 'business/gitee/giteeUsageReport', '', '', 1, 0, 'C', '0', '0', 'business:gitee:usage:list', 'chart', 'admin', sysdate(), '', null, 'Gitee模块使用统计菜单');
insert into sys_menu values('128',  'gitee分析', '8',  '2', 'gitee-analysis', 'business/gitee/giteeAnalysis', '', '', 1, 0, 'C', '0', '0', 'business:gitee:analysis:view', 'chart', 'admin', sysdate(), '', null, 'Gitee分析菜单');

-- ----------------------------
-- 5、积分统计表
-- ----------------------------
-- ----------------------------
-- 初始化积分规则数据
-- ----------------------------
INSERT INTO `wx_points_rule` (`rule_code`, `rule_name`, `points_value`, `limit_type`, `limit_value`, `max_amount`, `status`, `sort_order`, `remark`, `create_by`, `create_time`) VALUES
('GITEE_ANALYSIS', 'gitee分析', -1, '2', NULL, NULL, '0', 2, 'gitee分析', 'admin', NOW());
