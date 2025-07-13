import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  vus: 1000,            // 동시 사용자 수
  duration: '10s',    // 테스트 시간
};

export default function () {
  const url = 'http://localhost:8083/api/v1/reservations';
  const payload = JSON.stringify({
    ticketId: 4,
    username: `user${__VU}_${__ITER}`  // 유저별 구분
  });

  const headers = {
    'Content-Type': 'application/json',
  };

  const res = http.post(url, payload, { headers });

  check(res, {
    'status was 200': (r) => r.status === 200,
  });

  sleep(0.1);
}