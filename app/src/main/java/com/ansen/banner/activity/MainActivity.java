package com.ansen.banner.activity;

import android.os.Handler;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.ansen.banner.R;
import com.ansen.banner.adapter.BannerAdapter;

public class MainActivity extends AppCompatActivity {
    public static final int CAROUSEL_TIME = 5000;//banner 滚动间隔

    private ViewPager vpBanner;//
    private ViewGroup viewGroup;//显示点点点图片，可以看到ViewPager当前选中状态

    private BannerAdapter bannerAdapter;//ViewPager适配器

    private Handler handler=new Handler();
    private int currentItem = 0;//ViewPager当前位置

    private final Runnable mTicker = new Runnable(){
        public void run() {
            long now = SystemClock.uptimeMillis();
            long next = now + (CAROUSEL_TIME - now % CAROUSEL_TIME);

            handler.postAtTime(mTicker,next);//延迟5秒再次执行runnable,就跟计时器一样效果

            currentItem++;
            vpBanner.setCurrentItem(currentItem);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vpBanner= findViewById(R.id.vp_banner);
        bannerAdapter = new BannerAdapter(this);//初始化适配器
        bannerAdapter.setOnBannerClickListener(onBannerClickListener);//图片点击监听
        vpBanner.setOffscreenPageLimit(2);//缓存页数
        vpBanner.setAdapter(bannerAdapter);//设置适配器
        vpBanner.addOnPageChangeListener(onPageChangeListener);//页面改变监听

        viewGroup = findViewById(R.id.viewGroup);//显示点点点控件

        //将点点加入到ViewGroup中
        for (int i = 0; i < bannerAdapter.getBanners().length; i++) {
            ImageView imageView = new ImageView(this);
            //设置图片宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.mipmap.icon_page_select);//第一个默认选中
            } else {
                imageView.setBackgroundResource(R.mipmap.icon_page_normal);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 10;//左边距
            layoutParams.rightMargin = 10;//右边距
            viewGroup.addView(imageView,layoutParams);
        }

        //给ViewPager设置当前页，这样刚打开软件也能向左滑动
        currentItem = bannerAdapter.getBanners().length * 50;
        vpBanner.setCurrentItem(currentItem);

        handler.postDelayed(mTicker,CAROUSEL_TIME);//开启计时器
    }

    private ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) {
            currentItem = position;

            //改变点点点图片的选中状态
            setImageBackground(position %= bannerAdapter.getBanners().length);
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    /**
     * 改变点点点的切换效果
     * @param selectItems 当前选中位置
     */
    private void setImageBackground(int selectItems){
        for (int i = 0; i < bannerAdapter.getBanners().length; i++) {
            ImageView imageView = (ImageView) viewGroup.getChildAt(i);
            imageView.setBackgroundDrawable(null);//先把背景设置成无
            if (i == selectItems){
                imageView.setImageResource(R.mipmap.icon_page_select);
            } else {
                imageView.setImageResource(R.mipmap.icon_page_normal);
            }
        }
    }

    private View.OnClickListener onBannerClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position=(Integer) view.getTag();//从tag中取出当前点击的ImageView的位置
            Toast.makeText(MainActivity.this,"当前点击位置:"+position,Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(mTicker);//删除计时器
    }
}
