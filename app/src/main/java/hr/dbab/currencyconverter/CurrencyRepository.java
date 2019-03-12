package hr.dbab.currencyconverter;


import android.app.Application;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import hr.dbab.currencyconverter.model.Currency;
import hr.dbab.currencyconverter.rest.NetworkAPI;
import hr.dbab.currencyconverter.rest.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrencyRepository {

    //this is the data that we will fetch asynchronously
    private MutableLiveData<List<Currency>> currencyList;

    public CurrencyRepository(Application application) {

    }

    //we will call this method to get the data
    public MutableLiveData<List<Currency>> getCurrencyList() {
        //if the list is null
        if (currencyList == null){
            currencyList = new MutableLiveData<List<Currency>>();
            //we will load it asynchronously from server in this method
            loadData();
        }
        return currencyList;
    }

    private void loadData(){
        NetworkAPI networkAPI = RetrofitClient.getClient().create(NetworkAPI.class);

        Call<List<Currency>> myCall = networkAPI.getConversionRates();

        myCall.enqueue(new Callback<List<Currency>>() {
            @Override
            public void onResponse(Call<List<Currency>> call, Response<List<Currency>> response) {
                if (response.body() != null){
                    //saving all the JSON objects into a list
                    currencyList.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<List<Currency>> call, Throwable t) {

            }
        });
    }
    public String[] getCurrencyCodeArray(){
        //creating an array which stores the currency codes and returning it
        String[] availableCurrencies = new String[currencyList.getValue().size()];
        for (int i = 0; i < currencyList.getValue().size(); i++) {
            availableCurrencies[i] = currencyList.getValue().get(i).getCurrencyCode();
        }
        return availableCurrencies;
    }
}