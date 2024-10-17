package com.example.duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan1.Interface.IClickItemRoom;
import com.example.duan1.R;
import com.example.duan1.database.DbMotel;
import com.example.duan1.databinding.ItemRoomBinding;
import com.example.duan1.model.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {
    Context context;
    List<Room> list;
    private IClickItemRoom iClickItemListener;
    public RoomAdapter(Context context, List<Room> list, IClickItemRoom listener) {
        this.context = context;
        this.list = list;
        iClickItemListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ItemRoomBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Room room = list.get(position);
        DbMotel db = DbMotel.getInstance(context);

        Room roomCheck = db.roomDao().checkRoom(room.getRoomCode());
        if(roomCheck == null){
            //Room empty
            holder.binding.img.setImageResource(R.drawable.room_open2);
        }
        holder.itemView.setOnClickListener(view -> iClickItemListener.onClickItemRoom(room,roomCheck != null));
        holder.binding.tvRoomCode.setText(room.getRoomCode());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ItemRoomBinding binding;
        public MyViewHolder(ItemRoomBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}