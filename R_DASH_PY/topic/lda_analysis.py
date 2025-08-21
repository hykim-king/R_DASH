import pandas as pd
from gensim import corpora
from gensim.models import LdaModel
from collections import Counter


def run_lda(df, num_topics):
    # LDA를 위한 입력 형식 준비
    texts = df['nouns'].tolist()  # 각 문서의 명사 리스트
    dictionary = corpora.Dictionary(texts)  # 단어 사전
    corpus = [dictionary.doc2bow(text) for text in texts]  # 문서-단어 행렬 (bow)

    # LDA 모델 학습
    lda_model = LdaModel(
        corpus=corpus,
        id2word=dictionary,
        num_topics=num_topics,  # 토픽 개수 조절 가능
        random_state=42,
        passes=10,
        iterations=100
    )

    # 토픽
    # 각 문서의 대표 토픽 찾기.
    main_topics = []
    for bow in corpus:
        main_topic = max(lda_model.get_document_topics(bow), key=lambda x: x[1])
        main_topics.append(main_topic[0])

    # 토픽 건수 집계
    topic_counters = Counter(main_topics)

    # 토픽별 단어와 가중치
    topic_terms = {}
    for idx in range(num_topics):
        terms = lda_model.show_topic(idx, topn=5)  # [(단어, 확률), ...]
        topic_terms[idx] = terms

    #  전체 단어 개수
    all_words = [w for doc in texts for w in doc]
    word_counter = Counter(all_words)

    for topic_num, count in topic_counters.items():
        print(f'토픽 {topic_num}: {count}건')

    for idx, terms in topic_terms.items():
        print(f"토픽 {idx + 1}:")
        for word, weight in terms:
            print(f"  {word} ({weight:.4f})")
    lda_result = {
        "topic_counts": dict(topic_counters),
        "topic_terms": topic_terms,
        "word_counts": dict(word_counter)
    }
    return lda_result


def main():
    """

    """
    data = {
        "nouns": [["재난", "발생", "소식"], ["화재", "사고", "피해"], ["태풍", "경로", "예상"]]
    }
    df = pd.DataFrame(data)

    result = run_lda(df,num_topics=3)

    print("토픽별 문서 개수:", result["topic_counts"])
    for idx, terms in result["topic_terms"].items():
        print(f"토픽 {idx + 1}: {', '.join([w for w, _ in terms])}")

    for word, count in result["word_counts"].items():
        print(f"{word}: {count}")


if __name__ == '__main__':
    main()
