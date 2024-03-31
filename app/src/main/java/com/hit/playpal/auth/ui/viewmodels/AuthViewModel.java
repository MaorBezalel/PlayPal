package com.hit.playpal.auth.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit.playpal.auth.data.repositories.AuthRepository;
import com.hit.playpal.auth.domain.usecases.CreateUserUseCase;
import com.hit.playpal.auth.domain.usecases.GetUserByUidUseCase;
import com.hit.playpal.auth.domain.usecases.GetUserByUsernameUseCase;
import com.hit.playpal.auth.domain.usecases.GetUserPrivateByUidUseCase;
import com.hit.playpal.auth.domain.usecases.StoreUserDefaultSettingsDataUseCase;
import com.hit.playpal.auth.domain.usecases.StoreUserPrivateDataUseCase;
import com.hit.playpal.auth.domain.usecases.StoreUserPublicDataUseCase;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.auth.domain.utils.enums.LoginFailure;
import com.hit.playpal.auth.domain.utils.enums.SignupFailure;
import com.hit.playpal.auth.domain.usecases.IsUsernameUniqueUseCase;
import com.hit.playpal.auth.domain.usecases.LoginWithEmailUseCase;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.Settings;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.users.UserPrivate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";

    // Currently, if mUser changes, an observer in AuthActivity will be triggered indicating that the user is authenticated (either by login or signup)
    private final MutableLiveData<User> mUser = new MutableLiveData<>();
    public MutableLiveData<User> getUser() {
        return mUser;
    }

    // If mSignupFailure changes, an observer in AuthActivity will be triggered indicating that the signup failed
    private final MutableLiveData<AuthServerFailure> mSignupFailure = new MutableLiveData<>();
    public MutableLiveData<AuthServerFailure> getSignupFailure() {
        return mSignupFailure;
    }

    // If mLoginFailure changes, an observer in AuthActivity will be triggered indicating that the login failed
    private final MutableLiveData<AuthServerFailure> mLoginFailure = new MutableLiveData<>();
    public MutableLiveData<AuthServerFailure> getLoginFailure() {
        return mLoginFailure;
    }

    public void loginWithEmail(String iEmail, String iPassword) {
        LoginWithEmailUseCase loginWithEmailUseCase = new LoginWithEmailUseCase(new AuthRepository());
        GetUserByUidUseCase getUserByUidUseCase = new GetUserByUidUseCase(new AuthRepository());

        loginWithEmailUseCase.execute(iEmail, iPassword)
                // If the login is successful, get the user by UID; otherwise, set the failure and stop the chain
                .thenCompose(loginWithEmailResult -> {
                    if (loginWithEmailResult.isSuccessful()) {
                        return getUserByUidUseCase.execute(loginWithEmailResult.getResult());
                    } else {
                        mLoginFailure.setValue(loginWithEmailResult.getFailure());
                        return CompletableFuture.completedFuture(new UseCaseResult<>(loginWithEmailResult.getFailure()));
                    }
                })

                // If the user is retrieved, set the user in the view model, signaling that the user is authenticated; otherwise, set the failure
                .thenAccept(getUserByUidResult -> {
                    if (getUserByUidResult.isSuccessful()) {
                        mUser.setValue(getUserByUidResult.getResult());
                    } else {
                        mLoginFailure.setValue(getUserByUidResult.getFailure());
                    }
                });
    }

    public void loginWithUsername(String iUsername, String iPassword) {
        GetUserByUsernameUseCase getUserByUsernameUseCase = new GetUserByUsernameUseCase(new AuthRepository());
        GetUserPrivateByUidUseCase getUserPrivateByUidUseCase = new GetUserPrivateByUidUseCase(new AuthRepository());
        LoginWithEmailUseCase loginWithEmailUseCase = new LoginWithEmailUseCase(new AuthRepository());
        AtomicReference<User> user = new AtomicReference<>();

        getUserByUsernameUseCase.execute(iUsername)
                // If the user is retrieved, get the user's private data by UID; otherwise, set the failure and stop the chain
                .thenCompose(getUserByUsernameResult -> {
                    if (getUserByUsernameResult.isSuccessful()) {
                        user.set(getUserByUsernameResult.getResult());
                        return getUserPrivateByUidUseCase.execute(user.get().getUid());
                    } else {
                        mLoginFailure.setValue(getUserByUsernameResult.getFailure());
                        return CompletableFuture.completedFuture(new UseCaseResult<>(getUserByUsernameResult.getFailure()));
                    }
                })

                // If the user's private data is retrieved, login with the user's email and password; otherwise, set the failure and stop the chain
                .thenCompose(getUserPrivateByUidResult -> {
                    if (getUserPrivateByUidResult.isSuccessful()) {
                        return loginWithEmailUseCase.execute(getUserPrivateByUidResult.getResult().getEmail(), iPassword);
                    } else {
                        mLoginFailure.setValue(getUserPrivateByUidResult.getFailure());
                        return CompletableFuture.completedFuture(new UseCaseResult<>(getUserPrivateByUidResult.getFailure()));
                    }
                })

                // If the login is successful, set the user in the view model, signaling that the user is authenticated; otherwise, set the failure
                .thenAccept(loginWithEmailResult -> {
                    if (loginWithEmailResult.isSuccessful()) {
                        mUser.setValue(user.get());
                    } else {
                        mLoginFailure.setValue(loginWithEmailResult.getFailure());
                    }
                });
    }

    public void signup(String iEmail, String iUsername, String iDisplayName, String iPassword) {
        IsUsernameUniqueUseCase isUsernameUniqueUseCase = new IsUsernameUniqueUseCase(new AuthRepository());
        CreateUserUseCase createUserUseCase = new CreateUserUseCase(new AuthRepository());
        StoreUserPublicDataUseCase storeUserPublicDataUseCase = new StoreUserPublicDataUseCase(new AuthRepository());
        StoreUserPrivateDataUseCase storeUserPrivateDataUseCase = new StoreUserPrivateDataUseCase(new AuthRepository());
        StoreUserDefaultSettingsDataUseCase storeUserDefaultSettingsDataUseCase = new StoreUserDefaultSettingsDataUseCase(new AuthRepository());
        AtomicReference<String> uid = new AtomicReference<>();
        AtomicReference<User> userPublic = new AtomicReference<>();
        AtomicReference<UserPrivate> userPrivate = new AtomicReference<>();
        AtomicReference<Settings> defaultSettings = new AtomicReference<>();

        isUsernameUniqueUseCase.execute(iUsername)
                // If the username is unique, create the user; otherwise, set the failure and stop the chain
                .thenCompose(isUsernameUniqueResult -> {
                    if (isUsernameUniqueResult.isSuccessful()) {
                        return createUserUseCase.execute(iEmail, iPassword);
                    } else {
                        mSignupFailure.setValue(isUsernameUniqueResult.getFailure());
                        return CompletableFuture.completedFuture(new UseCaseResult<>(isUsernameUniqueResult.getFailure()));
                    }
                })

                // If the user is created, store the user's public data; otherwise, set the failure and stop the chain
                .thenCompose(createUserResult -> {
                    if (createUserResult.isSuccessful()) {
                        uid.set(createUserResult.getResult());
                        userPublic.set(new User(uid.get(), iUsername, iDisplayName));
                        return storeUserPublicDataUseCase.execute(userPublic.get());
                    } else {
                        mSignupFailure.setValue(createUserResult.getFailure());
                        return CompletableFuture.completedFuture(new UseCaseResult<>(createUserResult.getFailure()));
                    }
                })

                // If the user's public data is stored, store the user's private data; otherwise, set the failure and stop the chain
                .thenCompose(storeUserPublicDataResult -> {
                    if (storeUserPublicDataResult.isSuccessful()) {
                        userPrivate.set(new UserPrivate(iEmail, iPassword));
                        return storeUserPrivateDataUseCase.execute(uid.get(), userPrivate.get());
                    } else {
                        mSignupFailure.setValue(storeUserPublicDataResult.getFailure());
                        return CompletableFuture.completedFuture(new UseCaseResult<>(storeUserPublicDataResult.getFailure()));
                    }
                })

                // If the user's private data is stored, store the user's default settings; otherwise, set the failure and stop the chain
                .thenCompose(storeUserPrivateDataResult -> {
                    if (storeUserPrivateDataResult.isSuccessful()) {
                        defaultSettings.set(Settings.getDefaultSettings());
                        return storeUserDefaultSettingsDataUseCase.execute(uid.get(), defaultSettings.get());
                    } else {
                        mSignupFailure.setValue(storeUserPrivateDataResult.getFailure());
                        return CompletableFuture.completedFuture(new UseCaseResult<>(storeUserPrivateDataResult.getFailure()));
                    }
                })

                // If the user's default settings are stored, set the user in the view model, signaling that the user is authenticated; otherwise, set the failure
                .thenAccept(storeUserDefaultSettingsDataResult -> {
                    if (storeUserDefaultSettingsDataResult.isSuccessful()) {
                        mUser.setValue(userPublic.get());
                    } else {
                        mSignupFailure.setValue(storeUserDefaultSettingsDataResult.getFailure());
                    }
                });
    }
}
