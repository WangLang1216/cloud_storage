import axios from "axios";
import QS from "qs";
import { ElMessage } from "element-plus";
import router from "../router";


const request = axios.create({
  baseURL: 'http://10.0.3.6:8080/',
  timeout: 10000
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token');
    if (token) config.headers['Token'] = token
    return config;
}, error => {
  Promise.reject(error)
})

// 响应拦截
request.interceptors.response.use(
  response => {
    if (response.data.code == 1 || response.data.code == 200) {
      ElMessage({
        message: response.data.msg,
        type: 'success',
        duration: 2000
      });
      return response
    } else {
      ElMessage({
        message: '请重新登录！',
        type: 'warning',
        duration: 2000
      });
      localStorage.removeItem('token');
      router.replace({name: 'login'});
      return Promise.reject(response.data.msg);
    }
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          localStorage.removeItem('token');
          router.replace({name: 'login'});
          break;
        case 500:
          ElMessage({
            message: error.response.data.msg,
            type: 'error',
          });
          break;
        default:
          error.message = `连接错误${error.response.status}`
      }
    } else {
      // 超时处理
      if (JSON.stringify(error).includes('timeout')) {
        ElMessage.error('服务器响应超时，请刷新当前页');
      }
      error.message('连接服务器失败');
    }
    ElMessage.error(error.message);
    return Promise.resolve(error.response.msg);
  }
)

/**
 * get方法，对应get请求
 * @param {String} url [请求的url地址]
 * @param {Object} params [请求时携带的参数]
 */
export function get(url, params) {
  return new Promise((resolve, reject) =>{
    request.get(url, {
      params: params
    })
    .then(res => {
      resolve(res.data)
    })
    .catch(err => {
      reject(err.data)
    })
  })
}
/**
* post方法，对应post请求
* @param {String} url [请求的url地址]
* @param {Object} params [请求时携带的参数]
*/
export function post(url, params) {
  return new Promise((resolve, reject) => {
    request.post(url, params) //QS.stringify(params)
    .then(res => {
      resolve(res.data)
    })
    .catch(err => {
      reject(err.data)
    })
  })
}

/**
* put方法，对应put请求
* @param {String} url [请求的url地址]
* @param {Object} params [请求时携带的参数在url后面]
*/
export function put(url, params) {
  return new Promise((resolve, reject) => {
    let param = params.join('/')
    const completeUrl = `${url}/${param}`
    request.put(completeUrl, {}) //QS.stringify(params)
    .then(res => {
      resolve(res.data)
    })
    .catch(err => {
      reject(err.data)
    })
  })
}

/**
* Delete方法，对应Delete请求
* @param {String} url [请求的url地址]
* @param {Object} params [请求时携带的参数在url后面]
*/
export function Delete(url, params) {
  return new Promise((resolve, reject) => {
    request.delete(url, {
      params: params
    })
    .then(res => {
      resolve(res.data)
    })
    .catch(err => {
      reject(err.data)
    })
  })
}

/**
 * get方法，对应get请求,直接在地址后面拼串的形式
 * @param {String} url [请求的url地址]
 * @param {Array} params [请求时携带的参数]
 */
export function getDynamicynamic(url, params) {
  return new Promise((resolve, reject) =>{
    let param = params.join('/')
    const completeUrl = `${url}/${param}`
    request.get(completeUrl, {})
    .then(res => {
      resolve(res.data)
    })
    .catch(err => {
      reject(err.data)
    })
  })
}
/**
 * post方法，导出文件
 * @param {String} url [请求的url地址]
 * @param {String} params [请求时携带的参数]
 */
export function getFileUseBlobByPost(url, params = {}) {
	return new promise((resolve, reject)=> {
		request({
      method: 'post',
      url,
      data: params,
      responseType: 'blob'
    })
    .then(res => {
      resolve(res)
    })
    .catch(err => {
      reject(err.data)
    })
  })
}
