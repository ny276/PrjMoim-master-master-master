package moim.prjmoim;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class FindByPwd extends AppCompatActivity {

    EditText etId3, etName3, etEmail3;
    Button btFindByPwd2, btLogin4;
    String sId3, sName3, sEmail3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_by_pwd);

        etId3 = findViewById(R.id.etId3);
        etName3 = findViewById(R.id.etName3);
        etEmail3 = findViewById(R.id.etEmail3);

        btFindByPwd2 = findViewById(R.id.btFindByPwd2);
        btFindByPwd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phpdo task = new phpdo();
                task.execute(etId3.getText().toString(), etName3.getText().toString(), etEmail3.getText().toString());
            }
        });

        btLogin4 = findViewById(R.id.btLogin4);
        btLogin4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), Login.class);
                startActivity(login);
            }
        });
    }

    private class phpdo extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        protected void onPreExecute(){
            progressDialog = ProgressDialog.show(FindByPwd.this, "잠시만 기다려 주세요.", null, true, true);
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                sId3 = etId3.getText().toString();
                sName3 = etName3.getText().toString();
                sEmail3 = etEmail3.getText().toString();
                String link = "http://ny276.dothome.co.kr/pwdfind.php?id=" + sId3 + "&name="+ sName3 + "&eMail=" + sEmail3;
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result){
            progressDialog.dismiss();
            if(!(result.length() == 0)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindByPwd.this);
                builder.setTitle("비밀번호 찾기가 완료되었습니다.");
                builder.setMessage(sName3  + "님의 비밀번호는 " + result + "입니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindByPwd.this);
                builder.setTitle("MOIM");
                builder.setMessage("입력하신 정보로 가입 된 회원 비밀번호는 존재하지 않습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}
