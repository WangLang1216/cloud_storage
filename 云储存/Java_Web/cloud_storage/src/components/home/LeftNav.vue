<!--
 * @Author: WangCute 2605735186@qq.com
 * @Date: 2023-08-04 09:29:56
 * @LastEditors: WangCute 2605735186@qq.com
 * @LastEditTime: 2023-08-07 14:11:30
 * @FilePath: \cloud_storage\src\components\home\LeftNav.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
  <div class="leftNav">
    <a href="javascript:void(0)">
      <i class="bi bi-clouds"></i>
      <span>云存储</span>
    </a>
    <el-menu default-active="1" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" >
      <el-menu-item :index="item.index" v-for="(item, index) in menuList" :key="index" @click="clickMenu(index)" :style="getHighlightShow(index, item.isHighlight)">
        <i :class="item.icon"></i>
        <span>{{ item.mname }}</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import { queryMenuById } from '../../request/userApi';

export default {
  name: 'LeftNav',
  data () {
    return {
      menuList: [],
      icon: ['bi bi-box-seam-fill', 'bi bi-file-earmark-minus-fill', 'bi bi-file-earmark-image', 'bi bi-file-earmark-play-fill',  'bi bi-music-note-list', 'bi bi-send-check', 'bi bi-ui-checks-grid'],
      textColor: '#2980ff'
    }
  },
  async mounted () {
    const res = await queryMenuById();
    this.menuList = res.data;
    let i = 0;
    this.menuList.forEach(element => {
      element.icon = this.icon[i];
      i++
    });
    this.menuList[0].isHighlight = true;
  },
  methods: {
    clickMenu(index) {
      this.menuList.forEach(element => {
        element.isHighlight = false;
      });
      this.menuList[index].isHighlight = true;
      this.$emit("title", this.menuList[index].mname);
    },
    getHighlightShow(index, isHighlight) {
      // 分割线
      if (index == 5 && this.menuList.length > 6) return isHighlight ? {borderBottom: '1px solid #dcdfe6', color: '#2980ff', borderLeft: '#2980ff 7px solid'} : {borderBottom: '1px solid #dcdfe6', color: '#555', borderLeft: 'white 7px solid'};
      return isHighlight ? {color: '#2980ff', borderLeft: '#2980ff 7px solid'} : {color: '#555', borderLeft: 'white 7px solid'};
    }
  }
}
</script>

<style scoped>
  .leftNav {
    width: 100%;
    height: 100%;
    background: #f4f4f4;
  }
  .el-menu {
    border: 0;
    background: #f4f4f4;
  }
  a {
    text-decoration: none;
    list-style: none;
    color: #2980ff;
    font-size: 40px;
    display: block;
    height: 90px;
    line-height: 90px;
    text-align: center;
    letter-spacing: 5px;
  }
  a span {
    color: black;
    font-weight: bold;
  }
  .el-menu-item {
    display: block;
    font-size: 20px;
    font-weight: 600;
    margin-top: 1%;
    border-left: white 7px solid;
  }
  .el-menu-item i {
    font-size: 25px;
    margin-left: 15%;
  }
  .el-menu-item span {
    margin-left: 5%;
  }
</style>../../request/userApi