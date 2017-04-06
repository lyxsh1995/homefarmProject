package bean;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Date;
import java.util.Timer;

/**
 * Created by lyxsh on 2016/8/13.
 */
public class Sqlite extends SQLiteOpenHelper
{
    private static final String DB_NAME = "Icard_sqlite.db";//数据库名
    private static final int version = 1; //数据库版本

    public Sqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public Sqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlstr;
        sqlstr = "CREATE TABLE xinxi (_id INTEGER PRIMARY KEY AUTOINCREMENT,EQID TEXT,EQIDMD5 TEXT,TIME INTEGER);";
        db.execSQL(sqlstr);
        sqlstr = "insert into xinxi (_id,TIME) values('1','" + 1000000000 + "')";
        db.execSQL(sqlstr);

        db.execSQL("CREATE TABLE caiji (FInterID INTEGER PRIMARY KEY AUTOINCREMENT,FSensorID varchar(20) NOT NULL DEFAULT '',FSensorName varchar(20) NOT NULL DEFAULT '',FSampleValue int NOT NULL DEFAULT 0,FGatherDatetime varchar(20) NOT NULL DEFAULT '',FNote varchar(200))");


        db.execSQL("CREATE TABLE dataexchange (ID INTEGER PRIMARY KEY AUTOINCREMENT,EQID     varchar(100) not null default  '',SQLString varchar(1024) NOT NULL,method varchar(25),methodType varchar(10),dataexchangecol varchar(45),exchTime    varchar(30))");


        db.execSQL("CREATE TABLE device (d_bianhao  varchar(10) not null,d_name     varchar(30) not null,d_type     varchar(10) not null default '',d_status  int not null default 1,d_gongneng varchar(100) not null default '',d_qiyong   varchar(10) not null default 'dis',d_zhouqi     int not null default 0,d_qiyongtime varchar(20) not null default '',d_lasttime    varchar(20) not null default '',d_working    varchar(30) not null default '',d_wkstart    varchar(20) not null default '',d_lastvalue  int not null default 0,d_usedtime   int not null default 0,d_liang      int not null default 0,primary key (d_name));");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2004', 'shuibeng', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2011', 'shuifa1', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2012', 'shuifa2', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2013', 'shuifa3', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2017', 'deng1', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2018', 'deng2', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2019', 'deng3', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2020', 'jidiaqi1', 'kz', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2032', 'daqiwendu', 'cl', 1, '', 'en', 1, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2033', 'daqishidu', 'cl', 1, '', 'en', 1, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2034', 'turangwendu', 'cl', 1, '', 'en', 1, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2035', 'turangshidu', 'cl', 1, '', 'en', 1, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2040', 'turangwendu1', 'cl', 1, '', 'en', 5, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2041', 'turangwendu2', 'cl', 1, '', 'en', 5, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2042', 'turangwendu3', 'cl', 1, '', 'en', 5, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2043', 'turangshidu1', 'cl', 1, '', 'en', 6, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2044', 'turangshidu2', 'cl', 1, '', 'en', 6, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2045', 'turangshidu3', 'cl', 1, '', 'en', 6, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2037', 'guangzhao', 'cl', 1, '', 'en', 1, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2050', 'fuwuqitongxin', 'xt', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");
        db.execSQL("INSERT INTO device (d_bianhao, d_name, d_type, d_status, d_gongneng, d_qiyong, d_zhouqi, d_qiyongtime, d_lasttime, d_working, d_wkstart, d_lastvalue, d_usedtime, d_liang) VALUES ('2052', 'wifi', 'xt', 1, '', 'en', 0, '2017-01-16 00:17:58', '', '', '', 0, 0, 0)");

        db.execSQL("CREATE TABLE jihua (FInterID INTEGER PRIMARY KEY AUTOINCREMENT,FBillNo char(10) not null,FOnOrOff int not null default 0,FFloorOne int not null default 0,FFloorTwo int not null default 0,FFloorThree int not null default 0,FDouyaji int not null default 0,FMG int not null default 0,FShuiXiang int not null default 0,FTypeID int not null default 0,FSourceType int not null default 1,FFreq int not null default 0,FContinuePM int not null default 0,FCorS int not null default 0,zhouqijiange  varchar(20) not null default '',kaishitime  varchar(20) not null default '',begintime varchar(20) not null default '',endtime varchar(20) not null default '',firsttime varchar(20) not null default '',FExcTime varchar(20) not null default '',FStatus int not null default 0,FEndReason int not null default 0)");

