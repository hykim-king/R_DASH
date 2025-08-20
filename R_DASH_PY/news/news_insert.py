from news_vo import NewsVO
from news_dao import NewsDao
from dotenv import load_dotenv
import os
from rdash_package.oracle_db import OracleConnection



def save_news_to_db(title, keyword, company, url,pub_dt):
    load_dotenv()  # 현재 경로의 .env 파일을 로드

    host = os.getenv("ORACLE_HOST")
    port = os.getenv("ORACLE_PORT")
    service = os.getenv("ORACLE_SERVICE")
    user = os.getenv("ORACLE_USER")
    password = os.getenv("ORACLE_PASSWORD")

    dsn = f"{host}:{port}/{service}"

    # DB 연결 및 DAO 인스턴스 생성 (전역 또는 main 내에서)
    db = OracleConnection(user, password, dsn)
    dao = NewsDao(db)

    # newsVO 객체 생성
    news = NewsVO(
        title=title,
        keyword=keyword,
        company=company,  # 필요하면 현재 시간 넣어도 됨
        url=url,
        pub_dt=pub_dt
    )

    flag = dao.do_insert(news)
    if flag == 1:
        print(f"DB 저장 성공: {title}")
    else:
        print(f"DB 저장 실패: {title}")


def main():
    """

    """
    save_news_to_db(title="이제 그만",keyword="홍수",company="한국경제",url="n.naver.com",pub_dt="20250812")


if __name__ == '__main__':
    main()
