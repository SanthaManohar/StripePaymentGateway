package com.app.stripepaymentgateway;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.android.view.CardMultilineWidget;

public class StripeActivity extends AppCompatActivity {

    TextView paynow,stripe_publishkey,output_nonce;;
    Card card;
    CardMultilineWidget cardMultilineWidget;
    Stripe stripe;
    String nonce = "";
    String STRIPE_PUBLISHKEY = "pk_test_SR4FRoSZLK2c9fWBSPDYgvxj008uTPRT4Z";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe);

        paynow = findViewById(R.id.paynow);
        stripe_publishkey = findViewById(R.id.stripe_publishkey);
        output_nonce = findViewById(R.id.output_nonce);
        cardMultilineWidget = findViewById(R.id.card_input_widget);

        stripe_publishkey.setText("STRIPE_PUBLISHKEY: "+STRIPE_PUBLISHKEY);

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                card = cardMultilineWidget.getCard();
                if (card == null) {
                    Toast.makeText(StripeActivity.this, "Please fill valid Card Details", Toast.LENGTH_SHORT).show();
                } else {
                    getNonce();
                }

            }
        });

    }

    private void getNonce() {
        stripe = new Stripe(StripeActivity.this, STRIPE_PUBLISHKEY);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                nonce = token.getId();
                                output_nonce.setText("Output_Nonce: "+nonce);

                            }

                            public void onError(Exception error) {

                                Toast.makeText(StripeActivity.this, "Get Nonce Issue", Toast.LENGTH_SHORT).show();

                            }
                        }
                );
            }
        });
    }
}