        db.execSQL("CREATE TABLE lastzuoye (FFloorOne int not null default 0,FFloorTwo int not null default 0,FFloorThree int not null default 0,FDouyaji int not null default 0,FMG int not null default 0,FShuiXiang int not null default 0,FTypeId int not null default 0,FExcTime datetime,FContinueRM int not null default 0,FEndTime varchar(20))");
        db.execSQL("CREATE UNIQUE INDEX lastzuoye_jilu on lastzuoye(FTypeID, FFloorOne, FFloorTwo, FFloorThree, FDouyaji,FMG,FShuiXiang)");

        db.execSQL("CREATE TABLE pushmsg (FInterID INTEGER PRIMARY KEY AUTOINCREMENT,EQID     varchar(100) not null default  '',Ftype    int not null default 0,Fnote     varchar(100) not null default  '',Ftime    varchar(30))");

        db.execSQL("CREATE TABLE shoudong (FInterID INTEGER PRIMARY KEY AUTOINCREMENT,FBillNo char(10) not null,FOnOrOff int not null default 0,FFloorOne int not null default 0,FFloorTwo int not null default 0,FFloorThree int not null default 0,FDouyaji int not null default 0,FMG int not null default 0,FShuiXiang int not null default 0,FTypeID int not null default 0,FSourceType int not null default 3,FFreq int not null default 0,FContinuePM int not null default 0,FCorS int not null default 0,zhouqijiange  varchar(20) not null default '',kaishitime  varchar(20) not null default '',begintime varchar(20) not null default '',endtime varchar(20) not null default '',firsttime varchar(20) not null default '',FExcTime varchar(20) not null default '',FStatus int not null default 0,FEndReason int not null default 0)");

