package com.example.payment_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private List<Integer> list=new ArrayList<>();
public static   int height;
private Button sendbutton;
private EditText editTextamount,editTextupi_id,editTextname,editTextnote;
    final int UPI_PAYMENT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        for (int i=0;i<15;i++){
//            list.add(new Integer(i));
////            Toast.makeText(this,list.get(i).toString(),Toast)
//
//        }
//
//    Toast.makeText(this,String.valueOf(list.size()),Toast.LENGTH_SHORT).show();
//        RecyclerView recyclerView=findViewById(R.id.recyler);
//        Adapter adapter=new Adapter(list,this);
//        RecyclerView.LayoutManager manager=new LinearLayoutManager(this, LinearLayout.HORIZONTAL,false);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(adapter);
//        PagerSnapHelper pagerSnapHelper=new PagerSnapHelper();
//        pagerSnapHelper.attachToRecyclerView(recyclerView);
//        // add the decoration to the recyclerView
//        SeparatorDecoration decoration = new SeparatorDecoration();
//        recyclerView.addItemDecoration(decoration);
//      height =recyclerView.getHeight();

initialize();

  sendbutton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          String str_amount=editTextamount.getText().toString();
          String str_note=editTextnote.getText().toString();
          String str_name=editTextname.getText().toString();
          String str_upi_id=editTextupi_id.getText().toString();
          payusingupi(str_amount,str_upi_id,str_name,str_note);
      }
  });


    }
    public void initialize(){
        sendbutton=findViewById(R.id.send);
        editTextamount=findViewById(R.id.amount);
        editTextname=findViewById(R.id.name);
        editTextnote=findViewById(R.id.note);
        editTextupi_id=findViewById(R.id.upi_id);


    }
     void payusingupi(String amount,String upiId,String name,String note){

        Uri uri=Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",note)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();

         Intent upi_payment_intent=new Intent(Intent.ACTION_VIEW);
         upi_payment_intent.setData(uri);
         Intent chooser=Intent.createChooser(upi_payment_intent,"pay with");
         if (null!=chooser.resolveActivity(getPackageManager())){
             startActivityForResult(chooser,UPI_PAYMENT);
         }else{
             Toast.makeText(MainActivity.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
         }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(MainActivity.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(MainActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                Log.d("UPI", "responseStr: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(MainActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }
}
