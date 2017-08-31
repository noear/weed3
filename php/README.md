
```php
include 'Weed3.php';
use Noear\Weed\DbContext;
$cfg = ["connectString"=>"mysql:host=x.x.x.x;dbname=yyyy",
        "username"=>"uuuu",
        "password"=>"pppp"];
$db = new DbContext($cfg);
//=========================
//
//使用示例 （以下示例，都测试过）
//
//===============================
//示例1（SQL拼装）
//
$db->table("coupon")->limit(10)->select("*"); //ok
$db->table("coupon")->where("coupon_id=1")->select("*"); //ok
$db->table("coupon")->where("coupon_id=@id",["@id"=>1])->select("*"); //ok
$db->table("coupon")->where("coupon_id<@id AND con_money=@val",["@id"=>10,"@val"=>1000])->select("*"); //ok
$db->table("coupon")->where("coupon_id<? AND con_money=?",[10,1000])->select("*"); //ok
$db->table("coupon c")->innerJoin("coupon_lk_user l")->on("c.coupon_id=l.coupon_id")->where("c.coupon_id<? AND c.con_money=?",[10,1000])->select("c.coupon_id,l.user_id"); //ok
$db->table("coupon c")
   ->innerJoin("coupon_lk_user l")->on("c.coupon_id=l.coupon_id")
   ->where("c.coupon_id<? AND c.con_money<>?",[10,1000])
   ->groupBy("c.coupon_id")
   ->orderBy("c.coupon_id desc")
   ->select("c.coupon_id,count(l.user_id) num"); //ok
$db->table("test")->set("sex",1)->insert(); //ok
$db->table("test")->insert(["sex"=>2]); //ok
$db->table("test")->where("user_id=1")->update(["sex"=>1]); //ok
$db->table("test")->where("user_id=?",[15])->update(["name"=>'yyy']); //ok
$db->table("test")->set("sex",3)->where("user_id=@v",["@v"=>16])->update(); //ok
$db->table("test")->where("user_id=1")->delete();//ok
//应用特别情况
$db->table("coupon c")
   ->append("INNER JOIN coupon_lk_user l")
   ->append("ON c.coupon_id=l.coupon_id")
   ->append("WHERE c.coupon_id<? AND c.con_money=?",[10,1000])
   ->select("c.coupon_id,l.user_id");
//===============================
//示例2（完整SQL）
//
$db->sql("select * from `test` where user_id<@a and sex=@b;",["@a"=>17,"@b"=>1])->query(); //ok //查询语句
$db->sql("select * from `test` where user_id<? and sex=?;",[17,1])->query(); //ok //查询语句
$db->sql("delete from `test` where user_id=?;",[13])->execute(); //ok //查询语句
//===============================
//示例3（存储过程）
//
$db->call("test_xxx",[16])->query(); //存储过程
$db->call("test_yyy",[1])->execute(); //存储过程
```
