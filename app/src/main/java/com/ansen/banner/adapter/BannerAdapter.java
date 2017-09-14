package com.ansen.banner.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ansen.banner.R;

/**
 * banner适配器
 * @author ansen
 * @create time 2017-09-13
 */
public class BannerAdapter extends PagerAdapter {
    private Context context;
    private View.OnClickListener onBannerClickListener;

    //图片列表
    private int[] banners=new int[]{R.mipmap.banner_one,R.mipmap.banner_two,R.mipmap.banner_three};

    public BannerAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;//返回int的最大值 可以一直滑动
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //position的值范围是 0至2147483647  把这个值对图片长度求余之后  position的取值范围是0至banners.length-1
        position %= banners.length;

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置图片缩放类型
        imageView.setTag(position);//把当前的下标通过setTag方法设置进去
        imageView.setImageResource(banners[position]);
        imageView.setOnClickListener(onClickListener);
        container.addView(imageView);
        return imageView;
    }

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onBannerClickListener!=null){
                onBannerClickListener.onClick(v);
            }
        }
    };

    /**
     * 设置图片点击
     * @param onBannerClickListener
     */
    public void setOnBannerClickListener(View.OnClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }

    public int[] getBanners(){
        return banners;
    }
}