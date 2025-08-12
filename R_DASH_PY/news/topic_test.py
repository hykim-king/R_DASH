from news import summarize_lda_topics
from news_crawler import crawler
import extract
import lda_analysis
from summarize_lda_topics import smmr_topic
from oracledb import connect
import topic_topN
from wordcloud import WordCloud
import asyncio
from topic_dao import TopicDao
from to_db import save_topic_to_db
import topicWordCloud

def main():
    # 1) 크롤링: 뉴스 데이터 가져오기
    c = crawler(query='재난', max_link=5)  # 인스턴스 생성하면서 인자 전달
    df = asyncio.run(c.naver_crawler())  # 인스턴스 메서드 호출

    # 2) 명사 추출: df에 'nouns' 컬럼 추가
    df = extract.extract_all_nouns(df)

    # 3) LDA 분석 실행 (토픽 개수 4개)

    lda_results = lda_analysis.run_lda(df,num_topics=4)

    # 4) 토픽별 문서 개수 출력
    print("\n[토픽별 문서 수]")
    for topic_num, count in lda_results["topic_counts"].items():
        print(f"토픽 {topic_num}: {count}건")

    # 5) 토픽별 핵심 단어 출력
    print("\n[토픽별 핵심 단어]")
    for topic_num, terms in lda_results["topic_terms"].items():
        print(f"토픽 {topic_num}:")
        for word, weight in terms:
            print(f" {word} ({weight:.4f})")

    smmr = smmr_topic(lda_results)
    summary_chain, name_chain = smmr.summarize_lda_topics()


    reg_id = "AI"

    for topic_num, terms in lda_results["topic_terms"].items():
        keywords = ", ".join([word for word, _ in terms])
        summary = summary_chain.invoke({"topic": keywords})
        name = name_chain.invoke({"topic": keywords})
        print(f"\n[토픽 {topic_num} 요약]")
        print(summary)
        print(f"[토픽 {topic_num} 이름]")
        print(name)

        count = lda_results["topic_counts"].get(topic_num, 0)  # 토픽별 문서 수 가져오기

        save_topic_to_db(name, summary, reg_id, count)

    words, freqs = topic_topN.top_n(df)
    print(words)
    print(freqs)

    topicWordCloud.make_wordcloud(df)

if __name__ == '__main__':
    main()

