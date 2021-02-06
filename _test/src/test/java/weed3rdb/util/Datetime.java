package weed3rdb.util;


import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//时间类（参考了.net 的接口）
//
// demo:: int date = Datetime.Now().addDay(-3).getDate();
//
public class Datetime implements Serializable,Cloneable,Comparable<Datetime> {
    private Date _datetime;
    private Calendar _calendar = null;

    public Datetime(Date date){
        setFulltime(date);
    }

    public Datetime(long milliseconds){
        setFulltime(new Date(milliseconds));
    }

    public Datetime setFulltime(Date date){
        _datetime = date;
        _calendar = Calendar.getInstance();
        _calendar.setTime(date);

        return this;
    }

    public Date getFulltime(){
        return _datetime;
    }


    //当前时间
    public static Datetime Now(){
        return new Datetime(new Date());
    }


    //添加年
    public  Datetime addYear(int year) {
        return doAdd(Calendar.YEAR, +year);
    }

    //添加月
    public  Datetime addMonth(int month) {
        return doAdd(Calendar.MONTH, +month);
    }

    //添加日
    public  Datetime addDay(int day){
        return doAdd(Calendar.DAY_OF_MONTH, +day);
    }

    //添加小时
    public  Datetime addHour(int hour){
        return doAdd(Calendar.HOUR_OF_DAY, + hour);
    }

    //添加分钟
    public  Datetime addMinute(int minute){
        return doAdd(Calendar.MINUTE, + minute);
    }

    public Datetime addSecond(int second) {
        return doAdd(Calendar.SECOND, + second);
    }

    private Datetime doAdd(int field, int amount){
        _calendar.add(field, + amount);

        _datetime = _calendar.getTime();

        return this;
    }

    //获取当前年份
    public int getYear(){
        return _calendar.get(Calendar.YEAR);
    }

    //获取当前月份
    public int getMonth(){
        return _calendar.get(Calendar.MONTH);
    }

    //获取当前日份
    public int getDays(){
        return _calendar.get(Calendar.DAY_OF_MONTH);
    }

    //获取当前小时
    public int getHours(){
        return _calendar.get(Calendar.HOUR_OF_DAY);
    }

    //获取当前分钟
    public int getMinutes(){
        return _calendar.get(Calendar.MINUTE);
    }

    //获取当前秒数
    public int getSeconds(){
        return _calendar.get(Calendar.SECOND);
    }

    //获取当前豪秒
    public long getMilliseconds(){
        return _calendar.get(Calendar.MILLISECOND);
    }

    //获取总天数（相对于：1970.01.01 00:00:00 GMT）
    public long getAllDays() {
        return getAllHours() / 24;
    }

    //获取总小时（相对于：1970.01.01 00:00:00 GMT）
    public long getAllHours(){
        return getAllMinutes()/60;
    }

    //获取总分钟（相对于：1970.01.01 00:00:00 GMT）
    public long getAllMinutes(){
        return getAllSeconds()/ 60;
    }

    //获取总秒（相对于：1970.01.01 00:00:00 GMT）
    public long getAllSeconds(){
        return getTicks()/ 1000;
    }

    //获取总毫秒（相对于：1970.01.01 00:00:00 GMT）
    public long getAllMilliseconds(){
        return getTicks();
    }

    //获取计时周期数（相对于：1970.01.01 00:00:00 GMT）
    public long getTicks(){
        return _datetime.getTime();
    }


    //获取日期数字（yyyyMMdd）
    public int getDate(){
        return Integer.parseInt(toString("yyyyMMdd"));
    }

    //转成String
    public String toString(String format){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(_datetime);
    }

    @Override
    public String toString(){
        return toString("yyyy-MM-dd HH:mm:ss");
    }

    //===================
    //
    public static Datetime parse(String datetime, String format) throws ParseException {
        DateFormat df = new SimpleDateFormat(format);
        Date date = df.parse(datetime);
        return new Datetime(date);
    }

    public static Datetime parseEx(String datetime, List<String> formats) throws Exception {
        Date date = null;

        for(String f: formats) {
            date = do_parse(datetime, f);

            if (date != null) {
                break;
            }
        }

        if(date == null) {
            throw new Exception("Unparseable date: \"" + datetime + "\"");
        }else{
            return new Datetime(date);
        }
    }

    private static Date do_parse(String source, String format){
        DateFormat df = new SimpleDateFormat(format);

        ParsePosition pos = new ParsePosition(0);
        Date result = df.parse(source, pos);
        if (pos.getIndex() == 0)
            return null;
        else
            return result;
    }

    public static Datetime tryParse(String datetime, String format)  {
        DateFormat df = new SimpleDateFormat(format);

        try {
            Date date = df.parse(datetime);
            return new Datetime(date);
        }catch (Exception ex){
            return null;
        }
    }

    @Override
    public int compareTo(Datetime anotherDatetime) {
        long thisTime = getTicks();
        long anotherTime = anotherDatetime.getTicks();
        return (thisTime<anotherTime ? -1 : (thisTime==anotherTime ? 0 : 1));
    }
}
