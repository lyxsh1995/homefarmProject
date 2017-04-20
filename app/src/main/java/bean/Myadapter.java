package bean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import java.util.List;

import com.diyikeji.homefarm.R;

/**
 * Created by Administrator on 2016/12/5.
 */

public class Myadapter extends BaseAdapter
{
    ViewHolder holder;

    // 填充数据的list
    private List<Fajson> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;
    //选中的行数
    private int selected = 0;
    // 构造器
    public Myadapter(List list, Context context)
    {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        holder = null;
        if (convertView == null)
        {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.fanganlist, null);
            holder.fanganlist_mingcheng = (TextView) convertView.findViewById(R.id.fanganlist_mingcheng);
            holder.fanganlist_jianjie = (TextView) convertView.findViewById(R.id.fanganlist_jianjie);
            holder.fanganlist_xuanzhe = (RadioButton) convertView.findViewById(R.id.fanganlist_xuanzhe);
            // 为view设置标签
            convertView.setTag(holder);
        }
        else
        {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        //绑定数据
        holder.fanganlist_mingcheng.setText(list.get(position).pingzhong.toString());
        holder.fanganlist_jianjie.setText(list.get(position).jianjie.toString());
        if (position == selected)
        {
            holder.fanganlist_xuanzhe.setChecked(true);
        }else
        {
            holder.fanganlist_xuanzhe.setChecked(false);
        }
        return convertView;
    }

    public void select(int position) {
        selected = position;
        notifyDataSetChanged();
    }

    public class ViewHolder
    {
        public TextView fanganlist_mingcheng,fanganlist_jianjie;
        public RadioButton fanganlist_xuanzhe;
    }
}

