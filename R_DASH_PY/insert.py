import os
import oracledb
from dotenv import load_dotenv

# 1. .env 파일 불러오기
load_dotenv()

# 2. 접속 정보 구성
host = os.getenv("ORACLE_HOST")
port = os.getenv("ORACLE_PORT")
service = os.getenv("ORACLE_SERVICE")
user = os.getenv("ORACLE_USER")
password = os.getenv("ORACLE_PASSWORD")

dsn = f"{host}:{port}/{service}"

# 4. DB 연결
conn = oracledb.connect(user=user, password=password, dsn=dsn)

# 5. 커서 생성
cursor = conn.cursor()

# 예: SELECT 1 FROM DUAL
cursor.execute("SELECT 1 FROM DUAL")
rows = cursor.fetchall()


for row in rows:
    print(row)

cursor.close()
conn.close()