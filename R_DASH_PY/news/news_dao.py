from rdash_package.oracle_db import OracleConnection
from news_vo import NewsVO
import oracledb

class NewsDao:
    def __init__(self,db:OracleConnection):
        self.db = db

    def do_insert(self,news:NewsVO):
        flag = 0
        conn = self.db.connect()
        if conn is None:
            print('DB 연결 실패로 do_insert 중단')
            return flag
        cursor = conn.cursor()
        sql = '''
            INSERT INTO news (
                title,
                keyword,
                company,
                url,
                pub_dt
                ) VALUES ( :title,
                   :keyword,
                   :company,
                   :url,
                   :pub_dt )
        '''
        try:
            print(f'1.param: {sql}')

            cursor.execute(sql, {
                'title': news.title,
                'keyword': news.keyword,
                'company': news.company,
                'url': news.url,
                'pub_dt': news.pub_dt
            })
            conn.commit()
            flag = cursor.rowcount
            print(f'3. 반영 건수:{flag}')

        except oracledb.DatabaseError as e:
        # 데이터 베이스 관련 예외 처리
        # 오류 발생 시 롤백
            conn.rollback()
            error = e.args
            print(f'데이터 베이스 오류 발생, 롤백 수행: {error[0].message}')

        except Exception as e:
            conn.rollback()
            print(f'insert 실패')
        else:
            print(f'*insert 성공')
        finally:
            cursor.close()
            conn.close()

            return flag


def main():
    """

    """


if __name__ == '__main__':
    main()
