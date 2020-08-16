-- 常规测试
CREATE TABLE sys_user (
   ID int(20) NOT NULL ,
   CODE  varchar(16) DEFAULT NULL,
   PRIMARY KEY ( ID )
) ;

insert into  sys_user values (1,'用户一');
insert into  sys_user values (2,'用户二');
insert into    sys_user values (3,'用户三');

-- orm 测试的

CREATE TABLE sys_customer (
   ID int(20) NOT NULL ,
   CODE  varchar(16) DEFAULT NULL,
   NAME  varchar(16) DEFAULT NULL,
   PRIMARY KEY ( ID )
) ;

insert into  sys_customer values (1,'a','客户一');
insert into  sys_customer values (2,'b','客户二');
insert into  sys_customer values (3,'c','客户三');


CREATE TABLE sys_order (
     ID int(20) NOT NULL ,
     NAME  varchar(16) DEFAULT NULL,
     customer_id int(20) ,
   PRIMARY KEY ( ID )
) ;

insert into  sys_order values (1,'a',1);
insert into  sys_order values (2,'b',1);
insert into  sys_order values (3,'c',2);
insert into  sys_order values (4,'d',2);

