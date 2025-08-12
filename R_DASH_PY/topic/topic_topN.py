import matplotlib.pyplot as plt
from collections import Counter
import os
import pandas as pd
import extract



def top_n(df, column_name='content', save_path=None):
    # 한글 폰트 설정 (Windows 기준)
    plt.rcParams['font.family'] = 'Malgun Gothic'  # 또는 'NanumGothic'

    # 마이너스 깨짐 방지
    plt.rcParams['axes.unicode_minus'] = False

    texts = df['content'].apply(lambda x: extract.extract_nouns(x))
    flat_words = sum(texts, [])
    word_counts = Counter(flat_words)

    top_n = 20
    top_words = word_counts.most_common(top_n)

    print(f"\n📊 Top {top_n} 단어:")
    for i, (word, freq) in enumerate(top_words, 1):
        print(f"{i}. {word}: {freq}")

    words, freqs = zip(*top_words)

    plt.figure(figsize=(10, 6))
    plt.barh(words[::-1], freqs[::-1], color='orange')
    plt.xlabel("빈도")
    plt.title("Top 20 단어")
    plt.tight_layout()

    save_path = "topn_result.png"
    plt.savefig(save_path)
    print(f"그래프가 '{save_path}' 에 저장되었습니다.")

    # if save_path:
    #     # 디렉터리가 없으면 생성
    #     os.makedirs(os.path.dirname(save_path), exist_ok=True)
    #     plt.savefig(save_path)
    #     print(f"그래프가 '{save_path}' 에 저장되었습니다.")
    #     #plt.show()
    # else:
    #     #plt.show()
    # pass
    plt.close()
    return words,freqs

def main():
    """
    
    """
    data = {
        "content": [
            "재난 발생 소식입니다.",
            "화재 사고 피해가 컸습니다.",
            "태풍 경로가 예상됩니다.",
            "재난 화재 피해 발생",
            "재난 사고 예상"
        ]
    }
    df = pd.DataFrame(data)

    words, freqs = top_n(df, column_name='content')

    for word, freq in zip(words, freqs):
        print(f"{word}: {freq}")


if __name__ == '__main__':
    main()
