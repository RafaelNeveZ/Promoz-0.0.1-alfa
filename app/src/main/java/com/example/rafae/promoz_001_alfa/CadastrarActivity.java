package com.example.rafae.promoz_001_alfa;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
//import android.support.design.widget.FloatingActionButton;    // resolver import
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafae.promoz_001_alfa.dao.UserDAO;
import com.example.rafae.promoz_001_alfa.model.User;
import com.example.rafae.promoz_001_alfa.util.ImageUtil;

import java.io.IOException;

public class CadastrarActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 23;
    private int SELECT_IMAGE = 1;
    private RoundedBitmapDrawable drawable=null;
    private User user = null;
    private CadastrarActivity.UserLoginTask mAuthTask = null;
    private TextView viewName;
    private TextView viewEmail;
    private TextView viewCPF;
    private TextView viewPassword;
    private TextView viewConfirm;
    private ImageView perfilPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        viewName = (TextView) findViewById(R.id.user_name);
        viewEmail = (TextView) findViewById(R.id.email);
        viewCPF = (TextView) findViewById(R.id.user_cpf);
        viewPassword = (TextView) findViewById(R.id.password);
        viewConfirm = (TextView) findViewById(R.id.password_again);
        perfilPhoto = (ImageView) findViewById(R.id.choose_photo);

        Resources res = getResources();
        Bitmap src = BitmapFactory.decodeResource(res, R.drawable.default_photo);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), src);
        drawable.setCircular(true);

        perfilPhoto.setImageDrawable(drawable);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.change_photo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleyView();
            }
        });*/ // TODO: resolver import
    }

    public void galleyView(){
        if(isReadStorageAllowed()){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
        }else {
            requestStoragePermission();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {
                        Bitmap bitmap = ImageUtil.reSizeImageCrop(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()), perfilPhoto.getWidth());
                        drawable = RoundedBitmapDrawableFactory.create(this.getResources(), bitmap);
                        drawable.setCircular(true);
                        perfilPhoto.setImageDrawable(drawable);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //Caso o usuario tenha negado anteriormente a permissão
        }
        //Pedidndo a permissão
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //Caso a permissão tenha sido aceita
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleyView();
            } else {
                //Caso a permissão tenha sido recusada
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_LONG).show();
            }
        }
    }
    private boolean isReadStorageAllowed() {
        //Testando se a permissão já foi aceita
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //Caso sim
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //Caso não
        return false;
    }


    public void cadastrar (View v) {
        attemptLogin();
    }

    private boolean isPasswordValid(String password) {
        //TODO: isPasswordValid
        //return password.length() > 2;
        return true;
    }

    private boolean isEmailValid(String email) {
        //TODO: isEmailValid
        //return email.contains("@");
        return true;
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }
        String name = viewName.getText().toString();
        String email = viewEmail.getText().toString();
        String password = viewPassword.getText().toString();
        String confirm = viewConfirm.getText().toString();

        // Erros
        viewEmail.setError(null);
        viewPassword.setError(null);
        viewConfirm.setError(null);
        viewName.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Testando dados p/ saber se são validos

        if (!isPasswordValid(password) || !password.equals(confirm)) {
            viewPassword.setError(getString(R.string.error_invalid_password));
            focusView = viewPassword;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            viewEmail.setError(getString(R.string.error_field_required));
            focusView = viewEmail;
            cancel = true;
        } else
        if (!isEmailValid(email)) {
            viewEmail.setError(getString(R.string.error_invalid_email));
            focusView = viewEmail;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)) {
            viewName.setError(getString(R.string.error_field_required));
            focusView = viewName;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            //Efetuando o cadastro
            mAuthTask = new CadastrarActivity.UserLoginTask(name, email, password);
            mAuthTask.execute((Void) null);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
        private final String mEmail;
        private final String mPassword;
        private final String mName;
        private final String mCPF;
        private User authUser;

        UserLoginTask(String name, String email, String password) {
            mName = name;
            mEmail = email;
            mPassword = password;
            mCPF = viewCPF.getText().toString();
        }

        /**
         * Verificar se usuário já existe
         * @return true or false
         */
        private Boolean authUser(){

            UserDAO userDAO = new UserDAO(getApplicationContext());
            User result = userDAO.findUserByLogin(mEmail);
            Boolean sucess;

            if (result != null) {
                sucess = false;
            } else {
                createUser(userDAO);
                sucess = true;
            }

            //userDAO.closeDatabase();
            return sucess;
        }
        /**
         * Método para criar um novo usuário
         * @param userDAO
         */
        private void createUser(UserDAO userDAO) {

            user = new User();
            authUser = new User();

            if(drawable != null)
                authUser.setImg(ImageUtil.getThumbNailDrawable(drawable));

            authUser.setEmail(mEmail);
            authUser.setNome(mName);
            authUser.setCpf(mCPF);
            authUser.setPassword(mPassword);
            Long id = userDAO.save(authUser);
            authUser.set_id(id.intValue());
        }
        /**
         * //Login automático
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
                Thread.sleep(1000);
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
                viewEmail.setError("Usuário já cadastrado");
                viewEmail.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
