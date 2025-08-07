import requests
import json
import datetime
import pandas as pd


# --- base_date와 base_time 계산 함수 (이전과 동일) ---
def base_datetime():
    """
    현재 날짜와 시간을 구하고, 현재 분이 40분이 안될 시 이전 현재 시 - 1 후 30분 추가
    현재 시가 0시 일 시, 전날 23시30분 데이터를 가져옴.
    30분에 데이터 수집, 40분에 발표 ex) 23시30분에 데이터를 수집하면 23시 40분에 업데이트
    """
    now = datetime.datetime.now()
    current_hour = now.hour
    current_minute = now.minute

    if current_minute < 40:
        if current_hour == 0:
            base_date = (now - datetime.timedelta(days=1)).strftime('%Y%m%d')
            base_time = "2330"
        else:
            base_date = now.strftime('%Y%m%d')
            base_time = (now - datetime.timedelta(hours=1)).strftime('%H') + "30"
    else:
        base_date = now.strftime('%Y%m%d')
        base_time = now.strftime('%H') + "30"

    return base_date, base_time


URL = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst'
SERVICE_KEY = 'VJxg5p3Iyzp7FA0pzgtVA7AYRfaM2YSuLU4h8TMQAvQIJMGkIN7qEpL/QoDBEqo1MnsWnxGR+lN/9SsKlSmbZg=='

# --- 엑셀 파일에서 시군구별 NX, NY 좌표 읽기 ---
nowcast_excel = 'NowCast.xlsx'

try:
    # 엑셀 파일 읽기
    df = pd.read_excel(nowcast_excel)
    print(f"{nowcast_excel}' 로드 성공.")
    # '시군구' 컬럼이 NaN인 행을 제거
    df_cleaned = df.dropna(subset=['2단계'])

    # 2단계(시군구)까지의 데이터만 추출하고 중복 제거 (NX, NY는 시군구 단위로 대표값 하나만 필요)
    # 실제 엑셀 파일의 컬럼명.
    # 시군구 단위로 nx,ny를 대표하는 값 중복 제거
    sigungu_coords_df = df_cleaned[['1단계', '2단계', '격자 X', '격자 Y']].drop_duplicates(subset=['1단계', '2단계'])

    # DataFrame을 순회하며 API 호출
    all_temperatures = {}  # 결과를 저장할 딕셔너리

    base_date, base_time = base_datetime()
    display_time = base_time[:2]

    print(f"\n✔️ {base_date} {display_time}시 기준 전국 시군구 초단기 실황 데이터 수집 시작...")

    # iterrows()를 사용해 DataFrame의 각 행을 (인덱스, 시리즈) 형태로 순회
    for index, row in sigungu_coords_df.iterrows():
        sido_name = row['1단계']
        sigungu_name = row['2단계']
        nx = str(row['격자 X'])  # NX, NY가 숫자인 경우 문자열로 변환 필요
        ny = str(row['격자 Y'])

        region_name = f"{sido_name} {sigungu_name}"  # 출력용 지역 이름

        params = {
            'serviceKey': SERVICE_KEY,
            'numOfRows': '10',
            'pageNo': '1',
            'base_date': base_date,
            'base_time': base_time,
            'nx': nx,
            'ny': ny,
            'dataType': 'JSON'
        }

        try:
            response = requests.get(URL, params=params, verify=False)
            if response.status_code == 200:
                data = response.json()

                # API 응답 결과코드 확인 (중요!)
                result_code = data['response']['header']['resultCode']
                if result_code != '00':  # '00'이 아니면 오류 발생
                    print(
                        f"  ❌ {region_name} API 응답 오류: {data['response']['header']['resultMsg']} (코드: {result_code})")
                    continue  # 다음 지역으로 넘어감

                items = data['response']['body']['items']['item']
                current_temp = None

                for item in items:
                    if item['category'] == 'T1H':
                        current_temp = float(item['obsrValue'])
                        break

                if current_temp is not None:
                    all_temperatures[region_name] = current_temp
                    print(f"  ✅ {region_name}: {current_temp}℃")
                else:
                    print(f"  ⚠️ {region_name}: 기온 데이터(T1H)를 찾을 수 없습니다.")

            else:
                print(f"  ❌ {region_name} HTTP 요청 실패: {response.status_code}")

        except Exception as e:
            print(f"  🚨 {region_name} 데이터 처리 중 예외 발생: {e}")

except FileNotFoundError:
    print(f"❌ 오류: 엑셀 파일 '{nowcast_excel}'을(를) 찾을 수 없습니다. 경로를 확인해주세요.")
except KeyError as e:
    print(f"❌ 오류: 엑셀 파일의 컬럼명 오류입니다. '{e}' 컬럼이 존재하지 않거나 이름을 확인해주세요. ")
    print("엑셀 파일의 컬럼명('시도', '시군구', 'NX', 'NY')을 코드에 맞게 수정해야 합니다.")
except Exception as e:
    print(f"❌ 엑셀 파일 처리 중 예상치 못한 오류 발생: {e}")

print(f"\n--- 전국 기온 데이터 수집 완료) ---")

# 수집된 데이터를 새로운 DataFrame으로 만들어 엑셀로 저장할 수도 있습니다.
# output_df = pd.DataFrame(list(all_sigungu_temperatures.items()), columns=['지역', '현재기온'])
# output_df.to_excel("sigungu_current_temperatures.xlsx", index=False)
# print("\n수집된 데이터가 'sigungu_current_temperatures.xlsx' 파일로 저장되었습니다.")