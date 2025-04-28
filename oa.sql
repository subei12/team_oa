create table budget_log
(
    budget_log_id  varchar(100) default '' not null comment '主键'
        primary key,
    dept_id        varchar(100)            null comment '团队id',
    user_id        varchar(100)            null comment '用户id，用户id有值即为奖励给用户，用户id为空即代表当前只操作了团队预算。',
    create_date    datetime                null comment '创建时间',
    create_user_id varchar(100)            null comment '创建用户id',
    text           varchar(100)            null comment '修改时填入的理由',
    source         int                     null comment '操作数量，正为加、负为减'
)
    comment '团队预算葫芦日志表';

create table dept
(
    id               varchar(100)  not null
        primary key,
    name             varchar(100)  null comment '分类名',
    dept_key         VARCHAR(255)  NULL COMMENT '部门key',
    create_date      datetime      null comment '创建时间',
    create_user_id   varchar(100)  null comment '创建用户id,这里放用户名好了',
    state            int(1)        null comment '状态；0-正常，1-不可用',
    source           int default 0 null comment '团队剩余葫芦数量',
    settlement_state int default 0 null comment '结算状态；0-可以结算、1-不可结算'
)
    comment '团队表';

create table hlx_category_data
(
    id        int auto_increment
        primary key,
    date      date          not null comment '录入数据的日期',
    cat_id    int           null comment '板块id',
    cat_title varchar(20)   null comment '板块名称',
    cat_data  varchar(1000) null comment '板块数据',
    type      int           null comment '类型；0-板块热度数据，1-当日最新帖子（取当前时间泳池的最新帖子）'
)
    comment '板块数据表；不想多建表，一个表多用好了，用type区分';

create table hlx_user_info
(
    hlx_user_info_id int auto_increment
        primary key,
    user_id          varchar(100) null comment 'oa用户Id',
    hlx_user_name    varchar(50)  null comment '社区登录账号',
    hlx_user_pwd     varchar(100) null comment '社区登录密码',
    hlx_temp_key     varchar(200) null comment '社区临时登录凭证（key）',
    hlx_user_id      varchar(50)  null comment '社区用户id',
    create_date      datetime     null comment '创建时间',
    update_date      datetime     null comment '修改时间',
    state            int          not null comment '托管状态；0-正常，1-删除，2-账号登录失效'
)
    comment '社区用户信息表';

create table hlx_user_job
(
    hlx_user_job_id  int auto_increment comment '主键，自增'
        primary key,
    hlx_user_info_id int           null comment '逻辑外键，社区用户表主键',
    type             int           null comment '托管任务类型；0-仅登录，1-签到，2-点赞，3',
    job_info         varchar(1000) null comment '任务配置信息，（频率，任务名，帖子链接，帖子Id）',
    update_date      datetime      null comment '修改时间',
    create_date      datetime      null comment '创建时间',
    state            int           null comment '任务状态；0-启动，1-停止，2-删除'
)
    comment '用户任务表';

create table integral_mall
(
    integral_mall_id  varchar(100)  not null comment '主键'
        primary key,
    goods_name        varchar(20)   null comment '商品名',
    goods_images      varchar(200)  null comment '商品图片',
    goods_price       int default 1 null comment '商品价格',
    goods_count       int default 0 not null comment '剩余数量',
    update_date       datetime      null comment '修改时间',
    create_date       datetime      null comment '创建时间',
    create_user       varchar(20)   null comment '创建用户，其实就我一个超管可以创建',
    state             int default 0 null comment '状态，0-正常 1-下架',
    version           int           null comment '版本号，用于实现乐观锁',
    goods_description varchar(100)  null comment '商品描述',
    user_count        int default 1 not null comment '限制每个用户每月兑换的数量，为0则是不限制'
)
    comment '积分商城';

create table integral_mall_log
(
    integral_mall_log_id varchar(100)  not null comment '主键'
        primary key,
    integral_mall_id     varchar(100)  not null comment '积分商店表id',
    user_id              varchar(100)  null comment '用户Id；之前为了省事都用的是用户名，亏了呀，埋了一个深坑，以后不好整了呀。',
    create_date          datetime      null comment '创建时间',
    count                int default 1 null comment '单次兑换的个数，目前每个商城单次只能兑换一个',
    update_date          datetime      null comment '修改时间，也为管理员处理时间',
    update_user_id       varchar(100)  null comment '处理兑换的超级管理员id',
    state                int default 0 null comment '状态，0-待处理 1-处理完成',
    result               varchar(100)  null comment '处理结果，如放兑换码或者回复说已上报'
)
    comment '积分商城的兑换日志';

