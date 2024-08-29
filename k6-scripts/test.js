import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
    vus: 5,
    duration: '10s',
};

export default function () {
    let res = http.get('http://13.124.172.3/api/health-check');
    check(res, {
        'status is 200': (r) => r.status === 200,
    });
    sleep(1);
}