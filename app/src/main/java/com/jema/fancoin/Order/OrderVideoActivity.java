package com.jema.fancoin.Order;

import static com.jema.fancoin.Home.SHARED_PREFS;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.jema.fancoin.R;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderVideoActivity extends AppCompatActivity {


    ImageView img, back, pp;
    TextView proName, proPrice, proDesc, proCategory;
    EditText recipient, description;
    String name, price, desc, cat, image, id;
    Button orderVideo;
    Button bottomsheet;
    FirebaseFirestore db;
    BottomSheetDialog dialog, momoDialog;
    private FirebaseAuth auth;
    private String username, useremail, userphone, userimage;
    private UserViewModel viewModel;

    // on below line creating a Paypal Configuration Object
    // on below line creating a variable to store request code for paypal sdk
    private static final String clientID = "ARZQnHyXKh8sto5HHW-a-Wg18FqqyL_djSvH9u5zD40bVfhSSv8X_ZVXsJ7oG5Hd1bwwiLEvkgBJ5szK";
    public static final int PAYPAL_REQUEST_CODE = 123;
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready,
            // switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            // on below line we are passing a client id.
            .clientId(clientID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_video);
        dialog = new BottomSheetDialog(
                this,
                R.style.ThemeOverlay_App_BottomSheetDialog
        );
        momoDialog = new BottomSheetDialog(
                this,
                R.style.ThemeOverlay_App_BottomSheetDialog
        );

        Intent i = getIntent();

        name = i.getStringExtra("name");
        price = i.getStringExtra("price");
        desc = i.getStringExtra("bio");
        cat = i.getStringExtra("category");
        image = i.getStringExtra("image");
        id = i.getStringExtra("id");

        proName = findViewById(R.id.order_star_name);
        proDesc = findViewById(R.id.order_star_bio);
        back = findViewById(R.id.order_back_btn);
        pp = findViewById(R.id.order_profile_image);
        orderVideo = findViewById(R.id.order_get_video_btns);

        recipient = findViewById(R.id.order_recipient_input);
        description = findViewById(R.id.order_description_input);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        proName.setText(name);
        proDesc.setText(desc);
        Picasso.get().load(image).into(pp);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.username != null) {
//        setting elements in drawer
                    username = user.username;
                    useremail = user.email;
                    userimage = user.image;
                    userphone = user.phone; // this might be null
                }
            }
        });

        orderVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (recipient.getText().toString().matches("")) {
                    Toast.makeText(OrderVideoActivity.this, R.string.order_recipient_and_description, Toast.LENGTH_LONG).show();
                    return;
                }
                if (description.getText().toString().matches("")) {
                    Toast.makeText(OrderVideoActivity.this, R.string.order_recipient_and_description, Toast.LENGTH_LONG).show();
                    return;
                }
                showDialog();

            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        Log.d("JemaTag", sharedPreferences.getString(USERNAME, ""));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String showDialog() {
        View view = getLayoutInflater().inflate(R.layout.order_bottomsheet, null);
        Button sendOrder = view.findViewById(R.id.order_send_order_btn);
        ImageView paypalBtn = view.findViewById(R.id.order_send_paypal);
        ImageView momoBtn = view.findViewById(R.id.momo_pay);
        TextView descr = view.findViewById(R.id.order_bottomsheet_descr);
        TextView pricing = view.findViewById(R.id.order_bottomsheet_pricing);

        descr.setText("Your are about to order from ".concat(name));
        pricing.setText("Pricing : ".concat(price).concat(" XAF"));

        paypalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Integer.parseInt(price) < 50) {
                    makePayment("50");
                } else {
                    makePayment(price);
                }
            }
        });
        momoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showMomoDialog();
            }
        });
        dialog.setCancelable(true);
        dialog.setContentView(view);
        dialog.show();
        return null;
    }


    private String showMomoDialog() {
        View viewMomo = getLayoutInflater().inflate(R.layout.momo_bottomsheet, null);
        Button payOrder = viewMomo.findViewById(R.id.momo_bottomsheet_submit);
        EditText phoneNumberInput = viewMomo.findViewById(R.id.momo_bottomsheet_number);
        TextView descr = viewMomo.findViewById(R.id.momo_bottomsheet_descr);

        descr.setText("Confirm your payment of ".concat(price).concat(" XAF"));

        payOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                momoDialog.dismiss();
//                if(Integer.parseInt(price) < 50){
//                    makePaymentMomo("50");
//                } else {
//                    makePaymentMomo(price);
//                }
                checkPaymentMomo("p.iBm5mQ7zC4mDFAw92AbVJ1g5TKbUh1DjQdCXwovCjOed9o1CVQnpDZCjitIrEIhBlITUrf96EaxPOwcKqafQO9xYpG2CXDKS");
            }
        });
        momoDialog.setCancelable(true);
        momoDialog.setContentView(viewMomo);
        momoDialog.show();
        return null;
    }


    private void makePayment(String amount) {
        // Creating a paypal payment on below line.
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Course Fees",
                PayPalPayment.PAYMENT_INTENT_SALE);
        // Creating Paypal Payment activity intent on below line
        Intent intent = new Intent(this, PaymentActivity.class);
        //putting the paypal configuration to the intent from the configuration we have created above.
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        // Putting paypal payment to the intent on below line.
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        // Starting the intent activity for result
        // the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void makePaymentMomo(String amount) {
