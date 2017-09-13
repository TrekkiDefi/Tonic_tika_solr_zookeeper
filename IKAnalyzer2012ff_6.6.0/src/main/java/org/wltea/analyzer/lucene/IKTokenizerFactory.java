package org.wltea.analyzer.lucene;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.TokenizerFactory;
import org.apache.lucene.util.AttributeFactory;

import java.util.Map;

/**
 * Created by 刘春龙 on 2017/8/30.
 * <p>
 * IK分词器工厂类。用于配置文件中分析器添加分词器(必须工厂类)。
 */
public class IKTokenizerFactory extends TokenizerFactory {

    private boolean useSmart;
    private boolean useSingle;
    private boolean useItself;

    /**
     * 从{@code managed-schema}传递的值中。设置 useSmart 的值
     * @param args
     */
    public IKTokenizerFactory(Map<String, String> args) {
        super(args);
        /*
         * 判断Map容器中是否存在useSmart，如果有获取该key对应的value。
         * 如果没有,则设置默认值，也就是第三个参数 false
         */
        useSmart = this.getBoolean(args, "useSmart", false);// 执行完，useSmart会被从map移除

        /*
         * 判断Map容器中是否存在useSingle，如果有获取该key对应的value。
         * 如果没有,则设置默认值，也就是第三个参数 false
         */
        useSingle = this.getBoolean(args, "useSingle", false);// IKTokenFilter不针对英文和数字做单字切分

        useItself = this.getBoolean(args, "useItself", true);// IKTokenFilter保留IKTokenizer输出的英文和数字原语汇单元

        if (!args.isEmpty()) {
            throw new IllegalArgumentException("Unknown parameters: " + args);
        }
    }

    @Override
    public Tokenizer create(AttributeFactory factory) {
        return new IKTokenizer(factory, useSmart);
    }
}
