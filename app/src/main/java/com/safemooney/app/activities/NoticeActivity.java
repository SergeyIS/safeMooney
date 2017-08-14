package com.safemooney.app.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.safemooney.R;
import com.safemooney.app.adapters.NoticeAdapter;
import com.safemooney.app.services.NoticeService;
import com.safemooney.http.models.TransactionPreview;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class NoticeActivity extends AppCompatActivity
{
    private List<TransactionPreview> transactionPreviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        ListView noticeView = (ListView) findViewById(R.id.notice_view);
        noticeView.setAdapter(new NoticeAdapter(this,R.layout.notice_item ,transactionPreviewList));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        ListView noticeView = (ListView) findViewById(R.id.notice_view);
        NoticeAdapter adapter =  (NoticeAdapter) noticeView.getAdapter();
        Queue<TransactionPreview> previews = NoticeService.getQueue();
        if(previews == null)
            return;
        adapter.addAll(previews);
    }
}
