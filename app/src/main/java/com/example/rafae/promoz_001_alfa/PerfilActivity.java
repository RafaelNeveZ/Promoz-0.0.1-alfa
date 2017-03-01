package com.example.rafae.promoz_001_alfa;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import com.example.rafae.promoz_001_alfa.dao.UserDAO;
import com.example.rafae.promoz_001_alfa.model.User;
import com.example.rafae.promoz_001_alfa.util.ImageUtil;

public class PerfilActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 23;
    private int SELECT_IMAGE = 1;
    final Context context = this;
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_perfil);

        userId = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE).getInt(User.getChave_ID(),1);

        //Trocando foto
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.change_photo);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleyView();
            }
        });
        final ImageView ci = (ImageView)findViewById(R.id.perfil_foto);

        UserDAO userDAO = new UserDAO(this);
        User user = userDAO.userById(userId);
        userDAO.closeDataBase();

        if(user != null) {
            byte[] bitmapdata = user.getImg();
            if (bitmapdata != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                if (bitmap != null){
                    RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(this.getResources(), bitmap);
                    drawable.setCircular(true);
                    ci.setImageDrawable(drawable);
                }
            }else{
                Resources res = getResources();
                Bitmap src = BitmapFactory.decodeResource(res, R.drawable.default_photo);
                RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(), src);
                drawable.setCircular(true);
                ci.setImageDrawable(drawable);
            }
        }
        TextView email = (TextView) findViewById(R.id.email);
        if(user != null) {
            email.setText(user.getEmail());

            TextView name = (TextView) findViewById(R.id.nome);
            name.setText(user.getNome());
        }
        Button button_logout = (Button) findViewById(R.id.logoutbt);
        Button button_change = (Button) findViewById(R.id.change_pass);

        //Button LOG OUT
        button_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.logout_dialog);
                dialog.setTitle("Log Out");
                dialog.show();
                Button btY = (Button) dialog.findViewById(R.id.yes);
                Button btN = (Button) dialog.findViewById(R.id.no);
                btN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.app_name), Context.MODE_PRIVATE).edit();
                        editor.putInt(User.getChave_ID(), 0);
                        editor.commit();
                        dialog.dismiss();
                        finish();
                    }
                });
                dialog.show();
            }
        });

        //Button TROCAR SENHA
        button_change.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.change_pass_dialog);
                dialog.setTitle("Trocar senha");
                dialog.show();
                Button btconfirm = (Button) dialog.findViewById(R.id.confirm_change);
                Button btcancel = (Button) dialog.findViewById(R.id.cancel_change);

                btcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                btconfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        UserDAO userDao = new UserDAO(context);
                        User user = userDao.userById(userId);

                        EditText lastpass = (EditText) dialog.findViewById(R.id.last_pass);
                        EditText newpass = (EditText) dialog.findViewById(R.id.new_pass);
                        EditText confirmpass = (EditText) dialog.findViewById(R.id.new_pass_confirm);

                        String newPass = newpass.getText().toString();
                        if(user.getPassword().equals(lastpass.getText().toString())){
                            if(newPass.equals(confirmpass.getText().toString())){
                                user.setPassword(newPass);
                                userDao.save(user);
                                Toast.makeText(getApplicationContext(),R.string.senhaAtualizada,Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),R.string.camposNovaSenha,Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(),R.string.senhaAtualErrada,Toast.LENGTH_LONG).show();
                        }
                        userDao.closeDataBase();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void galleyView(){
        if(isReadStorageAllowed()){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
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
                        ImageView perfilPhoto = (ImageView) findViewById(R.id.perfil_foto);
                        Bitmap bitmap = ImageUtil.reSizeImageCrop(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()), perfilPhoto.getWidth());
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(this.getResources(), bitmap);
                        drawable.setCircular(true);
                        perfilPhoto.setImageDrawable(drawable);

                        // salvar no banco
                        UserDAO userDAO = new UserDAO(this);
                        User user = userDAO.userById(userId);
                        user.setImg(ImageUtil.getThumbNailDrawable(drawable));
                        userDAO.save(user);
                        userDAO.closeDataBase();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //Caso a permissão tenha sido aceita
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleyView();
            } else {
                //Caso a permissão tenha sido recusada
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
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
}
