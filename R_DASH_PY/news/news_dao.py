from rdash_package.oracle_db import OracleConnection
from news_vo import NewsVO
import oracledb
from datetime import datetime
import dateutil.parser  # pip install python-dateutil

class NewsDao:
    def __init__(self,db:OracleConnection):
        self.db = db



    def do_update(self,news:NewsVO):
        flag = 0
        conn = self.db.connect()
        if conn is None:
            print('DB 연결 실패로 do_insert 중단')
            return flag
        cursor = conn.cursor()
        sql = '''
        MERGE INTO NEWS t1
	    USING (
	        SELECT
	            :url AS url
	        FROM dual
	    ) t2
	    ON (t1.url = t2.url AND t2.url IS NOT NULL)
	    WHEN MATCHED THEN 
	        UPDATE SET
	            t1.keyword = :keyword,
	            t1.title = :title,
	            t1.company = :company,
	            t1.pub_dt = :pub_dt,
	            t1.reg_dt = SYSDATE
	    WHEN NOT MATCHED THEN
	        INSERT (keyword, title, company, url, pub_dt, reg_dt)
	        VALUES (
	        :keyword, 
	        :title, 
	        :company, 
	        :url, 
	        :pub_dt, 
	        SYSDATE
	        )
        '''
        try:
            print(f'1.param: {sql}')
            dt = dateutil.parser.parse(news.pub_dt)

            cursor.execute(sql, {
                'title': news.title,
                'keyword': news.keyword,
                'company': news.company,
                'url': news.url,
                'pub_dt': dt
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
                   :pub_dt
                   )
        '''
        try:
            print(f'1.param: {sql}')
            dt = dateutil.parser.parse(news.pub_dt)

            cursor.execute(sql, {
                'title': news.title,
                'keyword': news.keyword,
                'company': news.company,
                'url': news.url,
                'pub_dt': dt
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
