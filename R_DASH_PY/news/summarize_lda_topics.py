
from langchain.chains import LLMChain
from langchain.prompts.chat import ChatPromptTemplate
from langchain.schema.output_parser import StrOutputParser
from langchain_community.chat_models import ChatOpenAI
from langchain_openai import ChatOpenAI
from dotenv import load_dotenv
import os

load_dotenv()
openai_key = os.getenv("OPENAI_API_KEY")
os.environ["OPENAI_API_KEY"]=openai_key

class smmr_topic:
    def __init__(self,lda_result):
        self.lda_result = lda_result


    def summarize_lda_topics(self):
        topic_terms = self.lda_result["topic_terms"]

        # 언어 모델 초기화
        model = ChatOpenAI(temperature=0.7)



        # 프롬프트 템플릿 정의
        system_template = "다음 키워드를 바탕으로 뉴스를 300자 이내로 요약해줘:{topic}"
        name_template = "다음 키워드들을 바탕으로 이 뉴스 주제에 어울리는 간단한 이름(5~10자 정도)을 지어줘:\n{topic}"
        summary_prompt = ChatPromptTemplate.from_messages([
            ('system', system_template),
            ('user', '{topic}')
        ])
        name_prompt = ChatPromptTemplate.from_messages([
            ('system', name_template),
            ('user', '{topic}')
        ])
        # 출력 파서 정의 - 단순 문자열로 파서
        output_parser = StrOutputParser()

        # 전체 파이프라인 구성
        summary_chain = summary_prompt | model | output_parser
        name_chain = name_prompt | model | output_parser

        return summary_chain, name_chain



def main():
    """
    
    """
    lda_result = {
        "topic_terms": [ "기후변화", "환경", "탄소배출", "재생에너지", "대기오염", "지구온난화", "에너지효율", "친환경", "온실가스", "산림파괴",
    "수질오염", "해양쓰레기", "플라스틱", "전기차", "태양광", "풍력", "전력망", "에너지저장", "배출권거래", "에너지정책",
    "에너지전환", "지속가능성", "기후정책", "탄소중립", "환경보호", "생태계", "자원순환", "친환경농업", "도시녹화", "스마트그리드",
    "수소경제", "그린뉴딜", "환경교육", "환경법", "생물다양성", "폐기물관리", "에너지관리", "온실가스감축", "탄소배출권", "에너지산업",
    "환경영향평가", "기후위기", "청정에너지", "에너지수요", "기후적응", "산업혁신", "지속가능발전", "친환경기술", "환경감시", "탄소저감기술",
    "기후금융", "대체에너지", "에너지저감", "해양보호", "지속가능농업", "환경리스크", "기후변화적응", "도시환경", "재생자원", "탄소포집"]
    }

    smmr = smmr_topic(lda_result)
    summary_chain, name_chain = smmr.summarize_lda_topics()

    # 테스트용 키워드를 문자열로 준비 (예: topic_terms를 쉼표로 연결)
    topic_str = ", ".join(lda_result["topic_terms"])

    # 요약 생성
    summary = summary_chain.invoke({"topic": topic_str})
    print("요약 결과:")
    print(summary)
    print("-" * 40)

    # 이름 생성
    name = name_chain.invoke({"topic": topic_str})
    print("이름 결과:")
    print(name)

if __name__ == '__main__':
    main()
