#!/usr/bin/env python3

from typing import Optional
import subprocess
import os

class SocatManager:
    def __init__(self, log_file_path: str = "~/fix_socat_work.log"):
        self.log_file_path = os.path.expanduser(log_file_path)

    def find_process(self, process_name: str) -> Optional[str]:
        try:
            result = subprocess.check_output(["ps", "-aux"])
            for line in result.splitlines():
                if process_name in line.decode("utf-8"):
                    return line.decode("utf-8")
        except subprocess.CalledProcessError:
            return None
        return None

    def find_docker_container(self, container_name: str) -> Optional[str]:
        try:
            result = subprocess.check_output(["docker", "ps", "-a"])
            for line in result.splitlines():
                if container_name in line.decode("utf-8"):
                    return line.decode("utf-8")
        except subprocess.CalledProcessError:
            return None
        return None

    def run_socat(self, src_port: int, dest_port: int):
        subprocess.Popen(["nohup", "socat", "-t0", f"TCP-LISTEN:{src_port},fork,reuseaddr", f"TCP:localhost:{dest_port}", "&"])

    def log(self, message: str):
        with open(self.log_file_path, "a") as log_file:
            log_file.write(message + "\n")

    def fix_socat(self):
        socat_process = self.find_process("socat TCP")
        if socat_process is not None:
            print("socat is running")
        else:
            self.log(f"Socat process not found, fixing at {self.current_datetime()}")
            docker_container = self.find_docker_container("s_code_1_1")
            if docker_container is not None:
                self.run_socat(8081, 8082)
                print("Started socat for localhost:8082")
            else:
                self.run_socat(8081, 8083)
                print("Started socat for localhost:8083")

    @staticmethod
    def current_datetime() -> str:
        from datetime import datetime
        return datetime.now().strftime("%Y-%m-%d %H:%M:%S")

if __name__ == "__main__":
    manager = SocatManager()
    manager.fix_socat()