        db.execSQL("CREATE TABLE termparam(p_type varchar(10) not null default '',p_name varchar(30) not null default '',p_cname varchar(60) not null default '',p_value1 varchar(100) not null,p_value2 varchar(30),p_value3 varchar(30),p_value4 varchar(30),p_value5 varchar(30) not null default '',p_value6 varchar(30) not null default '',p_value7 varchar(30) not null default '')");

        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('EQID', 'WIFIModelMac', '', '', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('pa', 'shuibengfazhi', '', '5', '5', '30', NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('fw', 'ceng1', '', 'en', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('fw', 'ceng2', '', 'en', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('fw', 'ceng3', '', 'en', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('fw', 'shuixiang', '', 'en', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('cd', 'penshui', '', 'en', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('cd', 'shifeiandshishui', '', 'en', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('cd', 'buguang', '', 'en', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('rm', 'yunxingfangshi', '', 'en', 'en', 'en', NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('cl', 'caijishichang', '', 'dis', '10', NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('cl', 'rizhishichang', '', 'dis', '30', NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('ctrlsoft', 'homefarm', '', 'homefarm1.0', NULL, NULL, NULL, '', '', '')");
        db.execSQL("INSERT INTO termparam (p_type, p_name, p_cname, p_value1, p_value2, p_value3, p_value4, p_value5, p_value6, p_value7) VALUES ('ctrlsoft', 'edata', '', 'edata1.0', NULL, NULL, NULL, '', '', '')");

        db.execSQL("CREATE TABLE tiaojian (FInterID INTEGER PRIMARY KEY AUTOINCREMENT,FBillNo char(10) not null,FOnOrOff int not null default 0,FFloorOne int not null default 0,FFloorTwo int not null default 0,FFloorThree int not null default 0,FDouyaji int not null default 0,FMG int not null default 0,FShuiXiang int not null default 0,FTypeID int not null default 0,FSourceType int not null default 2,FFreq int not null default 0,FContinuePM int not null default 0,Flei varchar(20) not null default '',Fzhi int not null default 0,Fguanxi varchar(20) not null default '',FCorS int not null default 0,zhouqijiange  varchar(20) not null default '',kaishitime  varchar(20) not null default '',begintime varchar(20) not null default '',endtime varchar(20) not null default '',firsttime varchar(20) not null default '',FExcTime varchar(20) not null default '',FStatus int not null default 0,FEndReason int not null default 0)");

        db.execSQL("CREATE TABLE zuowu (zw_bianhao  varchar(10) not null,zw_name    varchar(30) not null,zw_picname varchar(40) not null default '',zw_fangan  varchar(20) not null default '',Primary key(zw_bianhao))");

        db.execSQL("CREATE TABLE zuoyelog (FInterID INTEGER PRIMARY KEY AUTOINCREMENT,FBillNo\tvarchar(10),FEntryID int,FTypeID int,FInStack int,FFloorOne int,FFloorTwo int,FFloorThree int,FDouyaji int not null default 0,FMG int not null default 0,FShuiXiang int not null default 0,FSourceType int,FSourceInterID int,FCorS int,FOnOrOff int,FExcTime datetime,FEndTime varchar(20),FContinuePM int not null default 0,FContinueRM int not null default 0,FExcFlag int,FDelayNu int,FFreq int,FStatus int)");

        db.execSQL("CREATE TABLE zuoyeshixu (FInterID INTEGER PRIMARY KEY AUTOINCREMENT, FBillNo varchar (10), FEntryID int, FTypeID int, FInStack int, FFloorOne int, FFloorTwo int, FFloorThree int, FDouyaji int NOT NULL DEFAULT 0, FMG int NOT NULL DEFAULT 0, FShuiXiang int NOT NULL DEFAULT 0, FSourceType int, FSourceInterID int, FCorS int, FOnOrOff INT, FExcTime datetime, FContinuePM int NOT NULL DEFAULT 0, FContinueRM int NOT NULL DEFAULT 0, FExcFlag int NOT NULL DEFAULT 0, FDelayNu int, FFreq int NOT NULL DEFAULT 100, FStatus int DEFAULT 1)");

        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (1, '100000001', NULL, 2, NULL, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, '1970-01-01 08:00:00', 0, 0, 4, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (2, '100000002', NULL, 2, NULL, 0, 1, 0, 0, 0, 0, 1, 0, 0, 1, '1970-01-01 08:00:00', 0, 0, 4, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (3, '100000003', NULL, 2, NULL, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, '1970-01-01 08:00:00', 0, 0, 4, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (4, '100000004', NULL, 4, NULL, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, '1970-01-01 08:00:00', 0, 0, 4, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (5, '100000012', NULL, 2, NULL, 1, 0, 0, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 07:30:00', 40, 0, 0, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (6, '100000011', NULL, 2, NULL, 0, 1, 0, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 07:35:00', 40, 0, 0, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (7, '100000013', NULL, 2, NULL, 0, 0, 1, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 07:40:00', 40, 0, 0, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (8, '100000014', NULL, 4, NULL, 1, 1, 1, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 08:00:00', 12600, 0, 0, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (9, '100000015', NULL, 4, NULL, 1, 1, 1, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 12:00:00', 12600, 0, 0, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (10, '100000016', NULL, 4, NULL, 1, 1, 1, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 16:00:00', 10800, 0, 0, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (11, '100000017', NULL, 2, NULL, 1, 0, 0, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 19:30:00', 40, 0, 4, '', 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (12, '100000018', NULL, 2, NULL, 0, 1, 0, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 19:35:00', 40, 0, 0, NULL, 1, 0)");
        db.execSQL("INSERT INTO zuoyeshixu (FInterID, FBillNo, FEntryID, FTypeID, FInStack, FFloorOne, FFloorTwo, FFloorThree, FDouyaji, FMG, FShuiXiang, FSourceType, FSourceInterID, FCorS, FOnOrOff, FExcTime, FContinuePM, FContinueRM, FExcFlag, FDelayNu, FFreq, FStatus) VALUES (13, '100000019', NULL, 2, NULL, 0, 0, 1, 0, 0, 0, 2, 0, 1, 1, '2017-03-15 19:40:00', 40, 0, 0, NULL, 1, 0)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
