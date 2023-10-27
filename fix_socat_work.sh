#!/bin/bash

# `ps -aux | fgrep socat` 명령어로 socat 프로세스 찾기
socat_process=$(ps -aux | fgrep socat | fgrep TCP)

# socat 프로세스가 존재하는지 체크
if [[ -n "$socat_process" ]]; then
    echo "socat is running"
else
    # 여기서 로깅
    # 현재 날짜를 ~/fix_socat_work.log 파일에 기록
    date >> ~/fix_socat_work.log

    # socat 프로세스가 없을 경우, `docker ps -a` 명령어로 Docker 컨테이너 찾기
    docker_container=$(docker ps -a | fgrep "s_code_1_1")

    # Docker 컨테이너가 존재하는지 체크
    if [[ -n "$docker_container" ]]; then
        # Docker 컨테이너가 있을 경우, socat 명령어 실행
        nohup socat -t0 TCP-LISTEN:8081,fork,reuseaddr TCP:localhost:8082 &
        echo "Started socat for localhost:8082"
    else
        # Docker 컨테이너가 없을 경우, 다른 socat 명령어 실행
        nohup socat -t0 TCP-LISTEN:8081,fork,reuseaddr TCP:localhost:8083 &
        echo "Started socat for localhost:8083"
    fi
fi