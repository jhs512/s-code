#!/bin/bash

declare -A services
services=(
  ["s_code_1_1"]="8082"
  ["s_code_1_2"]="8083"
)

SOCAT_PORT=8081
SLEEP_DURATION=20

current_service=$(ps aux | grep "socat -t0 TCP-LISTEN:$SOCAT_PORT" | grep -v grep | awk '{print $NF}')

# 현재 서비스가 없으면 초기 서비스 설정
if [[ -z "$current_service" ]]; then
  echo "No current service running. Setting up initial service."
  port="${services[$name]}"
  current_service="TCP:localhost:${port}"

  # 혹시나
  for name in "${!services[@]}"; do
    docker stop "$name"
    docker rm -f "$name"
  done
fi

# 현재 서비스의 이름과 포트 찾기
for name in "${!services[@]}"; do
  port="${services[$name]}"
  if [[ "TCP:localhost:$port" == "$current_service" ]]; then
    current_name="$name"
    current_port="$port"
    break
  fi
done

# 다음 서비스 찾기
next_name=""
for name in "${!services[@]}"; do
  if [[ "$name" != "$current_name" ]]; then
    next_name="$name"
    break
  fi
done
next_port="${services[$next_name]}"

# 다음 서비스 업데이트 및 재시작
docker stop "$next_name"
docker rm -f "$next_name"
docker run --name="$next_name" -p "$next_port":8080 -v /docker_projects/s_code_1/volumes/gen:/gen --restart unless-stopped -e TZ=Asia/Seoul --pull always -d ghcr.io/jhs512/s-code-1

# 다음 서비스가 완전히 실행될 때까지 기다림
sleep "$SLEEP_DURATION"

# 포트 전환
kill -9 $(ps aux | grep "socat -t0 TCP-LISTEN:$SOCAT_PORT" | grep -v grep | awk '{print $2}')
nohup socat -t0 "TCP-LISTEN:$SOCAT_PORT,fork,reuseaddr" "TCP:localhost:$next_port" &

# 현재 서비스 끄기
docker stop "$current_name"
docker rm -f "$current_name"

echo "Switched service successfully!"