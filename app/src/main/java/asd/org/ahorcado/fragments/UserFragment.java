package asd.org.ahorcado.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import asd.org.ahorcado.R;
import asd.org.ahorcado.activities.VersusActivity;
import asd.org.ahorcado.helpers.UserAdapter;
import asd.org.ahorcado.utils.MySharedPreference;

public class UserFragment extends Fragment {

    private ListView listView;
    private Context context;
    private UserAdapter userAdapter;
    private RequestQueue requestQueue;

    public UserFragment() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUserAdapter(UserAdapter userAdapter) {
        this.userAdapter = userAdapter;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.userList);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                Map<String, String> userMap = (Map<String, String>) userAdapter.getItem(position);
                getUserId(userMap.get(UserAdapter.COLUMN_ID), userMap.get(UserAdapter.COLUMN_NAME));
            }
        });
        return view;
    }

    public void updateList(UserAdapter userAdapter) {
        listView.setAdapter(userAdapter);
    }

    private void getUserId (final String idUserTo, final String nameUserTo) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);
        progressDialog.show();
        final String url = MySharedPreference.PREFIX_URL + "challenger";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(context, VersusActivity.class);
                        intent.putExtra(UserAdapter.COLUMN_NAME, nameUserTo);
                        intent.putExtra(UserAdapter.COLUMN_ID, idUserTo);
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(MySharedPreference.TOKEN_TO_SERVER, FirebaseInstanceId.getInstance().getToken());
                params.put(MySharedPreference.ID_USER_TO, idUserTo);
                return params;
            }
        };
        stringRequest.setTag(MySharedPreference.REQUEST_POST_TAG);
        requestQueue.add(stringRequest);
    }

}