package com.msop.es.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.msop.core.common.jackson.JsonUtil;
import com.msop.core.common.entity.PageResult;
import com.msop.core.common.utils.Func;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.UncategorizedElasticsearchException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * ES查询构建
 *
 * @author ruozhuliufeng
 */
@Getter
@Setter
public class SearchBuilder {

    /**
     * 高亮前缀
     */
    private static final String HIGHLIGHTER_PRE_TAGS = "<mark>";

    /**
     * 高亮后缀
     */
    private static final String HIGHLIGHTER_POST_TAGS = "</mark>";

    private SearchRequest searchRequest;

    private SearchSourceBuilder searchBuilder;

    private RestHighLevelClient client;

    private SearchBuilder(SearchRequest searchRequest, SearchSourceBuilder searchBuilder,
                          RestHighLevelClient client) {
        this.searchRequest = searchRequest;
        this.searchBuilder = searchBuilder;
        this.client = client;
    }

    /**
     * 生成SearchBuilder
     *
     * @param client    client
     * @param indexName indexName
     * @return SearchBuilder实例
     */
    public static SearchBuilder builder(RestHighLevelClient client, String indexName) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        return new SearchBuilder(searchRequest, searchSourceBuilder, client);
    }

    /**
     * 生成SearchBuilder实例
     *
     * @param client client
     * @return SearchBuilder实例
     */
    public static SearchBuilder builder(RestHighLevelClient client) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        return new SearchBuilder(searchRequest, searchSourceBuilder, client);
    }

    /**
     * 设置索引名
     *
     * @param indices 索引名数组
     * @return SearchBuilder
     */
    public SearchBuilder setIndices(String... indices) {
        if (Func.isNotEmpty(indices)) {
            searchRequest.indices(indices);
        }
        return this;
    }

    /**
     * 生成QueryStringQuery查询
     *
     * @param queryStr 查询关键字
     * @return SearchBuilder
     */
    public SearchBuilder setStringQuery(String queryStr) {
        QueryBuilder queryBuilder;
        if (Func.isNotEmpty(queryStr)) {
            queryBuilder = QueryBuilders.queryStringQuery(queryStr);
        } else {
            queryBuilder = QueryBuilders.matchAllQuery();
        }
        searchBuilder.query(queryBuilder);
        return this;
    }

    /**
     * 设置分页
     *
     * @param page  当前页数
     * @param limit 每页显示条数
     * @return SearchBuilder
     */
    public SearchBuilder setPage(Integer page, Integer limit) {
        setPage(page, limit, false);
        return this;
    }

    /**
     * 设置分页
     *
     * @param page           当前页数
     * @param limit          每页显示数
     * @param trackTotalHits 分页总数是否显示所有条数，默认只显示10000
     * @return SearchBuilder
     */
    public SearchBuilder setPage(Integer page, Integer limit, boolean trackTotalHits) {
        if (page != null && limit != null) {
            searchBuilder.from((page - 1) * limit)
                    .size(limit);
            if (trackTotalHits) {
                searchBuilder.trackTotalHits(trackTotalHits);
            }
        }
        return this;
    }

    /**
     * 增加排序
     *
     * @param field 排序字段
     * @param order 顺序方向
     * @return SearchBuilder
     */
    public SearchBuilder addSort(String field, SortOrder order) {
        if (Func.isNotEmpty(field) && order != null) {
            searchBuilder.sort(field, order);
        }
        return this;
    }

    /**
     * 设置是否需要高亮处理
     *
     * @param isHighlight 是否需要高亮处理
     * @return SearchBuilder
     */
    public SearchBuilder setIsHighlight(Boolean isHighlight) {
        if (isHighlight) {
            this.setHighlight("*",
                    HIGHLIGHTER_PRE_TAGS, HIGHLIGHTER_POST_TAGS);
        }
        return this;
    }

    /**
     * 设置高亮
     *
     * @param field    区域
     * @param preTags  高亮处理前缀
     * @param postTags 高亮处理后缀
     * @return SearchBuilder
     */
    public SearchBuilder setHighlight(String field, String preTags, String postTags) {
        if (Func.isNotEmpty(field) && Func.isNotEmpty(preTags) && Func.isNotEmpty(postTags)) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field(field)
                    .preTags(preTags)
                    .preTags(postTags);
            searchBuilder.highlighter(highlightBuilder);
        }
        return this;
    }

    /**
     * 设置查询路由
     *
     * @param routing 路由数组
     * @return SearchBuilder
     */
    public SearchBuilder setRouting(String... routing) {
        if (Func.isNotEmpty(routing)) {
            searchRequest.routing(routing);
        }
        return this;
    }

    /**
     * 查询返回结果
     *
     * @return SearchResponse
     * @throws IOException 异常
     */
    public SearchResponse get() throws IOException {
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 返回列表结果list
     *
     * @return 列表
     * @throws IOException 异常
     */
    public List<JsonNode> getList() throws IOException {
        return getList(this.get().getHits());
    }

    /**
     * 返回JSON列表数据
     *
     * @param searchHits 搜索命中
     * @return JSON列表数据
     */
    public List<JsonNode> getList(SearchHits searchHits) {
        List<JsonNode> list = new ArrayList<>();
        if (searchHits != null) {
            searchHits.forEach(item -> {
                JsonNode jsonNode = JsonUtil.parse(item.getSourceAsString());
                ObjectNode objectNode = (ObjectNode) jsonNode;
                objectNode.put("id", item.getId());
                Map<String, HighlightField> highlightFields = item.getHighlightFields();
                if (highlightFields != null) {
                    populateHighLightedFields(objectNode, highlightFields);
                }
                list.add(objectNode);
            });
        }

        return list;
    }

    /**
     * 组装高亮字符串
     *
     * @param result          目标对象
     * @param highlightFields 高亮配置
     * @param <T>             T
     */
    private <T> void populateHighLightedFields(T result, Map<String, HighlightField> highlightFields) {
        for (HighlightField field : highlightFields.values()) {
            try {
                String name = field.getName();
                if (!name.endsWith(".keyword")) {
                    if (result instanceof ObjectNode) {
                        ((ObjectNode) result).put(field.name(), concat(field.fragments()));
                    } else {
                        PropertyUtils.setProperty(result, field.name(), concat(field.getFragments()));
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new UncategorizedElasticsearchException("无法为字段组装高亮区域,字段名：" + field.getName()
                        + "组装失败的值：" + Arrays.toString(field.getFragments()), e);
            }
        }
    }

    /**
     * 返回分页结果
     *
     * @return PageResult
     * @throws IOException 异常
     */
    public PageResult<JsonNode> getPage() throws IOException {
        return this.getPage(null, null);
    }

    /**
     * 返回分页结果
     *
     * @param page  当前页数
     * @param limit 每页显示条数
     * @return 分页结果
     * @throws IOException 异常
     */
    public PageResult<JsonNode> getPage(Integer page, Integer limit) throws IOException {
        this.setPage(page, limit);
        SearchResponse response = this.get();
        SearchHits searchHits = response.getHits();
        long totalCnt = searchHits.getTotalHits().value;
        List<JsonNode> list = getList(searchHits);
        return PageResult.<JsonNode>builder().data(list).code(0).count(totalCnt).build();
    }

    /**
     * 拼凑数组为字符串
     *
     * @param texts 文本术中
     * @return 组装后的字符串
     */
    private String concat(Text[] texts) {
        StringBuffer sb = new StringBuffer();
        for (Text text : texts) {
            sb.append(text.toString());
        }
        return sb.toString();
    }
}
