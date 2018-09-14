<?php
/**
 * Created by PhpStorm.
 * User: noear
 * Date: 2017/8/30
 * Time: 下午12:25
 * Version: 0.0.2
 */

namespace Noear\Weed;

use PDO;
use Exception;
use PDOException;

class DbContext
{
    public $driver;
    public $charset;
    public $connectString;
    public $username;
    public $password;

    public function __construct($options = null){
        if (is_array($options) == false){
            return false;
        }

        if(isset($options['driver'])) {
            $this->driver = $options['driver'];
        }
        if(isset($options['charset'])) {
            $this->charset = $options['charset'];
        }else{
            $this->charset = "utf8";
        }

        $this->connectString = $options['connectString'];
        $this->username      = $options['username'];
        $this->password      = $options['password'];
    }

    public function __destruct(){
        unset($this->driver);
        unset($this->charset);
        unset($this->server);
        unset($this->username);
        unset($this->password);
    }

    public function table($table){
        $q = new DbTableQuery($this);

        return $q->table($table);
    }

    public function sql($sql,$data=null){
        $q = new DbQuery($this);

        return $q->sql($sql, $data);
    }

    public function call($sp,$data=null){
        $q = new DbQuery($this);

        return $q->call($sp, $data);
    }
}

class DbTableQuery extends DbQueryBase{
    private $_table;
    private $_item=[];

    public function __destruct(){
        parent::__destruct();

        unset($this->_table);
        unset($this->_item);
    }


    public function table($table){
        $this->_table = $table;
        return $this;
    }

    public function set($key,$val){
        $this->_item[$key] = $val;
        return $this;
    }



    public function where($condition,$data=null){
        $condition = $this->analyzeSQL($condition,$data);
        $this->builder->append(" WHERE ")->append($condition);

        return $this;
    }

    public function append($expr,$data=null){
        $expr = $this->analyzeSQL($expr,$data);
        $this->builder->append(" ")->append($expr);

        return $this;
    }

    public function innerJoin($table){
        $this->builder->append(" INNER JOIN ")->append($table);
        return $this;
    }

    public function leftJoin($table){
        $this->builder->append(" LEFT JOIN ")->append($table);
        return $this;
    }

    public function rightJoin($table){
        $this->builder->append(" RIGHT JOIN ")->append($table);
        return $this;
    }

    public function on($condition,$data=null){
        $condition = $this->analyzeSQL($condition,$data);
        $this->builder->append(" ON ")->append($condition);

        return $this;
    }

    public function groupBy($field){
        $this->builder->append(" GROUP BY ")->append($field);
        return $this;
    }

    public function orderBy($field){
        $this->builder->append(" ORDER BY ")->append($field);
        return $this;
    }

    public function limit($rows=20, $start=0){
        if($start>0) {
            $this->builder->append(" LIMIT ")->append($start)->append(",")->append($rows);
        }else{
            $this->builder->append(" LIMIT ")->append($rows);
        }
        return $this;
    }

    public function select($fields){
        $sb = new StringBuilder();

        $sb->append("SELECT ")->append($fields)->append(" FROM ")->append($this->_table);

        $this->builder->insert($sb->toString());
        $this->builder->append(";");

        $sql = $this->builder->toString();

        return $this->SQLer()->query($sql,$this->params);
    }

    public function exists(){
        $list = $this->limit(1)->select("1");

        return count($list)>0;
    }

    //$data = {key:val};
    public function insert($data = null){
        if($data == null){
            $data = $this->_item;
        }

        if($data == null || count($data) ==0){
            return 0;
        }

        $sb = new StringBuilder();
        $values = new StringBuilder();

        $sb->append(" INSERT INTO ")->append($this->_table)->append(" (");

        foreach ($data as $key => $val)
        {
            $sb->append($key)->append(",");
            $values->append(":".$key)->append(",");
            $this->params[":".$key] = $val;
        }

        if($values->count()>2){
            $sb->removeLast();
            $values->removeLast();
        }

        $sb->append(") VALUES(")->append($values->toString())->append(");");


        $sql = $sb->toString();
        return $this->SQLer()->execute($sql,$this->params);
    }

    public function update($data = null){
        if($data == null){
            $data = $this->_item;
        }

        if($data == null || count($data) ==0){
            return 0;
        }

        $sb = new StringBuilder();

        $sb->append("UPDATE ")->append($this->_table)->append(" SET ");

        foreach ($data as $key => $val)
        {
            $sb->append($key)->append("=:")->append($key)->append(",");
            $this->params[":".$key] = $val;
        }

        $sb->removeLast();
        $sb->append(" ");

        $this->builder->insert($sb->toString());
        $this->builder->append(";");

        $sql = $this->builder->toString();

        return $this->SQLer()->execute($sql,$this->params);
    }

