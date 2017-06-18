package com.lib.lapp.widget.utils;

import com.fengmap.android.analysis.search.FMSearchAnalyser;
import com.fengmap.android.analysis.search.FMSearchResult;
import com.fengmap.android.analysis.search.model.FMSearchModelByKeywordRequest;
import com.fengmap.android.analysis.search.model.FMSearchModelByTypeRequest;
import com.fengmap.android.map.FMMap;
import com.fengmap.android.map.marker.FMModel;
import com.lib.lapp.model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wxx
 * @Description 搜素分析
 */
public class AnalysisUtils {

    /**
     * 通过关键字查询模型
     *
     * @param searchAnalyser 搜素控制
     * @param map            地图
     * @param keyword        关键字
     */
    public static ArrayList<FMModel> queryModelByKeyword(FMMap map, FMSearchAnalyser searchAnalyser, String keyword) {
        int[] groupIds = map.getMapGroupIds();
        return queryModelByKeyword(map, groupIds, searchAnalyser, keyword);
    }

    /**
     * 通过关键字查询模型
     *
     * @param searchAnalyser 搜素控制
     * @param map            地图
     * @param keyword        关键字
     * @param groupIds       楼层集合
     */
    public static ArrayList<FMModel> queryModelByKeyword(FMMap map, int[] groupIds, FMSearchAnalyser searchAnalyser, String keyword) {
        ArrayList<FMModel> list = new ArrayList<FMModel>();
        FMSearchModelByKeywordRequest request = new FMSearchModelByKeywordRequest(groupIds, keyword);
        ArrayList<FMSearchResult> result = searchAnalyser.executeFMSearchRequest(request);
        for (FMSearchResult r : result) {
            String fid = (String) r.get("FID");
            FMModel model = map.getFMLayerProxy().queryFMModelByFid(fid);
            list.add(model);
        }
        return list;
    }


    /**
     * 通过类型查询模型
     *
     * @param searchAnalyser 搜素控制
     * @param map            地图
     * @param type           类型
     */
    public static ArrayList<FMModel> queryModelByType(FMMap map, FMSearchAnalyser searchAnalyser, int type) {
        int[] groupIds = map.getMapGroupIds();
        return queryModelByType(map, groupIds, searchAnalyser, type);
    }

    /**
     * 通过类型查询模型
     *
     * @param searchAnalyser 搜素控制
     * @param map            地图
     * @param type           类型
     * @param groupIds       楼层集合
     */
    public static ArrayList<FMModel> queryModelByType(FMMap map, int[] groupIds, FMSearchAnalyser searchAnalyser, int type) {
        ArrayList<FMModel> list = new ArrayList<FMModel>();
        FMSearchModelByTypeRequest request = new FMSearchModelByTypeRequest(groupIds, type);
        ArrayList<FMSearchResult> result = searchAnalyser.executeFMSearchRequest(request);
        for (FMSearchResult r : result) {
            String fid = (String) r.get("FID");
            FMModel model = map.getFMLayerProxy().queryFMModelByFid(fid);
            list.add(model);
        }
        return list;
    }

    /**
     * 关键字查询
     *
     * @param datas   查询的数据集
     * @param keyWord 关键字
     * @return 匹配结果集
     */
    public static ArrayList<String> queryDataByKeyword(ArrayList<String> datas, String keyWord) {
        ArrayList<String> list = new ArrayList<String>();
        for (String r : datas) {
            if (r.toLowerCase().contains(keyWord.toLowerCase())) {
                list.add(r);
            }
        }
        return list;
    }

    /**
     * 通过关键字查询图书
     *
     * @param keyword 关键字
     * @return
     */
    public static ArrayList<Book> queryBookByKeyword(List<Book> books, String keyword) {
        ArrayList<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.BOOKNAME.toLowerCase().contains(keyword) || book.BOOKTYPE.toLowerCase().contains(keyword)) {
                result.add(book);
            }
        }
        return result;
    }
}
