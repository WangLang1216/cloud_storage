<template>
  <div class="mainContent">
    <div class="common-layout">
      <el-container>
        <!--头部-->
        <el-header>
          <!--按钮组-->
          <div>
            <!--上传-->
              <div class="upload" v-show="isUpload == true">
                <el-row>
                  <el-col :span="12" style="display: flex;">
                    <!--文件-->
                    <el-upload
                      class="upload-demo"
                      :file-list="uploadForm"
                      :limit="10"
                      :auto-upload="false"
                      :on-change="changeFile"
                      :on-remove="removeFile">
                      <template #trigger>
                        <el-button type="primary" style="background-color: #2980ff;border-color: #2980ff;">选择文件</el-button>
                      </template>
                      <el-button type="success" @click="submitUpload" style="margin-top: -0.9%;">
                        <el-icon size="25" class="el-icon--left"><Upload /></el-icon>上传
                      </el-button>
                    </el-upload>
                  </el-col>
                  <el-col :span="12" style="display: flex;padding-top: 2.5%;">
                    <el-input v-model="catalogName" placeholder="请输入名称" />
                    <el-button style="color: black;" @click="createCatalog"><el-icon size="25" class="el-icon--left"><Plus /></el-icon>新建目录</el-button>
                    <el-button @click="queryFileTable = true" size="large" icon="Search" style="width: 50px;border-radius: 50%;border: 0;color: black;" circle />
                  </el-col>
                </el-row>
              </div>
              <!--文件查询-->
              <el-dialog v-model="queryFileTable" title="文件查询" style="width: 40%;">
                <el-input placeholder="输入名称" v-model="queryFileByName" clearable style="width: 30%;margin-right: 2%;" />
                <el-button type="primary" :icon="Search" @click="queryFilesByName">查询</el-button>
                  <el-table :data="gridData" style="width: 100%;font-size: 16px;">
                    <el-table-column prop="fileName" label="名称" width="500"/>
                  </el-table>
              </el-dialog>
            <!--下载、分享、删除-->
            <div class="setOf" v-show="isUpload == false">
              <div>
                <el-button-group>
                <el-button @click="downloadFile"><el-icon size="25" class="el-icon--left"><Download /></el-icon>下载</el-button>
                <el-button @click="shareFile"><el-icon size="25" class="el-icon--left"><Promotion /></el-icon>分享</el-button>
                <el-button @click="deleteCatalogOrFile"><el-icon size="25" class="el-icon--left"><Delete /></el-icon>删除</el-button>
                <el-button><el-icon size="25" class="el-icon--left"><More /></el-icon></el-button>
              </el-button-group>
              </div>
              <div>
                <!-- <el-input v-model="target" placeholder="请输入用户名" style="width: 50%;height: 48px;margin-top: 5%;border-radius: 0;font-size: 20px;" /> -->
                <el-autocomplete
                  style="margin-top: 5%;height: 50px;"
                  v-model="target"
                  size="medium"
                  :fetch-suggestions="querySearch"
                  placeholder="请输入用户名"
                  :trigger-on-focus="false"
                />
              </div>
            </div>
          </div>
          <!--信息头-->
          <div>
            <div class="user">
              <el-dropdown  trigger="click">
                <a href="javascript:void(0)"><span class="el-dropdown-link">
                  {{ uname }}
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
            </div>
          </div>
        </el-header>
        <!--文件内容-->
        <el-main>
          <div class="title">
            <el-breadcrumb separator-icon="ArrowRight">
              <el-breadcrumb-item v-show="isTitle == false" v-for="(item, index) in titleList" :key="index">
                <!--路径后续设置-->
                <a href="javascript:void(0)" @click="titleClick(item.catalogId, index)"><span>{{ item.title }}</span></a>
              </el-breadcrumb-item>
              <el-breadcrumb-item v-if="isTitle == true"><span style="font-weight: 600;">{{ title }}</span></el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="table">
            <el-table
              :data="dateInfo"
              ref="multipleTable" 
              @selection-change="handleSelectionChange" 
              @row-click="click_row" 
              :row-style="modality"
              @row-dblclick="doubleClickFolder"
              @cell-contextmenu="rightClick"
              style="width: 100%;font-size: 16px;">
              <el-table-column type="selection" width="55" />
              <el-table-column prop="name" label="名称" width="500">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <!--根据类型显示图标-->
                    <a href="javascript:void(0)" @click="doubleClickFolder(scope.row)"><img :src="getIcon(scope.row.type)" /></a>
                    <a href="javascript:void(0)" @click="doubleClickFolder(scope.row)">
                      <span style="margin-left: 10px;">{{ scope.row.name }}</span>
                    </a>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="gmtcreate" label="创建时间" width="250" />
              <el-table-column prop="size" label="大小">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <span>{{ scope.row.size }} {{ scope.row.unit }}</span>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <!--修改目录名-->
            <el-dialog
              v-model="centerDialogVisible"
              title="修改目录名"
              width="30%"
              align-center
            >
              <el-input v-model="newName" placeholder="请输入名称" />
              <template #footer>
                <span class="dialog-footer">
                  <el-button @click="centerDialogVisible = false">关闭</el-button>
                  <el-button type="primary" @click="updateDirectory">
                    修改
                  </el-button>
                </span>
              </template>
            </el-dialog>
            <!--显示总数-->
            <div class="total"><span>共 {{ total }} 项</span></div>
          </div>
        </el-main>
      </el-container>
    </div>
  </div>
