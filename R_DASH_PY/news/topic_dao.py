from rdash_package.oracle_db import OracleConnection
from topic_vo import TopicVO
import cx_Oracle

class TopicDao:
    def __init__(self,db:OracleConnection):
        self.db = db

    # 저장 만
    def do_insert(self,topic:TopicVO):
        flag = 0
        conn = self.db.connect()
        if conn is None:
            print('DB 연결 실패로 do_insert 중단')
            return flag

        cursor = conn.cursor()
        sql = '''
        INSERT INTO topic (
            title,
            contents,
            reg_dt,
            reg_id,
            mod_dt,
            mod_id,
            topic_ratio
        ) VALUES ( :title,
                   :contents,
                   :reg_dt,
                   :reg_id,
                   :mod_dt,
                   :mod_id,
                   :topic_ratio )
        '''
        try:
            print(f'1.param: {topic}')
            print(f'1.param: {sql}')

            cursor.execute(sql, {
                'title': topic.title,
                'contents': topic.contents,
                'reg_dt': topic.reg_dt,
                'reg_id': topic.reg_id,
                'mod_dt': topic.mod_dt,
                'mod_id': topic.mod_id,
                'topic_ratio': topic.topic_ratio
            })
            conn.commit()
            flag = cursor.rowcount
            print(f'3. 반영 건수:{flag}')

        except cx_Oracle.DatabaseError as e:
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
