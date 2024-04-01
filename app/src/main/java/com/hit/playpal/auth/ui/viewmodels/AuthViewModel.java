package com.hit.playpal.auth.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit.playpal.auth.data.repositories.AuthRepository;
import com.hit.playpal.auth.domain.usecases.CreateUserUseCase;
import com.hit.playpal.auth.domain.usecases.GetUserByUidUseCase;
import com.hit.playpal.auth.domain.usecases.GetUserByUsernameUseCase;
import com.hit.playpal.auth.domain.usecases.GetUserPrivateByUidUseCase;
import com.hit.playpal.auth.domain.usecases.ResetPasswordUseCase;
import com.hit.playpal.auth.domain.usecases.StoreUserDefaultSettingsDataUseCase;
import com.hit.playpal.auth.domain.usecases.StoreUserPrivateDataUseCase;
import com.hit.playpal.auth.domain.usecases.StoreUserPublicDataUseCase;
import com.hit.playpal.auth.domain.utils.enums.AuthServerFailure;
import com.hit.playpal.auth.domain.usecases.IsUsernameUniqueUseCase;
import com.hit.playpal.auth.domain.usecases.LoginWithEmailUseCase;
import com.hit.playpal.utils.SingleLiveEvent;
import com.hit.playpal.utils.UseCaseResult;
import com.hit.playpal.entities.users.Settings;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.users.UserPrivate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>The AuthViewModel class is a ViewModel that provides the data and functionality needed for the AuthActivity and its fragments.</p>
 * <p>It is responsible for handling the authentication logic and updating the UI accordingly.</p>
 */
public class AuthViewModel extends ViewModel {
    private static final String TAG = "AuthViewModel";

    // Currently, if mUser changes, an observer in AuthActivity will be triggered indicating that the user is authenticated (either by login or signup)
    private final MutableLiveData<User> mUser = new SingleLiveEvent<>();
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

    // If mResetPasswordSuccess changes, an observer in ForgotPasswordFragment will be triggered indicating that the password reset was successful
    private final SingleLiveEvent<Void> mResetPasswordSuccess = new SingleLiveEvent<>();
    public SingleLiveEvent<Void> getResetPasswordSuccess() {
        return mResetPasswordSuccess;
    }

    // If mResetPasswordFailure changes, an observer in ForgotPasswordFragment will be triggered indicating that the password reset failed
    private final MutableLiveData<AuthServerFailure> mResetPasswordFailure = new MutableLiveData<>();
    public MutableLiveData<AuthServerFailure> getResetPasswordFailure() {
        return mResetPasswordFailure;
    }

    /**
     * <p>Logs in a user with an email and password.</p>
     * <p>Runs the following sequence of use cases:</p>
     *
     * <ol>
     *     <li>LoginWithEmailUseCase</li>
     *     <li>GetUserByUidUseCase</li>
     * </ol>
     *
     * <p>If the login is successful, the user is retrieved by UID and set in the view model, signaling that the user is authenticated.</p>
     * <p>If the login fails, the failure is set in the view model.</p>
     *
     * @param iEmail The user's email
     * @param iPassword The user's password
     * @see LoginWithEmailUseCase
     * @see GetUserByUidUseCase
     */
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

    /**
     * <p>Logs in a user with a username and password.</p>
     * <p>Runs the following sequence of use cases:</p>
     *
     * <ol>
     *     <li>GetUserByUsernameUseCase</li>
     *     <li>GetUserPrivateByUidUseCase</li>
     *     <li>LoginWithEmailUseCase</li>
     * </ol>
     *
     * <p>If the login is successful, the user is set in the view model, signaling that the user is authenticated.</p>
     * <p>If the login fails, the failure is set in the view model.</p>
     *
     * @param iUsername The user's username
     * @param iPassword The user's password
     * @see GetUserByUsernameUseCase
     * @see GetUserPrivateByUidUseCase
     */
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

    /**
     * <p>Signs up a user with an email, username, display name, and password.</p>
     * <p>Runs the following sequence of use cases:</p>
     *
     * <ol>
     *     <li>IsUsernameUniqueUseCase</li>
     *     <li>CreateUserUseCase</li>
     *     <li>StoreUserPublicDataUseCase</li>
     *     <li>StoreUserPrivateDataUseCase</li>
     *     <li>StoreUserDefaultSettingsDataUseCase</li>
     * </ol>
     *
     * <p>If the signup is successful, the user is set in the view model, signaling that the user is authenticated.</p>
     * <p>If the signup fails, the failure is set in the view model.</p>
     *
     * @param iEmail The user's email
     * @param iUsername The user's username
     * @param iDisplayName The user's display name
     * @param iPassword The user's password
     * @see IsUsernameUniqueUseCase
     * @see CreateUserUseCase
     * @see StoreUserPublicDataUseCase
     * @see StoreUserPrivateDataUseCase
     * @see StoreUserDefaultSettingsDataUseCase
     */
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

    /**
     * <p>Sends a password reset email to the user's email.</p>
     * <p>Runs the following sequence of use cases:</p>
     *
     * <ol>
     *     <li>ResetPasswordUseCase</li>
     * </ol>
     *
     * <p>If the password reset is successful, the success is set in the view model.</p>
     * <p>If the password reset fails, the failure is set in the view model.</p>
     *
     * @param iEmail The user's email
     * @see ResetPasswordUseCase
     */
    public void forgotPassword(String iEmail) {
        ResetPasswordUseCase resetPasswordUseCase = new ResetPasswordUseCase(new AuthRepository());

        resetPasswordUseCase.execute(iEmail)
                .thenAccept(resetPasswordResult -> {
                    if (resetPasswordResult.isSuccessful()) {
                        mResetPasswordSuccess.call();
                    } else {
                        mResetPasswordFailure.setValue(resetPasswordResult.getFailure());
                    }
                });
    }
}
