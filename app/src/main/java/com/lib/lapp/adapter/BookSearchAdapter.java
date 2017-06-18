package com.lib.lapp.adapter;

import android.content.Context;

import com.lib.lapp.R;
import com.lib.lapp.model.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wxx
 * @Date 2017/5/29 15:29
 * @Description
 */

public class BookSearchAdapter extends CommonFilterAdapter<Book> {

    public BookSearchAdapter(Context context, ArrayList<Book> books) {
        super(context, books, R.layout.search_layout_item_model);
    }

    @Override
    public void convert(ViewHolder viewHolder, Book book, int position) {
        viewHolder.setText(R.id.txt_model_name, book.BOOKNAME);
    }
}