    public function delete(){
        $sb = new StringBuilder();

        $sb->append("DELETE FROM ")->append($this->_table)->append(" ");

        $this->builder->insert($sb->toString());
        $this->builder->append(";");

        $sql = $this->builder->toString();

        return $this->SQLer()->execute($sql,$this->params);
    }
}

class DbQuery extends DbQueryBase{

    public function sql($sql, $data=null){
        $sql = $this->analyzeSQL($sql,$data);
        $this->builder->append($sql);

        return $this;
    }

    public function call($sq, $data=null){
        $this->builder->append("CALL ")->append($sq)->append(" ");

        if($data != null && count($data)>0){
            $this->builder->append("(");
            foreach($data as $val){
                $this->builder->append("?")->append(",");
            }
            $this->builder->removeLast();
        }
        $this->builder->append(");");

        $this->params = $data;

        return $this;
    }

    public function query(){
        $sql = $this->builder->toString();

        return $this->SQLer()->query($sql,$this->params);
    }

    public function execute(){
        $sql = $this->builder->toString();
        return $this->SQLer()->execute($sql,$this->params);
    }
}

class DbQueryBase{
    protected $context;

    protected $builder;
    protected $params=[];

    public function __construct($context = null){
        $this->context = $context;
        $this->builder = new StringBuilder();
    }

    public function __destruct()
    {
        unset($this->context);
        unset($this->builder);
        unset($this->params);
    }

    protected function analyzeSQL($sql,$data=null){
        if($data != null && count($data) > 0) {
            $num = count($this->params);

            if (strrpos($sql, "?") > 0) {
                foreach ($data as $val) {
                    $key = ":v" . $num;
                    $sql = preg_replace('/\?/', $key, $sql, 1);
                    $this->params[$key] = $val;
                    $num+=1;
                }

            } else {
                $sql = str_replace("@", ":", $sql);
                foreach ($data as $key => $val) {
                    $key = str_replace("@", ":", $key);
                    $this->params[$key] = $val;
                }
            }
        }

        return $sql;
    }

    protected function SQLer(){
        return new SQLer($this->context);
    }
}

class SQLer{
    protected $context;

    public function __construct($context = null){
        $this->context = $context;
    }

    public function __destruct()
    {
        unset($this->context);
    }
    
    public function execute($sql,$params=null){ //->num
        $db  = $this->buildConnect();
        $cmd = $db->prepare($sql);

        $this->buildParams($cmd,$sql,$params);


        $cmd->execute();

        if(strrpos($sql,"INSERT INTO")>=0){
            return $db->lastInsertId();
        }else{
            return $cmd->rowCount();
        }
    }

    public function query($sql,$params=null){ //->[]
        $db  = $this->buildConnect();
        $cmd = $db->prepare($sql);

        $this->buildParams($cmd,$sql,$params);

        $cmd->execute();

        return $cmd->fetchAll(PDO::FETCH_ASSOC);
    }

    private function buildParams($cmd,$sql,$params=null){
        //构建cmd参数
        if($params != null){
            if(strrpos($sql, "?")>0){
                $idx = 1;
                foreach ($params as  $val) {
                    $cmd->bindValue($idx, $val);
                    $idx+=1;
                }
            }else {
                foreach ($params as $key => $val) {
                    $cmd->bindValue($key, $val);
                }
            }
        }
    }

    private function buildConnect(){
        $db = new PDO($this->context->connectString,  $this->context->username, $this->context->password,[PDO::MYSQL_ATTR_INIT_COMMAND => "set names ".$this->context->charset]);
        $db->setAttribute(PDO::ATTR_PERSISTENT,true);
        //设置抛出错误
        $db->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
        //设置当字符串为空时转换为sql的null
        //$db->setAttribute(PDO::ATTR_ORACLE_NULLS,true);
        //由MySQL完成变量的转义处理
        $db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);

        return $db;
    }
}

class StringBuilder
{
    const LINE = "\r\n";
    protected $list = array('');

    public function __construct($str = NULL)
    {
        array_push($this->list, $str);
    }

    public function count()
    {
        return count($this->list);
    }

    public function insert($str)
    {
        array_unshift($this->list, $str);
        return $this;
    }

    public function append($str)
    {
        array_push($this->list, $str);
        return $this;
    }

    public function appendLine($str)
    {
        array_push($this->list, $str . self::LINE);
        return $this;
    }

    public function appendFormat($str, $args)
    {
        array_push($this->list, sprintf($str, $args));
        return $this;
    }

    public function toString()
    {
        return implode("", $this->list);
    }

    public function removeLast()
    {
        array_splice($this->list, $this->count() - 1, 1);
    }

    public function __destruct()
    {
        unset($this->list);
    }
}