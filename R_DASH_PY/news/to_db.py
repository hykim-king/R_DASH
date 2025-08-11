from topic_vo import TopicVO
from topic_dao import TopicDao
from dotenv import load_dotenv
import os
from rdash_package.oracle_db import OracleConnection

def save_topic_to_db(name, summary, reg_id, count):
    load_dotenv()  # 현재 경로의 .env 파일을 로드

    host = os.getenv("ORACLE_HOST")
    port = os.getenv("ORACLE_PORT")
    service = os.getenv("ORACLE_SERVICE")
    user = os.getenv("ORACLE_USER")
    password = os.getenv("ORACLE_PASSWORD")

    dsn = f"{host}:{port}/{service}"

    # DB 연결 및 DAO 인스턴스 생성 (전역 또는 main 내에서)
    db = OracleConnection(user, password, dsn)
    dao = TopicDao(db)

    # TopicVO 객체 생성
    topic = TopicVO(
        title=name,
        contents=summary,
        reg_dt=None,  # 필요하면 현재 시간 넣어도 됨
        reg_id=reg_id,
        mod_dt=None,
        mod_id=None,
        topic_ratio=float(count)
    )

    flag = dao.do_insert(topic)
    if flag == 1:
        print(f"DB 저장 성공: {name}")
    else:
        print(f"DB 저장 실패: {name}")

def main():
    """
    
    """
    save_topic_to_db(reg_id="AI",name="주제",summary="주제 내용",count=7.2)


if __name__ == '__main__':
    main()
