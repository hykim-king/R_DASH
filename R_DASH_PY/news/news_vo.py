from dataclasses import dataclass
from typing import Optional

@dataclass
class NewsVO:
    keyword:str
    url: str
    title:Optional[str] = None
    company:Optional[str] = None
    pub_dt:Optional[str] = None

def main():
    news01 = NewsVO(keyword="재난",url="https://n.news.naver.com/mnews/article/003/0013379607?sid=100")

    print(f'news01: {news01}')
    print(f'news01.keyword: {news01.keyword}')
    print(f'news01.url: {news01.url}')

if __name__ == '__main__':
    main()