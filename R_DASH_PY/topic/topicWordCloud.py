import matplotlib.pyplot as plt
import numpy as np
from PIL import Image
from wordcloud import WordCloud
import pandas as pd
import seaborn


def make_wordcloud(df,mask_path=None):

    # 명사 리스트를 하나의 문자열로 합치기
    all_nouns = sum(df['nouns'].tolist(), [])  # 리스트 안의 리스트 펼치기
    text = ' '.join(all_nouns)  # WordCloud 입력용 문자열

    ## 이미지 파일 읽어오기
    mask_arr = None
    if mask_path:
        im = Image.open(mask_path)
        mask_arr = np.array(im)

    # 6워드클라우드 생성 및 시각화
    wordcloud = WordCloud(
        font_path='C:/Windows/Fonts/malgun.ttf',  # 한글 폰트 지정 (Windows 기준)
        background_color='black',
        colormap=seaborn.color_palette("Spectral", as_cmap=True),
        # mask=mask_arr,
        prefer_horizontal=True,
        max_words=300,
        width=800,
        height=600
    ).generate(text)

    # 워드클라우드 저장
    image_path = "wordcloud_result.png"
    wordcloud.to_file(image_path)
    print(f"워드클라우드 이미지가 저장되었습니다: {image_path}")

    plt.figure(figsize=(10, 10))
    plt.imshow(wordcloud, interpolation='bilinear')
    plt.axis('off')
    #plt.show()
    plt.close()


def main():
    """
    
    """
    data = {
        "nouns": [["재난", "발생", "소식"], ["화재", "사고", "피해"], ["태풍", "경로", "예상"]]
    }
    df = pd.DataFrame(data)

    make_wordcloud(df)

if __name__ == '__main__':
    main()
