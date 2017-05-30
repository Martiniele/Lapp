package com.lib.lapp.views.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.fengmap.android.analysis.navi.FMNaviAnalyser;
import com.fengmap.android.analysis.navi.FMNaviResult;
import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.FMMapView;
import com.fengmap.android.map.event.OnFMNodeListener;
import com.fengmap.android.map.geometry.FMMapCoord;
import com.fengmap.android.map.layer.FMFacilityLayer;
import com.fengmap.android.map.layer.FMImageLayer;
import com.fengmap.android.map.layer.FMLineLayer;
import com.fengmap.android.map.layer.FMLocationLayer;
import com.fengmap.android.map.layer.FMModelLayer;
import com.fengmap.android.map.layer.FMTextLayer;
import com.fengmap.android.map.marker.FMFacility;
import com.fengmap.android.map.marker.FMImageMarker;
import com.fengmap.android.map.marker.FMLineMarker;
import com.fengmap.android.map.marker.FMLocationMarker;
import com.fengmap.android.map.marker.FMModel;
import com.fengmap.android.map.marker.FMNode;
import com.fengmap.android.map.marker.FMSegment;
import com.fengmap.android.utils.FMMath;
import com.fengmap.android.widget.FM3DControllerButton;
import com.fengmap.android.widget.FMNodeInfoWindow;
import com.fengmap.android.widget.FMSwitchFloorComponent;
import com.fengmap.android.widget.FMZoomComponent;
import com.lib.lapp.R;
import com.lib.lapp.location.MapLocationAPI;
import com.lib.lapp.model.MapCoord;
import com.lib.lapp.view.utils.ViewHelper;
import com.lib.lapp.widget.SearchBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author wxx
 * @Date 2017/03/12
 * @Description 地图页面基类
 */
public abstract class BaseFragment extends Fragment {

    protected boolean isViewInitiated;
    protected boolean isVisibleToUser;
    protected boolean isDataInitiated;

    protected HashMap<Integer, FMImageLayer> mImageLayers = new HashMap<>();

    protected FMModel mLastClicked;                                                       //地图模型
    protected FMFacility fLastClicked;                                                    //公共设施模型
    protected SearchBar mSearchBar;                                                       //搜索框控件
    protected FM3DControllerButton m3DTextButton;                                         //2D/3D切换控件
    protected FMZoomComponent mZoomComponent;                                             //地图缩放控件
    protected FMLineLayer mLineLayer;                                                     //画线图层
    protected FMLocationLayer mLocationLayer;                                             //定位图层
    protected FMImageLayer mImageLayer;                                                   //通用图片标注图层
    protected FMTextLayer mTextLayer;                                                     //文字图层
    protected FMFacilityLayer mFacilityLayer;                                             //公共设施图层
    protected FMModelLayer mModelLayer;                                                   //模型图层
    protected FMNaviAnalyser mNaviAnalyser;                                               //导航分析
    protected FMSearchAnalyser mSearchAnalyser;                                           //搜索分析
    protected FMMapView mMapView;                                                         //地图视图
    protected FMMap mFmap;                                                                //地图控制

    protected FMMapCoord stCoord;                                                        //起点坐标
    protected int stGroupId;                                                             //起点楼层id
    protected FMImageLayer stImageLayer;                                                 //起点图片标注图层
    protected FMMapCoord endCoord;                                                       //终点坐标
    protected int endGroupId;                                                            //终点楼层id
    protected FMImageLayer endImageLayer;                                                //终点图片标注图层
    protected FMNodeInfoWindow mInfoWindow = null;                                       //书架信息弹窗
    protected LayoutInflater inflater;                                                   //视图获取器
    protected FMImageLayer bookImageLayer;                                               //书架图层添加
    protected FMImageMarker bookMaker;                                                   //书架定位图标

    protected FMLocationMarker mHandledMarker;                                           //约束过的定位标注
    protected FMMapCoord mLastMoveCoord;                                                 //上一次行走坐标
    protected boolean mIsFirstView = true;                                               //是否为第一人称
    protected boolean mHasFollowed = true;                                               //是否为跟随状态
    protected double mTotalDistance;                                                     //总共距离
    protected volatile double mLeftDistance;                                             //剩余距离

