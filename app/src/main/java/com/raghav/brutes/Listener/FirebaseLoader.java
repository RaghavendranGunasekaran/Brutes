package com.raghav.brutes.Listener;

import com.raghav.brutes.Model.Quotes;

import java.util.List;

public interface FirebaseLoader
{

    void OnFirebaseLoadSuccess(List<Quotes> quotesList);
    void OnFirebaseLoadFailed(String message);
}
