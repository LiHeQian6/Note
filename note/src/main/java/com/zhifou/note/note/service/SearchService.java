package com.zhifou.note.note.service;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhifou.note.bean.Constant;
import com.zhifou.note.bean.NoteVO;
import com.zhifou.note.note.entity.Note;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author : li
 * @Date: 2021-05-05 08:02
 */
@Service
@Transactional
public class SearchService implements Constant {
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private NoteService noteService;

    public boolean importMYSQLData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<NoteVO> notes = noteService.getAllNotes();
        BulkRequest request = new BulkRequest(INDEX);
        request.timeout("2m");
        for (NoteVO note : notes) {
            request.add(new IndexRequest().source(mapper.writeValueAsString(note), XContentType.JSON).id(String.valueOf(note.getId())));
        }
        return !restHighLevelClient.bulk(request, RequestOptions.DEFAULT).hasFailures();

    }

    public Page<NoteVO> searchNote(String keyword,int page,int size) throws IOException {
        PageRequest pageRequest = PageRequest.of(page, size);
        ArrayList<NoteVO> notes = new ArrayList<>();
        SearchRequest request = new SearchRequest(INDEX);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.multiMatchQuery(keyword, "title").boost(3))
                .must(QueryBuilders.multiMatchQuery(keyword, "content","type.name","tags.name").boost(2));
        sourceBuilder.query(queryBuilder);

        sourceBuilder.timeout(TimeValue.MINUS_ONE);
        sourceBuilder.from(page*size);
        sourceBuilder.size(size);

        List<String> highlightFields = new ArrayList<>();
        highlightFields.add("title");
        highlightFields.add("content");
        sourceBuilder.highlighter(new HighlightBuilder()
                .field(highlightFields.get(0))
                .field(highlightFields.get(1))
                .preTags(HIGH_LIGHT_START_TAG)
                .postTags(HIGH_LIGHT_END_TAG));
        request.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
        for (SearchHit hit : response.getHits().getHits()) {
            Map<String, Object> map = hit.getSourceAsMap();
            Map<String, HighlightField> hitHighlightFields = hit.getHighlightFields();
            HighlightField title = hitHighlightFields.get("title");
            HighlightField content = hitHighlightFields.get("content");
            if (title!=null){
                Text[] fragments = title.fragments();
                StringBuilder n_title = new StringBuilder();
                for (Text text : fragments) {
                    n_title.append(text);
                }
                map.put("title",n_title);
            }
            if (content!=null){
                Text[] fragments = content.fragments();
                StringBuilder n_content = new StringBuilder();
                for (Text text : fragments) {
                    n_content.append(text);
                }
                map.put("content",n_content);
            }
            Note note = BeanUtil.mapToBean(map, Note.class, true);
            notes.add(noteService.getNoteData(note));
        }

        CountRequest countRequest = new CountRequest();
        countRequest.query(queryBuilder);
        CountResponse countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        long count = countResponse.getCount();


        return new PageImpl<>(notes,pageRequest,count);
    }







}
