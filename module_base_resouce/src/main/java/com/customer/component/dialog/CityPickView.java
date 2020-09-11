package com.customer.component.dialog;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.lib.basiclib.base.xui.widget.picker.wheelview.interfaces.IPickerViewItem;
import com.lib.basiclib.base.xui.widget.picker.widget.OptionsPickerView;
import com.lib.basiclib.base.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.lib.basiclib.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @ Author  QinTian
 * @ Date  2020/8/25
 * @ Describe
 */
public class CityPickView {

    private Context mContext;

    private CityGetSelect mCityGetSelect;

    private List<ProvinceInfo> options1Items = new ArrayList<>();
    private List<List<String>> options2Items = new ArrayList<>();
    private List<List<List<String>>> options3Items = new ArrayList<>();

    private boolean mHasLoaded;


    public CityPickView(Context context, CityGetSelect CityGetSelect) {
        this.mContext = context;
        this.mCityGetSelect = CityGetSelect;
        initViews();
    }

    private void initViews() {
        List<ProvinceInfo> provinceInfos = fromJson(getJson("province.json", mContext), new TypeToken<List<ProvinceInfo>>() {}.getType());
        loadData(provinceInfos);
    }


    public void showPickerView(boolean isDialog) {// 弹出选择器
        if (!mHasLoaded) {
            ToastUtils.INSTANCE.showToast("数据加载中...");
            return;
        }

        int[] defaultSelectOptions = getDefaultCity();

        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, (v, options1, options2, options3) -> {
            //返回的分别是三个级别的选中位置
            mCityGetSelect.setSelect(options1Items.get(options1).getPickerViewText() + "," + options2Items.get(options1).get(options2) + " " + options3Items.get(options1).get(options2).get(options3));
            return false;
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                //切换选项时，还原到第一项
                .isRestoreItem(true)
                //设置选中项文字颜色
                .setTextColorCenter(Color.RED)
                .setContentTextSize(20)
                .isDialog(isDialog)
                .setSelectOptions(defaultSelectOptions[0], defaultSelectOptions[1], defaultSelectOptions[2])
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void loadData(List<ProvinceInfo> provinceInfos) {//加载数据
        /**
         * 添加省份数据
         */
        options1Items = provinceInfos;

        //遍历省份（第一级）
        for (ProvinceInfo provinceInfo : provinceInfos) {
            //该省的城市列表（第二级）
            List<String> cityList = new ArrayList<>();
            //该省的所有地区列表（第三级）
            List<List<String>> areaList = new ArrayList<>();

            for (ProvinceInfo.City city : provinceInfo.getCityList()) {
                //添加城市
                String cityName = city.getName();
                cityList.add(cityName);
                //该城市的所有地区列表
                List<String> cityAreaList = new ArrayList<>();
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (city.getArea() == null || city.getArea().size() == 0) {
                    cityAreaList.add("");
                } else {
                    cityAreaList.addAll(city.getArea());
                }
                //添加该省所有地区数据
                areaList.add(cityAreaList);
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(areaList);
        }

        mHasLoaded = true;
    }


    private String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * 解析Json字符串
     *
     * @param json    Json字符串
     * @param typeOfT 泛型类
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        try {
            return new Gson().fromJson(json, typeOfT);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return 获取默认城市的索引
     */
    private int[] getDefaultCity() {
        int[] res = new int[3];
        ProvinceInfo provinceInfo;
        List<ProvinceInfo.City> cities;
        ProvinceInfo.City city;
        List<String> ares;
        for (int i = 0; i < options1Items.size(); i++) {
            provinceInfo = options1Items.get(i);
            if ("北京市".equals(provinceInfo.getName())) {
                res[0] = i;
                cities = provinceInfo.getCityList();
                for (int j = 0; j < cities.size(); j++) {
                    city = cities.get(j);
                    if ("北京市".equals(city.getName())) {
                        res[1] = j;
                        ares = city.getArea();
                        for (int k = 0; k < ares.size(); k++) {
                            if ("东城区".equals(ares.get(k))) {
                                res[2] = k;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return res;
    }

    static class ProvinceInfo implements IPickerViewItem {
        /**
         * name : 省份
         * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
         */
        private String name;
        private List<City> city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<City> getCityList() {
            return city;
        }

        public void setCityList(List<City> city) {
            this.city = city;
        }

        // 实现 IPickerViewData 接口，
        // 这个用来显示在PickerView上面的字符串，
        // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
        @Override
        public String getPickerViewText() {
            return this.name;
        }

        static class City {
            /**
             * name : 城市
             * area : ["东城区","西城区","崇文区","昌平区"]
             */

            private String name;
            private List<String> area;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getArea() {
                return area;
            }

            public void setArea(List<String> area) {
                this.area = area;
            }
        }
    }



}
