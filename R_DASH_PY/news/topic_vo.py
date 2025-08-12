from dataclasses import dataclass
from typing import Optional

@dataclass
class TopicVO:
    title: str
    contents : Optional[str] = None
    reg_dt : Optional[str] = None
    reg_id : Optional[str] = None
    mod_dt : Optional[str] = None
    mod_id : Optional[str] = None
    topic_ratio : Optional[float] = None

def main():
    """
    
    """
    topic01 = TopicVO(title='토픽 주제',contents='토픽 주제 내용')

    print(f'topic01: {topic01}')
    print(f'topic01.title: {topic01.title}')
    #topic01: TopicVO(topic_no=1, title='토픽 주제', contents='토픽 주제 내용', reg_dt=None, reg_id=None, mod_dt=None, mod_id=None, topic_ratio=None)
    #topic01.title: 토픽 주제

if __name__ == '__main__':
    main()