create table permission
(
    id          int auto_increment
        primary key,
    name        varchar(100)     not null comment '权限名称',
    description varchar(255)     null comment '权限描述',
    url         varchar(255)     not null comment '权限访问路径,需要加与method的分隔符；如：/users==GET',
    perms       varchar(255)     not null comment '权限标识',
    parent_id   int              null comment '父级权限id',
    type        int(1) default 2 null comment '类型   0：⽬录   1：菜单   2：按钮',
    order_num   int(3)           not null comment '排序，从小到大',
    icon        varchar(50)      null comment '图标',
    status      int(1) default 0 null comment '状态：0有效；1删除',
    create_time datetime         null comment '创建时间',
    update_time datetime         null comment '修改时间'
)
    comment '资源（url）与权限表';

create table post_log
(
    post_id               varchar(100) not null comment '主键'
        primary key,
    hlx_post_id           varchar(10)  null comment '操作结算的帖子id，社区帖子id',
    hlx_user_id           varchar(10)  null comment '所操作帖子的楼主，社区用户id',
    operation_hlx_user_id varchar(10)  null comment '申请结算的人，存社区用户id',
    create_date           datetime     null comment '创建时间',
    create_state          varchar(1)   null comment '创建的类别，0-OA系统内结算、1-社区命令结算',
    grade                 int          null comment '结算等级，一共三个等级；1-普通贴、2热贴、3精帖',
    state                 varchar(1)   null comment '结算状态，0-结算成功、1-本次结算操作失败；用了新的日志方案，此表每贴存一条，所以部分字段基本弃用了'
)
    comment '帖子结算记录' collate = utf8_bin;

create table post_logic
(
    id                int auto_increment comment '主键、id'
        primary key,
    logic             varchar(200) null comment '逻辑',
    logic_description varchar(200) null comment '逻辑描述',
    prompt            varchar(200) null comment '提示，用于返回结果',
    create_date       datetime     null comment '创建时间',
    update_date       datetime     null comment '修改时间',
    state             int          null comment '状态；0-正常、1-已删除、2、已停用'
)
    comment '结算前置逻辑表';

create table report_config
(
    id           varchar(100) default '' not null
        primary key,
    process_id   varchar(50)             null comment '流程id',
    title_name   varchar(50)             null comment '表单名称',
    entity_class varchar(500)            null comment '对应表单实体',
    state        int          default 0  null comment '状态：0-启用，1-停用',
    type         int          default 0  not null comment '配置配型（拿来做公共配置表了）；0-上报配置、1-任务配置'
)
    comment '上报配置表';

create table report_info
(
    report_id      int auto_increment
        primary key,
    report_content blob          null comment '上报内容',
    process_id     varchar(50)   null comment '流程id',
    create_user_id varchar(100)  null,
    create_date    datetime      null,
    update_user_id varchar(100)  null,
    update_date    datetime      null,
    state          int default 0 null comment '状态，0-新增，1-已处理'
)
    comment '上报信息表';

create table reward_level
(
    reward_level_id varchar(100) not null comment '主键id'
        primary key,
    level           int          null comment '等级',
    level_describe  varchar(10)  null comment '等级描述',
    reward_num      int          null comment '奖励数量',
    create_date     date         null comment '创建时间',
    update_date     date         null comment '修改时间',
    state           int          null comment '状态，0-可用、1-不可用'
)
    comment '结算等级' collate = utf8_bin;

create table role
(
    id          varchar(100)  not null
        primary key,
    name        varchar(128)  null comment '名称',
    description varchar(64)   null comment '描述',
    status      int default 0 not null comment '状态，0-可用、1-删除',
    level       int           not null comment '等级，用于区分是当前角色得分配权限。2机可分配3级'
)
    comment '角色表';

create table role_permission
(
    id            int auto_increment
        primary key,
    role_id       varchar(100) not null comment '角色id',
    permission_id varchar(100) not null comment '权限id'
)
    comment '角色与权限关系表';

create table send_score_log
(
    send_score_log_id varchar(100)  not null comment '主键'
        primary key,
    user_name         varchar(50)   null comment '赠送葫芦操作人',
    hlx_user_id       varchar(10)   null comment '所得葫芦的用户在社区的id',
    hlx_post_id       varchar(10)   null comment '所得葫芦的帖子',
    source_number     int default 0 null comment '所得葫芦的数量',
    msg               varchar(100)  null comment '返回消息，赠送成功为空',
    state             int           null comment '状态；0-成功、1-失败',
    create_date       date          null comment '创建时间'
)
    comment '赠送葫芦日志' collate = utf8_bin;

