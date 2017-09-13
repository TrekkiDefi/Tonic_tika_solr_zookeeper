# Tonic_tika_solr_zookeeper

tika solr zookeeper学习与实践

## IK中文分词

IKAnalyzer2012ff_6.6.0支持英文和数字单字分词。

用法：

```text
<!-- IKAnalyzer Field-->
<field name="ik_filename" type="IK_cnAnalyzer" indexed="true" stored="true"  multiValued="true"/>
<field name="ik_mail_fromaddress" type="IK_cnAnalyzer" indexed="true" stored="true" multiValued="true"/>
<field name="ik_keywords" type="IK_cnAnalyzer" indexed="true" stored="false" multiValued="true"/>
<field name="ik_content" type="IK_cnAnalyzer" indexed="true" stored="true" multiValued="true"/>
<field name="ik_mail_subject" type="IK_cnAnalyzer" indexed="true" stored="true"/>


<!-- IKAnalyzer -->
<fieldType name="IK_cnAnalyzer" class="solr.TextField" positionIncrementGap="100">
    <analyzer type="index">
        <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false"/>
        <filter class="org.wltea.analyzer.lucene.IKTokenFilterFactory" useSingle="true" useItself="false" />
    </analyzer>
    <analyzer type="query">
        <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false"/>
        <filter class="org.wltea.analyzer.lucene.IKTokenFilterFactory" useSingle="true" useItself="false" />
    </analyzer>
</fieldType>

<copyField source="ik_content" dest="ik_keywords"/>
<copyField source="ik_filename" dest="ik_keywords"/>
<copyField source="ik_mail_fromaddress" dest="ik_keywords"/>
<copyField source="ik_mail_subject" dest="ik_keywords"/>
```

解释：

- useSingle;// 是否对英文和数字单字分词
- useItself;// 是否保留英文和数字原语汇单元

比如：123456

useSingle为false，分词结果为：

```text
123456
```

useSingle为true，分词结果为：

```text
123456
1
2
3
4
5
6
```

useItself为false，分词结果为：

```text
1
2
3
4
5
6
```