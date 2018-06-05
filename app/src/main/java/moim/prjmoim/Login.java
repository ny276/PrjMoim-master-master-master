package moim.prjmoim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText etId2, etPwd2;
    Button btLogin2, btFindById1, btFindByPwd1;
    HttpPost httppost;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        etId2 = findViewById(R.id.etId2);
        etPwd2 = findViewById(R.id.etPwd2);

        btLogin2 = findViewById(R.id.btLogin2);
        btLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    dialog = ProgressDialog.show(Login.this, "",
                            "로딩중...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            login();
                        }
                    }).start();
                }
            });

        btFindById1 = findViewById(R.id.btFindById1);
        btFindById1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findbyid = new Intent(getApplicationContext(), FindById.class);
                startActivity(findbyid);
            }
        });

        btFindByPwd1 = findViewById(R.id.btFindByPwd1);
        btFindByPwd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findbypwd = new Intent(getApplicationContext(), FindByPwd.class);
                startActivity(findbypwd);
            }
        });
    }

    void login() {
        try{
            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://ny276.dothome.co.kr/login.php"); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("id",etId2.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("pwd",etPwd2.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    dialog.dismiss();
                }
            });

            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Login.this,"환영합니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                startActivity(new Intent(Login.this, Main.class));
            }else{
                showAlert();
            }
        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("MOIM");
                builder.setMessage("아이디 또는 비밀번호 오류입니다")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}


