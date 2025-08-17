import requests
import re
from bs4 import BeautifulSoup
import pandas as pd
from dotenv import load_dotenv
import os
from news_insert import save_news_to_db

load_dotenv()
client_id = os.getenv('client_id')
client_secret = os.getenv('client_secret')

PRESS_MAP = {
        '서울신문': '081',
        'JTBC': '437',
        '연합뉴스': '001',
        '국민일보': '005',
        'MBC': '214',
        '이데일리': '018',
        '부산일보': '082',
        'YTN': '052',
        '머니투데이': '008',
        '한국경제': '015',
        '한국일보': '469',
        '매일경제': '009',
        '채널A': '449',
        '서울경제': '011',
        '헤럴드경제': '016',
        '조선일보': '023',
        '한국경제TV': '215',
        '노컷뉴스': '079',
        'SBS': '055',
        '조선비즈': '366',
        'kbc광주방송': '660',
        '한겨레': '028',
        '경향신문': '032',
        '디지털타임스': '029',
        '매일신문': '088',
        '중앙일보': '025',
        '동아일보': '020',
        'TV조선': '448',
        '뉴시스': '003',
        'KBS': '056',
        '뉴스1': '421',
        '미디어오늘': '006',
        '문화일보': '021',
        '조세일보': '123',
        '농민신문': '662',
        '세계일보': '022',
        '아시아경제': '277',
        '파이낸셜뉴스': '014',
        '데일리안': '119',
        '시사저널': '586',
        '아이뉴스24': '031',
        'MBN': '057',
        '비즈워치': '648',
        '강원도민일보': '654',
        '연합뉴스TV': '422',
        'CJB청주방송': '655',
        '대전일보': '656',
        '프레시안': '002',
        '강원일보': '087',
        '경기일보': '666',
        '오마이뉴스': '047',
        '시사IN': '308',
        'SBS Biz': '374',
        '지디넷코리아': '092',
        '헬스조선': '346',
        '전자신문': '030',
        '더팩트': '629',
        '한겨레21': '036',
        '대구MBC': '657',
        '신동아': '262',
        '이코노미스트': '243',
        '블로터': '293',
        '코메디닷컴': '296',
        '매경이코노미': '024',
        '뉴스타파': '607',
        '디지털데일리': '138',
        '기자협회보': '127',
        '여성신문': '310',
        '한경비즈니스': '050',
        '주간조선': '053',
        '코리아헤럴드': '044',
        '전주MBC': '659',
        '주간동아': '037',
        '머니S': '417',
        '월간 산': '094',
        '주간경향': '033',
        '일다': '007',
        '동아사이언스': '584',
        '국제신문': '658',
        '더스쿠프': '665',
        '동아사이언스': '584',
        '국제신문': '658',
        '더스쿠프': '665',
        'JIBS': '661',
        '레이디경향': '145'
    }

# 코드 → 언론사명 매핑 (역매핑 생성)
CODE_TO_PRESS = {v: k for k, v in PRESS_MAP.items()}

def get_press_name_for_url(url:str)->str:
    match = re.search(r"/article/(\d{3})/", url)
    if match:
        code = match.group(1)
        return CODE_TO_PRESS.get(code, f"Unknown({code})")
    return "Invalid URL"

def naver_news_api(qeury):
    # 요청 URL (뉴스 검색)
    url = "https://openapi.naver.com/v1/search/news.json"

    # 요청 헤더 설정
    headers = {
        "X-Naver-Client-Id": client_id,
        "X-Naver-Client-Secret": client_secret
    }

    # 요청 파라미터
    params = {
        "query": qeury,
        "display": 10,  # 결과 5개만
        "start": 1,
        "sort": "date"  # 정확도순 , data : 날짜순 내림차순
    }
    # GET 요청
    response = requests.get(url, headers=headers, params=params)

    # 결과 저장용 리스트
    results = []

    if response.status_code == 200:
        news_data = response.json()
        for item in news_data["items"]:
            news_title = re.sub(r'<.*?>', '', item['title'])
            news_title = re.sub(r'&quot;', '', news_title)
            link = item['link']
            pub_dt = item['pubDate']

            if re.search(r"n\.news\.naver\.com", link):
                print("포함")
                press = get_press_name_for_url(link)
                print(f"{qeury} | {press} | {news_title} | {link} | {pub_dt}")

            else:
                press = "기타 신문사"
                print(f"[네이버 뉴스 아님] {qeury} | {press} | {news_title} | {link} | {pub_dt}")

            results.append({
                "query": qeury,
                "press": press,
                "title": news_title,
                "link": link,
                "pub_dt": pub_dt
            })
    else:
        print(response.status_code, response.text)

    return results


def main():
    all_results = []
    all_results.extend(naver_news_api("한파"))
    all_results.extend(naver_news_api("홍수"))
    all_results.extend(naver_news_api("싱크홀"))
    all_results.extend(naver_news_api("폭염"))
    all_results.extend(naver_news_api("태풍"))
    all_results.extend(naver_news_api("산사태"))
    all_results.extend(naver_news_api("황사"))
    all_results.extend(naver_news_api("화재"))

    # 테스트
    #naver_news_api("한파")

    #DB 저장 반복문
    for result in all_results:
        # print(type(all_results)) 리스트
        # print(type(result)) DICT
        save_news_to_db(
            keyword=result["query"],
            title=result["title"],
            company=result["press"],
            url=result["link"],
            pub_dt=result["pub_dt"]
        )

if __name__ == "__main__":
    main()

