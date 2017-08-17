package com.github.fnpac.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by 刘春龙 on 2017/8/15.
 */
public class Lucene {

    /**
     * 创建索引
     */
    public static void index() {

        IndexWriter writer = null;
        try {
            // 1. 创建Directory
//        Directory directory = new RAMDirectory();// 创建在内存中
            Directory directory = FSDirectory.open(new File("index"));// 创建到硬盘上

            // 2. 创建IndexWriter
            Analyzer analyzer = new StandardAnalyzer();// 标准分词器，对英文分词效果较好，中文单字分词
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
            writer = new IndexWriter(directory, config);

            File f = new File("files");
            for (File file : f.listFiles()) {
                // 3. 创建Document对象
                Document doc = new Document();
                // 4. 为Document添加Field
                doc.add(new TextField("content", new FileReader(file)));
                doc.add(new TextField("filename", file.getName(), Field.Store.YES));
                doc.add(new LongField("filesize", FileUtils.sizeOf(file), Field.Store.YES));
                // 5. 通过IndexWriter添加文档到索引中
                writer.addDocument(doc);
            }
            // 提交
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索
     *
     * @param f 查询字词的字段
     * @param q 要解析的查询字符串
     */
    public static void query(String f, String q) {
        IndexReader reader = null;

        try {

            // 1. 创建Directory
            Directory directory = FSDirectory.open(new File("index"));// 创建到硬盘上

            // 2. 创建IndexReader
            reader = IndexReader.open(directory);

            // 3. 根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4. 创建搜索的Query
            Analyzer analyzer = new StandardAnalyzer();
            QueryParser parser = new QueryParser(f, analyzer);
            Query query = parser.parse(q);

            // 5. 根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6. 根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7. 根据searcher和ScoreDoc对象获取具体的Document对象
//                Document doc = searcher.doc(sd.doc);
                Document doc = reader.document(sd.doc);
                // 8. 根据Document对象获取需要的值
                System.out.println(doc.get("filename"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索
     *
     * @param f 查询字词的字段
     * @param q 要解析的查询字符串
     */
    public static void termQuery(String f, String q) {
        IndexReader reader = null;

        try {

            // 1. 创建Directory
            Directory directory = FSDirectory.open(new File("index"));// 创建到硬盘上

            // 2. 创建IndexReader
            reader = IndexReader.open(directory);

            // 3. 根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4. 创建搜索的Query
            Term term = new Term(f, q);
            TermQuery query = new TermQuery(term);

            // 5. 根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6. 根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7. 根据searcher和ScoreDoc对象获取具体的Document对象
//                Document doc = searcher.doc(sd.doc);
                Document doc = reader.document(sd.doc);
                // 8. 根据Document对象获取需要的值
                System.out.println(doc.get("filename"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 数值范围搜索
     *
     * @param f   查询字词的字段
     * @param min 范围最小值，字节为单位
     * @param max 范围最大值，字节为单位
     */
    public static void numericRangeQuery(String f, Long min, Long max) {
        IndexReader reader = null;

        try {

            // 1. 创建Directory
            Directory directory = FSDirectory.open(new File("index"));// 创建到硬盘上

            // 2. 创建IndexReader
            reader = IndexReader.open(directory);

            // 3. 根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4. 创建搜索的Query
            Query query = NumericRangeQuery.newLongRange(f, min, max, true, true);

            // 5. 根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6. 根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7. 根据searcher和ScoreDoc对象获取具体的Document对象
//                Document doc = searcher.doc(sd.doc);
                Document doc = reader.document(sd.doc);
                // 8. 根据Document对象获取需要的值
                System.out.println("filename - " + doc.get("filename"));
                System.out.println("filesize - " + doc.get("filesize"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 组合搜索
     *
     * @param f1  查询字词的字段
     * @param q   要解析的查询字符串
     * @param f2  查询字词的字段
     * @param min 范围最小值，字节为单位
     * @param max 范围最大值，字节为单位
     */
    public static void booleanQuery(String f1, String q, String f2, Long min, Long max) {
        IndexReader reader = null;

        try {

            // 1. 创建Directory
            Directory directory = FSDirectory.open(new File("index"));// 创建到硬盘上

            // 2. 创建IndexReader
            reader = IndexReader.open(directory);

            // 3. 根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4. 创建搜索的Query
            BooleanQuery query = new BooleanQuery();

            Term term = new Term(f1, q);
            TermQuery termQuery = new TermQuery(term);
            NumericRangeQuery numericQuery = NumericRangeQuery.newLongRange(f2, min, max, true, true);
            query.add(termQuery, BooleanClause.Occur.MUST);
            query.add(numericQuery, BooleanClause.Occur.MUST);

            // 5. 根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6. 根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7. 根据searcher和ScoreDoc对象获取具体的Document对象
//                Document doc = searcher.doc(sd.doc);
                Document doc = reader.document(sd.doc);
                // 8. 根据Document对象获取需要的值
                System.out.println("filename - " + doc.get("filename"));
                System.out.println("filesize - " + doc.get("filesize"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 搜索全部
     */
    public static void matchAllDocsQuery() {
        IndexReader reader = null;

        try {

            // 1. 创建Directory
            Directory directory = FSDirectory.open(new File("index"));// 创建到硬盘上

            // 2. 创建IndexReader
            reader = IndexReader.open(directory);

            // 3. 根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4. 创建搜索的Query
            MatchAllDocsQuery query = new MatchAllDocsQuery();

            // 5. 根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6. 根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7. 根据searcher和ScoreDoc对象获取具体的Document对象
//                Document doc = searcher.doc(sd.doc);
                Document doc = reader.document(sd.doc);
                // 8. 根据Document对象获取需要的值
                System.out.println("filename - " + doc.get("filename"));
                System.out.println("filesize - " + doc.get("filesize"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 多字段搜索
     */
    public static void multiFieldQueryParser(String q, String ... f) {
        IndexReader reader = null;

        try {

            // 1. 创建Directory
            Directory directory = FSDirectory.open(new File("index"));// 创建到硬盘上

            // 2. 创建IndexReader
            reader = IndexReader.open(directory);

            // 3. 根据IndexReader创建IndexSearcher
            IndexSearcher searcher = new IndexSearcher(reader);

            // 4. 创建搜索的Query
            Analyzer analyzer = new StandardAnalyzer();
            MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(f, analyzer);
            Query query = multiFieldQueryParser.parse(q);

            // 5. 根据searcher搜索并且返回TopDocs
            TopDocs tds = searcher.search(query, 10);

            // 6. 根据TopDocs获取ScoreDoc对象
            ScoreDoc[] sds = tds.scoreDocs;
            for (ScoreDoc sd : sds) {
                // 7. 根据searcher和ScoreDoc对象获取具体的Document对象
//                Document doc = searcher.doc(sd.doc);
                Document doc = reader.document(sd.doc);
                // 8. 根据Document对象获取需要的值
                System.out.println("filename - " + doc.get("filename"));
                System.out.println("filesize - " + doc.get("filesize"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