//      We initiate the payment on notchpay

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH-m-s"); // we change the format to give a valid name string
        String strDate = formatter.format(now);
        String payRef = "payment_" + auth.getCurrentUser().getUid().concat("_" + strDate); // the name containing the user's id, date formatted and the extension
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("email", "astrideevans@gmail.com")
                        .addFormDataPart("currency", "XAF")
                        .addFormDataPart("amount", "100") // amount in fcfa
                        .addFormDataPart("phone", "651136617")
                        .addFormDataPart("reference", payRef)
                        .addFormDataPart("description", "Payment description")
                        .build();
                Request request = new Request.Builder()
                        .url("https://api.notchpay.co/payments/initialize")
                        .method("POST", body)
                        .addHeader("Authorization", "b.e2jtSqi3lAPzHNov")
                        .addHeader("Accept", "application/json")
                        .build();

                try {

                    Response response = client.newCall(request).execute();

                    if (response.isSuccessful()) {
                        // Get the response body as a string.
                        String responseData = response.body().string();
                        Log.d("JemaTag", responseData);
                        // Create a regular expression to match the reference value.
                        String regex = "\"reference\"\\s*:\\s*\"([^\"]*)\"";
                        Pattern pattern = Pattern.compile(regex);

                        // Use the regex to match the reference value in the response.
                        Matcher matcher = pattern.matcher(responseData);

                        // Extract the reference value from the matched string.
                        String referenceValue = null;
                        String referenceValue2 = null;
                        if (matcher.find()) {
                            referenceValue = matcher.group();
                            referenceValue2 = referenceValue.replaceFirst("\"reference\":\"", "").replace("\"", "");
                            Log.d("JemaTag", referenceValue2);

                            OkHttpClient clientValidate = new OkHttpClient().newBuilder()
                                    .build();
                            MediaType mediaTypeValidate = MediaType.parse("application/json");
                            RequestBody bodyValidate = RequestBody.create(mediaTypeValidate, "{\r\n\"channel\":\"cm.mtn\",\r\n\"data\":{\r\n\"phone\":\"+237651136617\"\r\n}\r\n}\r\n");
                            Request requestValidate = new Request.Builder()
                                    .url("https://api.notchpay.co/payments/" + referenceValue2)
                                    .method("PUT", bodyValidate)
                                    .addHeader("Authorization", "b.e2jtSqi3lAPzHNov")
                                    .addHeader("Accept", "application/json")
                                    .addHeader("Content-Type", "application/json")
                                    .build();

                            Response responseValidate = clientValidate.newCall(requestValidate).execute();

                            if (response.isSuccessful()) {
                                String responseData2 = responseValidate.body().string();
                                Log.d("JemaTag", responseData2);
                            }
                        }
                        // Do something with the response data.
                    } else {
                        // Handle the error response.
                        Log.d("JemaTag", "Notchpay initiate payment failed");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();

    }

    private void checkPaymentMomo(String reference) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                Request request = new Request.Builder()
                        .url("https://api.notchpay.co/payments/" + reference)
                        .addHeader("Authorization", "b.e2jtSqi3lAPzHNov")
                        .build();
                try {
                    Response paymentCheckresponse = client.newCall(request).execute();

                    if (paymentCheckresponse.isSuccessful()) {
                        String paymentCheckresponseData = paymentCheckresponse.body().string();
                        // Create a regular expression to match the reference value.
                        String regex = "\"message\"\\s*:\\s*\"([^\"]*)\"";
                        Pattern pattern = Pattern.compile(regex);

                        // Use the regex to match the reference value in the response.
                        Matcher matcher = pattern.matcher(paymentCheckresponseData);

                        // Extract the reference value from the matched string.
                        String referenceValue = null;
                        String referenceValue2 = null;
                        if (matcher.find()) {
                            referenceValue = matcher.group();
                            referenceValue2 = referenceValue.replaceFirst("\"message\":\"", "").replace("\"", "");

                            if (referenceValue2.equalsIgnoreCase("payment retrieved")) {
                                Log.d("Jematag", "payment confirmed");

                                // TODO : add redirection to payment confirmed page


                            } else {
                                Log.d("Jematag", "payment incomplete");

                                // TODO : show message that payment not confirmed

                            }
                        } else {
                            Log.d("Jematag", "Checking payment string failed");
                        }
                    } else {
                        Log.d("JemaTag", "Payment check failed");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // If the result is from paypal request code
        if (requestCode == PAYPAL_REQUEST_CODE) {
            // If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                // if confirmation is not null
                if (confirm != null) {
                    try {
                        // Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        // on below line we are extracting json response and displaying it in a text view.
                        JSONObject payObj = new JSONObject(paymentDetails);
                        String payID = payObj.getJSONObject("response").getString("id");
                        String state = payObj.getJSONObject("response").getString("state");
                        // on below line displaying a toast message with the payment status
                        HashMap<String, Object> order = new HashMap<>();

                        order.put("star_uid", id);
                        order.put("star_image", image);
                        order.put("star_pricing", price);
                        order.put("star_name", name);

                        order.put("client_uid", auth.getCurrentUser().getUid());
                        order.put("client_image", userimage);
                        order.put("client_name", username);
                        order.put("client_phoneNumber", userphone);
                        order.put("client_email", useremail);


                        order.put("recipient", recipient.getText().toString());
                        order.put("description", description.getText().toString());
                        order.put("date", new Date());
                        order.put("id", auth.getCurrentUser().getUid());

                        db.collection("Orders").document().set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Toast.makeText(OrderVideoActivity.this, "Order Sent", Toast.LENGTH_SHORT).show();
                                Intent myIntent = new Intent(OrderVideoActivity.this, SuccessOrderActivity.class);
                                myIntent.putExtra("name", name);
                                myIntent.putExtra("image", image);
                                startActivity(myIntent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OrderVideoActivity.this, R.string.unable_to_send_order, Toast.LENGTH_SHORT).show();
                            }
                        });

                        Toast.makeText(this, "Payment : " + state + " with payment id is : " + payID, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        // handling json exception on below line
                        Log.e("Error", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // on below line we are displaying a toast message as user cancelled the payment.
                Toast.makeText(this, "User cancelled the payment..", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                // on below line displaying toast message for invalid payment config.
                Toast.makeText(this, "Invalid payment config was submitted..", Toast.LENGTH_SHORT).show();
            }
        }
    }
}