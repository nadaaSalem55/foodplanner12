package com.example.foodplanner.db;

import android.content.Context;

import com.example.foodplanner.model.dto.MealsItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseUtils {
    public static String FAV_COLLECTION_NAME="fav";
    public static final String PLAN_MEAL_COLLECTION_NAME ="planMeal";
    public static FirebaseAuth firebaseAuth=getFirebaseInstance();
    public static final int RC_SIGN_IN = 123;
   static FirebaseUser currentUser = getFirebaseInstance().getCurrentUser();

    public static FirebaseAuth getFirebaseInstance(){
        if(firebaseAuth==null){
            firebaseAuth=FirebaseAuth.getInstance();
        }
        return firebaseAuth;
    }

    private static CollectionReference getCollectionReference(String collectionName){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(collectionName);
    }

    public static void getFavMeals(Context context,OnCompleteListener onCompleteListener){
        String savedEmail=SharedPreferencesManager.getUserEmail(context);

        getCollectionReference(FAV_COLLECTION_NAME).whereEqualTo("strCreativeCommonsConfirmed",
             savedEmail).get().addOnCompleteListener(onCompleteListener);

    }
    public static void getWeeklyPlannedMeals(Context context,String date,
                                             OnCompleteListener<QuerySnapshot> onCompleteListener,
                                             OnFailureListener onFailureListener){
        String savedEmail=SharedPreferencesManager.getUserEmail(context);
        getCollectionReference(PLAN_MEAL_COLLECTION_NAME)
                .whereEqualTo("strCreativeCommonsConfirmed",savedEmail)
                .whereEqualTo("dateModified", date)
                .get()
                .addOnCompleteListener(onCompleteListener)
                .addOnFailureListener(onFailureListener);

    }
    public static void deleteFavMeal(Context context,String mealId,
                                     OnSuccessListener<Void> onSuccessListener,
                                     OnFailureListener onFailureListener){
        String savedEmail=SharedPreferencesManager.getUserEmail(context);
        getCollectionReference(FAV_COLLECTION_NAME)
                .whereEqualTo("strCreativeCommonsConfirmed",savedEmail)
                .whereEqualTo("idMeal",mealId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        snapshot.getReference().delete()
                                .addOnSuccessListener(onSuccessListener)
                                .addOnFailureListener(onFailureListener);
                    }
                }).addOnFailureListener(onFailureListener);

    }
    public static void deletePlannedMeal(Context context,String mealId,
                                         OnSuccessListener<Void> onSuccessListener,OnFailureListener onFailureListener){
        String savedEmail=SharedPreferencesManager.getUserEmail(context);
        getCollectionReference(PLAN_MEAL_COLLECTION_NAME)
                .whereEqualTo("strCreativeCommonsConfirmed",savedEmail)
                .whereEqualTo("idMeal",mealId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots){
                        snapshot.getReference().delete()
                                .addOnSuccessListener(onSuccessListener)
                                .addOnFailureListener(onFailureListener);
                    }
                }).addOnFailureListener(onFailureListener);
    }

    public static void addMealToFav(MealsItem mealsItem,
                                    OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){

        if (currentUser != null) {
            getCollectionReference(FAV_COLLECTION_NAME)
                    .document(String.valueOf(currentUser))
                    .set(mealsItem)
                    .addOnSuccessListener(onSuccessListener)
                    .addOnFailureListener(onFailureListener);
        }
    }

    public static void addMealToPlan(MealsItem mealsItem,
                                     OnSuccessListener<Void> onSuccessListener, OnFailureListener onFailureListener){

           if(mealsItem.getIdMeal()!=null){
               getCollectionReference(PLAN_MEAL_COLLECTION_NAME)
                       .document(String.valueOf(mealsItem.getIdMeal()))
                       .set(mealsItem)
                       .addOnSuccessListener(onSuccessListener)
                       .addOnFailureListener(onFailureListener);
           }
    }

    public static void createAccount(String email,String password,OnCompleteListener<AuthResult> onCompleteListener){
        FirebaseAuth auth=FirebaseUtils.getFirebaseInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(onCompleteListener);
    }
    public static void signIn(String email, String password, OnCompleteListener<AuthResult> onCompleteListener){
        getFirebaseInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);

    }

    public static void signInWithGoogle(){


    }

}
