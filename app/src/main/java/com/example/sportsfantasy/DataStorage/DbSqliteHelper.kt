package com.example.sportsfantasy.DataStorage

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.sportsfantasy.Model.Player


class DbSqliteHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION)
{
    companion object{
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "sprts.db"

        private const val TABLE_SEASON = "ThisSeason"
        private const val TABLE_LAST7GAMES = "Last7Games"
        private const val TABLE_LAST3GAMES = "Last3Games"
        private const val TABLE_LASTGAME = "LastGame"

        private const val KEY_POSITION_LIST_ID = "positionListId"
        private const val KEY_PLAYERID = "player_id"
        private const val KEY_PID = "pid"
        private const val KEY_FULLNAME = "fullname"
        private const val KEY_POSITIONTYPE = "positiontype"
        private const val KEY_PRIMARYPOSITION = "primaryposition"
        private const val KEY_SALARY = "salary"
        private const val KEY_POINTS = "points"
        private const val KEY_SALARYPERCENTAGE = "salary_percentage"
        private const val KEY_FGM = "FGM"
        private const val KEY_FG = "FG"
        private const val KEY_FT = "FT"
        private const val KEY_REB = "Reb"
        private const val KEY_AST = "Ast"
        private const val KEY_BLK = "BLK"
        private const val KEY_STL = "STL"
        private const val KEY_TOV = "TOV"
        private const val KEY_ISENABLE = "isEnable"
    }



