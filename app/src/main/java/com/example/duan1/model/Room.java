package com.example.duan1.model;

import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(foreignKeys = @ForeignKey(entity = RoomType.class, childColumns = "idRoomType" , parentColumns = "idRoomType" , onDelete = ForeignKey.CASCADE))
public class Room implements Serializable {
    @PrimaryKey
    @NonNull
    private String roomCode;
    private int floor;
    private String describe;
    private String image;
    private int idRoomType;

    public Room(FragmentActivity activity) {
    }

    public Room(@NonNull String roomCode, int floor, String describe, String image, int idRoomType) {
        this.roomCode = roomCode;
        this.floor = floor;
        this.describe = describe;
        this.image = image;
        this.idRoomType = idRoomType;
    }

    @NonNull
    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(@NonNull String roomCode) {
        this.roomCode = roomCode;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getIdRoomType() {
        return idRoomType;
    }

    public void setIdRoomType(int idRoomType) {
        this.idRoomType = idRoomType;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public SQLiteDatabase getReadableDatabase() {
        synchronized (this) {
            return getReadableDatabase();
        }
    }


}
