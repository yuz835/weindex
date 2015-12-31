package ca.weindex.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.weindex.common.model.Blog;
import ca.weindex.search.helper.LabelHelper;

@Component
public class BlogIndexBuilder {
	@Autowired
	private LabelHelper labelHelper;

	public void addBlogIndex(Blog blog) throws Exception {
		Document doc = getBlogDocument(blog);
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		writer.addDocument(doc);
		writer.close();
		System.out.println("Build blog index finished: " + blog.getId());
	}

	public void updateBlogIndex(Blog blog) throws Exception {
		try {
			deleteBlogIndex(blog.getId());
		} catch (Exception e) {
			// do nothing
		}
		Term term = new Term("blogId", Integer.toString(blog.getId()));
		Document doc = getBlogDocument(blog);
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		writer.updateDocument(term, doc);
		writer.close();
	}

	public void deleteBlogIndex(int index) throws Exception {
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		Term term = new Term("blogId", Integer.toString(index));
		writer.deleteDocuments(term);
		writer.commit();
		writer.close();
		System.out.println("Delete blog index finished: " + index);
	}

	private Document getBlogDocument(Blog blog) {
		Field idField = new Field("blogId", Integer.toString(blog.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED);
		Field nameField = new Field("name", blog.getTitle(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
		Field contentField = new Field("content", blog.getContent() == null ? "" : blog.getContent(), Field.Store.YES,
				Field.Index.ANALYZED);
		Field authorField = new Field("author", blog.getAuthor() == null ? "" : blog.getAuthor(), Field.Store.YES,
				Field.Index.ANALYZED);
		
		Document doc = new Document();
		Field docTypeField = new Field("docType", "blog", Field.Store.YES, Field.Index.NOT_ANALYZED);
		doc.add(docTypeField);

		doc.add(idField);
		doc.add(nameField);
		doc.add(contentField);
		doc.add(authorField);
		
		String labelStr = blog.getLabel();
		Field labelField = labelHelper.getLabelField(labelStr);
		if (labelField != null) {
			doc.add(labelField);
		}

		
		return doc;
	}

	// public static void main(String[] args) throws Exception { // 操作增，删,改索引库的
	// IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE);
	// // 数据源的位置
	// File sourceFile = LuceneUtils.createSourceFile();
	// System.out.println("文件路径：" + sourceFile.getAbsolutePath());
	// // 进行写入文档
	// Document doc = new Document();
	// doc.add(new Field("name", sourceFile.getName(), Field.Store.YES,
	// Field.Index.ANALYZED_NO_NORMS));
	// // 文件路径
	// Field pathField = new Field("path", sourceFile.getPath(),
	// Field.Store.YES, Field.Index.NO);
	// pathField.setIndexOptions(org.apache.lucene.index.FieldInfo.IndexOptions.DOCS_ONLY);
	// doc.add(pathField);
	// // 文件最后修改时间
	// doc.add(new Field("modified", String.valueOf(sourceFile.lastModified()),
	// Field.Store.YES, Field.Index.NO));
	// // 添加文件内容
	// String content = LuceneUtils.readFileContext(sourceFile);
	// System.out.println("content: " + content);
	// doc.add(new Field("contents", content, Field.Store.YES,
	// Field.Index.ANALYZED));
	// // 以下是官网的实现
	// /*
	// * FileInputStream fis = new FileInputStream(sourceFile); doc.add(new
	// * Field("contents", new BufferedReader(new InputStreamReader(fis,
	// * "UTF-8"))));
	// */
	//
	// if (writer.getConfig().getOpenMode() ==
	// IndexWriterConfig.OpenMode.CREATE) {
	// writer.addDocument(doc);
	// } else {
	// writer.updateDocument(new Term("path", sourceFile.getPath()), doc);
	// }
	// // 释放资源
	// writer.close();
	// // fis.close();
	// }
}
