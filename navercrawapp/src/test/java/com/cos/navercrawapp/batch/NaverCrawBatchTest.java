package com.cos.navercrawapp.batch;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import com.cos.navercrawapp.domain.NaverNews;


// 동기적 배치 프로그램
public class NaverCrawBatchTest {
	// 8Byte
	long aid = 277493;

	@Test
	public void 뉴스수집_테스트() {
		List<NaverNews> newsList = new ArrayList<>();

		int successCount = 0;
		int errorCount = 0;
		int crawCount = 0;

		while (true) {
			String aidStr = String.format("%010d", aid);

			// aid 10자리로 변경
			System.out.println("aidStr : " + aidStr);

			String url = "https://news.naver.com/main/read.naver?mode=LSD&mid=shm&sid1=103&oid=437&aid=" + aidStr;

			try {
				Document doc = Jsoup.connect(url).get();

				// 페이지 전체 element
				// System.out.println(doc);

				// company, title, createAt 파싱
				String title = doc.selectFirst("#articleTitle").text();
				String company = doc.selectFirst(".press_logo img").attr("alt");
				String createAt = doc.selectFirst(".t11").text();
				System.out.println("title : " + title);
				System.out.println("company : " + company);
				System.out.println("createAt : " + createAt);

				// 오늘 날짜
				LocalDate today = LocalDate.now();

				// 어제 날짜
				LocalDate yesterday = today.minusDays(1);

				createAt = createAt.substring(0, 10); // 처음부터 10번째 앞자리까지
				createAt = createAt.replace(".", "-");// .을 -로 변경
				System.out.println("변경된 createAt: " + createAt);
				System.out.println("오늘 날짜: " + today);

				if ((today.toString()).equals(createAt)) {
					System.out.println("createAt :" + createAt);
					break; // while 문 종료
				}

				if ((yesterday.toString()).equals(createAt)) { // 어제날짜랑 createAt이 같으면 List 컬렉션에 모았다가 DB에 save()하기
					System.out.println("어제 기사입니다");

					newsList.add(NaverNews.builder().title(title).company(company)
							.createdAt(Timestamp.valueOf(LocalDateTime.now().minusDays(1))).build());
					crawCount++;

				}
				successCount++;
			} catch (Exception e) {
				System.out.println("해당 주소에 페이지를 찾을 수 없습니다. :" + e.getMessage());
				errorCount++;
			}
			aid++;

		}

		System.out.println("배치프로그램 종료=========");
		System.out.println("성공횟수 :" + successCount);
		System.out.println("실패횟수 :" + errorCount);
		System.out.println("크롤링횟수 :" + crawCount);
		System.out.println("마지막 aid : " + aid);

	}
}