create table setting
(
    id             int auto_increment comment '主键'
        primary key,
    name           varchar(100) null comment '配置名',
    value          varchar(100) null comment '配置值',
    remark         varchar(100) null comment '备注',
    type           int          null comment '配置类型：１－系统配置、２－结算配置',
    create_date    datetime     null comment '创建时间',
    create_user_id varchar(100) null comment '创建人',
    update_date    datetime     null comment '修改时间',
    update_user_id varchar(100) null comment '修改人',
    state          int          null comment '状态：０－正常，１－不启用，２－已删除'
)
    comment '配置表';

create table sys_source_log
(
    id             int auto_increment
        primary key,
    hlx_user_id    varchar(20)  null comment '葫芦接收人社区id',
    source         int          not null comment '奖励数量',
    type           int          null comment '来源类型；1-帖子直接结算、2-oa账户提现、3-团队自定义奖励、4-团队成员自荐优质内容奖励',
    create_user_id varchar(100) null comment '操作人id(oa系统用户id)，没有就留空。划掉不要这个字段了',
    create_date    datetime     null comment '创建时间'
)
    comment '全局葫芦消耗日志，所有消耗都会汇总到这里';

create table t_syslog
(
    id          int auto_increment
        primary key,
    username    varchar(255) null comment '用户名',
    operation   varchar(30)  null comment '操作',
    method      varchar(255) null comment '方法名',
    params      mediumtext   null comment '参数',
    ip          varchar(30)  null comment 'ip',
    create_time timestamp    null comment '创建时间'
)
    collate = utf8_bin;

create table user
(
    id            varchar(100)     not null
        primary key,
    username      varchar(128)     not null comment '用户名',
    password      varchar(256)     null comment '密码',
    create_time   datetime         null comment '创建时间',
    salt          varchar(128)     null comment '盐',
    state         int(1) default 0 null comment '用户状态；0-正常，1-不可用',
    last_time     datetime         null comment '最后登录时间',
    update_time   datetime         null comment '修改时间',
    qq            varchar(255)     not null comment '绑定QQ',
    hlx_user_nick varchar(255)     null comment '葫芦侠昵称',
    hlx_user_id   varchar(20)      not null comment '葫芦侠用户id',
    dept_id       varchar(100)     null comment '部门(团队)id',
    integral      int    default 0 null comment '用户个人积分',
    gourd         int    default 0 not null comment '个人oa账户结算剩余葫芦数量'
)
    comment '用户表';

create table user_role
(
    id      varchar(100) not null
        primary key,
    role_id varchar(100) null comment '角色id',
    user_id varchar(100) null comment '用户id',
    remarks varchar(64)  null comment '备注'
);

create table web_details
(
    id             varchar(100)  not null
        primary key,
    web_path       varchar(100)  null comment '网站路径',
    web_name       varchar(100)  null comment '网站名',
    email          varchar(100)  null comment '提交邮箱',
    create_user_id varchar(100)  null comment '创建黑名单的用户id,为空则表示游客添加',
    web_remarks    varchar(2000) null comment '举报时的理由',
    start_date     datetime      null comment '创建时间',
    update_date    datetime      null comment '修改时间,相当于审核的时间',
    update_user_id varchar(100)  null comment '修改的用户名，也就是操作审核的用户名',
    web_sort_id    varchar(100)  null comment '分类id',
    admin_remark   varchar(2000) null comment '管理员审核回复',
    state          int           null comment '网站状态，0-审核通过，1-审核中,2-不通过，3-以删除'
)
    comment '网站详情表';
	
	


INSERT INTO team_oa.dept (id, name, create_date, create_user_id, state, source, settlement_state) VALUES ('2f427b92-abe0-11eb-879d-1831bf447e84', '测试团队', '2021-05-04 23:20:14', 'superAdmin', 0, 901, 0);


INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('0ab4a429-cd01-11eb-bc92-00163e12aeb5', '勋章_筑梦师', null, 50, 2, '2021-06-14 19:10:28', '2021-06-14 19:10:30', 'bsulike', 0, 14, '板块筑梦师勋章，时限30天。注意：勋章仅月初处理，请注意时间。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('37596027-cd01-11eb-bc92-00163e12aeb5', '称号_筑梦者', null, 50, 5, '2021-06-14 19:11:53', '2021-06-14 19:11:55', 'bsulike', 0, 8, '民间大神称号，时限30天。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('5ac9a767-240f-4312-88b5-fce76cc570d7', '背景码_明日方舟-煌', '', 20, 5, '2021-06-14 19:07:17', '2021-06-14 19:07:20', 'bsulike', 0, 0, '一个好看的背景码，永久使用。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('b85f6bc1-22ee-4f07-a4a1-a6741d7bf03e', '背景码_鬼灭之刃', '', 20, 4, '2021-06-14 19:07:17', '2021-06-14 19:07:20', 'bsulike', 0, 1, '一个好看的背景码，永久使用。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('c72ea9a2-cd00-11eb-bc92-00163e12aeb5', '背景码_芭芭拉', 'http://cdn.u1.huluxia.com/g4/M02/28/31/rBAAdl-7ffCAWVW8ABCpNdt4lFY168.png', 20, 0, '2021-06-14 19:02:07', '2021-06-14 19:02:04', 'bsulike', 1, 9, '一个好看的背景码，永久使用。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('cd580405-cd00-11eb-bc92-00163e12aeb5', '背景码_天刀-眠狼', 'http://cdn.u1.huluxia.com/g4/M00/BE/92/rBAAdl-NUC-ANvF7AAINVkfnEgo007.jpg', 20, 1, '2021-06-14 19:04:33', '2021-06-14 19:04:36', 'bsulike', 0, 6, '一个好看的背景码，永久使用。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('d314d12a-cd00-11eb-bc92-00163e12aeb5', '背景码_骑士团-安柏', 'http://cdn.u1.huluxia.com/g4/M00/93/C6/rBAAdl-GpdWAN21AABODR4RqQLA567.png', 20, 0, '2021-06-14 19:05:32', '2021-06-14 19:05:34', 'bsulike', 0, 6, '一个好看的背景码，永久使用。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('d6d771ff-6b4e-4a4b-8082-ba5246067c05', '背景码_集原美', '', 20, 0, '2021-06-14 19:07:17', '2021-06-14 19:07:20', 'bsulike', 1, 4, '一个好看的背景码，永久使用。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('db3f6d13-cd00-11eb-bc92-00163e12aeb5', '背景码_2021-谨贺新年', 'http://cdn.u1.huluxia.com/g4/M00/83/46/rBAAdl_2xYWACWVTAA76G95Abeg389.jpg', 20, 5, '2021-06-14 19:07:17', '2021-06-14 19:07:20', 'bsulike', 0, 6, '一个好看的背景码，永久使用。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('e1ca1b5a-cd00-11eb-bc92-00163e12aeb5', '勋章_八周年', null, 300, 1, '2021-06-14 19:09:18', '2021-06-14 19:09:20', 'bsulike', 0, 2, '周年勋章。', 1);
INSERT INTO team_oa.integral_mall (integral_mall_id, goods_name, goods_images, goods_price, goods_count, update_date, create_date, create_user, state, version, goods_description, user_count) VALUES ('e61c03a5-cd17-11eb-bc92-00163e12aeb5', '实物_T恤', null, 500, 5, '2021-06-14 21:54:26', '2021-06-14 21:54:28', 'bsulike', 0, 1, '葫芦侠社区纪念T恤。', 1);
	

INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (1, '团队人员管理', null, '/users==MENU', 'system:user:view', null, 1, 10, null, 0, '2022-04-19 22:13:59', '2022-04-28 23:01:03');
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (2, '团队人员管理-查询所有用户', null, '/users==GET', 'system:user:list', 1, 2, 11, null, 0, '2022-04-19 22:24:14', '2022-04-28 22:59:53');
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (3, '团队人员管理-查看', null, '/user/*==GET', 'system:user:get', 1, 2, 14, null, 0, '2022-04-19 22:24:15', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (4, '团队人员管理-修改', null, '/user/*==PUT', 'system:user:update', 1, 2, 15, null, 0, '2022-04-19 22:24:16', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (5, '团队人员管理-删除', '', '/user/*==DELETE', 'system:user:delete', 1, 2, 16, null, 0, '2022-04-19 22:24:16', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (6, '我的信息', null, '/user/my==GET', 'system:user:my', 1, 2, 12, null, 0, '2022-04-19 22:24:17', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (7, '发放自定义奖励', '需要扣除团队预算', '/user/reward/*==PUT', 'system:user:reward', 1, 2, 13, null, 0, '2022-04-19 22:35:15', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (8, '团队人员管理-添加', null, '/user==POST', 'system:user:add', 1, 2, 17, null, 0, '2022-04-20 18:03:57', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (9, '帖子结算前置逻辑', null, '/postLogic==MENU', 'system:postLogic:view', null, 1, 50, null, 0, '2022-04-23 19:49:42', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (10, '帖子结算前置逻辑-查询所有', null, '/postLogic==GET', 'system:postLogic:list', 9, 2, 52, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (11, '帖子结算前置逻辑-查看一个', null, '/postLogic/*==GET', 'system:postLogic:get', 9, 2, 51, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (12, '帖子结算前置逻辑-修改', null, '/postLogic/*==PUT', 'system:postLogic:update', 9, 2, 53, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (13, '帖子结算前置逻辑-新增', null, '/postLogic==POST', 'system:postLogic:add', 9, 2, 54, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (14, '帖子结算前置逻辑-删除', null, '/postLogic/*==DELETE', 'system:postLogic:delete', 9, 2, 55, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (15, '权限配置-查询所有', null, '/permissions==GET', 'system:permissions:list', 17, 2, 61, null, 0, null, '2022-04-28 22:47:32');
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (16, '权限配置-查询', null, '/permission/*==GET', 'system:permissions:get', 17, 2, 62, null, 0, null, '2022-04-29 09:43:41');
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (17, '权限配置', null, '/permissions==MENU', 'system:permissions:view', null, 1, 60, null, 0, '2022-04-29 09:42:55', '2022-04-29 09:43:48');
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (18, '权限配置-修改', '', '/permission/*==PUT', 'system:permissions:update', 17, 2, 63, null, 0, '2022-04-29 09:47:18', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (19, '权限管理-删除', null, '/permission/*==DELETE', 'system:permissions:delete', 17, 2, 64, null, 0, '2022-04-29 09:48:23', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (20, '权限管理-新增', null, '/permission==POST', 'system:permissions:add', 17, 2, 65, null, 0, '2022-04-29 09:49:36', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (21, '上报管理', null, '/report==MENU', 'system:report:view', null, 1, 70, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (22, '上报管理-上报', '需要搭配用户管理-查询/查询所有使用，为方便也可以带上部门管理-查询所有', '/report==POST', 'system:report:post', 21, 2, 71, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (23, '部门管理', null, '/depts==MENU', 'system:dept:view', null, 1, 80, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (24, '部门管理-查询全部', null, '/depts==GET', 'system:dept:get', 23, 2, 81, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (25, '部门管理-查询全部部门预算', null, '/dept/budget==GET', 'system:dept:budget:get', 23, 2, 82, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (26, '部门管理-预算修改', null, '/dept/budget/*==PUT', 'system:dept:budget:update', 23, 2, 83, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (27, '社区帖子处理（结算管理）', null, '/post==MENU', 'system:post:view', null, 1, 90, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (28, '结算管理-结算', null, '/post/*/*==POST', 'system:post:post', 27, 2, 92, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (29, '结算管理-查询（新）', null, '/post==GET', 'system:post:get', 27, 2, 91, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (30, '结算管理-查询（旧）', null, '/post_old==GET', 'system:post:get:old', 27, 2, 93, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (31, '积分商城', '', '/goods==MENU', 'system:goods:get:view', null, 1, 100, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (32, '积分商城-全局兑换日志', null, '/goodsLogs==GET', 'system:goods:get', 31, 2, 101, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (33, '积分商城-我的兑换记录', null, '/myGoods==GET', 'system:myGoods:get', 31, 2, 102, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (34, '积分商城-兑换', null, '/buy/*==POST', 'system:buy:post', 31, 2, 103, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (35, '积分商城-处理兑换', null, '/updateGoodsLog/*==PUT', 'system:updateGoodsLog:put', 31, 2, 104, null, 0, null, null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (36, '查询所有权限配置-InputTree', '本接口用作为角色提供可选得权限配置', '/permissionsInputTree==GET', 'system:permissions:tree', 17, 2, 66, null, 0, '2022-05-06 17:20:20', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (37, '团队人员管理-给用户赋予角色', '', '/user/role/*', 'system:user:role:update', 1, 2, 18, null, 0, '2022-05-14 18:14:22', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (38, '角色管理', '', '/roles==MENU', 'system:roles:view', null, 1, 110, null, 0, '2022-05-14 22:00:58', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (39, '角色管理-查询所有角色（为分配角色提供支持）', '', '/roleCheckboxes==GET', 'system:roles:roleCheckboxes', 38, 2, 111, null, 0, '2022-05-14 22:01:57', '2022-05-14 22:03:33');
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (40, '角色管理-查询所有角色-crudList', '', '/roles==GET', 'system:roles:list', 38, 2, 112, null, 0, '2022-05-14 22:03:55', '2022-05-14 22:05:47');
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (41, '角色管理-增加角色', null, '/role==POST', 'system:roles:add', 38, 2, 113, null, 0, '2022-05-14 22:04:47', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (42, '角色管理-查询某一个角色', null, '/role/*==GET', 'system:roles:get', 38, 2, 114, null, 0, '2022-05-14 22:06:01', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (43, '角色管理-修改角色', null, '/role/*==PUT', 'system:roles:put', 38, 2, 115, null, 0, '2022-05-14 22:07:02', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (44, '角色管理-删除（批量）角色', '', '/role/*==DELETE', 'system:roles:del', 38, 2, 116, null, 0, '2022-05-14 22:08:03', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (45, '我的信息-OA账户余额提取', '提现到社区', '/user/gourd/withdrawal/**==POST', 'system:user:my:withdrawal', 6, 2, 19, null, 0, '2022-06-18 16:57:55', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (46, '部门管理-查看各团队一个月内未发帖情况', '', '/user/posSituationt==GET', 'system:dept:user:posSituationt', 23, 2, 84, null, 0, '2022-06-18 19:42:51', null);
INSERT INTO team_oa.permission (id, name, description, url, perms, parent_id, type, order_num, icon, status, create_time, update_time) VALUES (47, '部门管理-奖励日志', '', '/sysSourceLogs==GET', 'system:dept:sysSourceLogs', 23, 2, 85, null, 0, '2022-08-25 15:43:12', '2022-08-25 15:54:09');


INSERT INTO team_oa.post_logic (id, logic, logic_description, prompt, create_date, update_date, state) VALUES (1, '$title.indexOf(''NB'') >= 0 || $title.indexOf(''HR'') >= 0 || $title.indexOf(''WD'') >= 0 || $title.indexOf(''MER'') >= 0 || $title.indexOf(''真假程序员'') >= 0', '标题需要有团队名', '非团队贴，不予结算。', '2022-01-08 17:10:10', '2022-10-25 16:40:03', 0);
INSERT INTO team_oa.post_logic (id, logic, logic_description, prompt, create_date, update_date, state) VALUES (2, '$nick.indexOf(''NB'') >= 0 || $nick.indexOf(''HR'') >= 0 || $nick.indexOf(''WD'') >= 0 || $nick.indexOf(''MER'') >= 0', '团队用户需要用团队前缀', '非团队成员，不予结算。', '2022-01-09 17:55:11', '2022-12-13 20:38:18', 2);
INSERT INTO team_oa.post_logic (id, logic, logic_description, prompt, create_date, update_date, state) VALUES (3, '$hit >= 50', '帖子浏览量大于等于50，表达式为true方可结算。', '帖子浏览量过低，不予结算。', '2022-01-15 17:06:50', '2022-01-15 17:07:19', 0);
INSERT INTO team_oa.post_logic (id, logic, logic_description, prompt, create_date, update_date, state) VALUES (4, '$commentCount != 0', '回复数量不等于0', '回复数量不可为0，结算失败。', '2022-01-15 17:23:10', '2022-01-15 17:23:10', 0);
INSERT INTO team_oa.post_logic (id, logic, logic_description, prompt, create_date, update_date, state) VALUES (5, '$title.indexOf(''悬赏'') < 0', '悬赏贴不予结算', '结算失败，悬赏贴不予结算。', '2022-01-16 14:01:19', '2022-01-16 14:01:19', 0);


INSERT INTO team_oa.report_config (id, process_id, title_name, entity_class, state, type) VALUES ('33178547-0327-11ec-bc92-00163e12aeb5', '1', '称号上报', 'top.jsls9.oajsfx.model.entity.TitleReportEntity', 0, 0);
INSERT INTO team_oa.report_config (id, process_id, title_name, entity_class, state, type) VALUES ('60c87552-0327-11ec-bc92-00163e12aeb5', '2', '改名上报', 'top.jsls9.oajsfx.model.entity.ReNameReportEntity', 0, 0);


INSERT INTO team_oa.reward_level (reward_level_id, level, level_describe, reward_num, create_date, update_date, state) VALUES ('299d5806-b51e-11eb-879d-1831bf447e84', 1, '普通贴奖励', 50, '2021-05-16', '2021-05-16', 0);
INSERT INTO team_oa.reward_level (reward_level_id, level, level_describe, reward_num, create_date, update_date, state) VALUES ('2d8d9184-b51e-11eb-879d-1831bf447e84', 2, '热贴奖励', 100, '2021-05-16', '2021-05-16', 0);
INSERT INTO team_oa.reward_level (reward_level_id, level, level_describe, reward_num, create_date, update_date, state) VALUES ('30f7522f-b51e-11eb-879d-1831bf447e84', 3, '精帖奖励', 180, '2021-05-16', '2021-05-16', 0);


INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('0889d907-cea5-11ec-8189-00163e0cb384', 'test02', '测试角色02', 1, 4);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('1990013d-d42c-11ec-8189-00163e0cb384', 'userAdmin', '团队用户管理员', 0, 3);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('24c1021c-cd2a-11ec-8189-00163e0cb384', 'test', '测试角色', 1, 3);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('2869a54f-cea5-11ec-8189-00163e0cb384', 'test02', '测试角色02', 1, 4);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('30e48acd-ce9d-11ec-8189-00163e0cb384', 'test02', '测试角色2', 1, 3);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('4823648b-d42c-11ec-8189-00163e0cb384', 'rewardAdmin', '奖励管理员', 0, 3);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('57f2ee71-ce86-11ec-8189-00163e0cb384', 'test01', '测试角色1', 1, 3);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('71b98e27-cea6-11ec-8189-00163e0cb384', 'test02', '测试角色02', 1, 4);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('74192c44-cea7-11ec-8189-00163e0cb384', 'test05', '测试角色05', 1, 5);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('80426e52-cea4-11ec-8189-00163e0cb384', 'test02', '测试用户02', 1, 3);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('a6067628-cea6-11ec-8189-00163e0cb384', 'test03', '测试角色03', 1, 4);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('b8fcdf42-8bdc-11eb-b113-00163e0a10e9', 'superAdmin', '超级管理员', 0, 1);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('c9af179c-8bdc-11eb-b113-00163e0a10e9', 'admin', '管理员', 0, 2);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('cd1ad172-8bdc-11eb-b113-00163e0a10e9', 'dev', '开发者', 0, 2);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('d120560d-cea6-11ec-8189-00163e0cb384', 'test03', '测试角色03', 1, 4);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('dbd0eeb4-cea5-11ec-8189-00163e0cb384', 'test02', '测试角色02', 1, 4);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('e9b7762a-d38d-11ec-8189-00163e0cb384', 'common', '普通用户', 0, 4);
INSERT INTO team_oa.role (id, name, description, status, level) VALUES ('f36dba3e-d42b-11ec-8189-00163e0cb384', 'reportAdmin', '上报管理员', 0, 3);


INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (18, 'd120560d-cea6-11ec-8189-00163e0cb384', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (19, 'd120560d-cea6-11ec-8189-00163e0cb384', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (20, 'd120560d-cea6-11ec-8189-00163e0cb384', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (21, 'd120560d-cea6-11ec-8189-00163e0cb384', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (22, 'd120560d-cea6-11ec-8189-00163e0cb384', '9');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (23, 'd120560d-cea6-11ec-8189-00163e0cb384', '17');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (24, 'd120560d-cea6-11ec-8189-00163e0cb384', '21');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (25, 'd120560d-cea6-11ec-8189-00163e0cb384', '23');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (26, 'd120560d-cea6-11ec-8189-00163e0cb384', '27');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (27, 'd120560d-cea6-11ec-8189-00163e0cb384', '31');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (51, '74192c44-cea7-11ec-8189-00163e0cb384', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (52, '74192c44-cea7-11ec-8189-00163e0cb384', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (53, '74192c44-cea7-11ec-8189-00163e0cb384', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (54, '24c1021c-cd2a-11ec-8189-00163e0cb384', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (55, '24c1021c-cd2a-11ec-8189-00163e0cb384', '9');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (56, '24c1021c-cd2a-11ec-8189-00163e0cb384', '17');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (57, '24c1021c-cd2a-11ec-8189-00163e0cb384', '21');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (58, '24c1021c-cd2a-11ec-8189-00163e0cb384', '23');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (59, '24c1021c-cd2a-11ec-8189-00163e0cb384', '27');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (60, '24c1021c-cd2a-11ec-8189-00163e0cb384', '31');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (187, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (188, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (189, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (190, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (191, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '7');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (192, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (193, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '4');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (194, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '5');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (195, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '8');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (196, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '37');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (197, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '9');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (198, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '11');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (199, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '10');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (200, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '12');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (201, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '13');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (202, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '14');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (203, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '17');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (204, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '15');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (205, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '16');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (206, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '18');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (207, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '19');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (208, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '20');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (209, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '36');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (210, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '21');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (211, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '22');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (212, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '23');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (213, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '24');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (214, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '25');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (215, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '26');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (216, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '27');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (217, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '29');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (218, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '28');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (219, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '30');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (220, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '31');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (221, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '32');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (222, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '33');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (223, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '34');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (224, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '35');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (225, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '38');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (226, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '39');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (227, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '40');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (228, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '41');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (229, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '42');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (230, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '43');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (231, 'cd1ad172-8bdc-11eb-b113-00163e0a10e9', '44');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (292, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (293, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (294, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '27');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (295, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '29');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (296, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '28');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (297, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '31');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (298, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '33');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (299, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '34');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (300, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '45');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (301, 'e9b7762a-d38d-11ec-8189-00163e0cb384', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (349, 'f36dba3e-d42b-11ec-8189-00163e0cb384', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (350, 'f36dba3e-d42b-11ec-8189-00163e0cb384', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (351, 'f36dba3e-d42b-11ec-8189-00163e0cb384', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (352, 'f36dba3e-d42b-11ec-8189-00163e0cb384', '21');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (353, 'f36dba3e-d42b-11ec-8189-00163e0cb384', '22');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (354, 'f36dba3e-d42b-11ec-8189-00163e0cb384', '24');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (355, 'f36dba3e-d42b-11ec-8189-00163e0cb384', '25');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (356, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (357, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (358, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (359, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '7');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (360, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (361, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '4');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (362, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '5');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (363, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '8');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (364, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '37');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (365, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '9');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (366, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '11');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (367, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '10');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (368, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '12');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (369, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '13');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (370, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '14');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (371, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '17');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (372, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '15');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (373, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '16');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (374, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '18');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (375, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '19');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (376, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '20');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (377, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '36');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (378, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '21');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (379, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '22');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (380, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '23');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (381, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '24');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (382, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '25');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (383, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '26');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (384, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '27');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (385, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '29');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (386, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '28');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (387, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '30');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (388, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '31');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (389, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '32');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (390, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '33');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (391, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '34');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (392, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '35');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (393, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '38');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (394, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '39');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (395, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '40');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (396, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '41');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (397, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '42');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (398, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '43');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (399, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '44');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (400, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '45');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (401, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (402, 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (403, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (404, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (405, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (406, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (407, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '6');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (408, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '7');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (409, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (410, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '4');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (411, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '5');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (412, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '8');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (413, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '37');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (414, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '45');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (415, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '9');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (416, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '11');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (417, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '10');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (418, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '12');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (419, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '13');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (420, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '14');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (421, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '17');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (422, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '15');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (423, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '16');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (424, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '36');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (425, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '21');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (426, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '22');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (427, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '23');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (428, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '24');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (429, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '25');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (430, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '46');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (431, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '27');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (432, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '29');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (433, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '28');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (434, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '31');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (435, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '33');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (436, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '34');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (437, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '38');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (438, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '39');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (439, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '40');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (440, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '42');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (441, 'c9af179c-8bdc-11eb-b113-00163e0a10e9', '47');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (442, '4823648b-d42c-11ec-8189-00163e0cb384', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (443, '4823648b-d42c-11ec-8189-00163e0cb384', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (444, '4823648b-d42c-11ec-8189-00163e0cb384', '7');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (445, '4823648b-d42c-11ec-8189-00163e0cb384', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (446, '4823648b-d42c-11ec-8189-00163e0cb384', '24');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (447, '4823648b-d42c-11ec-8189-00163e0cb384', '47');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (448, '1990013d-d42c-11ec-8189-00163e0cb384', '1');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (449, '1990013d-d42c-11ec-8189-00163e0cb384', '2');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (450, '1990013d-d42c-11ec-8189-00163e0cb384', '3');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (451, '1990013d-d42c-11ec-8189-00163e0cb384', '4');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (452, '1990013d-d42c-11ec-8189-00163e0cb384', '5');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (453, '1990013d-d42c-11ec-8189-00163e0cb384', '8');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (454, '1990013d-d42c-11ec-8189-00163e0cb384', '37');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (455, '1990013d-d42c-11ec-8189-00163e0cb384', '24');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (456, '1990013d-d42c-11ec-8189-00163e0cb384', '46');
INSERT INTO team_oa.role_permission (id, role_id, permission_id) VALUES (457, '1990013d-d42c-11ec-8189-00163e0cb384', '47');


INSERT INTO team_oa.user (id, username, password, create_time, salt, state, last_time, update_time, qq, hlx_user_nick, hlx_user_id, dept_id, integral, gourd) VALUES ('fa50e3d147e810945b18e384d40a4ca6', 'superAdmin', '8da386aa55b0f5a2b3aecd39465feb5d', '2021-03-16 22:37:13', 'superAdmin', 0, null, '2021-08-01 17:25:45', '27688603', '超级管理员', '123456', '18d02440-abe0-11eb-879d-1831bf447e84', 0, 0);

INSERT INTO team_oa.user_role (id, role_id, user_id, remarks) VALUES ('565d3b81-c472-11eb-bc92-00163e12aeb5', 'b8fcdf42-8bdc-11eb-b113-00163e0a10e9', 'fa50e3d147e810945b18e384d40a4ca6', null);
INSERT INTO team_oa.user_role (id, role_id, user_id, remarks) VALUES ('5cde7a3a-d42b-11ec-8189-00163e0cb384', 'e9b7762a-d38d-11ec-8189-00163e0cb384', 'fa50e3d147e810945b18e384d40a4ca6', null);







	
	
	
	

