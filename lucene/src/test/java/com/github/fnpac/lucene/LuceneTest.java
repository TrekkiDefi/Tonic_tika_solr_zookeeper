package com.github.fnpac.lucene;

import org.junit.Test;

/**
 * Created by 刘春龙 on 2017/8/15.
 */
public class LuceneTest {

    @Test
    public void testIndex() {
        Lucene.index();
    }

    @Test
    public void testQuery() {
        Lucene.query("content", "lucene");
    }

    @Test
    public void testTermQuery() {
        Lucene.termQuery("content", "lucene");
    }

    @Test
    public void testNumericRangeQuery() {
        Lucene.numericRangeQuery("filesize", 0L, 1100L);
    }

    @Test
    public void testBooleanQuery() {
        Lucene.booleanQuery("content", "lucene", "filesize", 0L, 1100L);
    }

    @Test
    public void testMatchAllDocsQuery() {
        Lucene.matchAllDocsQuery();
    }

    @Test
    public void testMultiFieldQueryParser() {
        Lucene.multiFieldQueryParser("lucene", "filename", "content");
    }
}
