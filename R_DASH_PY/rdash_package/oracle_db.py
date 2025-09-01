import oracledb

class OracleConnection:
    def __init__(self,user,password,dsn):
        self.user = user
        self.password = password
        self.dsn = dsn
    def connect(self):
        try:
            #DB 연결
            return oracledb.connect(user=self.user, password=self.password, dsn=self.dsn)
        except oracledb.DatabaseError as e:
            #데이터 베이스 관련 예외 처리
            error = e.args
            print(f'데이터베이스 오류: {error[0].message}')
        except Exception as e:
            print(f'Exception: {e}')

def main():
    """
    
    """
    from dotenv import load_dotenv
    import os

    load_dotenv()  # 현재 경로의 .env 파일을 로드

    host = os.getenv("ORACLE_HOST")
    port = os.getenv("ORACLE_PORT")
    service = os.getenv("ORACLE_SERVICE")
    user = os.getenv("ORACLE_USER")
    password = os.getenv("ORACLE_PASSWORD")

    dsn = f"{host}:{port}/{service}"

    # DB 연결 및 DAO 인스턴스 생성 (전역 또는 main 내에서)
    conn = OracleConnection(user,password,dsn)
    print(f'conn: {conn}')
    connection = conn.connect()

    if connection:
        print(" DB 연결 성공!")
        cursor = connection.cursor()
        cursor.execute("SELECT sysdate FROM dual")
        result = cursor.fetchone()
        print("현재 DB 시간:", result[0])
        cursor.close()
        connection.close()
    else:
        print(" DB 연결 실패")

if __name__ == '__main__':
    main()
