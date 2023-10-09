<template>
  <div class="crmUser">
    <el-container>
        <!--头部-->
        <el-header>
          <!--信息头-->
          <el-dropdown trigger="click">
            <a href="javascript:void(0)"><span class="el-dropdown-link">
              {{ uName }}
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </span></a>
            <template #dropdown>
              <el-dropdown-menu style="width: 200px">
                <el-dropdown-item><span>Action 1</span></el-dropdown-item>
                <el-dropdown-item><span>Action 2</span></el-dropdown-item>
                <el-dropdown-item><span>Action 3</span></el-dropdown-item>
                <el-dropdown-item divided @click="logOff"><h3>退出登录</h3></el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </el-header>
        <!--用户列表-->
        <el-main>
          <div class="tableInfo">
            <el-table :data="userList.filter(data => !search || data.uname.toLowerCase().includes(search.toLowerCase()))" style="width: 100%">
              <el-table-column label="注册日期" prop="gmtCreate" />
              <el-table-column label="用户名" prop="uname" />
              <el-table-column
                prop="ustate"
                label="状态"
                width="100"
                :filters="[
                  { text: '正常', value: '0' },
                  { text: '异常', value: '1' },
                ]"
                :filter-method="filterTag"
                filter-placement="bottom-end"
              >
                <template #default="scope">
                  <el-tag
                    :type="scope.row.ustate == '1' ? 'info' : 'success'"
                    disable-transitions
                    >{{ getTag(scope.row.ustate) }}</el-tag
                  >
                </template>
              </el-table-column>
              <el-table-column align="right">
                <template #header>
                  <el-input v-model="search" size="large" placeholder="请输入用户名" />
                </template>
                <template #default="scope">
                  <el-button size="default" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                  <el-button v-if="scope.row.ustate == '0'" size="default" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                  <el-button v-else size="default" type="success" @click="handleDelete(scope.$index, scope.row)">上线</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <!--修改内容-->
          <div class="dialog">
            <el-dialog width="20%" v-model="dialogFormVisible" title="修改用户信息">
              <label>用户名：</label><span>{{ userInfo.uName }}</span>
              <br/><br/>
              <label>密  码：</label><el-input style="width: 80%;" v-model="userInfo.uPassword" placeholder="请输入密码" clearable />
              <br/><br/>
              <label>身  份：</label>
              <el-select v-model="purview" class="m-2" placeholder="请选中角色">
                <el-option
                  v-for="item in roleList"
                  :key="item.rid"
                  :label="item.rname"
                  :value="item.rid"
                />
              </el-select>
              <p>{{ purview == '1' ? '可管理用户信息' : '仅限于文件操作' }}</p>
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="dialogFormVisible = false">关闭</el-button>
                  <el-button type="primary" @click="updateUser">提交</el-button>
                </span>
              </template>
            </el-dialog>
          </div>
        </el-main>
      </el-container>
  </div>
</template>

<script>
import { queryUserNameById, userLogOffByName, userInfoList, queryRoleList, userInfoUpdate, updateUserRoleById, userDeleteById } from '../../request/userApi';
import { ElMessage } from "element-plus";
import { useStore } from 'vuex';

export default {
  name: 'CrmUser',
  data () {
    return {
      uName: '诗随远方',
      search: null,
      dialogFormVisible: false,
      userInfo: {
        uId: '',
        uName: '',
        uPassword: '',
      },
      purview: '',
      index: '',
      userList: [],
      roleList: []
    }
  },
  async mounted () {
    // 用户名查询
    const resName = await queryUserNameById();
    this.uName = resName.data;
    // 用户信息查询
    const resUser = await userInfoList();
    this.userList = resUser.data;
    console.log(this.userList);
  },
  methods: {
    filterTag(value, row) {
      return row.ustate == value
    },
    getTag(state) {
      return state == '0' ? '正常' : '异常';
    },
    // 编辑
    async handleEdit(index, row) {
      // 角色信息查询
      const resRole = await queryRoleList();
      this.roleList = resRole.data;
      this.dialogFormVisible = true;
      this.userInfo.uId = row.uid;
      this.userInfo.uName = row.uname;
      this.userInfo.uPassword = row.upassword;
      this.purview = row.rid;
      this.index = index;
    },
    // 删除
    async handleDelete(index, row) {
      let uState = row.ustate == '0' ? '1' : '0'
      let temp = [];
      let uId = row.uid;
      temp.push(uId);
      temp.push(uState);
      const res = await userDeleteById(temp);
      if (res.code == '0') return ElMessage({
        message: '修改失败！',
        type: 'warning',
        duration: 2000
      });
      return this.userList[index].ustate = temp[1];
    },
    // 提交
    async updateUser() {
      if (this.userInfo.uPassword != this.userList[this.index].upassword) {
        if (this.userInfo.uPassword == null || this.userInfo.uPassword == '') return ElMessage({
          message: '密码不能为空！',
          type: 'warning',
          duration: 2000
        });
        const resUpdateUser = await userInfoUpdate(this.userInfo);
        if (resUpdateUser.code == '0') return ElMessage({
          message: '密码修改失败，请重试！',
          type: 'warning',
          duration: 2000
        });
        this.userList[this.index].uname = this.userInfo.uName;
        this.userList[this.index].upassword = this.userInfo.uPassword;
      }
      if (this.purview != this.userList[this.index].rid) {
        let temp = [];
        temp.push(this.userInfo.uId);
        temp.push(this.purview);
        const resPurview = await updateUserRoleById(temp);
        if (resPurview.code == '0') return ElMessage({
          message: '角色修改失败，请重试！',
          type: 'warning',
          duration: 2000
        });
        // 用户信息查询
        const resUser = await userInfoList();
        this.userList = resUser.data;
      }
      return this.dialogFormVisible = false;
    },
    // 注销
    async logOff() {
      const res = await userLogOffByName();
      if (res.code == 1) {
        ElMessage({
          message: '注销成功',
          type: 'success',
          duration: 2000
        });
        this.setToken(res.data);
        console.log(localStorage.getItem('token'));
        this.$router.push({name: 'login'});
      }
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
  .crmUser {
    width: 100%;
  }
  .el-header {
    width: 100%;
    height: 90px;
    border-bottom: #dcdfe6 1px solid;
    text-align: right;
    padding-right: 5%;
  }
  .el-dropdown a {
    line-height: 90px;
    color: black;
    font-size: 20px;
    text-decoration: none;
    letter-spacing: 5px;
  }
  .tableInfo {
    width: 80%;
  }
  .el-table {
    font-size: 18px;
  }
</style>../../request/userApi