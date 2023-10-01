package com.jema.fancoin.Campay.services;

import com.jema.fancoin.Campay.models.TransactionStatusResponse;

import com.jema.fancoin.Campay.models.ApplicationBalanceResponse;
import com.jema.fancoin.Campay.models.PaymentRequestResponse;
import com.jema.fancoin.Campay.models.Token;
import com.jema.fancoin.Campay.models.WithdrawalResponse;
import com.jema.fancoin.Campay.models.requests.CollectionRequest;
import com.jema.fancoin.Campay.models.requests.TokenRequest;
import com.jema.fancoin.Campay.models.requests.WithdrawRequest;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * The interface Rest services.
 */
public interface RestServices {

    /**
     * Retrieve token.
     *
     * @param tokenRequest the token request
     * @return the observable
     */
    @POST("api/token/")
    Observable<Token> retrieveToken(@Body TokenRequest tokenRequest);

    /**
     * Collection endpoint.
     *
     * @param collectionRequest the collection request
     * @param authorization     the authorization
     * @return the observable
     */
    @POST("api/collect/")
    Observable<PaymentRequestResponse> collect(@Body CollectionRequest collectionRequest, @Header("Authorization") String authorization);

    /**
     * Transaction status.
     *
     * @param transactionId the transaction id
     * @param authorization the authorization
     * @return the observable
     */
    @GET("api/transaction/{id}/")
    Observable<TransactionStatusResponse> transactionStatus(@Path("id") String transactionId, @Header("Authorization") String authorization);

    /**
     * Withdraw.
     *
     * @param withdrawRequest the withdraw request
     * @param authorization   the authorization
     * @return the observable
     */
    @POST("api/withdraw/")
    Observable<WithdrawalResponse> withdraw(@Body WithdrawRequest withdrawRequest, @Header("Authorization") String authorization);

    /**
     * Application balance.
     *
     * @param authorization the authorization
     * @return the observable
     */
    @GET("api/balance/")
    Observable<ApplicationBalanceResponse> applicationBalance(@Header("Authorization") String authorization);

}
