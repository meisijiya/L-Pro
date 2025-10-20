create table if not exists user
(
    id           bigint auto_increment comment 'id'
    primary key,
    username     varchar(255)                       null comment '用户昵称',
    password     varchar(512)                       not null comment '用户密码',
    user_account varchar(255)                       null comment '账号',
    avatar_url   varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别 0-女 1-男 2-保密',
    profile      varchar(255)                       null,
    phone        varchar(128)                       null comment '手机号',
    email        varchar(512)                       null comment '邮箱',
    status       int      default 0                 null comment '用户状态，0为正常',
    role         int      default 0                 not null comment '用户角色 0-普通用户,1-管理员',
    friend_ids   varchar(255)                       null,
    tags         varchar(1024)                      null comment '标签列表',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete    tinyint  default 0                 not null comment '是否删除'
    )
    charset = utf8mb3
    row_format = DYNAMIC;