    protected FMSwitchFloorComponent mSwitchFloorComponent;                              //楼层切换控件
    protected MapLocationAPI mLocationAPI;                                                //差值动画

    protected static final int UPDATE_INFOWINDOW_MSG = 3;                                //信息窗信息更新信号
    protected static final int WHAT_WALKING_ROUTE_LINE = 2;                              //行走显示详情
    protected static final int WHAT_LOCATE_SWITCH_GROUP = 1;                             //定位楼层切换
    protected static final double MAX_BETWEEN_LENGTH = 20;                                 //两个点之间的最大距离为20米
    protected static final int MAP_NORMAL_LEVEL = 20;                                      //进入地图显示级别

    protected MapCoord lstCoord = new MapCoord(1, new FMMapCoord(12291225.0, 2914593.5));  //默认起点
    protected MapCoord lendCoord = new MapCoord(1, new FMMapCoord(12291183.0, 2914551.0)); //默认终点


    protected ArrayList<ArrayList<FMMapCoord>> mNaviPoints = new ArrayList<>();            //导航行走点集合
    protected ArrayList<Integer> mNaviGroupIds = new ArrayList<>();                        //导航行走的楼层集合
    protected int mCurrentIndex = 0;                                                       //导航行走索引
    protected FMImageLayer mEndImageLayer;                                                 //终点图层
    protected FMImageLayer mStartImageLayer;                                               //起点图层

