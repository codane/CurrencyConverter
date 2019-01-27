package hr.dbab.currencyconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hr.dbab.currencyconverter.model.Currency;
import hr.dbab.currencyconverter.rest.NetworkAPI;
import hr.dbab.currencyconverter.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Spinner spinnerFrom;
    Spinner spinnerTo;
    TextView tvFrom;
    TextView tvTo;
    TextView tvResultBuying;
    TextView tvResultSelling;
    Button btnConvert;
    EditText etEntered;
    List<Currency> currencyList;
    String buyingRateFrom;
    String sellingRateFrom;
    String buyingRateTo;
    String sellingRateTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerFrom = findViewById(R.id.spinner_from);
        spinnerTo = findViewById(R.id.spinner_to);
        tvFrom = findViewById(R.id.tv_from);
        tvTo = findViewById(R.id.tv_to);
        tvResultBuying = findViewById(R.id.tv_result_buying);
        tvResultSelling = findViewById(R.id.tv_result_selling);
        btnConvert = findViewById(R.id.convert_button);
        etEntered = findViewById(R.id.et_entered);

        loadData();

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking if there is any amount entered
                if (etEntered.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "You did not enter the amount", Toast.LENGTH_SHORT).show();
                } else {
                    convertAmount();
                }
            }
        });
    }
    //method which gets data from the API
    private void loadData() {
        final NetworkAPI networkService = RetrofitClient.getClient().create(NetworkAPI.class);

        Call<List<Currency>> call = networkService.getConversionRates();

        call.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                //saving all the JSON objects into a list
                currencyList = response.body();

                //creating an array which stores the currency codes
                String[] availableCurrencies = new String[currencyList.size()];
                for (int i = 0; i < currencyList.size(); i++) {
                    availableCurrencies[i] = currencyList.get(i).getCurrencyCode();
                }
                //displaying those currency codes into both spinners
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, availableCurrencies);
                spinnerFrom.setAdapter(adapter);
                spinnerTo.setAdapter(adapter);

                spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //getting buying and selling rates from JSON object and store them into variables
                        buyingRateFrom = currencyList.get(i).getBuyingRate();
                        sellingRateFrom = currencyList.get(i).getSellingRate();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        buyingRateTo = currencyList.get(i).getBuyingRate();
                        sellingRateTo = currencyList.get(i).getSellingRate();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR" + t.toString(), Toast.LENGTH_SHORT).show();
                Log.e("FAILED", t.toString());
            }
        });
    }
    //method which calculates the sum
    public void convertAmount() {
        double enteredValue = Double.parseDouble(etEntered.getText().toString());
        double amountBuyingFrom = Double.parseDouble(buyingRateFrom);
        double amountSellingFrom = Double.parseDouble(sellingRateFrom);
        double amountBuyingTo = Double.parseDouble(buyingRateTo);
        double amountSellingTo = Double.parseDouble(sellingRateTo);


        //checking if both spinners have the same currency selected
        if (spinnerFrom.getSelectedItem().toString().equals(spinnerTo.getSelectedItem().toString())) {
            Toast.makeText(this, "You selected the same currencies!", Toast.LENGTH_LONG).show();
        }
        //checking if the HUF and JPY are not selected
        else if ((spinnerFrom.getSelectedItem().toString().equals("HUF") || spinnerFrom.getSelectedItem().toString().equals("JPY")) &&
                (spinnerTo.getSelectedItem().toString().equals("HUF") || spinnerTo.getSelectedItem().toString().equals("JPY"))) {

            double sumBuyingRate = enteredValue * (amountBuyingFrom / amountBuyingTo);
            double sumSellingRate = enteredValue * (amountSellingFrom / amountSellingTo);

            tvResultBuying.setText("Buying amount: " + (Double.toString(sumBuyingRate)));
            tvResultSelling.setText("Selling amount: " + (Double.toString(sumSellingRate)));
        }
        //checking if the HUF or JPY are selected in the first spinner not selected in the second spinner
        else if (spinnerFrom.getSelectedItem().toString().equals("HUF") || spinnerFrom.getSelectedItem().toString().equals("JPY") &&
                (!spinnerTo.getSelectedItem().toString().equals("HUF") || !spinnerTo.getSelectedItem().toString().equals("JPY"))) {

            double sumBuyingRate = enteredValue * ((amountBuyingFrom / amountBuyingTo) / 100);
            double sumSellingRate = enteredValue * ((amountSellingFrom / amountSellingTo) / 100);

            tvResultBuying.setText("Buying amount: " + (Double.toString(sumBuyingRate)));
            tvResultSelling.setText("Selling amount: " + (Double.toString(sumSellingRate)));
        }
        //checking if the HUF or JPY are not selected in the first spinner and selected in the second one
        else if ((!spinnerFrom.getSelectedItem().toString().equals("HUF") || !spinnerFrom.getSelectedItem().toString().equals("JPY")) &&
                (spinnerTo.getSelectedItem().toString().equals("HUF") || spinnerTo.getSelectedItem().toString().equals("JPY"))){

            double sumBuyingRate = enteredValue * (100 * (amountBuyingFrom / amountBuyingTo));
            double sumSellingRate = enteredValue * (100 * (amountSellingFrom / amountSellingTo));

            tvResultBuying.setText("Buying amount: " + (Double.toString(sumBuyingRate)));
            tvResultSelling.setText("Selling amount: " + (Double.toString(sumSellingRate)));

        }
        //if the HUF or JPY are not selected in any spinner
        else {
            double sumBuyingRate = enteredValue * (amountBuyingFrom / amountBuyingTo);
            double sumSellingRate = enteredValue * (amountSellingFrom / amountSellingTo);

            tvResultBuying.setText("Buying amount: " + (Double.toString(sumBuyingRate)));
            tvResultSelling.setText("Selling amount: " + (Double.toString(sumSellingRate)));
        }
    }
}





