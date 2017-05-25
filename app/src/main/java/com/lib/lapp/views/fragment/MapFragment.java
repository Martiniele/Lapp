package com.lib.lapp.views.fragment;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fengmap.android.FMDevice;
import com.fengmap.android.FMErrorMsg;
import com.fengmap.android.FMMapSDK;
import com.fengmap.android.analysis.navi.FMNaviAnalyser;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.exception.FMObjectException;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapCoordZType;
import com.fengmap.android.map.FMMapUpgradeInfo;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.FMPickMapCoordResult;
import com.fengmap.android.map.FMViewMode;
import com.fengmap.android.map.event.OnFMMapClickListener;
import com.fengmap.android.map.event.OnFMMapInitListener;
import com.fengmap.android.map.event.OnFMNodeListener;
import com.fengmap.android.map.event.OnFMSwitchGroupListener;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.geometry.FMScreenCoord;
import com.fengmap.android.map.layer.FMFacilityLayer;
import com.fengmap.android.map.layer.FMImageLayer;
import com.fengmap.android.map.layer.FMModelLayer;
import com.fengmap.android.map.marker.FMFacility;
import com.fengmap.android.map.marker.FMImageMarker;
import com.fengmap.android.map.marker.FMModel;
import com.fengmap.android.map.marker.FMNode;
import com.fengmap.android.widget.FM3DControllerButton;
import com.fengmap.android.widget.FMSwitchFloorComponent;
import com.fengmap.android.widget.FMZoomComponent;
import com.lib.lapp.R;
import com.lib.lapp.adapter.SearchBarAdapter;
import com.lib.lapp.contract.IMapViewFragment;
import com.lib.lapp.file.utils.FileUtils;
import com.lib.lapp.location.ConvertUtils;
import com.lib.lapp.location.FMLocationAPI;
import com.lib.lapp.view.utils.ViewHelper;
import com.lib.lapp.widget.ImageViewCheckBox;
import com.lib.lapp.widget.SearchBar;
import com.lib.lapp.widget.utils.AnalysisUtils;
import com.lib.lapp.widget.utils.DataloadUtils;
import com.lib.lapp.widget.utils.KeyBoardUtils;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import at.markushi.ui.CircleButton;

/**
 * @author wxx
 * @date 2017-03-15
 * @descripe 负责展示地图及其相关的控件，监听地图的初始化，地图的点击事件，搜索框的搜索事件
 */
