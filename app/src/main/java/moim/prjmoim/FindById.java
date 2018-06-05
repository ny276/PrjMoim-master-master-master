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

public class FindById extends AppCompatActivity {

    EditText etName2, etEmail2;
    Button btFindById2, btLogin3;
    String sName2, sEmail2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_by_id);

        etName2 = findViewById(R.id.etName2);
        etEmail2 = findViewById(R.id.etEmail2);

        btFindById2 = findViewById(R.id.btFindById2);
        btFindById2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                phpdo task = new phpdo();
                task.execute(etName2.getText().toString(), etEmail2.getText().toString());
            }
        });

        btLogin3 = findViewById(R.id.btLogin3);
        btLogin3.setOnClickListener(new View.OnClickListener() {
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
            progressDialog = ProgressDialog.show(FindById.this, "잠시만 기다려 주세요.", null, true, true);
        }

        @Override
        protected String doInBackground(String... arg0) {

            try {
                sName2 = etName2.getText().toString();
                sEmail2 = etEmail2.getText().toString();
                String link = "http://ny276.dothome.co.kr/idfind.php?name=" + sName2 + "&eMail=" + sEmail2;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(FindById.this);
                builder.setTitle("아이디 찾기가 완료되었습니다.");
                builder.setMessage(sName2+ "님의 아이디는 " + result + "입니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }else {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindById.this);
                builder.setTitle("MOIM");
                builder.setMessage("입력하신 정보로 가입 된 회원 아이디는 존재하지 않습니다.").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }
}

