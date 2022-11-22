package com.example.duan1.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.duan1.model.Room;

public class RoomViewModel extends ViewModel {
    private final MutableLiveData<Room> room = new MutableLiveData<>();
    public void setUser(Room room){
        this.room.setValue(room);
    }
    public LiveData getRoom(){
        return this.room;
    }
}
