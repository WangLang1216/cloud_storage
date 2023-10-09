<!--
 * @Author: WangCute 2605735186@qq.com
 * @Date: 2023-08-04 09:20:17
 * @LastEditors: WangCute 2605735186@qq.com
 * @LastEditTime: 2023-08-07 16:11:43
 * @FilePath: \cloud_storage\src\components\home\Home.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="home">
    <div class="common-layout">
      <el-container>
        <el-aside width="15%">
          <LeftNav @title="getTitle"></LeftNav>
        </el-aside>
        <el-main>
          <MainContent :title="title" v-if="isFile == true"></MainContent>
          <CrmUser v-else></CrmUser>
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
import LeftNav from './LeftNav.vue';
import MainContent from './MainContent.vue';
import CrmUser from './CrmUser.vue';

export default {
  name: 'Home',
  data () {
    return {
      title: '全部',
      isFile: true,
    }
  },
  mounted () {
    // 禁止网页后退
    // history.pushState(null, null, document.URL);
    // window.addEventListener(
    //   'popstate',
    //   function(e) {
    //     history.pushState(null, null, document.URL);
    //   },
    // false);
  },
  methods: {
    getTitle(val) {
      this.title = val;
    },
  },
  components: {
    LeftNav,
    MainContent,
    CrmUser
  },
  watch: {
    title() {
      return this.title === '用户管理' ? this.isFile = false : this.isFile = true;
    }
  }
}
</script>

<style scoped>
  .home {
    width: 100%;
    height: 100%;
  }
  .common-layout {
    height: 100%;
  }
  .el-container {
    height: 100%;
  }
  .el-aside {
    height: 100%;
    background: #f4f4f4;
    border-right: #dcdfe6 1px solid;
  }
  .el-main {
    padding: 0;
  }
</style>