public class MapFragment extends BaseFragment implements OnFMMapInitListener,
        IMapViewFragment,                                 //扩展业务接口
        OnFMMapClickListener,                             //地图点击事件监听
        SearchBar.OnSearchResultCallback,                 //搜索框事件监听
        AdapterView.OnItemClickListener,                  //搜索结果列表选项点击事件监听
        FMLocationAPI.OnFMLocationListener,               //定位事件监听
        ImageViewCheckBox.OnCheckStateChangedListener,    //地图控件状态监听
        OnFMSwitchGroupListener {                         //楼层改变的事件监听

    private Dialog loadDialog;                          //延时加载
    private View view = null;                           //Fragment的视图
    private SearchBarAdapter mSearchAdapter;           //搜索框用于处理结果的适配器

    private Button button;
    private TextView txt_info;
    private View showWin;
    private volatile boolean isClick = false;
    private boolean isThemeCheck = true;
    private boolean isPathCheck = false;


    private Handler loadMaphandler = new Handler(){    //用于延迟加载的子线程
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10:
                    DataloadUtils.closeDialog(loadDialog);
                    break;
                case WHAT_LOCATE_SWITCH_GROUP:
                    updateLocateGroupView();
                    break;
                case WHAT_WALKING_ROUTE_LINE:
                    updateWalkRouteLine((FMMapCoord) msg.obj);
                    break;
            }
        }
    };

    /**
     * 加载Fragment页面
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fg_map_view_content, container, false);
        }
        mMapView = (FMMapView) view.findViewById(R.id.mapview);
        mSearchBar = (SearchBar) view.findViewById(R.id.search);
        mSearchBar.setOnSearchResultCallback(this);
        mSearchBar.setOnItemClickListener(this);

        button = (Button) view.findViewById(R.id.pathBtn);
        txt_info = (TextView) view.findViewById(R.id.txt_info);
        txt_info.bringToFront();
        showWin = view.findViewById(R.id.find_path_layout);
        showWin.bringToFront();
        return view;
    }

    /**
     * 地图加载成功时的回调处理
     * @param str
     */
    @Override
    public void onMapInitSuccess(String str) {
        mFmap.loadThemeById("3006");
        mFmap.setFMViewMode(FMViewMode.FMVIEW_MODE_3D);
        mImageLayer = mFmap.getFMLayerProxy().createFMImageLayer(mFmap.getFocusGroupId());
        mFmap.addLayer(mImageLayer);
        int gid = mFmap.getFocusGroupId();
        //公共设施图层
        mFacilityLayer = mFmap.getFMLayerProxy().getFMFacilityLayer(gid);
        mFacilityLayer.setOnFMNodeListener(mOnFacilityClickListener);
        mFmap.addLayer(mFacilityLayer);

        //模型图层
        mModelLayer = mFmap.getFMLayerProxy().getFMModelLayer(gid);
        mModelLayer.setOnFMNodeListener(mOnModelCLickListener);
        mFmap.addLayer(mModelLayer);

        mLineLayer = mFmap.getFMLayerProxy().getFMLineLayer();
        mFmap.addLayer(mLineLayer);

        //定位层
        mLocationLayer = mFmap.getFMLayerProxy().getFMLocationLayer();
        mFmap.addLayer(mLocationLayer);

        ViewHelper.setViewCheckedChangeListener(getActivity(), R.id.btn_locate, this);
        ViewHelper.setViewCheckedChangeListener(getActivity(), R.id.btn_view, this);
        ViewHelper.setViewCheckedChangeListener(getActivity(), R.id.btn_theme, this);
        ViewHelper.setViewCheckedChangeListener(getActivity(),R.id.btn_path, this);

        //导航分析
        try {
            mNaviAnalyser = FMNaviAnalyser.getFMNaviAnalyserById(FileUtils.DEFAULT_MAP_ID);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FMObjectException e) {
            e.printStackTrace();
        }

        int groupSize = mFmap.getFMMapInfo().getGroupSize();
        for (int i = 0; i < groupSize; i++) {
            int groupId = mFmap.getMapGroupIds()[i];
            FMImageLayer imageLayer = mFmap.getFMLayerProxy().createFMImageLayer(groupId);
            mFmap.addLayer(imageLayer);
            mImageLayers.put(groupId, imageLayer);
        }
        try {
            mSearchAnalyser = FMSearchAnalyser.getFMSearchAnalyserById(FileUtils.DEFAULT_MAP_ID);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FMObjectException e) {
            e.printStackTrace();
        }

        initZoomComponent();
        init3DControllerComponent();
    }

    /**
     * 耗时加载地图（处理耗时操作）防止生命周期混乱带来的APP强退情况
     */
    @Override
    public void fetchData() {
        loadDialog = DataloadUtils.createLoadingDialog(getActivity(),"地图加载中...");
        openMapByPath(view);
        loadMaphandler.sendEmptyMessageDelayed(10,2000);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_info.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 地图点击事件监听
     * @param x
     * @param y
     */
    @Override
    public void onMapClick(float x, float y) {
        // 获取屏幕点击位置的地图坐标
        final FMPickMapCoordResult mapCoordResult = mFmap.pickMapCoord(x, y);
        if (mapCoordResult == null) {
            return;
        }

        txt_info.setVisibility(View.INVISIBLE);

        // 起点
        if (stCoord == null) {
            clear();
            stCoord = mapCoordResult.getMapCoord();
            Log.e("StartX------>",String.valueOf(stCoord.x));
            Log.e("StartY------>",String.valueOf(stCoord.y));
            stGroupId = mapCoordResult.getGroupId();
            createStartImageMarker();
            return;
        }

        // 终点
        if (endCoord == null) {
            endCoord = mapCoordResult.getMapCoord();
            Log.e("EndX------>",String.valueOf(endCoord.x));
            Log.e("EndY------>",String.valueOf(endCoord.y));
            endGroupId = mapCoordResult.getGroupId();
            createEndImageMarker();
        }
        analyzeNavigation();
        // 画完置空
        stCoord = null;
        endCoord = null;
        mTotalDistance = mNaviAnalyser.getSceneRouteLength();
    }

    /**
     * 搜索框事件监听回调
     * @param keyword 关键字
     */
    @Override
    public void onSearchCallback(String keyword) {
        //地图未显示前，不执行搜索事件
        boolean isCompleted = mFmap.getMapFirstRenderCompleted();
        if (!isCompleted) {
            return;
        }
        ArrayList<FMModel> models = AnalysisUtils.queryModelByKeyword(mFmap, mSearchAnalyser, keyword);
        if (mSearchAdapter == null) {
            mSearchAdapter = new SearchBarAdapter(getActivity(), models);
            mSearchBar.setAdapter(mSearchAdapter);
        } else {
            mSearchAdapter.setDatas(models);
            mSearchAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 搜索框搜索结果选项条事件监听
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //关闭软键盘
        KeyBoardUtils.closeKeybord(mSearchBar.getCompleteText(), getActivity());
        FMModel model = (FMModel) parent.getItemAtPosition(position);
        //切换楼层
        int groupId = model.getGroupId();
        if (groupId != mFmap.getFocusGroupId()) {
            mFmap.setFocusByGroupId(groupId, null);
        }
        //移动至中心点
        FMMapCoord mapCoord = model.getCenterMapCoord();
        mFmap.moveToCenter(mapCoord, false);
        clearImageMarker();
        //添加图片
        FMImageMarker imageMarker = ViewHelper.buildImageMarker(getResources(), mapCoord, R.drawable.ic_nav_end);
        imageMarker.setCustomOffsetHeight(3);
        imageMarker.setFMImageMarkerOffsetMode(FMImageMarker.FMImageMarkerOffsetMode.FMNODE_CUSTOM_HEIGHT);
        mImageLayers.get(model.getGroupId()).addMarker(imageMarker);
        clearFocus(model);
    }


    /**
     * 离线加载地图
     */
    @Override
    public void openMapByPath(View view) {
        mFmap = mMapView.getFMMap();
        mFmap.setOnFMMapInitListener(this);
        mFmap.setOnFMMapClickListener(this);
        //加载离线数据
        String path = FileUtils.getDefaultMapPath(getActivity());
        mFmap.openMapByPath(path);
    }

    /**
     * 在线加载地图
     */
    @Override
    public void openMapById(View view) {
        mFmap = mMapView.getFMMap();
        mFmap.setOnFMMapInitListener(this);
        mFmap.setOnFMMapClickListener(this);
        String mid = FileUtils.DEFAULT_MAP_ID;
        mFmap.openMapById(mid,true);
    }

    /**
     * 初始化地图缩放操作的控件的布局
     */
    @Override
    public void initZoomComponent() {
        mZoomComponent = new FMZoomComponent(this.getActivity());
        mZoomComponent.measure(0, 0);
        int width = mZoomComponent.getMeasuredWidth();
        int height = mZoomComponent.getMeasuredHeight();
        //缩放控件位置
        int offsetX = FMDevice.getDeviceWidth() - width - 10;
        int offsetY = FMDevice.getDeviceHeight() - 1000 - height;
        mMapView.addComponent(mZoomComponent, offsetX, offsetY);
        mZoomComponent.setOnFMZoomComponentListener(new FMZoomComponent.OnFMZoomComponentListener() {
            @Override
            public void onZoomIn(View view) {
                //地图放大
                mFmap.zoomIn();
            }
            @Override
            public void onZoomOut(View view) {
                //地图缩小
                mFmap.zoomOut();
            }
        });
    }

    /**
     * 地图更新监听
     * @param fmMapUpgradeInfo
     * @return
     */
    @Override
    public boolean onUpgrade(FMMapUpgradeInfo fmMapUpgradeInfo) {
        return false;
    }

    /**
     * 初始化2D/3D模式切换控件
     */
    @Override
    public void init3DControllerComponent() {
        m3DTextButton = new FM3DControllerButton(this.getContext());
        //设置初始状态为3D(true),设置为false为2D模式
        m3DTextButton.initState(true);
        m3DTextButton.measure(0, 0);
        int width = m3DTextButton.getMeasuredWidth();
        //设置3D控件位置
        mMapView.addComponent(m3DTextButton, FMDevice.getDeviceWidth() - 10 - width, 50);
        //2、3D点击监听
        m3DTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FM3DControllerButton button = (FM3DControllerButton) v;
                if (button.isSelected()) {
                    button.setSelected(false);
                    mFmap.setFMViewMode(FMViewMode.FMVIEW_MODE_2D);
                } else {
                    button.setSelected(true);
                    mFmap.setFMViewMode(FMViewMode.FMVIEW_MODE_3D);
                }
            }
        });
    }

    /**
     * 地图加载失败时的回调处理
     * @param path
     * @param errorCode
     */
    @Override
    public void onMapInitFailure(String path, int errorCode) {
        String error = FMErrorMsg.getErrorMsg(errorCode);
        Log.i("INIT MAP FAILURE IS", "onMapInitFailure: " + error);
        String sha1code = FMMapSDK.getSha1Value();
        Log.i("INIT SHA1 CODE IS", "onMapInitFailure: " + sha1code);
    }

    /**
     * 在Fragment创建的时候申请SD卡读写权限
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 23) {
            //Android 6.0 之前无需运行时权限申请
        } else {
            // 先检测权限  目前SDK只需2个危险权限，读和写存储卡
            int p1 = this.getContext().checkSelfPermission(FMMapSDK.SDK_PERMISSIONS[0]);
            int p2 = this.getContext().checkSelfPermission(FMMapSDK.SDK_PERMISSIONS[1]);
            // 只要有任一权限没通过，则申请
            if (p1 != PackageManager.PERMISSION_GRANTED || p2 != PackageManager.PERMISSION_GRANTED ) {
                this.requestPermissions(FMMapSDK.SDK_PERMISSIONS,                //SDK所需权限数组
                        FMMapSDK.SDK_PERMISSION_RESULT_CODE);   //SDK权限申请处理结果返回码
            } else {
                Log.d("Android 6.0 系统运行时权限申请", "onCreate: "+"权限已经拥有");
            }
        }
    }

    /**
     * 权限申请情况回调函数
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            this.getActivity().onBackPressed();
        } else if (requestCode == FMMapSDK.SDK_PERMISSION_RESULT_CODE) {
            Log.d("Android 6.0 系统运行时权限申请码", "onCreate: "+" 权限已经拥有");
        }
    }

    /**
     * 切换页面时销毁地图
     */
    @Override
    public void onDestroyView() {
        if(mFmap != null){
            mFmap.onDestroy();
        }
        super.onDestroyView();
    }


    /**
     * 定位导航事件监听动画开始
     */
    @Override
    public void onAnimationStart() {

    }

    /**
     * 定位导航事件 导航过程路线和定位图标的更新
     * @param mapCoord 当前点坐标
     * @param distance 行走距离
     * @param angle    当前点角度
     */
    @Override
    public void onAnimationUpdate(FMMapCoord mapCoord, double distance, double angle) {
        updateHandledMarker(mapCoord, angle);
        scheduleCalcWalkingRouteLine(mapCoord, distance);
    }

    /**
     * 定位导航事件动画结束
     */
    @Override
    public void onAnimationEnd() {
        if (isWalkComplete()) {
            setStartAnimationEnable(true);
            loadMaphandler.post(new Runnable() {
                @Override
                public void run() {
                    String info = getResources().getString(R.string.label_walk_format, 0f,
                            0, "到达目的地");
                    ViewHelper.setViewText(getActivity(), R.id.txt_info, info);
                }
            });
            return;
        }
        int focusGroupId = getWillWalkingGroupId();
        //跳转至下一层
        setFocusGroupId(focusGroupId);
    }

    /**
     * 地图控件状态监听处理
     * @param view
     * @param isChecked
     */
    @Override
    public void onCheckStateChanged(View view, boolean isChecked) {
        switch (view.getId()) {
            case R.id.btn_view: {
                setViewState(isChecked);
            }
            break;
            case R.id.btn_locate: {
                setFollowState(isChecked);
            }
            break;
            case R.id.btn_theme: {
                setTheme(isChecked);
            }
            break;
            case R.id.btn_path: {
                showPathSeachWindow(isChecked);
            }
            break;
        }
    }

    @Override
    public void beforeGroupChanged() {

    }

    @Override
    public void afterGroupChanged() {

    }

    @Override
    public void updateLocateGroupView() {
        int groupSize = mFmap.getFMMapInfo().getGroupSize();
        int position = groupSize - mFmap.getFocusGroupId();
        mSwitchFloorComponent.setSelected(position);
    }

    /**
     * 更新约束定位点
     *
     * @param coord 坐标
     */
    private void updateHandledMarker(FMMapCoord coord, double angle) {
        if (mHandledMarker == null) {
            mHandledMarker = ViewHelper.buildLocationMarker(lstCoord.getGroupId(),
                    coord);
            mLocationLayer.addMarker(mHandledMarker);
        } else {
            FMMapCoord mapCoord = makeConstraint(coord);
            if (mIsFirstView && angle != 0) {
                animateRotate((float) -angle);
            }
            mHandledMarker.updateAngleAndPosition((float) angle, mapCoord);
        }

        //判断当前定位标注是否显示
        updateLocationMarker(mFmap.getFocusGroupId());

        //跟随效果
        mLastMoveCoord = coord.clone();
        checkLocationFollowState();

        checkLocationIsCenter();
    }

    /**
     * 行走真实路线
     */
    public void startWalkingRouteLine() {
        mLeftDistance = mTotalDistance;
        super.startWalkingRouteLine();
    }

    /**
     * 判断定位点是否应该处于屏幕中央
     */
    private void checkLocationIsCenter() {
        if (mHasFollowed || mIsFirstView) {
            //切换楼层
            int groupId = mLocationAPI.getGroupId();
            if (mFmap.getFocusGroupId() != groupId) {
                mFmap.setFocusByGroupId(groupId, this);
            }
        }
    }

    /**
     * 设置是否为第一人称
     *
     * @param enable true 第一人称
     *               false 第三人称
     */
    private void setViewState(boolean enable) {
        this.mIsFirstView = !enable;
        setFloorControlEnable();
    }

    /**
     * 设置楼层控件是否可用
     */
    private void setFloorControlEnable() {
        if (getFloorControlEnable()) {
            mSwitchFloorComponent.close();
            mSwitchFloorComponent.setEnabled(false);
        } else {
            mSwitchFloorComponent.setEnabled(true);
        }
    }

    /**
     * 楼层控件是否可以使用
     *
     * @return
     */
    private boolean getFloorControlEnable() {
        return mHasFollowed || mIsFirstView;
    }

    /**
     * 路径约束
     *
     * @param mapCoord 地图坐标点
     * @return
     */
    private FMMapCoord makeConstraint(FMMapCoord mapCoord) {
        FMMapCoord currentCoord = mapCoord.clone();
        int groupId = mLocationAPI.getGroupId();
        //获取当层绘制路径线点集合
        ArrayList<FMMapCoord> coords = mLocationAPI.getSimulateCoords();
        //路径约束
        mNaviAnalyser.naviConstraint(groupId, coords, mLastMoveCoord, currentCoord);
        return currentCoord;
    }

    /**
     * 计算行走距离
     *
     * @param mapCoord 定位点
     * @param distance 已行走距离
     */
    private void scheduleCalcWalkingRouteLine(FMMapCoord mapCoord, double distance) {
        mLeftDistance -= distance;
        if (mLeftDistance <= 0) {
            mLeftDistance = 0;
        }

        Message message = Message.obtain();
        message.what = WHAT_WALKING_ROUTE_LINE;
        message.obj = mapCoord;
       loadMaphandler.sendMessage(message);
    }

    /**
     * 更新行走距离和文字导航
     *
     * @param mapCoord 定位点坐标
     */
    private void updateWalkRouteLine(FMMapCoord mapCoord) {
        int timeByWalk = ConvertUtils.getTimeByWalk(mLeftDistance);
        String description = ConvertUtils.getDescription(mNaviAnalyser.getNaviDescriptionData(),
                mapCoord.clone(), mLocationAPI.getGroupId());
        String info = getResources().getString(R.string.label_walk_format, mLeftDistance,
                timeByWalk, description);
        ViewHelper.setViewText(getActivity(), R.id.txt_info, info);
    }

    /**
     * 楼层切换控件初始化
     */
    private void initSwitchFloorComponent() {
        mSwitchFloorComponent = new FMSwitchFloorComponent(getActivity());
        //最多显示6个
        mSwitchFloorComponent.setMaxItemCount(6);
        mSwitchFloorComponent.setEnabled(false);
        mSwitchFloorComponent.setOnFMSwitchFloorComponentListener(new FMSwitchFloorComponent.OnFMSwitchFloorComponentListener() {
            @Override
            public boolean onItemSelected(int groupId, String floorName) {
                mFmap.setFocusByGroupId(groupId, null);
                //判断定位标注是否显示
                if (mLocationAPI.getGroupId() != -1) {
                    updateLocationMarker(groupId);
                }
                return true;
            }
        });
        mSwitchFloorComponent.setFloorDataFromFMMapInfo(mFmap.getFMMapInfo(), mFmap.getFocusGroupId());
        addSwitchFloorComponent();
    }

    /**
     * 添加楼层切换按钮
     */
    private void addSwitchFloorComponent() {
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        FrameLayout viewGroup = (FrameLayout) getActivity().findViewById(R.id.layout_group_control);
        viewGroup.addView(mSwitchFloorComponent, lp);
    }

    /**
     * 更新定位点状态
     *
     * @param groupId 楼层id
     */
    private void updateLocationMarker(int groupId) {
        boolean visible = mLocationAPI.getGroupId() == groupId;
        mHandledMarker.setVisible(visible);
    }

    /**
     * 设置跟随状态
     *
     * @param enable true 跟随  false 不跟随
     *
     */
    private void setFollowState(boolean enable) {
        mHasFollowed = enable;
        setFloorControlEnable();
    }

    /**
     * 判断定位点是否为跟随状态
     */
    private void checkLocationFollowState() {
        if (mHasFollowed) {
            moveToCenter(mLastMoveCoord);
        }
    }

    /**
     * 主题切换控件，切换地图主题
     * @param enable
     */
    private void setTheme(boolean enable){
        isThemeCheck = enable;
        if(isThemeCheck){
            mFmap.loadThemeById("3006");
        }else {
            mFmap.loadThemeById("3008");
        }
    }

    /**
     * 起终点路径搜索页面加载
     * @param enable
     */
    private void showPathSeachWindow(boolean enable){
        isPathCheck = enable;
        if(isPathCheck){
            showWin.setVisibility(View.VISIBLE);
        }else {
            showWin.setVisibility(View.INVISIBLE);
        }
    }
}
