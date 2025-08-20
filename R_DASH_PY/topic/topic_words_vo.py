from dataclasses import dataclass
from typing import Optional

@dataclass
class TopicWordsVO:
    word: str
    freq : int
    prev_freq : Optional[int] = None
    reg_dt : Optional[str] = None


def main():
    """
    
    """
    topic01 = TopicWordsVO(word='토마토', freq=100, prev_freq=0, reg_dt="")

    print(f'topic01: {topic01}')
    print(f'topic01.title: {topic01.word}')
    # topic01: TopicWordsVO(word='토마토', freq=100, prev_freq=0, reg_dt='')
    # topic01.title: 토마토

if __name__ == '__main__':
    main()
