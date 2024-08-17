package com.example.foodplanner.model.repo.remote;

import android.content.Context;

import com.example.foodplanner.R;
import com.example.foodplanner.api.ApiManager;
import com.example.foodplanner.api.WebService;
import com.example.foodplanner.db.FirebaseUtils;
import com.example.foodplanner.model.dto.Country;
import com.example.foodplanner.model.dto.Ingredient;
import com.example.foodplanner.model.dto.IngredientResponse;
import com.example.foodplanner.model.dto.MealResponse;
import com.example.foodplanner.model.dto.MealsItem;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface MealRemoteDataSource {
    @NonNull Observable<List<MealsItem>> getMealByCategory(String category);

    Observable<Object> getMealById(String mealId);

    Observable<List<MealsItem>> getFavMealsFromFireStore();

    void addMealToPlan(MealsItem mealsItem,
                       OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    void addMealToFav(MealsItem mealsItem, OnSuccessListener<Void> onSuccessListener,
                      OnFailureListener onFailureListener);

    Observable<List<MealsItem>> getWeeklyPlannedMealsObservable(String date);

    void deleteFavoriteMealFromFireStore(MealsItem mealsItem,
                                         OnSuccessListener<Void> onSuccessListener,
                                         OnFailureListener onFailureListener);

    void deletePlannedMealFireStore(MealsItem mealsItem, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener);

    Observable<List<MealsItem>> searchMeals(String name);

    Observable<List<Ingredient>> getIngredients();

    Observable<List<MealsItem>> getMealsByIngredient(String ingredient);

    Observable<List<Country>> getCountries();

    Observable<List<MealsItem>> getMealsByCountry(String country);
    public Single<FirebaseUser> signInWithGoogle(GoogleSignInAccount account);
    Observable<AuthResult> signUpWithGoogle(String idToken);


    class MealRemoteDataSourceImp implements MealRemoteDataSource {
        WebService webService;
        Context context;

        public MealRemoteDataSourceImp(Context context) {
            this.webService = ApiManager.getApi();
            this.context = context;
        }

        @Override
        public @NonNull Observable<List<MealsItem>> getMealByCategory(String category) {
            return webService.getAllMealsByCategory(category)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(mealResponse -> {
                        List<MealsItem> meals = new ArrayList<>();
                        if (mealResponse != null && mealResponse.getMeals() != null) {
                            meals.addAll(mealResponse.getMeals());
                        }
                        return meals;
                    });
        }

        @Override
        public @NonNull Observable<Object> getMealById(String mealId) {
            return Observable.create(emitter -> webService.getMealById(mealId).enqueue(new Callback<MealResponse>() {
                @Override
                public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        emitter.onNext(response.body().getMeals().get(0));
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Throwable("Failed to get meals"));
                    }
                }

                @Override
                public void onFailure(Call<MealResponse> call, Throwable t) {
                    emitter.onError(t);
                }
            })).subscribeOn(Schedulers.io());
        }

        @Override
        public void addMealToPlan(MealsItem mealsItem,
                                  OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
            FirebaseUtils.addMealToPlan(mealsItem, onSuccessListener, onFailureListener);
        }

        @Override
        public void addMealToFav(MealsItem mealsItem, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
            FirebaseUtils.addMealToFav(mealsItem, onSuccessListener, onFailureListener);
        }

        @Override
        public Observable<List<MealsItem>> getFavMealsFromFireStore() {
            return Observable.<List<MealsItem>>create(emitter -> FirebaseUtils.getFavMeals(context, task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = (QuerySnapshot) task.getResult();
                    if (querySnapshot != null) {
                        List<MealsItem> mealsItems = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            MealsItem mealsItem = documentSnapshot.toObject(MealsItem.class);
                            if (mealsItem != null) {
                                mealsItems.add(mealsItem);
                            }
                        }
                        emitter.onNext(mealsItems);
                        emitter.onComplete();
                    } else {
                        emitter.onError(new Exception("QuerySnapshot is null"));
                    }
                } else {
                    emitter.onError(task.getException());
                }
            })).subscribeOn(Schedulers.io());
        }

        @Override
        public Observable<List<MealsItem>> getWeeklyPlannedMealsObservable(String date) {
            return Observable.create((ObservableOnSubscribe<List<MealsItem>>) emitter -> FirebaseUtils.getWeeklyPlannedMeals(context, date, task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null) {
                        List<MealsItem> mealsItems = new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                            MealsItem mealsItem = documentSnapshot.toObject(MealsItem.class);
                            if (mealsItem != null) {
                                mealsItems.add(mealsItem);
                            }
                        }
                        emitter.onNext(mealsItems);
                        emitter.onComplete();
                    } else {
                        emitter.onError(new IllegalStateException("No Data"));
                    }
                } else {
                    emitter.onError(task.getException());
                }
            }, e -> emitter.onError(e.fillInStackTrace()))).subscribeOn(Schedulers.io());
        }

        @Override
        public void deleteFavoriteMealFromFireStore(MealsItem mealsItem, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
            FirebaseUtils.deleteFavMeal(context, mealsItem.getIdMeal(), onSuccessListener, onFailureListener);
        }

        @Override
        public void deletePlannedMealFireStore(MealsItem mealsItem, OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener) {
            FirebaseUtils.deletePlannedMeal(context, mealsItem.getIdMeal(), onSuccessListener, onFailureListener);
        }

        @Override
        public Observable<List<MealsItem>> searchMeals(String name) {
            return webService.searchMealByName(name)
                    .map(MealResponse::getMeals)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }

        @Override
        public Observable<List<Ingredient>> getIngredients() {
            return webService.getIngredients().map(IngredientResponse::getMeals);
        }

        @Override
        public Observable<List<MealsItem>> getMealsByIngredient(String ingredient) {
            return webService.getMealsBIngredient(ingredient).map(MealResponse::getMeals).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    ;
        }

        @Override
        public Observable<List<Country>> getCountries() {
            return webService.getCountries()
                    .map(response -> {
                        List<Country> countryList = new ArrayList<>();
                        for (MealsItem meal : response.getMeals()) {
                            String countryName = meal.getStrArea();
                            int imageResourceId = getResourceIdForCountry(countryName); // Implement this method
                            countryList.add(new Country(countryName, imageResourceId));
                        }
                        return countryList;
                    });

        }

        @Override
        public Observable<List<MealsItem>> getMealsByCountry(String country) {
            return webService.getMealsByCountry(country).map(MealResponse::getMeals);
        }

        @Override
        public Single<FirebaseUser> signInWithGoogle(GoogleSignInAccount account) {

            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            return Single.create(emitter -> FirebaseUtils.getFirebaseInstance().signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onSuccess(task.getResult().getUser());
                        } else {
                            emitter.onError(task.getException());
                        }
                    }));
           /* return Observable.create(emitter -> {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(activity.getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);
                Intent signInIntent = googleSignInClient.getSignInIntent();
                activity.startActivityForResult(signInIntent, FirebaseUtils.RC_SIGN_IN);
            });*/
            /*return Observable.create(emitter -> {
                SignInCredential googleCredential = Identity.getSignInClient(context).
                        getSignInCredentialFromIntent(data);
                String idToken = googleCredential.getGoogleIdToken();
                if (idToken != null) {
                    AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    FirebaseUtils.getFirebaseInstance().signInWithCredential(firebaseCredential)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    emitter.onNext(Objects.requireNonNull(FirebaseUtils.getFirebaseInstance().getCurrentUser()).getDisplayName());
                                    emitter.onComplete();
                                } else {
                                    emitter.onError(task.getException());
                                }
                            });
                } else {
                    emitter.onError(new Exception("Failed to retrieve Google ID token"));
                }
            }).subscribeOn(Schedulers.io());*/
        }

        @Override
        public Observable<AuthResult> signUpWithGoogle(String idToken) {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            return Observable.create(emitter -> FirebaseUtils.getFirebaseInstance().signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            emitter.onNext(task.getResult());
                        } else {
                            emitter.onError(task.getException());
                        }
                        emitter.onComplete();
                    }));
        }


        private int getResourceIdForCountry(String countryName) {
            switch (countryName) {
                case "American":
                    return R.drawable.us;
                case "British":
                    return R.drawable.gb_eng;
                case "Canadian":
                    return R.drawable.ca;
                case "Chinese":
                    return R.drawable.cn;
                case "Croatian":
                    return R.drawable.hr;
                case "Dutch":
                    return R.drawable.nl;
                case "Egyptian":
                    return R.drawable.eg;
                case "Filipino":
                    return R.drawable.ph;
                case "French":
                    return R.drawable.fr;
                case "Greek":
                    return R.drawable.gr;
                case "Indian":
                    return R.drawable.in;
                case "Irish":
                    return R.drawable.ie;
                case "Italian":
                    return R.drawable.it;
                case "Jamaican":
                    return R.drawable.jm;
                case "Japanese":
                    return R.drawable.jp;
                case "Kenyan":
                    return R.drawable.ke;
                case "Malaysian":
                    return R.drawable.my;
                case "Mexican":
                    return R.drawable.mx;
                case "Moroccan":
                    return R.drawable.ma;
                case "Polish":
                    return R.drawable.pl;
                case "Portuguese":
                    return R.drawable.pt;
                case "Russian":
                    return R.drawable.ru;
                case "Spanish":
                    return R.drawable.es;
                case "Thai":
                    return R.drawable.th;
                case "Tunisian":
                    return R.drawable.tn;
                case "Turkish":
                    return R.drawable.tr;
                case "Unknown":
                    return R.drawable.flag_black_24dp;
                case "Vietnamese":
                    return R.drawable.vn;
                default:
                    return R.drawable.flag_black_24dp;
            }
        }
        }

    }


