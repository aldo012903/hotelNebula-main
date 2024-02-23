package Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hotelnebula.R;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> userList;
    private User oneUser;

    public UserAdapter(Context context, ArrayList<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View oneItem = null;

        TextView tvFullName,  tvTeamName;

        LayoutInflater inflater = LayoutInflater.from(context);
        oneItem = inflater.inflate(R.layout.one_user, viewGroup, false);

        tvFullName = oneItem.findViewById(R.id.tvFullName);
        tvTeamName = oneItem.findViewById(R.id.tvTeamName);

        oneUser = (User) getItem(i);
        tvFullName.setText(oneUser.getName());
        tvTeamName.setText(oneUser.getEmail());

        return oneItem;
    }
}
