from rdash_package.oracle_db import OracleConnection
from topic_vo import TopicVO
import oracledb


class TopicWordsDao:
    def __init__(self, db: OracleConnection):
        self.db = db

    def do_insert(self, topic_list: list[TopicVO]):
        """
        topic_list: TopicVO 리스트
        topic.word, topic.freq를 사용
        """
        flag = 0
        conn = self.db.connect()
        if conn is None:
            print('DB 연결 실패로 do_insert 중단')
            return flag

        cursor = conn.cursor()

        try:
            sql = """
                  INSERT INTO TOPIC_WORDS (WORD, FREQ, PREV_FREQ, REG_DT)
                  SELECT :word          AS word,
                         :freq          AS freq,
                         NVL(Y.FREQ, 0) AS prev_freq,
                         SYSDATE
                  FROM TOPIC_WORDS Y
                  WHERE TRUNC(Y.REG_DT) = TRUNC(SYSDATE - 1)
                    AND Y.WORD = :word 
                  """
            for t in topic_list:
                cursor.execute(sql, {'word': t.word, 'freq': t.freq})

            conn.commit()
            flag = len(topic_list)
            print(f'* 반영 건수: {flag}')

        except oracledb.DatabaseError as e:
            conn.rollback()
            print(f'DB 오류 발생, 롤백 수행: {e.args[0].message}')
        except Exception as e:
            conn.rollback()
            print(f'insert 실패: {e}')
        finally:
            cursor.close()
            conn.close()

        return flag


def main():
    pass


if __name__ == '__main__':
    main()
