package edu.ouc.rpc.async;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import edu.ouc.rpc.model.RpcResponse;


public class ResponseFuture {
    public static ThreadLocal<Future<RpcResponse>> futureThreadLocal = new ThreadLocal<Future<RpcResponse>>();

    public static Object getResponse(long timeout) throws InterruptedException {
        if (null == futureThreadLocal.get()) {
            throw new RuntimeException("Thread [" + Thread.currentThread() + "] have not set the response future!");
        }

        try {
            RpcResponse response =futureThreadLocal.get().get(timeout, TimeUnit.MILLISECONDS);
            if (response.isError()) {
                throw new RuntimeException(response.getErrorMsg());
            }
            return response.getResponseBody();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException("Time out", e);
        }
    }

    public static void setFuture(Future<RpcResponse> future){
        futureThreadLocal.set(future);
    }
}
