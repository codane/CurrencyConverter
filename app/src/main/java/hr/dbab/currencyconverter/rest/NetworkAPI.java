package hr.dbab.currencyconverter.rest;

import java.util.List;

import hr.dbab.currencyconverter.model.Currency;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NetworkAPI {

    @GET("daily")
    Call<List<Currency>> getConversionRates();
}
