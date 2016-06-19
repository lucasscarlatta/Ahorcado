package asd.org.ahorcado.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
                getUserId(userMap);
            }
        });
        return view;
    }

    public void updateList(UserAdapter userAdapter) {
        listView.setAdapter(userAdapter);
    }

    private void getUserId (final Map<String, String> userMap) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(getString(R.string.processing));
        progressDialog.setCancelable(false);
        progressDialog.show();
        //TODO change url
        final String url = MySharedPreference.PREFIX_URL + "words/1";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                String id = userMap.get(UserAdapter.COLUMN_ID);
                params.put(MySharedPreference.TOKEN_TO_SERVER, FirebaseInstanceId.getInstance().getToken());
                params.put("idUserTo", id);
                return params;
            }
        };
        stringRequest.setTag(MySharedPreference.REQUEST_POST_TAG);
        requestQueue.add(stringRequest);
    }

}