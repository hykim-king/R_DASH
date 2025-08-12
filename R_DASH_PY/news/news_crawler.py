import asyncio
from playwright.async_api import async_playwright
import datetime
import pandas as pd
from langchain.chains import LLMChain


# 오늘 날짜
today = datetime.date.today()
start = today.replace(day=1).strftime("%Y.%m.%d")
end = today.strftime("%Y.%m.%d")
print(today.isoformat())

class crawler:

    def __init__(self, query, max_link):
        self.query = query
        self.max_link = max_link

    async def naver_crawler(self):

        start, end = "today", "today"
        url = (
            f"https://search.naver.com/search.naver?where=news&query={self.query}&sort=0&pd=1&ds={start}&de={end}&start=1"
        )

        async with async_playwright() as p:
            browser = await p.chromium.launch(headless=False)  # headless=True로 하면 브라우저 안뜸
            context = await browser.new_context(
                user_agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36"
            )
            page = await context.new_page()
            await page.goto(url)

            # 스크롤 함수 (5초간)
            end_time = datetime.datetime.now() + datetime.timedelta(seconds=5)
            while datetime.datetime.now() < end_time:
                await page.evaluate("window.scrollTo(0, document.body.scrollHeight)")
                await asyncio.sleep(1)

            # 뉴스 링크 선택자: span.sds-comps-profile-info-subtext > a
            await page.wait_for_selector("span.sds-comps-profile-info-subtext > a")

            # 최대 100개만
            news_links = await page.query_selector_all("span.sds-comps-profile-info-subtext > a")
            news_links = news_links[:self.max_link]

            news_data = []

            for article in news_links:
                try:
                    article_url = await article.get_attribute("href")
                    title = await article.get_attribute("title") or await article.inner_text()

                    # 새 페이지 열기
                    detail_page = await context.new_page()
                    await detail_page.goto(article_url)

                    # 본문 로드 대기
                    await detail_page.wait_for_selector("div.newsct_article")
                    content = (await detail_page.locator("div.newsct_article").inner_text()).strip()

                    news_data.append({
                        "content": content
                    })

                    await detail_page.close()

                except Exception as e:
                    print(f"오류 발생: {e}")
                    if not detail_page.is_closed():
                        await detail_page.close()
                    continue

            for news in news_data:
                print(f"본문: {news['content'][:]}...")
                print("-" * 50)
            print(f"총 추출된 기사 수: {len(news_data)}")

            await browser.close()

            # 판다스에 저장
            df = pd.DataFrame(news_data)
            # df.to_csv("naver_news.csv", index=False, encoding="utf-8-sig")

            # 간단 출력
            print(df[["content"]].head())
            print(f"총 {len(df)}개의 뉴스 저장 완료!")

        return df


def main():
    """

    """



if __name__ == '__main__':
    main()
