package kingja.com.kingja_flowlayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

private final String[] mItmeArr={"ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ","情趣内衣","防狼器","太阳镜","ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ","帆船","熔断机制","二孩政策","二次元女朋友","旅游背包","数码相机","阿迪王","书籍","苍老师","床上用品","NBA壁纸","营养快线","超级麻辣烫"};
    private KingJaFlowLayout kj_fl_inAverage;
    private KingJaFlowLayout kj_fl_average;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        kj_fl_inAverage = (KingJaFlowLayout) findViewById(R.id.kj_fl_inAverage);
        kj_fl_average = (KingJaFlowLayout) findViewById(R.id.kj_fl_average);
        for (int i = 0; i <mItmeArr.length ; i++) {
            TextView itemTv = (TextView) View.inflate(this,R.layout.single_tv,null);
            itemTv.setText(mItmeArr[i]);
            kj_fl_inAverage.addView(itemTv);
        }
        for (int i = 0; i <mItmeArr.length ; i++) {
            TextView itemTv = (TextView) View.inflate(this,R.layout.single_tv,null);
            itemTv.setText(mItmeArr[i]);
            kj_fl_average.addView(itemTv);
        }
    }

}
