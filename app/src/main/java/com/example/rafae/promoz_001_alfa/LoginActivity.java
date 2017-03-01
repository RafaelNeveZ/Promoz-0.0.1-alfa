package com.example.rafae.promoz_001_alfa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.rafae.promoz_001_alfa.dao.UserDAO;
import com.example.rafae.promoz_001_alfa.model.User;
import com.example.rafae.promoz_001_alfa.util.Util;

public class LoginActivity extends AppCompatActivity {
    private UserLoginTask mAuthTask = null;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //TODO lembrar senha
        TextView mLembrarSenhaLink = (TextView) findViewById(R.id.lembrar_senha);
        mLembrarSenhaLink.setMovementMethod(LinkMovementMethod.getInstance());

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Erros
        mEmailView.setError(null);
        mPasswordView.setError(null);
        // Pegando os valores de email e senha
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Testando dados p/ saber se são validos

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            //Efetuando o login
            Util.showProgress(true, getResources(), mLoginFormView, mProgressView);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: isEmailValid
        //return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: isPasswordValid
        //return password.length() > 2;
        return true;
    }

    //Login Task
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private int mError;
        private User authUser;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        /**
         * Autenticar usuário pelo login e senha
         * @return true or false
         */
        private Boolean authUser(){
            UserDAO userDAO = new UserDAO(getApplicationContext());
            User result = userDAO.findUserByLogin(mEmail);
            Boolean sucess;
            if (result != null) {
                if(result.getPassword() != null && result.getPassword().equals(mPassword)){
                    authUser = result;
                    sucess = true;
                }else{
                    mError = Util.Constants.ERROR_SENHA;
                    sucess = false;
                }
            } else {
                mError = Util.Constants.ERROR_LOGIN;
                sucess = false;
            }
            userDAO.closeDataBase();
            return sucess;
        }
        /**
         * Login automático
         */
        private void setSharedPreferences(){
            SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
            editor.putInt(User.getChave_ID(), authUser.get_id());
            editor.commit();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                //TODO: Acessando o WebService
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                return false;
            }
            return authUser();
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                setSharedPreferences();
                finish();
            } else {
                Util.showProgress(false, getResources(), mLoginFormView, mProgressView);
                if(mError == Util.Constants.ERROR_LOGIN){
                    mEmailView.setError(getString(R.string.error_login));
                    mEmailView.requestFocus();
                } else if(mError == Util.Constants.ERROR_SENHA){
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_funny), Toast.LENGTH_LONG).show();
                }

            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            Util.showProgress(false, getResources(), mLoginFormView, mProgressView);
        }
    }
}
