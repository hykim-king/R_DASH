import logging

# 로그 생성
logger = logging.getLogger("pcwk_logger")
logger.setLevel(logging.DEBUG)

# Handler 추가
console_handler = logging.StreamHandler()  # 콘솔 출력
file_handler = logging.FileHandler('pcwk_app.log')  # 파일에 저장

# 포맷 설정
formatter = logging.Formatter('%(asctime)s'
                              '[%(levelname)s] '
                              '%(filename)s '
                              '%(lineno)d '
                              '%(message)s',
                              datefmt='%y-%m-%d %H:%M:%S')

console_handler.setFormatter(formatter)
file_handler.setFormatter(formatter)

# 로깅에 핸들러 추가
logger.addHandler(console_handler)
logger.addHandler(file_handler)

def main():
    """
    
    """

    # 로그 메시지 출력
    logging.debug('debug 메시지')
    logging.info('debug 메시지')
    logging.warning('debug 메시지')
    logging.error('debug 메시지')
    logging.critical('debug 메시지')


if __name__ == '__main__':
    main()
