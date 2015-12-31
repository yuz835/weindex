package ca.weindex.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Component;

import ca.weindex.common.model.Blog;
import ca.weindex.common.model.Offer;
import ca.weindex.common.model.Pagination;
import ca.weindex.common.model.SearchResult;
import ca.weindex.common.model.Shop;

@Component
public class LuceneSearch {

	public SearchResult<Offer> searchOffer(String keyword, Pagination page) {
		SearchResult<Offer> result = new SearchResult<Offer>();
		result.setPageNum(page.getPageNum());
		result.setPageSize(page.getPageSize());
	
		List<Offer> list = new ArrayList<Offer>();
		String[] queryFileds = { "name", "desc", "label" };
		try {
			IndexSearcher searcher = LuceneUtils.createIndexSearcher();
			Query query = LuceneUtils.createQuery(queryFileds, keyword);
			Query type = new TermQuery(new Term("docType", "offer"));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(type, BooleanClause.Occur.MUST);
			booleanQuery.add(query, BooleanClause.Occur.MUST);
			TopDocs topDocs = searcher.search(booleanQuery, 10000);
			result.setTotalNum(topDocs.totalHits);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//topDocs.totalHits;
			int startIndex = page.getBeginIndex();
			int endIndex = page.getEndIndex();
			for (int i = startIndex; i < endIndex && i < topDocs.totalHits; i++) {
				Document doc = searcher.doc(scoreDocs[i].doc);
				int id = Integer.parseInt(doc.get("offerId"));
				String name = doc.get("name");
				String desc = doc.get("desc");
				String label = doc.get("label");
				String price = doc.get("price");
				String imgUrl = doc.get("imgUrl");
				String city = doc.get("city");
				String telephone = doc.get("telephone");
				String shopIdStr = doc.get("offerShopId");
				String shopName = doc.get("offerShopName");
				String shopDispName = doc.get("offerShopDispName");
				String userIdStr = doc.get("offerUserId");
				String userName = doc.get("offerUserName");
				
				Offer offer = new Offer();
				offer.setId(id);
				offer.setName(name);
				offer.setDesc(desc);
				offer.setLabel(label);
				offer.setPrice(price);
				offer.setImgUrl(imgUrl);
				offer.setCity(city);
				offer.setTelephone(telephone);
				try {
					if (shopIdStr != null && shopIdStr.length() > 0) {
						offer.setShopId(Integer.parseInt(shopIdStr));
					}
				} catch (Exception e) {
					// do nothing
				}
				offer.setShopName(shopName);
				offer.setShopDispName(shopDispName);
				try {
					if (userIdStr != null && userIdStr.length() > 0) {
						offer.setUserId(Integer.parseInt(userIdStr));
					}
				} catch (Exception e) {
					// do nothing
				}
				offer.setUserName(userName);
				list.add(offer);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.setList(list);
		return result;
	}

	public SearchResult<Blog> searchBlog(String keyword, Pagination page) {
		SearchResult<Blog> result = new SearchResult<Blog>();
		result.setPageNum(page.getPageNum());
		result.setPageSize(page.getPageSize());

		List<Blog> list = new ArrayList<Blog>();
		String[] queryFileds = { "name", "content" };
		try {
			IndexSearcher searcher = LuceneUtils.createIndexSearcher();
			Query query = LuceneUtils.createQuery(queryFileds, keyword);
			Query type = new TermQuery(new Term("docType", "blog"));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(type, BooleanClause.Occur.MUST);
			booleanQuery.add(query, BooleanClause.Occur.MUST);
			TopDocs topDocs = searcher.search(booleanQuery, 10000);
			result.setTotalNum(topDocs.totalHits);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//topDocs.totalHits;
			int startIndex = page.getBeginIndex();
			int endIndex = page.getEndIndex();
			for (int i = startIndex; i < endIndex && i < topDocs.totalHits; i++) {
				Document doc = searcher.doc(scoreDocs[i].doc);
				int id = Integer.parseInt(doc.get("blogId"));
				String title = doc.get("name");
				String author = doc.get("author");
				Blog blog = new Blog();
				blog.setId(id);
				blog.setTitle(title);
				blog.setAuthor(author);
				list.add(blog);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.setList(list);
		return result;
	}
	
	public SearchResult<Shop> searchShop(String keyword, Pagination page) {
		SearchResult<Shop> result = new SearchResult<Shop>();
		result.setPageNum(page.getPageNum());
		result.setPageSize(page.getPageSize());

		List<Shop> list = new ArrayList<Shop>();
		String[] queryFileds = { "name", "dispName", "desc" };
		try {
			IndexSearcher searcher = LuceneUtils.createIndexSearcher();
			Query query = LuceneUtils.createQuery(queryFileds, keyword);
			Query type = new TermQuery(new Term("docType", "shop"));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(type, BooleanClause.Occur.MUST);
			booleanQuery.add(query, BooleanClause.Occur.MUST);
			TopDocs topDocs = searcher.search(booleanQuery, 10000);
			result.setTotalNum(topDocs.totalHits);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//topDocs.totalHits;
			int startIndex = page.getBeginIndex();
			int endIndex = page.getEndIndex();
			for (int i = startIndex; i < endIndex && i < topDocs.totalHits; i++) {
				Document doc = searcher.doc(scoreDocs[i].doc);
				int id = Integer.parseInt(doc.get("shopId"));
				String name = doc.get("name");
				String dispName = doc.get("dispName");
				String desc = doc.get("desc");
				String address = doc.get("address");
				String logoUrl = doc.get("logoUrl");
				Shop shop = new Shop();
				shop.setId(id);
				shop.setName(name);
				shop.setDisplayName(dispName);
				shop.setDesc(desc);
				shop.setAddress(address);
				shop.setLogoUrl(logoUrl);
				list.add(shop);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result.setList(list);
		return result;
	}
	
	public SearchResult<Shop> searchShopByRegion(int startLati, int endLati, int startLongi, int endLongi, Pagination page) {
		SearchResult<Shop> result = new SearchResult<Shop>();
		result.setPageNum(page.getPageNum());
		result.setPageSize(page.getPageSize());

		List<Shop> list = new ArrayList<Shop>();
		try {
			IndexSearcher searcher = LuceneUtils.createIndexSearcher();
			NumericRangeQuery<Integer> longiQuery = NumericRangeQuery.newIntRange("longi", startLongi, endLongi, true, true);
			NumericRangeQuery<Integer> latiQuery = NumericRangeQuery.newIntRange("lati", startLati, endLati, true, true);
			Query type = new TermQuery(new Term("docType", "shop"));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(type, BooleanClause.Occur.MUST);
			booleanQuery.add(longiQuery, BooleanClause.Occur.MUST);
			booleanQuery.add(latiQuery, BooleanClause.Occur.MUST);
			TopDocs topDocs = searcher.search(booleanQuery, 10000);
			result.setTotalNum(topDocs.totalHits);
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			//topDocs.totalHits;
			int startIndex = page.getBeginIndex();
			int endIndex = page.getEndIndex();
			for (int i = startIndex; i < endIndex && i < topDocs.totalHits; i++) {
				Document doc = searcher.doc(scoreDocs[i].doc);
				int id = Integer.parseInt(doc.get("shopId"));
				String name = doc.get("name");
				String dispName = doc.get("dispName");
				String desc = doc.get("desc");
				String longi = doc.get("longi");
				String lati = doc.get("lati");
				String address = doc.get("address");
				String zip = doc.get("zip");
				String logoUrl = doc.get("logoUrl");

				Shop shop = new Shop();
				shop.setId(id);
				shop.setName(name);
				shop.setDisplayName(dispName);
				shop.setDesc(desc);
				shop.setAddress(address);
				shop.setZip(zip);
				shop.setLogoUrl(logoUrl);
				if (longi != null && lati != null) {
					try {
						int lng = Integer.parseInt(longi);
						int lat = Integer.parseInt(lati);
						shop.setLati(lat);
						shop.setLongi(lng);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				list.add(shop);
			}
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		result.setList(list);
		return result;
	}
	
	public static void testDistance() throws CorruptIndexException, IOException {
//		int longi = -936786447, lati = 420135708;
		NumericRangeQuery<Integer> longiquery = NumericRangeQuery.newIntRange("longi", -768304220,
				-765557637, true, true);
		NumericRangeQuery<Integer> latiquery = NumericRangeQuery.newIntRange("lati", 392602991, 394196141,
				true, true);
		BooleanQuery bq = new BooleanQuery();
		Query type = new TermQuery(new Term("docType", "shop"));
		bq.add(type, BooleanClause.Occur.MUST);
		bq.add(longiquery, BooleanClause.Occur.MUST);
		bq.add(latiquery, BooleanClause.Occur.MUST);
		IndexSearcher searcher = LuceneUtils.createIndexSearcher();
		Filter filter = null;
		// 一次在索引器查询多少条数据
		int queryCount = 100;
		TopDocs results = searcher.search(bq, filter, queryCount);
		System.out.println("总符合: " + results.totalHits + "条数！");

	}

	public static void main(String[] args) {
		try {
			testDistance();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @throws ParseException
	 */
	public static void main1(String[] args) throws CorruptIndexException, IOException, ParseException {
		// TODO Auto-generated method stub
		// 查询的字符串:输入不存在的字符串是查询不到的,如：中国
		String queryString = "updated";
		// 查询字段集合
		String[] queryFileds = { "contents" };
		IndexSearcher searcher = LuceneUtils.createIndexSearcher();
		Query query = LuceneUtils.createQuery(queryFileds, queryString);
		// 在搜索器中进行查询
		// 对查询内容进行过滤
		Filter filter = null;
		// 一次在索引器查询多少条数据
		int queryCount = 100;
		TopDocs results = searcher.search(query, filter, queryCount);
		System.out.println("总符合: " + results.totalHits + "条数！");

		// 显示记录
		for (ScoreDoc sr : results.scoreDocs) {
			// 文档编号
			int docID = sr.doc;
			// 真正的内容
			Document doc = searcher.doc(docID);
			System.out.println("name = " + doc.get("name"));
			System.out.println("path = " + doc.get("path"));
			System.out.println("modified = " + doc.get("modified"));
			System.out.println("contents = " + doc.get("contents"));
		}
	}

}
