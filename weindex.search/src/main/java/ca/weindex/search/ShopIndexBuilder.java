package ca.weindex.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.weindex.common.model.Shop;
import ca.weindex.search.helper.LabelHelper;

@Component
public class ShopIndexBuilder {
	@Autowired
	private LabelHelper labelHelper;

	public void addShopIndex(Shop shop) throws Exception {
		Document doc = getShopDocument(shop);
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		writer.addDocument(doc);
		writer.close();
		System.out.println("Build shop index finished: " + shop.getId());
	}

	public void updateShopIndex(Shop shop) throws Exception {
		try {
			this.deleteShopIndex(shop.getId());
		} catch (Exception e) {
			// do nothing
			e.printStackTrace();
		}
		Term term = new Term("shopId", Integer.toString(shop.getId()));
		Document doc = getShopDocument(shop);
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		writer.updateDocument(term, doc);
		writer.close();
	}

	public void deleteShopIndex(int index) throws Exception {
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		Term term = new Term("shopId", Integer.toString(index));
		writer.deleteDocuments(term);
		writer.commit();
		writer.close();
		System.out.println("Delete shop index finished: " + index);
	}

	private Document getShopDocument(Shop shop) {
		Document doc = new Document();
		Field docTypeField = new Field("docType", "shop", Field.Store.YES, Field.Index.NOT_ANALYZED);
		doc.add(docTypeField);

		Field idField = new Field("shopId", Integer.toString(shop.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED);
		Field nameField = new Field("name", shop.getName(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
		Field dispNameField = new Field("dispName", shop.getDisplayName() == null ? "" : shop.getDisplayName(),
				Field.Store.YES, Field.Index.ANALYZED);
		Field descField = new Field("desc", shop.getDesc() == null ? "" : shop.getDesc(), Field.Store.YES,
				Field.Index.ANALYZED);

		doc.add(idField);
		doc.add(nameField);
		doc.add(dispNameField);
		doc.add(descField);
		String logo = shop.getLogoUrl();
		if (logo != null && logo.length() > 0) {
			Field logoField = new Field("logoUrl", logo, Field.Store.YES, Field.Index.NO);
			doc.add(logoField);
		}
		
		String labelStr = shop.getLabel();
		Field labelField = labelHelper.getLabelField(labelStr);
		if (labelField != null) {
			doc.add(labelField);
		}

		String address = shop.getAddress() + ", " + shop.getCity() + ", " + shop.getCountry();
		Field addressField = new Field("address", address, Field.Store.YES, Field.Index.ANALYZED);
		Field zipField = new Field("zip", shop.getZip() == null ? "" : shop.getZip(), Field.Store.YES,
				Field.Index.ANALYZED_NO_NORMS);
		doc.add(addressField);
		doc.add(zipField);

		if (shop.getLongi() != null && shop.getLati() != null) {
			NumericField longiField = new NumericField("longi", Field.Store.YES, true);
			longiField.setIntValue(shop.getLongi());
			NumericField latiField = new NumericField("lati", Field.Store.YES, true);
			latiField.setIntValue(shop.getLati());

			doc.add(longiField);
			doc.add(latiField);
			// System.out.println(shop.getLongi() + "--------" + shop.getLati());
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
