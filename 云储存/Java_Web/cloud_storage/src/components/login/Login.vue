<template>
  <div class="login">
    <div class="common-layout">
      <el-container>
        <el-header>
          <a href="javascript:void(0)"><p>Cloud Storage</p></a>
        </el-header>
        <el-main>
          <div class="left">
            <p>云存储</p>
            <p>智能高效</p>
          </div>
          <div class="right">
            <div class="login">
              <div class="head">
                <a href="javascript:void(0)"><div @click="isLogin = true; isRegister = false" :style="getTopHighlight(1)">登录</div></a>
                <a href="javascript:void(0)"><div @click="isLogin = false; isRegister = true" :style="getTopHighlight(2)">注册</div></a>
              </div>
              <div class="loginInto" v-show="isLogin == true">
                <div>
                  <el-input v-model="userInfo.uName" placeholder="请输入用户名/账户" clearable />
                  <el-input v-model="userInfo.uPassword" placeholder="请输入密码" show-password clearable />
                  <el-button type="primary" @click="login">登录</el-button>
                  <el-link>密码丢失可联系管理员找回！</el-link>
                </div>
              </div>
              <div class="loginInto" v-show="isLogin == false">
                <div>
                  <el-input v-model="userInfo.uName" placeholder="请输入用户名/账户" clearable />
                  <el-input v-model="userInfo.uPassword" placeholder="请输入密码" show-password clearable />
                  <el-input v-model="passwordTemp" placeholder="请确认密码" show-password clearable />
                  <el-button type="success" @click="register">注册</el-button>
                  <el-link>欢迎使用 云存储！</el-link>
                </div>
              </div>
            </div>
          </div>
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
import { userLogin, userRegister } from '../../request/userApi';
import { useStore } from 'vuex';
import { ElMessage } from "element-plus";

export default {
  name: 'Login',
  data () {
    return {
      isLogin: true,
      isRegister: false,
      styleOne: {borderTop: '#2980ff 5px solid', color: '#2980ff'},
      styleTwo: {borderTop: 'white 5px solid', color: '#666'},
      userInfo: {
        uName: null,
        uPassword: null,
      },
      gmtCreate: 'null',
      passwordTemp: null,
    }
  },
  methods: {
    getTopHighlight(index) {
      if (index == 1) return this.isLogin ? this.styleOne : this.styleTwo;
      if (index == 2) return this.isRegister ? this.styleOne : this.styleTwo;
    },
    async login() {
      let is = this.formatValidation(this.userInfo.uName, this.userInfo.uPassword);
      if (!is) return;
      const res = await userLogin(this.userInfo);
      this.setToken(res.data);
      console.log(localStorage.getItem('token'));
      this.$router.push({name: 'home'});
    },
    async register() {
      let is = this.formatValidation(this.userInfo.uName, this.userInfo.uPassword);
      if (!is) return;
      if (this.userInfo.uPassword != this.passwordTemp) return ElMessage({
        message: '两次密码不一致！',
        type: 'warning',
        duration: 2000
      });
      this.gmtCreate = this.myDate();
      let temp = this.userInfo;
      temp.gmtCreate = this.gmtCreate;
      const res = await userRegister(temp);
      this.$router.push({name: 'login'});
    },
    formatValidation(uName, uPassword) {
      if (uName === '' || uName == null) { 
        ElMessage({
          message: '账户不能为空！',
          type: 'warning',
          duration: 2000
        });
        return false;
      }
      if (uPassword === '' || uPassword == null){
        ElMessage({
          message: '密码不能为空！',
          type: 'warning',
          duration: 2000
        });
        return false;
      }
      return true;
      // 格式验证
    },
    myDate() {
      let myDate = new Date();
      let Y = myDate.getFullYear();
      let M = myDate.getMonth() + 1;
      let D = myDate.getDate();
      let H = myDate.getHours();
      let i = myDate.getMinutes();
      let s = myDate.getSeconds();
      if(M < 10) M = '0' + M;
      if(D < 10) D = '0' + D;
      if(H < 10) H = '0' + H;
      if(i < 10) i = '0' + i;
      if(s < 10) s = '0' + s;
      return Y + '-' + M + '-' + D + ' ' + H + ':' + i + ':' + s;
    }
  },
  setup() {
    const store = useStore();
    const setToken = (token) => {
      store.commit('setToken', token);
    };
    return {
      setToken,
    };
  }
}
</script>

<style scoped>
  .login {
    width: 100%;
  }
  .el-header {
    width: 100%;
    height: 70px;
    border: solid transparent;
    background: #d8dce5;
    background-color: rgba(0,0,0,.3);
    display: flex;
    padding-left: 8%;
    z-index: 99;
  }
  .el-header p {
    font-size: 28px;
    color: white;
    position: relative;
    top: -16px;
  }
  .el-main {
    position: absolute;
    width: 100%;
    height: 100%;
    background: url(../../assets/image/back.jpg);
    background-size: cover;
    background-repeat: no-repeat;
    display: flex;
  }
  .el-main .left {
    flex: 1;
    padding-top: 13%;
    padding-left: 20%;
  }
  .el-main .left p:nth-child(1) {
    font-size: 90px;
    letter-spacing: 6px;
    color: white;
  }
  .el-main .left p:nth-child(2) {
    font-size: 35px;
    margin-top: -8%;
    letter-spacing: 45px;
    color: white;
  }
  .el-main .right {
    flex: 1;
    padding-top: 12%;
  }
  .el-main .right .login {
    width: 80%;
    height: 80%;
    background: white;
    border-radius: 2px;
  }
  .el-main .right .login .head {
    display: flex;
  }
  .el-main .right .login .head a{
    flex: 1;
  }
  .el-main .right .login .head div {
    text-align: center;
    height: 60px;
    font-size: 20px;
    color: #666;
    font-weight: 600;
    letter-spacing: 5px;
    border-top: white 5px solid;
    font-family: helvetica,arial,微软雅黑,华文黑体;
    line-height: 60px;
  }
  .el-main .right .login .head div:hover {
    color: #2980ff;
    border-top: #2980ff 5px solid;
  }
  .el-main .right .login .loginInto {
    width: 100%;
    padding-top: 10%;
    padding-left: 10%;
    padding-right: 10%;
  }
  .el-main .right .login .loginInto div {
    width: 80%;
    text-align: center;
  }
  .el-input {
    width: 100%;
    height: 50px;
    border: 1px solid #CCC;
    border-radius: 4px;
    color: #000;
    font-family: PingFang SC;
    font-size: 18px;
    line-height: 50px;
    margin-top: 5%;
  }
  .el-button {
    display: block;
    margin-top: 5%;
    width: 80%;
    height: 50px;
    font-size: 18px;
    margin-left: 10%;
  }
  .el-link {
    margin-top: 10%;
    font-size: 15px;
    color: #CCC;
  }
  .el-link:hover {
    color: #2980ff;
  }
  a {
    list-style: none;
    text-decoration: none;
  }
</style>../../request/userApi