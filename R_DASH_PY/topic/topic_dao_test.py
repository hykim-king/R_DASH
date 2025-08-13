from rdash_package.oracle_db import OracleConnection
from topic_vo import TopicVO
from topic_dao import TopicDao
import cx_Oracle
from dotenv import load_dotenv
import os

load_dotenv()  # 현재 경로의 .env 파일을 로드

host = os.getenv("ORACLE_HOST")
port = os.getenv("ORACLE_PORT")
service = os.getenv("ORACLE_SERVICE")
user = os.getenv("ORACLE_USER")
password = os.getenv("ORACLE_PASSWORD")

dsn = f"{host}:{port}/{service}"

def do_insert():
    print(f'do_insert')

    db = OracleConnection(user=user,password=password,dsn=dsn)
    dao = TopicDao(db)

    print(f'db: {db}')
    print(f'dao: {dao}')
    print(f'db.connect(): {db.connect()}')

    new_topic = TopicVO(title='토픽 이름',contents='토픽 이름',reg_id='AI',topic_ratio=7.2)
    flag = dao.do_insert(new_topic)

    if flag == 1:
        print('-'*53)
        print(f'성공: {flag}')
        print('-' * 53)
    else:
        print(f'등록 실패: {flag}')


def main():
    """
    
    """
    do_insert()

if __name__ == '__main__':
    main()
