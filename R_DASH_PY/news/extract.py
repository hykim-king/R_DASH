import pandas as pd
from kiwipiepy import Kiwi




# 명사 추출 객체 생성
kiwi = Kiwi()

# 명사 추출 객체 생성
stopwords = set([
        "뉴스", "기자", "보도", "있다", "됐다", "대한", "위해", "관련", "이번", "지난",
        "최근", "통해", "것으로", "하는", "있는", "하고", "하며", "이다", "한다",
        "이후", "있으며", "됐다", "등", "중", "때문", "그리고", "더불어", "또한",
        '것', '수', '등', '더', '이', '그', '저', '및', '를', '은', '는', '이', '가',
        "일","개","시","곳","군","오늘","구"
    ])

merge_dict = {
        '재난 ': '재난'
    }

# 명사만 추출하는 함수 + 불용어 제거
def extract_nouns(text):
    if pd.isnull(text):
        return []
    tokens = [token.form for token in kiwi.analyze(text)[0][0]
              if token.tag.startswith('N')or token.tag.startswith('VA')]
    return [merge_dict.get(word, word) for word in tokens if word not in stopwords]


def extract_all_nouns(df):
    # content 컬럼에서 명사 추출해 nouns 컬럼 생성
    df["nouns"] = df["content"].apply(extract_nouns)

    return df

def main():
    """
    
    """
    # 예시 DataFrame
    data = {"content": [
        "오늘 오전 9시경, ○○시 ○○구 일대에서 대규모 재난이 발생하였습니다. 갑작스러운 폭우로 인해 주요 도로가 침수되었고, 인근 하천의 수위가 급격히 상승하면서 일부 주택과 상가가 물에 잠겼습니다. 소방당국과 경찰은 즉시 구조 및 대피 작업에 착수했으며, 주민들에게는 인근 대피소로 신속히 이동할 것을 권고했습니다. 기상청은 오늘 오후까지 강한 비와 돌풍이 이어질 것으로 예보하며, 추가 피해가 우려된다고 전했습니다.",
        "이것은 테스트를 위한 문장입니다. 현재 시스템에서는 긴 텍스트 데이터를 분석하고, 명사와 형용사 등의 품사를 추출하여 키워드 분석을 수행하는 기능을 검증하고 있습니다. 이번 테스트 문장은 총 500자 내외로 작성되었으며, 실제 서비스 환경에서는 뉴스 기사, 재난 보고서, 현장 상황 보고서 등 다양한 형태의 데이터를 처리하게 됩니다. 이를 통해 워드 클라우드 생성, 빈도 분석, 감성 분석과 같은 여러 자연어 처리 기능이 올바르게 작동하는지 확인할 수 있습니다."
    ]}
    df = pd.DataFrame(data)

    df = extract_all_nouns(df)
    print(df)



if __name__ == '__main__':
    main()
