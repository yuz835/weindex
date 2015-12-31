package ca.weindex.search;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
  
public class LuceneOfferUtils {  
  
    //当前目录位置  
    public static final String USERDIR = System.getProperty("user.dir");  
    //存放索引的目录  
//    private static final String INDEXPATH = USERDIR + File.separator + "index";  
	private static final String INDEXPATH = "/home/weindex/index";
    //数据源  
    private static final String INDEXSOURCE = USERDIR + File.separator  
            + "source" + File.separator + "lucene.txt";  
    //使用版本  
    public static final Version version = Version.LUCENE_36;
      
    /** 
     * 获取分词器 
     * */  
    public static Analyzer getAnalyzer(){  
        // 分词器  
        Analyzer analyzer = new StandardAnalyzer(version);  
        return analyzer;  
    }  
  
    /** 
     * 创建一个索引器的操作类 
     *  
     * @param openMode 
     * @return 
     * @throws Exception 
     */  
    public static IndexWriter createIndexWriter(OpenMode openMode)  
            throws Exception {  
        // 索引存放位置设置  
        Directory dir = FSDirectory.open(new File(INDEXPATH));        
        // 索引配置类设置  
        IndexWriterConfig iwc = new IndexWriterConfig(version,  
                getAnalyzer());  
        iwc.setOpenMode(openMode);  
        IndexWriter writer = new IndexWriter(dir, iwc);  
        return writer;  
    }  
  
    /*** 
     * 创建一个搜索的索引器 
     * @throws IOException  
     * @throws CorruptIndexException  
     * */  
    public static IndexSearcher createIndexSearcher() throws CorruptIndexException, IOException {  
        IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEXPATH)));  
        IndexSearcher searcher = new IndexSearcher(reader);  
        return searcher;  
    }  
  
    /** 
     * 创建一个查询器 
     * @param queryFileds  在哪些字段上进行查询 
     * @param queryString  查询内容 
     * @return 
     * @throws ParseException 
     */  
    public static Query createQuery(String [] queryFileds,String queryString) throws ParseException{  
         QueryParser parser = new MultiFieldQueryParser(version, queryFileds, getAnalyzer());  
         Query query = parser.parse(queryString);  
         return query;  
    }  
      
    /*** 
     * 读取文件内容 
     * */  
    public static String readFileContext(File file){  
        try {  
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));  
            StringBuilder content = new StringBuilder();  
            for(String line = null; (line = br.readLine())!= null;){  
                content.append(line).append("\n");  
            }  
            return content.toString();  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
      
    }  
      
      
    public static void main(String[] args) {  
  
        System.out.println(Thread.currentThread().getContextClassLoader()  
                .getResource(""));  
        System.out.println(LuceneOfferUtils.class.getClassLoader().getResource(""));  
        System.out.println(ClassLoader.getSystemResource(""));  
        System.out.println(LuceneOfferUtils.class.getResource(""));  
        System.out.println(LuceneOfferUtils.class.getResource("/")); // Class文件所在路径  
        System.out.println(new File("/").getAbsolutePath());  
        System.out.println(System.getProperty("user.dir"));  
    }  
  
    /** 
     * 创建索引的数据源 
     *  
     * @return 
     */  
    public static File createSourceFile() {  
        File file = new File(INDEXSOURCE);  
        return file;  
    }  
}  