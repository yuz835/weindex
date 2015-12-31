package ca.weindex.search;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Shop;
import ca.weindex.search.helper.LabelHelper;

@Component
public class OfferIndexBuilder {
	@Autowired
	private LabelHelper labelHelper;

	public void addOfferIndex(Offer offer, Shop shop) throws Exception {
		Document doc = getOfferDocument(offer, shop);
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		writer.addDocument(doc);
		writer.close();
		System.out.println("Build Offer index finished: " + offer.getId());
	}

	public void updateOfferIndex(Offer offer, Shop shop) throws Exception {
		try {
			deleteOfferIndex(offer.getId());
		} catch (Exception e) {
			// do nothing
		}
		Term term = new Term("offerId", Integer.toString(offer.getId()));
		Document doc = getOfferDocument(offer, shop);
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		writer.updateDocument(term, doc);
		writer.close();
	}

	public void deleteOfferIndex(int index) throws Exception {
		IndexWriter writer = LuceneUtils.createIndexWriter(OpenMode.CREATE_OR_APPEND);
		Term term = new Term("offerId", Integer.toString(index));
		writer.deleteDocuments(term);
		writer.commit();
		writer.close();
		System.out.println("Delete Offer index finished: " + index);
	}

	private Document getOfferDocument(Offer offer, Shop shop) {
		Document doc = new Document();
		Field docTypeField = new Field("docType", "offer", Field.Store.YES, Field.Index.NOT_ANALYZED);
		doc.add(docTypeField);

		Field idField = new Field("offerId", Integer.toString(offer.getId()), Field.Store.YES, Field.Index.NOT_ANALYZED);
		Field nameField = new Field("name", offer.getName(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
		String desc = offer.getDesc();
		if (desc == null) {
			desc = "";
		}
		Field descField = new Field("desc", desc, Field.Store.YES, Field.Index.ANALYZED);

		doc.add(idField);
		doc.add(nameField);
		doc.add(descField);

		String labelStr = offer.getLabel();
		Field labelField = labelHelper.getLabelField(labelStr);
		if (labelField != null) {
			doc.add(labelField);
		}
		// Field labelField = new Field("label", offer.getLabel(),
		// Field.Store.YES, Field.Index.ANALYZED);
		String price = offer.getPrice();
		if (price == null) {
			price = "";
		}

		Field priceField = new Field("price", price, Field.Store.YES, Field.Index.NO);
		String address = shop.getAddress() + ", " + shop.getCity() + ", " + shop.getCountry();
		Field addressField = new Field("address", address, Field.Store.YES, Field.Index.ANALYZED);
		String zip = shop.getZip();
		if (zip == null) {
			zip = "";
		}
		Field zipField = new Field("zip", zip, Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);

		// doc.add(labelField);
		doc.add(priceField);
		doc.add(addressField);
		doc.add(zipField);
		try {
			NumericField longiField = new NumericField("longi", Field.Store.YES, true);
			longiField.setIntValue(shop.getLongi());
			NumericField latiField = new NumericField("lati", Field.Store.YES, true);
			latiField.setIntValue(shop.getLati());
			doc.add(longiField);
			doc.add(latiField);
		} catch (Exception e) {
			// do nothing
		}

		
		if (offer.getImgUrl() != null && offer.getImgUrl().length() > 0) {
			Field imgField = new Field("imgUrl", offer.getImgUrl(), Field.Store.YES, Field.Index.NO);
			doc.add(imgField);
		}
		if (shop.getCity() != null && shop.getCity().length() > 0) {
			Field cityField = new Field("city", shop.getCity(), Field.Store.YES, Field.Index.NOT_ANALYZED);
			doc.add(cityField);
		}
		if (shop.getTelephone() != null && shop.getTelephone().length() > 0) {
			Field telephoneField = new Field("telephone", shop.getTelephone(), Field.Store.YES,
					Field.Index.ANALYZED_NO_NORMS);
			doc.add(telephoneField);
		}
		Field shopIdField = new Field("offerShopId", Integer.toString(shop.getId()), Field.Store.YES, Field.Index.NO);
		doc.add(shopIdField);
		Field shopNameField = new Field("offerShopName", shop.getName(), Field.Store.YES, Field.Index.ANALYZED_NO_NORMS);
		doc.add(shopNameField);
		Field shopDispNameField = new Field("offerShopDispName", shop.getDisplayName(), Field.Store.YES,
				Field.Index.ANALYZED);
		doc.add(shopDispNameField);
		Field userIdField = new Field("offerUserId", Integer.toString(shop.getUserId()), Field.Store.YES,
				Field.Index.NO);
		doc.add(userIdField);
		if (offer.getUserName() != null && offer.getUserName().length() > 0) {
			Field userNameField = new Field("offerUserName", offer.getUserName(), Field.Store.YES,
					Field.Index.ANALYZED_NO_NORMS);
			doc.add(userNameField);
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
