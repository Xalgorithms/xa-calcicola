package org.lichen.geghard.api;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;

public abstract class Completion<T> implements Observer<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            handle_http((HttpException) e);
        } else if (e instanceof IOException) {
            // network??
        }
    }

    @Override
    public void onNext(T t) {
        response(t);
    }

    protected abstract void response(T t);

    private void handle_http(HttpException e) {
        switch (e.code()) {
            case 404:
                handle_404();
        }
    }

    protected void handle_404() {

    }
}
