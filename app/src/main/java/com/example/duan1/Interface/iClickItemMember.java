package com.example.duan1.Interface;

import com.example.duan1.model.Member;

public interface iClickItemMember {
    void onClickDelete(Member member, int position);
    void onClickEdit(Member member, int position);
    void onClickItem(Member member, int position);
}