</template>

<script>
import { selectFileAndCatalog, createCatalog, updateCatalog, deleteCatalog, deleteFiles, upload, shareFiles, selectLikeFile } from '../../request/fileApi';
import { queryUserNameById, userLogOffByName, userInfoList } from '../../request/userApi';
import { ElMessage } from "element-plus";
import { useStore } from 'vuex';
import axios from 'axios';

export default {
  name: 'MainContent',
  props: {
    title: {
      type: String,
      default: '全部'
    }
  },
  data () {
    return {
      queryFileTable: false,
      gridData: [],  // 模糊查询
      queryFileByName: null,
      isUpload: true,
      uploadRef: null,
      catalogName: null,  // 新建目录名称
      isTitle: true,
      form: {  // 上传文件
        files: [],
        catalogId: '0',
      },
      uname: '',
      myFileList: [],  // 选中数据
      mouseRowTemp: null,
      titleList: [{title: '全部', catalogId: '0'}],  // 面包屑
      target: null,  // 分享目标
      loading: false,
      centerDialogVisible: false,
      newName: '',  // 修改目录名称
      tempRow: null,
      // 文件夹信息
      folderList: [],
      // 文件信息
      fileList: [],
      // 图片信息
      imageList: [],
      userListTemp: [],
      // table数据
      dateInfo: [],
      catalogId: '0',  // 目录id
      total: 0,  // 总数
      queryUserList: [],  //  用户信息
      restaurants: [],
      uploadForm: []
    }
  },
  async mounted () {
    // 用户名查询
    const resUname = await queryUserNameById();
    this.uname = resUname.data;

    // 文件查询
    let temp = [];
    temp.push(this.catalogId);
    console.log(this.catalogId);
    const resQueryFile = await selectFileAndCatalog(temp);
    console.log(resQueryFile);
    this.folderList = resQueryFile.data.catalog;
    this.fileList = resQueryFile.data.file;
    // 文件/目录整合
    this.allDateInfo();

    // 用户列表
    const resUserList = await userInfoList();
    this.queryUserList = resUserList.data;
    this.queryUserList.forEach(element => {
      let temp = {};
      temp.value = element.uname;
      this.restaurants.push(temp);
    });
  },
  methods: {
    // 全部数据
    allDateInfo() {
      this.dateInfo = [];
      let temp = {};
      // 目录
      this.folderList.forEach(element => {
        temp = {};
        temp.id = element.catalogId;
        temp.name = element.catalogName;
        temp.catalogPid = element.catalogPid;
        temp.userId = element.userId;
        temp.catalogLevel = element.catalogLevel;
        temp.gmtcreate = element.gmtCreate;
        temp.type = '文件夹';
        this.dateInfo.push(temp);
      });
      // 文件
      this.fileList.forEach(element => {
        temp = {};
        temp.id = element.fileId;
        temp.name = element.fileName;
        temp.catalogId = element.catalogId;
        temp.size = element.fileSize;
        temp.unit = element.fileUnit;
        temp.type = this.fileType(element.fileName);
        temp.fileSource = element.fileSource
        temp.gmtcreate = element.gmtCreate;
        temp.version = element.version;
        this.dateInfo.push(temp);
      });
      this.total = this.dateInfo.length;
      return this.dateInfo;
    },
    // 上传
    async submitUpload() {
      if(this.form.files.length == 0) return ElMessage({
        message: '请选择文件',
        type: 'warning',
        duration: 2000
      }); 
      let formData = new FormData();
      for (const file of this.form.files) {  //多个文件全部都放到files
        if(file.raw) {
          formData.append('files', file.raw);
        }
      }
      formData.append('catalogId', this.form.catalogId);
      // 上传
      const res = await upload(formData);
      if(res.code == "200" || res.code == '1') {
        this.form.files = [];
        this.uploadForm = [];
        console.log(this.form);
        ElMessage({
          message: '上传成功',
          type: 'success',
          duration: 2000
        });
        // 文件查询
        let temp = [];
        temp.push(this.catalogId);
        const resQueryFile = await selectFileAndCatalog(temp);
        this.folderList = resQueryFile.data.catalog;
        this.fileList = resQueryFile.data.file;
        return this.allDateInfo();
        // return window.location.reload();
      }
      return ElMessage({
        message: '上传失败，请重试',
        type: 'warning',
        duration: 2000
      });
    },
    // 下载
    async downloadFile() {
      // 批量下载
      console.log(this.myFileList);
      if(this.myFileList.length > 1) {
        let fileNameDtoList = [];
        this.myFileList.forEach(element => {
          fileNameDtoList.push(element.name);
        });
        await axios({
          url: 'http://10.0.3.6:8080/minio/downloadMultiFileToMinIO',
          method: 'post',
          data: fileNameDtoList,
          headers: {
            'Accept': '	*/*',
            'Content-Type': 'application/json',
            token: localStorage.getItem('token'),
          },
          responseType: 'blob'
        }).then((res) => {
          const blob = new Blob([res.data]);
          console.log(blob,res);
          const fileName = res.headers["content-disposition"].split(";")[1].split("filename=")[1];
          if ('download' in document.createElement("a")) {
            const link = document.createElement("a");
            link.download = fileName;
            link.style.display = 'none';
            link.href = URL.createObjectURL(blob);
            document.body.appendChild(link);
            link.click();
            URL.revokeObjectURL(link.href);
            document.body.removeChild(link);
          } else {
            navigator.msSaveBlob(blob, fileName);
          }
        })
      } else {  // 单独下载
        let fileName = this.myFileList[0].name;
        let temp = [];
        temp.push(fileName);
        await axios({
          method: 'get',
          url: 'http://10.0.3.6:8080/minio/download/' + fileName,
          headers: {
            'Accept': '	*/*',
            'Content-Type': 'application/json',
            token: localStorage.getItem('token'),
          },
          responseType: 'blob'
        }).then((res)=> {
          console.log(res.data);
          const blob = new Blob([res.data]);
          const fileName = res.headers["content-disposition"].split(";")[1].split("filename=")[1];
          if ('download' in document.createElement("a")) {
            const link = document.createElement("a");
            link.download = fileName;
            link.style.display = 'none';
            link.href = URL.createObjectURL(blob);
            document.body.appendChild(link);
            link.click();
            URL.revokeObjectURL(link.href);
            document.body.removeChild(link);
          } else {
            navigator.msSaveBlob(blob, fileName);
          }
        })
      }
    },
    // 分享
    async shareFile() {
      let userId = null;
      this.queryUserList.forEach(element => {
        if(this.target == element.uname) userId = element.uid;
      });
      if(userId == null){
        ElMessage({
          message: '未找到该用户',
          type: 'warning',
          duration: 2000
        });
        return null;
      }
      let obj = {};
      let list = [];
      this.myFileList.forEach(element => {
        list.push(element.name);
      });
      obj.userId = userId;
      obj.files = list;
      console.log(obj);
      const res = await shareFiles(obj);
      if(res.code == "200" || res.code == '1') return ElMessage({
        message: '分享成功',
        type: 'success',
        duration: 2000
      });
      else return ElMessage({
        message: '分享失败',
        type: 'warning',
        duration: 2000
      });
    },
    // 文件改变
    changeFile(file, fileList) {
      let flag = false;
      this.form.files.forEach(element => {
        if (element.name === file.name) {
          flag = true;
          ElMessage({
            message: '文件重复',
            type: 'warning',
            duration: 2000
          });
        }
      });
      this.form.files = fileList;
      if (flag) return this.form.files.pop();
      ElMessage({
        message: '添加成功',
        type: 'success',
        duration: 2000
      });
    },
    // 移除文件
    removeFile(file, fileList) {
      console.log(file);
      this.form.files = fileList;
    },
    // 数据选中
    handleSelectionChange(list) {
      // 改变按钮组显示
      list.length == 0 ? this.isUpload = true : this.isUpload = false;
      // 已选中复选框
      this.myFileList = [];
      list.forEach(element => {
        this.myFileList.push(element)
      });
      console.log(this.myFileList);
    },
    // 行点击选中
    click_row(row) {
      // console.log(row);
      let index = this.myFileList.findIndex(item => {
        return item == row;  // 判断已选数组中是否已存在该条数据
      })
      if(index === -1) {
        this.$refs.multipleTable.toggleRowSelection(row, true);  // 设置复选框为选中状态
        // this.myFileList.push(row);
      } else {
        this.$refs.multipleTable.toggleRowSelection(row, false); //设置复选框为未选状态
        // this.myFileList.splice(index, 1)
      }
    },
    // 点击后改变行样式
    modality({row}) {
      let index = this.myFileList.findIndex(item => {
        return item == row;  // 判断已选数组中是否已存在该条数据
      });  //(row.energyEnd == null || row.energyStart == null || row.energy < 0)
      return index === -1 ? {background: 'white'} : {background: '#308eff'};
    },
    // 双击文件夹进入该文件夹
    async doubleClickFolder(row) {
      if(row.type === '文件夹') {
        // 根据该文件夹查询内部信息
        // 文件查询
        let catalogId = row.id;
        let temp = [];
        temp.push(catalogId);
        const resQueryFile = await selectFileAndCatalog(temp);
        this.folderList = resQueryFile.data.catalog;
        this.fileList = resQueryFile.data.file;
        this.allDateInfo();
      } else return alert('非目录！');
      this.isTitle = false;
      // 面包屑
      let titleHead = {};
      titleHead.title = row.name;
      titleHead.catalogId = row.id;
      this.titleList.push(titleHead);
      console.log(this.titleList);
      // 更改目录id
      this.catalogId = row.id;
      this.form.catalogId = row.id;
    },
    // 点击面包屑标题
    async titleClick(catalogId, index) {
      console.log(catalogId);
      let temp = [];
      temp.push(catalogId);
      const resQueryFile = await selectFileAndCatalog(temp);
      this.folderList = resQueryFile.data.catalog;
      this.fileList = resQueryFile.data.file;
      this.allDateInfo();
      this.form.catalogId = catalogId;
      this.catalogId = catalogId;
      // 修改面包屑
      console.log("this.titleList");
      console.log(this.titleList);
      this.titleList.splice(index + 1, this.titleList.length - index - 1);
      console.log(this.titleList);
    },
    // 右键修改目录名
    rightClick(row) {
      if(row.type == '文件夹') {
        this.centerDialogVisible = true;
        this.newName = row.name;
        this.tempRow = row;
      }
      window.oncontextmenu = function(){return false;}
    },
    // 修改目录提交
    async updateDirectory() {
      let flag = false;
      this.dateInfo.forEach(element => {
        if(element.type == '文件夹') {
          if(this.newName == element.name) {
            flag = true;
            ElMessage({
              message: '目录名重复！',
              type: 'warning',
              duration: 2000
            });
          }
        }
      });
      if(flag) return null;
      let temp = [];
      temp.push(this.tempRow.id);
      temp.push(this.newName);
      const res = await updateCatalog(temp);
      if(res.code == "200") {
        this.dateInfo[this.dateInfo.indexOf(this.tempRow)].name = this.newName;
        this.centerDialogVisible = false;
        return ElMessage({
          message: '修改成功！',
          type: 'success',
          duration: 2000
        })
      }
      return ElMessage({
        message: '修改失败！',
        type: 'warning',
        duration: 2000
      })
    },
    // 新建目录
    async createCatalog() {
      if (this.catalogName == null || this.catalogName == '') return ElMessage({
        message: '请输入目录名',
        type: 'warning',
        duration: 2000
      });
      let flag = false;
      this.dateInfo.forEach(element => {
        if(element.type === '文件夹') {
          if(this.catalogName === element.name) {
            flag = true;
            ElMessage({
              message: '目录名重复！',
              type: 'warning',
              duration: 2000
            })
          }
        }
      });
      if(flag) return null;
      let temp = [];
      temp.push(this.form.catalogId);
      temp.push(this.catalogName);
      // 新建目录
      const res = await createCatalog(temp);
      if (res.code == "200" || res.code == '1') {
        ElMessage({
          message: '新建成功',
          type: 'success',
          duration: 2000
        });
        this.catalogName = '';
        // 文件查询
        let temp = [];
        temp.push(this.catalogId);
        const resQueryFile = await selectFileAndCatalog(temp);
        this.folderList = resQueryFile.data.catalog;
        this.fileList = resQueryFile.data.file;
        return this.allDateInfo();
      } return ElMessage({
        message: '新建失败',
        type: 'warning',
        duration: 2000
      });
    },
    // 删除目录/文件
    async deleteCatalogOrFile() {
      let catalogIdList = [];
      let deleteFile = [];
      this.myFileList.forEach(element => {
        if(element.type === '文件夹') catalogIdList.push(element);
        else {
          let temp = {};
          temp.fileName = element.name
          deleteFile.push(temp)
        };
      });
      console.log(catalogIdList);
      if(catalogIdList.length > 1) return ElMessage({
        message: '目录不唯一',
        type: 'warning',
        duration: 2000
      });
      // 删除目录
      if(catalogIdList.length == 1) {
        let catalogId = catalogIdList[0].id;
        let temp = [];
        temp.push(catalogId)
        let res = await deleteCatalog(temp);
        console.log(res);
        if(res.code == "200") this.dateInfo.splice(this.dateInfo.indexOf(catalogIdList[0]), 1)
        else return ElMessage({
          message: '删除失败！',
          type: 'warning',
          duration: 2000
        });
      }
      // 删除文件
      let res = await deleteFiles(deleteFile);
      if(res.code == "200" || res.code == '1') {
        // 文件查询
        let temp = [];
        temp.push(this.catalogId);
        const resQueryFile = await selectFileAndCatalog(temp);
        this.folderList = resQueryFile.data.catalog;
        this.fileList = resQueryFile.data.file;
        this.allDateInfo();
      } else return ElMessage({
        message: '文件未删除成功！',
        type: 'warning',
        duration: 2000
      });
      return ElMessage({
        message: '删除成功！',
        type: 'success',
        duration: 2000
      });
    },
    // 模糊查询
    async queryFilesByName() {
      if(this.queryFileByName == null || this.queryFileByName == '') return ElMessage({
        message: '不能为空',
        type: 'warning',
        duration: 2000
      });
      let temp = [];
      let fileName = this.queryFileByName;
      temp.push(fileName);
      const res = await selectLikeFile(temp);
      if(res.code == '200') {
        res.data.forEach(element => {
          let temp = {};
          temp.fileName = element;
          this.gridData.push(temp);
        });
      } else ElMessage({
        message: '查询失败',
        type: 'warning',
        duration: 2000
      });
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
    },
    // 用户
    querySearch(queryString, cb) {
      let restaurants = this.restaurants;
      let results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
        return (restaurant) => {
          return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
        };
      },
    // 类型判断，后续优化
    fileType(name) {
      let index = name.lastIndexOf('.');
      let ext = name.substr(index + 1).toLowerCase();
      if(ext == 'png' || ext == 'jpg' || ext == 'png' || ext == 'svg' || ext == 'gif' || ext == 'webp' || ext == 'jpeg') return '图片'
      else if (ext == 'txt' || ext == 'doc' || ext == 'docx' || ext == 'xls' || ext == 'xlsx' || ext == 'pptx' || ext == 'chm') return '文档'
      else if (ext == 'flv' || ext == 'rmvb' || ext == 'mp4' || ext == 'mvb' || ext == 'mov' || ext == 'rm') return '视频'
      else if (ext == 'wav' || ext == 'midi' || ext == 'cda' || ext == 'mp3' || ext == 'wma' || ext == 'vqf') return '音乐'
      else return '压缩包'
    },
    // 图标
    getIcon(type) {
      switch (type) {
        case '压缩包':
          return new URL('../../assets/icon/icon-zip-zip.svg', import.meta.url).href;
        case '文档':
          return new URL('../../assets/icon/icon-pdf.svg', import.meta.url).href;
        case '音乐':
          return new URL('../../assets/icon/icon-audio-music.svg', import.meta.url).href;
        case '图片':
          return new URL('../../assets/icon/icon-pic-img.svg', import.meta.url).href;
        case '视频':
          return new URL('../../assets/icon/icon-video-video.svg', import.meta.url).href;
        default:
          return new URL('../../assets/icon/icon-file.svg', import.meta.url).href;
      }
    }
  },
  watch: {
    async title() {
      switch(this.title) {
        case '全部':
          this.catalogId = '0';
          // 文件查询
          let temp = [];
          temp.push(this.catalogId);
          const resQueryFile = await selectFileAndCatalog(temp);
          this.folderList = resQueryFile.data.catalog;
          this.fileList = resQueryFile.data.file;
          this.allDateInfo();
          break;
        case '文档':
          this.catalogId = 1;
          this.dateInfo = [];
          break;
        case '图片':
          this.catalogId = 2;
          this.dateInfo = this.imageList;
          break;
        case '视频':
          this.catalogId = 3;
          this.dateInfo = [];
          break;
        case '音乐':
          this.catalogId = 4;
          this.dateInfo = [];
          break;
        case '我的分享':
          this.catalogId = 5;
          this.dateInfo = [];
          break;
      }
      console.log(this.catalogId);
      this.total = this.dateInfo.length;
      this.isTitle = true;
      this.titleList = [{title, catalogId}];
      this.form.catalogName = this.title;
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
  .mainContent {
    width: 100%;
    height: 100%;
  }
  .el-header {
    width: 100%;
    height: 90px;
    display: flex;
    border-bottom: #dcdfe6 1px solid;
  }
  .el-header div {
    flex: 1;
  }
  .upload {
    display: flex;
    line-height: 90px;
    padding-left: 5%;
  }
  .upload .el-button {
    height: 50px;
    color: #fff;
    margin-left: 3%;
    font-size: 19px;
    border-radius: 0;
  }
  .upload .el-input {
    width: 50%;
    height: 48px;
    font-size: 20px;
    border-radius: 0;
  }
  .setOf {
    width: 100%;
    height: 90px;
    display: flex;
    padding-left: 5%;
  }
  .setOf .el-button {
    height: 50px;
    width: 25%;
    margin-top: 5.5%;
    color: black;
    font-size: 19px;
    border-radius: 0;
  }
  .user {
    height: 100%;
    text-align: right;
    padding-right: 8%;
    font-size: 60px;
  }
  .user a {
    line-height: 90px;
    color: black;
    font-size: 20px;
    text-decoration: none;
    letter-spacing: 5px;
  }
  .el-main {
    padding: 0;
  }
  .el-main .title {
    padding-left: 4%;
    padding-top: 2%;
    padding-bottom: 2%;
  }
  .el-main .title .el-breadcrumb {
    font-size: 20px;
  }
  .el-main .table {
    /* padding-left: 4%; */
    padding-right: 5%;
  }
  .el-table a {
    color: black;
    font-size: 20px;
    font-weight: 300;
    text-decoration: none;
  }
  .el-table a:hover {
    text-decoration: underline;
    text-decoration-color: #2c2a2a;
    text-decoration-thickness: 1px;
  }
  .total {
    width: 100%;
    margin-top: 1%;
    text-align: center;
  }
  .total span {
    color: #777;
  }
</style>