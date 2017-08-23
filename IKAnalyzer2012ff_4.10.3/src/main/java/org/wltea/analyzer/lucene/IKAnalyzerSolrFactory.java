package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.io.Reader;
import java.util.Map;

//lucene:4.8之前的版本
//import org.apache.lucene.util.AttributeSource.AttributeFactory;

/**
 * Created by 刘春龙 on 2017/8/18.
 */
public class IKAnalyzerSolrFactory extends TokenizerFactory {

    private boolean useSmart;

    public boolean useSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public IKAnalyzerSolrFactory(Map<String, String> args) {
        super(args);
        assureMatchVersion();
        this.setUseSmart(args.get("useSmart").toString().equals("true"));
    }


    @Override
    public Tokenizer create(AttributeFactory factory, Reader input) {
        Tokenizer _IKTokenizer = new IKTokenizer(input, this.useSmart);
        return _IKTokenizer;
    }
}