    override fun onCreate(db: SQLiteDatabase?)
    {
        val creatTablePlayer = ("CREATE TABLE "+ TABLE_SEASON+"("
                + KEY_POSITION_LIST_ID + " TEXT,"
                + KEY_PLAYERID + " INTEGER PRIMARY KEY,"
                + KEY_PID + " TEXT ,"
                + KEY_FULLNAME + " TEXT,"
                + KEY_POSITIONTYPE + " TEXT,"
                + KEY_PRIMARYPOSITION + " TEXT,"
                + KEY_SALARY + " TEXT,"
                + KEY_POINTS + " TEXT,"
                + KEY_SALARYPERCENTAGE + " TEXT,"
                + KEY_FGM + " TEXT," + KEY_FG + " TEXT,"
                + KEY_FT + " TEXT," + KEY_REB + " TEXT,"
                + KEY_AST + " TEXT," + KEY_BLK + " TEXT,"
                + KEY_STL + " TEXT," + KEY_TOV + " TEXT"
                + ")")

        db?.execSQL(creatTablePlayer)



        val createTableLast7games = ("CREATE TABLE "+ TABLE_LAST7GAMES+"("
        + KEY_POSITION_LIST_ID + " TEXT,"
        + KEY_PLAYERID + " INTEGER PRIMARY KEY,"
        + KEY_PID + " TEXT,"
        + KEY_FULLNAME + " TEXT,"
        + KEY_POSITIONTYPE + " TEXT,"
        + KEY_PRIMARYPOSITION + " TEXT,"
        + KEY_SALARY + " TEXT,"
        + KEY_POINTS + " TEXT,"
        + KEY_SALARYPERCENTAGE + " TEXT,"
        + KEY_FGM + " TEXT," + KEY_FG + " TEXT,"
        + KEY_FT + " TEXT," + KEY_REB + " TEXT,"
        + KEY_AST + " TEXT," + KEY_BLK + " TEXT,"
        + KEY_STL + " TEXT," + KEY_TOV + " TEXT"
        + ")")
        db?.execSQL(createTableLast7games)


        val createTableLast3games = ("CREATE TABLE "+ TABLE_LAST3GAMES+"("
                + KEY_POSITION_LIST_ID + " TEXT,"
                + KEY_PLAYERID + " INTEGER PRIMARY KEY,"
                + KEY_PID + " TEXT ,"
                + KEY_FULLNAME + " TEXT,"
                + KEY_POSITIONTYPE + " TEXT,"
                + KEY_PRIMARYPOSITION + " TEXT,"
                + KEY_SALARY + " TEXT,"
                + KEY_POINTS + " TEXT,"
                + KEY_SALARYPERCENTAGE + " TEXT,"
                + KEY_FGM + " TEXT," + KEY_FG + " TEXT,"
                + KEY_FT + " TEXT," + KEY_REB + " TEXT,"
                + KEY_AST + " TEXT," + KEY_BLK + " TEXT,"
                + KEY_STL + " TEXT," + KEY_TOV + " TEXT"
                + ")")
        db?.execSQL(createTableLast3games)


        val createTableLastgame = ("CREATE TABLE "+ TABLE_LASTGAME+"("
                + KEY_POSITION_LIST_ID + " TEXT,"
                + KEY_PLAYERID + " INTEGER PRIMARY KEY,"
                + KEY_PID + " TEXT,"
                + KEY_FULLNAME + " TEXT,"
                + KEY_POSITIONTYPE + " TEXT,"
                + KEY_PRIMARYPOSITION + " TEXT,"
                + KEY_SALARY + " TEXT,"
                + KEY_POINTS + " TEXT,"
                + KEY_SALARYPERCENTAGE + " TEXT,"
                + KEY_FGM + " TEXT," + KEY_FG + " TEXT,"
                + KEY_FT + " TEXT," + KEY_REB + " TEXT,"
                + KEY_AST + " TEXT," + KEY_BLK + " TEXT,"
                + KEY_STL + " TEXT," + KEY_TOV + " TEXT"
                + ")")
        db?.execSQL(createTableLastgame)


    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int)
    {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_SEASON")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LAST7GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LAST3GAMES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LASTGAME")
        onCreate(db)
    }


    fun insterPlayer(player: Player, tableId:Int, positionListId: String):Long
    {
        val db = this.writableDatabase
        val tbl=if (tableId==0) TABLE_SEASON else if (tableId==1) TABLE_LAST7GAMES else if (tableId==2) TABLE_LAST3GAMES else TABLE_LASTGAME

        val contentValues = ContentValues()
        contentValues.put(KEY_POSITION_LIST_ID,positionListId)
        contentValues.put(KEY_PLAYERID,player.player_id)
        contentValues.put(KEY_PID,player.pid)
        contentValues.put(KEY_FULLNAME,player.fullname)
        contentValues.put(KEY_POSITIONTYPE,player.positiontype)
        contentValues.put(KEY_PRIMARYPOSITION,player.primaryposition)
        contentValues.put(KEY_SALARY,player.salary)
        contentValues.put(KEY_POINTS,player.points)
        contentValues.put(KEY_SALARYPERCENTAGE,player.salary_percentage)
        contentValues.put(KEY_FGM,player.FGM)
        contentValues.put(KEY_FG,player.FG)
        contentValues.put(KEY_FT,player.FT)
        contentValues.put(KEY_REB,player.Reb)
        contentValues.put(KEY_AST,player.Ast)
        contentValues.put(KEY_BLK,player.BLK)
        contentValues.put(KEY_STL,player.STL)
        contentValues.put(KEY_TOV,player.TOV)
        val success = db.insert(tbl,null,contentValues)


        db.close()
        return success
    }

    fun updatePlayer(uplayer: Player, positionListId: String, tableId:Int): Int
    {

        val db = this.writableDatabase
        val contentValues = ContentValues()

        val tbl=if (tableId==0) TABLE_SEASON else if (tableId==1) TABLE_LAST7GAMES else if (tableId==2) TABLE_LAST3GAMES else TABLE_LASTGAME

        contentValues.put(KEY_POSITION_LIST_ID,positionListId)
        contentValues.put(KEY_PLAYERID,uplayer.player_id)
        contentValues.put(KEY_FULLNAME,uplayer.fullname)
        contentValues.put(KEY_POSITIONTYPE,uplayer.positiontype)
        contentValues.put(KEY_PRIMARYPOSITION,uplayer.primaryposition)
        contentValues.put(KEY_SALARY,uplayer.salary)
        contentValues.put(KEY_POINTS,uplayer.points)
        contentValues.put(KEY_SALARYPERCENTAGE,uplayer.salary_percentage)
        contentValues.put(KEY_FGM,uplayer.FGM)
        contentValues.put(KEY_FG,uplayer.FG)
        contentValues.put(KEY_FT,uplayer.FT)
        contentValues.put(KEY_REB,uplayer.Reb)
        contentValues.put(KEY_AST,uplayer.Ast)
        contentValues.put(KEY_BLK,uplayer.BLK)
        contentValues.put(KEY_STL,uplayer.STL)
        contentValues.put(KEY_TOV,uplayer.TOV)

        val success = db.update(tbl,contentValues,"player_id =" + uplayer.player_id ,null)
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getPlayersData(positionListId:Int, tableId: Int): ArrayList<Player>
    {
        val tbl=if (tableId==0) TABLE_SEASON else if (tableId==1) TABLE_LAST7GAMES else if (tableId==2) TABLE_LAST3GAMES else TABLE_LASTGAME

        val arrPlayerList: ArrayList<Player> = ArrayList()
        val selectQuery = "SELECT * FROM $tbl WHERE positionListId = '$positionListId' LIMIT 50"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {

                var players = Player(
                    player_id = cursor.getString(cursor.getColumnIndex("player_id")),
                    pid = cursor.getInt(cursor.getColumnIndex("pid")),
                    fullname = cursor.getString(cursor.getColumnIndex("fullname")),
                    positiontype = cursor.getString(cursor.getColumnIndex("positiontype")),
                    primaryposition = cursor.getString(cursor.getColumnIndex("primaryposition")),
                    salary = cursor.getString(cursor.getColumnIndex("salary")),
                    salary_percentage = cursor.getString(cursor.getColumnIndex("salary_percentage")),
                    FGM = cursor.getString(cursor.getColumnIndex("FGM")),
                    FG = cursor.getString(cursor.getColumnIndex("FG")),
                    FT = cursor.getString(cursor.getColumnIndex("FT")),
                    Reb = cursor.getString(cursor.getColumnIndex("Reb")),
                    Ast = cursor.getString(cursor.getColumnIndex("Ast")),
                    BLK = cursor.getString(cursor.getColumnIndex("BLK")),
                    STL = cursor.getString(cursor.getColumnIndex("STL")),
                    TOV = cursor.getString(cursor.getColumnIndex("TOV")),
                    points = cursor.getString(cursor.getColumnIndex("points")),
                        true, "")

                arrPlayerList.add(players)

            } while (cursor.moveToNext())
        }

        return arrPlayerList

    }

    //filter code such as table id
    @SuppressLint("Range")
    fun getPlayersAllData(positionListId:Int, tableId: Int): ArrayList<Player>
    {
        val tbl=if (tableId==0) TABLE_SEASON else if (tableId==1) TABLE_LAST7GAMES else if (tableId==2) TABLE_LAST3GAMES else TABLE_LASTGAME

        val arrPlayerList: ArrayList<Player> = ArrayList()
        val selectQuery = "SELECT * FROM $tbl WHERE positionListId = '$positionListId' "
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {

                var players = Player(
                    player_id = cursor.getString(cursor.getColumnIndex("player_id")),
                    pid = cursor.getInt(cursor.getColumnIndex("pid")),
                    fullname = cursor.getString(cursor.getColumnIndex("fullname")),
                    positiontype = cursor.getString(cursor.getColumnIndex("positiontype")),
                    primaryposition = cursor.getString(cursor.getColumnIndex("primaryposition")),
                    salary = cursor.getString(cursor.getColumnIndex("salary")),
                    salary_percentage = cursor.getString(cursor.getColumnIndex("salary_percentage")),
                    FGM = cursor.getString(cursor.getColumnIndex("FGM")),
                    FG = cursor.getString(cursor.getColumnIndex("FG")),
                    FT = cursor.getString(cursor.getColumnIndex("FT")),
                    Reb = cursor.getString(cursor.getColumnIndex("Reb")),
                    Ast = cursor.getString(cursor.getColumnIndex("Ast")),
                    BLK = cursor.getString(cursor.getColumnIndex("BLK")),
                    STL = cursor.getString(cursor.getColumnIndex("STL")),
                    TOV = cursor.getString(cursor.getColumnIndex("TOV")),
                    points = cursor.getString(cursor.getColumnIndex("points")),
                    true,"")

                arrPlayerList.add(players)

            } while (cursor.moveToNext())
        }

        return arrPlayerList

    }

}