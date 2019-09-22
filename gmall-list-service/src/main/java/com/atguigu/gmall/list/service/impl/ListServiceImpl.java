package com.atguigu.gmall.list.service.impl;

import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParams;
import com.atguigu.gmall.bean.SkuLsResult;
import com.atguigu.gmall.service.ListService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListServiceImpl implements ListService {

    @Autowired
    private JestClient jestClient;

    public static final String ES_INDEX = "gmall";
    public static final String ES_TYPE = "SkuInfo";

    @Override
    public void saveSkuInfo(SkuLsInfo skuLsInfo) throws IOException {
        Index index = new Index.Builder(skuLsInfo).index(ES_INDEX).type(ES_TYPE).id(skuLsInfo.getId()).build();

        jestClient.execute(index);
    }

    //根据前台输入的信息查询es
    @Override
    public SkuLsResult search(SkuLsParams skuLsParams) throws IOException {
        //编写dsl语句
        String query = makeQueryStringForSearch(skuLsParams);

        //执行
        Search search = new Search.Builder(query).addIndex(ES_INDEX).addType(ES_TYPE).build();
        SearchResult execute = jestClient.execute(search);
        SkuLsResult skuLsResult = makeResultForSearch(skuLsParams,execute);
        return skuLsResult;
    }

    private SkuLsResult makeResultForSearch(SkuLsParams skuLsParams, SearchResult searchResult) {
        SkuLsResult skuLsResult = new SkuLsResult();

        ArrayList<SkuLsInfo> arrList = new ArrayList<SkuLsInfo>();
        List<SearchResult.Hit<SkuLsInfo, Void>> hits = searchResult.getHits(SkuLsInfo.class);
        for (SearchResult.Hit<SkuLsInfo, Void> hit : hits) {
            SkuLsInfo skuLsInfo = hit.source;
            //高亮
            if(hit.highlight != null && hit.highlight.size() > 0){
                Map<String, List<String>> highlight = hit.highlight;
                List<String> skuName = highlight.get("skuName");
                String skuNameHi = skuName.get(0);
                //替换
                skuLsInfo.setSkuName(skuNameHi);
            }

            arrList.add(skuLsInfo);
        }
        skuLsResult.setSkuLsInfoList(arrList);
        //获取total
        skuLsResult.setTotal(searchResult.getTotal());
        //设置总页数
        long page = (searchResult.getTotal() + skuLsParams.getPageSize() -1) / skuLsParams.getPageSize();
        skuLsResult.setTotalPages(page);
        ArrayList<String> stringArrayList = new ArrayList<String>();
        //平台属性值得id searchResult聚合
        MetricAggregation aggregations = searchResult.getAggregations();
        TermsAggregation groupby_attr = aggregations.getTermsAggregation("groupby_attr");
        List<TermsAggregation.Entry> buckets = groupby_attr.getBuckets();
        if(buckets != null && buckets.size() > 0){
            for (TermsAggregation.Entry bucket : buckets) {
                String key = bucket.getKey();
                stringArrayList.add(key);
            }
        }
        skuLsResult.setAttrValueIdList(stringArrayList);

        return skuLsResult;
    }

    private String makeQueryStringForSearch(SkuLsParams skuLsParams) {
        //创建查询对象，构建查询器
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //创建bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //keyword
        if(skuLsParams.getKeyword() != null && skuLsParams.getKeyword().length()> 0){
            //match
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName",skuLsParams.getKeyword());
            //将match装入must
            boolQueryBuilder.must(matchQueryBuilder);

            //设置高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            //fields
            highlightBuilder.field("skuNmae");
            //设置高亮的格式
            highlightBuilder.postTags("</span>");
            highlightBuilder.preTags("<span style='color:red'>");
            //将高亮放入查询器中
            searchSourceBuilder.highlight(highlightBuilder);
        }
        //cataLogId3
        if(skuLsParams.getCatalog3Id() != null && skuLsParams.getCatalog3Id().length() > 0){
          TermQueryBuilder termQueryBuilder = new TermQueryBuilder("catalog3Id",skuLsParams.getCatalog3Id());
          boolQueryBuilder.filter(termQueryBuilder);
        }

        //平台属性值id
        if(skuLsParams.getValueId() != null && skuLsParams.getValueId().length > 0){
            //循环将平台属性值id放入term
            String valueId = "";
            for (int i = 0; i < skuLsParams.getValueId().length; i++) {
                 valueId = skuLsParams.getValueId()[i];
                 TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", valueId);
                 boolQueryBuilder.filter(termQueryBuilder);

            }
        }
        searchSourceBuilder.query(boolQueryBuilder);

        //设置分页
        int from = (skuLsParams.getPageNo()-1) * skuLsParams.getPageSize();
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(skuLsParams.getPageSize());

        //设置排序
        searchSourceBuilder.sort("hotScore", SortOrder.DESC);

        //设置聚合
        TermsBuilder groupby_attr = AggregationBuilders.terms("groupby_attr").field("skuAttrValueList.valueId");
        searchSourceBuilder.aggregation(groupby_attr);
        String query = searchSourceBuilder.toString();
        System.out.println(query);
        return query;
    }
}
