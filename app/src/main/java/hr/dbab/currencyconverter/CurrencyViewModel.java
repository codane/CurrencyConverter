package hr.dbab.currencyconverter;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import hr.dbab.currencyconverter.model.Currency;

public class CurrencyViewModel extends AndroidViewModel {
    //creating member variables
    private CurrencyRepository repository;
    private MutableLiveData<List<Currency>> currencyList;
    private String[] availableCurrencies;

    //creating a constructor
    public CurrencyViewModel(@NonNull Application application) {
        super(application);
        repository = new CurrencyRepository(application);
        currencyList = repository.getCurrencyList();
    }

    public MutableLiveData<List<Currency>> getCurrencyList() {
        return currencyList;
    }

    public String[] getCurrencyCodeArray(){
        availableCurrencies = repository.getCurrencyCodeArray();
        return availableCurrencies;
    }
}
