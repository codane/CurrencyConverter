package hr.dbab.currencyconverter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import hr.dbab.currencyconverter.model.Currency;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerFrom;
    private Spinner spinnerTo;
    private TextView tvFrom;
    private TextView tvTo;
    private TextView tvResultBuying;
    private TextView tvResultSelling;
    private Button btnConvert;
    private EditText etEntered;
    private String buyingRateFrom;
    private String sellingRateFrom;
    private String buyingRateTo;
    private String sellingRateTo;
    private CurrencyViewModel currencyViewModel;

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

        currencyViewModel = ViewModelProviders.of(this).get(CurrencyViewModel.class);
        currencyViewModel.getCurrencyList().observe(this, new Observer<List<Currency>>() {
            @Override
            public void onChanged(final List<Currency> currencies) {
                //creating an array which stores the currency codes
                String[] availableCurrencies = currencyViewModel.getCurrencyCodeArray();

                //displaying currency codes into both spinners
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, availableCurrencies);
                spinnerFrom.setAdapter(adapter);
                spinnerTo.setAdapter(adapter);

                spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        //getting buying and selling rates from JSON object and store them into variables
                        buyingRateFrom = currencies.get(i).getBuyingRate();
                        sellingRateFrom = currencies.get(i).getSellingRate();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        buyingRateTo = currencies.get(i).getBuyingRate();
                        sellingRateTo = currencies.get(i).getSellingRate();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checking if there is any amount entered
                if (etEntered.getText().toString().trim().equals("")) {
                    Toast.makeText(MainActivity.this, "You did not enter the amount", Toast.LENGTH_SHORT).show();                } else {
                    convertAmount();
                }
            }
        });
    }
    public void convertAmount(){
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