    private TextView iw_content;

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_LOCATE_SWITCH_GROUP:
                    updateLocateGroupView();
                    break;
                case UPDATE_INFOWINDOW_MSG:
                    Bundle data = msg.getData();
                    iw_content = (TextView) getActivity().findViewById(R.id.iw_content);
                    iw_content.setMovementMethod(ScrollingMovementMethod.getInstance());
                    iw_content.setText("FID：" + data.get("FID") + "\n" +
                            "名称：" + data.get("NAME") + "\n" +
                            "类型：" + data.get("TYPE") + "\n" +
                            "面数：两面共6层" + "\n" +
                            "详细信息：计算机，电子工程相关书籍");
                    break;
            }
        }
    };

    /**
     * 模型点击事件
     */
    protected OnFMNodeListener mOnModelCLickListener = new OnFMNodeListener() {
        @Override
        public boolean onClick(FMNode node) {
            if (mLastClicked != null) {
                mLastClicked.setSelected(false);
            }
            FMModel model = (FMModel) node;
            mLastClicked = model;
            model.setSelected(true);
            mFmap.updateMap();
            FMMapCoord centerMapCoord = model.getCenterMapCoord();

            int groupId = mFmap.getFocusGroupId();
            clearBookImageLayer();
            //获取图片图层
            bookImageLayer = mFmap.getFMLayerProxy().createFMImageLayer(groupId);
            mFmap.addLayer(bookImageLayer);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_marker_blue);
            bookMaker = new FMImageMarker(centerMapCoord, bitmap);

            //设置图片宽高
            bookMaker.setMarkerWidth(90);
            bookMaker.setMarkerHeight(90);
            bookMaker.setCustomOffsetHeight(3);
            //设置图片在模型之上
            bookMaker.setFMImageMarkerOffsetMode(FMImageMarker.FMImageMarkerOffsetMode.FMNODE_CUSTOM_HEIGHT);
            bookImageLayer.addMarker(bookMaker);
            updateInfoWinData(model);
            showInfoWindow(centerMapCoord, model);
            return true;
        }

        @Override
        public boolean onLongPress(FMNode node) {
            return false;
        }
    };

    /**
     * 更新信息窗的信息
     *
     * @param model
     */
    protected void updateInfoWinData(FMModel model) {
        Message msg = new Message();
        msg.what = UPDATE_INFOWINDOW_MSG;
        Bundle data = new Bundle();
        data.putString("FID", String.valueOf(model.getFID()));
        data.putString("NAME", model.getName());
        data.putString("TYPE", String.valueOf(model.getDataType()));
        msg.setData(data);
        mHandler.sendMessage(msg);
    }

    /**
     * 显示信息框
     */
    protected void showInfoWindow(FMMapCoord centorCoor, FMModel model) {
        if (mInfoWindow == null) {
            mInfoWindow = new FMNodeInfoWindow(mMapView, R.layout.pw_layout_info_window);
            mInfoWindow.setPosition(mFmap.getFocusGroupId(), centorCoor);
        }
        if (mInfoWindow.isOpened()) {
            mInfoWindow.close();
        } else {
            mInfoWindow.openOnTarget(model);
        }
        mFmap.updateMap();
    }

    /**
     * 公共设施点击事件
     */
    protected OnFMNodeListener mOnFacilityClickListener = new OnFMNodeListener() {
        @Override
        public boolean onClick(FMNode node) {
            FMFacility facility = (FMFacility) node;
            fLastClicked = facility;
            facility.setSelected(true);
            mFmap.updateMap();
            FMMapCoord centerMapCoord = facility.getPosition();
            return true;
        }

        @Override
        public boolean onLongPress(FMNode node) {
            return false;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    /* 用这个方法来判断当前UI是否可见 */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    /**
     * 子类继承的方法，用于延迟加载数据的抽象方法
     */
    public abstract void fetchData();

    /**
     * 内部函数
     *
     * @return
     */
    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    /**
     * 当前UI可见，并且fragment已经初始化完毕，如果网络数据未加载，
     * 那么请求数据，或者需要强制刷新页面，那么也去请求数据
     */
    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }


    /**
     * 清理搜索框焦点
     *
     * @param model
     */
    protected void clearFocus(FMModel model) {
        if (!model.equals(mLastClicked)) {
            if (mLastClicked != null) {
                mLastClicked.setSelected(false);
            }
            this.mLastClicked = model;
            this.mLastClicked.setSelected(true);
        }
    }

    /**
     * 清楚图片标志
     */
    protected void clearImageMarker() {
        for (FMImageLayer imageLayer : mImageLayers.values()) {
            imageLayer.removeAll();
        }
    }

    /**
     * 清理所有的线与图层
     */
    protected void clear() {
        clearLineLayer();
        clearStartImageLayer();
        clearEndImageLayer();
    }


    /**
     * 清除线图层
     */
    protected void clearLineLayer() {
        if (mLineLayer != null) {
            mLineLayer.removeAll();
        }
    }

    /**
     * 清除起点图层
     */
    protected void clearBookImageLayer() {
        if (bookImageLayer != null) {
            bookImageLayer.removeAll();
            mFmap.removeLayer(bookImageLayer); // 移除图层
            bookImageLayer = null;
        }
    }

    /**
     * 清除起点图层
     */
    protected void clearStartImageLayer() {
        if (stImageLayer != null) {
            stImageLayer.removeAll();
            mFmap.removeLayer(stImageLayer); // 移除图层
            stImageLayer = null;
        }
    }


    /**
     * 清除终点图层
     */
    protected void clearEndImageLayer() {
        if (endImageLayer != null) {
            endImageLayer.removeAll();
            mFmap.removeLayer(endImageLayer); // 移除图层

            endImageLayer = null;
        }
    }


    /**
     * 添加线标注
     */
    protected void addLineMarker() {
        ArrayList<FMNaviResult> results = mNaviAnalyser.getNaviResults();
        // 填充导航数据
        ArrayList<FMSegment> segments = new ArrayList<>();
        for (FMNaviResult r : results) {
            int groupId = r.getGroupId();
            FMSegment s = new FMSegment(groupId, r.getPointList());
            segments.add(s);
        }
        //添加LineMarker
        FMLineMarker lineMarker = new FMLineMarker(segments);
        lineMarker.setLineWidth(2f);
        mLineLayer.addMarker(lineMarker);
    }

    /**
     * 创建起点图标
     */
    protected void createStartImageMarker() {
        clearStartImageLayer();
        // 添加起点图层
        stImageLayer = new FMImageLayer(mFmap, stGroupId);
        mFmap.addLayer(stImageLayer);
        // 标注物样式
        FMImageMarker imageMarker = ViewHelper.buildImageMarker(getResources(), stCoord, R.drawable.ic_nav_start);
        imageMarker.setCustomOffsetHeight(2);
        //设置图片在模型之上
        imageMarker.setFMImageMarkerOffsetMode(FMImageMarker.FMImageMarkerOffsetMode.FMNODE_CUSTOM_HEIGHT);
        stImageLayer.addMarker(imageMarker);
    }

    /**
     * 创建终点图层
     */
    protected void createEndImageMarker() {
        clearEndImageLayer();
        // 添加起点图层
        endImageLayer = new FMImageLayer(mFmap, endGroupId);
        mFmap.addLayer(endImageLayer);
        // 标注物样式
        FMImageMarker imageMarker = ViewHelper.buildImageMarker(getResources(), endCoord, R.drawable.ic_nav_end);
        imageMarker.setCustomOffsetHeight(2);
        //设置图片在模型之上
        imageMarker.setFMImageMarkerOffsetMode(FMImageMarker.FMImageMarkerOffsetMode.FMNODE_CUSTOM_HEIGHT);
        endImageLayer.addMarker(imageMarker);
    }

    /**
     * 开始分析导航
     */
    protected void analyzeNavigation() {
        int type = mNaviAnalyser.analyzeNavi(stGroupId, stCoord, endGroupId, endCoord,
                FMNaviAnalyser.FMNaviModule.MODULE_SHORTEST);
        if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_SUCCESS) {
            addLineMarker();
        }
    }

    /**
     * 导航分析
     */
    protected void analyzeNavigation(MapCoord stPoint, MapCoord endPoint) {
        clearImageLayer();
        // 添加起点图层
        mStartImageLayer = new FMImageLayer(mFmap, stPoint.getGroupId());
        mFmap.addLayer(mStartImageLayer);
        // 标注物样式
        FMImageMarker imageMarker = ViewHelper.buildImageMarker(getResources(), stPoint.getMapCoord(), R.drawable.ic_nav_start);
        mStartImageLayer.addMarker(imageMarker);

        // 添加终点图层
        mEndImageLayer = new FMImageLayer(mFmap, endPoint.getGroupId());
        mFmap.addLayer(mEndImageLayer);
        // 标注物样式
        imageMarker = ViewHelper.buildImageMarker(getResources(), endPoint.getMapCoord(), R.drawable.ic_nav_end);
        mEndImageLayer.addMarker(imageMarker);

        analyzeNavigation(stPoint.getGroupId(), stPoint.getMapCoord(), endPoint.getGroupId(), endPoint.getMapCoord());
    }

    /**
     * 导航分析。
     *
     * @param startGroupId 起点所在层
     * @param startPt      起点坐标
     * @param endGroupId   终点所在层
     * @param endPt        终点坐标
     */
    protected void analyzeNavigation(int startGroupId, FMMapCoord startPt, int endGroupId, FMMapCoord endPt) {
        int type = mNaviAnalyser.analyzeNavi(startGroupId, startPt, endGroupId, endPt, FMNaviAnalyser.FMNaviModule.MODULE_SHORTEST);
        if (type == FMNaviAnalyser.FMRouteCalcuResult.ROUTE_SUCCESS) {
            fillWithPoints();
            addLineMarker();
        }
    }

    /**
     * 填充导航线段点
     */
    protected void fillWithPoints() {
        clearWalkPoints();

        //获取路径规划上点集合数据
        ArrayList<FMNaviResult> results = mNaviAnalyser.getNaviResults();
        int focusGroupId = Integer.MIN_VALUE;
        for (FMNaviResult r : results) {
            int groupId = r.getGroupId();
            ArrayList<FMMapCoord> points = r.getPointList();
            //点数据小于2，则为单个数据集合
            if (points.size() < 2) {
                continue;
            }
            //判断是否为同层导航数据，非同层数据即其他层数据
            if (focusGroupId == Integer.MIN_VALUE || focusGroupId != groupId) {
                focusGroupId = groupId;
                //添加即将行走的楼层与点集合
                mNaviGroupIds.add(groupId);
                mNaviPoints.add(points);
            } else {
                mNaviPoints.get(mNaviPoints.size() - 1).addAll(points);
            }
        }
    }

    /**
     * 清空行走的点集合数据
     */
    private void clearWalkPoints() {
        mCurrentIndex = 0;
        mNaviPoints.clear();
        mNaviGroupIds.clear();
    }


    /**
     * 清除图片标注
     */
    protected void clearImageLayer() {
        //清理起点图层
        if (mStartImageLayer != null) {
            mStartImageLayer.removeAll();
            mStartImageLayer = null;
        }

        //清理终点图层
        if (mEndImageLayer != null) {
            mEndImageLayer.removeAll();
            mEndImageLayer = null;
        }
    }


    /**
     * 判断是否行走到终点
     *
     * @return
     */
    protected boolean isWalkComplete() {
        if (mCurrentIndex > mNaviGroupIds.size() - 1) {
            return true;
        }
        return false;
    }

    /**
     * 设置动画按钮是否可以使用
     *
     * @param enable true 可以执行, false 不可以执行
     */
    protected void setStartAnimationEnable(final boolean enable) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setViewEnable(getActivity(), R.id.pathBtn, enable);
            }
        });
    }

    /**
     * 开始点击导航
     */
    public void startWalkingRouteLine() {
        //行走索引初始为0
        mCurrentIndex = 0;
        setStartAnimationEnable(false);
        //缩放地图状态
        setZoomLevel();
        //开始进行模拟行走
        int groupId = getWillWalkingGroupId();
        setFocusGroupId(groupId);
    }

    /**
     * 设置缩放动画
     *
     * @return
     */
    protected void setZoomLevel() {
        if (mFmap.getZoomLevel() != MAP_NORMAL_LEVEL) {
            mFmap.setZoomLevel(MAP_NORMAL_LEVEL, true);
        }
    }

    /**
     * 获取即将行走的下一层groupId
     *
     * @return
     */
    protected int getWillWalkingGroupId() {
        if (mCurrentIndex > mNaviGroupIds.size() - 1) {
            return mFmap.getFocusGroupId();
        } else {
            return mNaviGroupIds.get(mCurrentIndex);
        }
    }

    /**
     * 切换楼层行走
     *
     * @param groupId 楼层id
     */
    protected void setFocusGroupId(int groupId) {
        if (groupId != mFmap.getFocusGroupId()) {
            mFmap.setFocusByGroupId(groupId, null);
            mHandler.sendEmptyMessage(WHAT_LOCATE_SWITCH_GROUP);
        }

        setupTargetLine(groupId);
    }

    /**
     * 开始模拟行走路线
     *
     * @param groupId 楼层id
     */
    protected void setupTargetLine(int groupId) {
        ArrayList<FMMapCoord> points = getWillWalkingPoints();
        mLocationAPI.setupTargetLine(points, groupId);
        mLocationAPI.start();
    }

    /**
     * 获取即将行走的下一层点集合
     *
     * @return
     */
    protected ArrayList<FMMapCoord> getWillWalkingPoints() {
        if (mCurrentIndex > mNaviGroupIds.size() - 1) {
            return null;
        }
        return mNaviPoints.get(mCurrentIndex++);
    }

    /**
     * 动画旋转
     */
    protected void animateRotate(final float angle) {
        if (Math.abs(mFmap.getRotateAngle() - angle) > 2) {
            mFmap.setRotateAngle(angle);
        }
    }

    /**
     * 移动至中心点,如果中心与屏幕中心点距离大于20米，将移动
     *
     * @param mapCoord 坐标
     */
    protected void moveToCenter(final FMMapCoord mapCoord) {
        FMMapCoord centerCoord = mFmap.getMapCenter();
        double length = FMMath.length(centerCoord, mapCoord);
        if (length > MAX_BETWEEN_LENGTH) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mFmap.moveToCenter(mapCoord, true);
                }
            });
        }
    }

    /**
     * 切换楼层显示
     */
    public abstract void updateLocateGroupView();
}
