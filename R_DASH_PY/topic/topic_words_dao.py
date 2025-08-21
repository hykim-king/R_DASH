from rdash_package.oracle_db import OracleConnection
from topic_words_vo import TopicWordsVO
import oracledb


class TopicWordsDao:
    def __init__(self, db: OracleConnection):
        self.db = db

    def do_insert(self, topic_list: list[TopicWordsVO]):
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
                  INSERT INTO TOPIC_WORDS (WORD, FREQ, PREV_FREQ)
                  SELECT :word AS word,
                         :freq AS freq,
                         NVL(Y.FREQ, 0) AS PREV_FREQ
                  FROM DUAL
                  LEFT JOIN TOPIC_WORDS Y
                    ON Y.WORD = :word
                   AND TRUNC(Y.REG_DT) = TRUNC(SYSDATE-1)
                  
                  UNION
                  
                  SELECT :word, :freq, 0
                  FROM DUAL
                  WHERE NOT EXISTS (
                    SELECT 1 FROM TOPIC_WORDS
                    WHERE TRUNC(REG_DT) = TRUNC(SYSDATE-1)
                    )
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
