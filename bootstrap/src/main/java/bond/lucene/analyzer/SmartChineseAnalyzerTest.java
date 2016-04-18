//package bond.lucene.analyzer;  
//  
//import java.util.Iterator;  
//  
//import org.apache.lucene.analysis.TokenStream;  
//import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;  
//import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;  
//import org.apache.lucene.analysis.util.CharArraySet;  
//import org.apache.lucene.util.Version;  
//  
//public class SmartChineseAnalyzerTest {  
//  
//    public static void main(String[] args) {  
//        try {  
//            // 要处理的文本  
//            String text = "lucene分析器使用分词器和过滤器构成一个“管道”，文本在流经这个管道后成为可以进入索引的最小单位，因此，一个标准的分析器有两个部分组成，一个是分词器tokenizer,它用于将文本按照规则切分为一个个可以进入索引的最小单位。另外一个是TokenFilter，它主要作用是对切出来的词进行进一步的处理（如去掉敏感词、英文大小写转换、单复数处理）等。lucene中的Tokenstram方法首先创建一个tokenizer对象处理Reader对象中的流式文本，然后利用TokenFilter对输出流进行过滤处理";  
//  
//            // 自定义停用词  
//            String[] self_stop_words = { "的", "了", "呢", "，", "0", "：", ",", "是", "流" };  
//            CharArraySet cas = new CharArraySet(Version.LUCENE_44, 0, true);  
//            for (int i = 0; i < self_stop_words.length; i++) {  
//                cas.add(self_stop_words[i]);  
//            }  
//  
//            // 加入系统默认停用词  
//            Iterator<Object> itor = SmartChineseAnalyzer.getDefaultStopSet().iterator();  
//            while (itor.hasNext()) {  
//                cas.add(itor.next());  
//            }  
//  
//            // 中英文混合分词器(其他几个分词器对中文的分析都不行)  
//            SmartChineseAnalyzer sca = new SmartChineseAnalyzer(Version.LUCENE_44, cas);  
//  
//            TokenStream ts = sca.tokenStream("field", text);  
//            CharTermAttribute ch = ts.addAttribute(CharTermAttribute.class);  
//  
//            ts.reset();  
//            while (ts.incrementToken()) {  
//                System.out.println(ch.toString());  
//            }  
//            ts.end();  
//            ts.close();  
//        } catch (Exception ex) {  
//            ex.printStackTrace();  
//        }  
//    }  
//}