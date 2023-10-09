import { get, post, getDynamicynamic, getFileUseBlobByPost } from './index'

/**
 * @function 文件查询
 * @param {String} 目录名
 */
export const selectFileAndCatalog = p => getDynamicynamic('file/selectFileAndCatalog', p);

/**
 * @function 新建目录
 * @param {String} 父目录名，新建目录名字
 */
export const createCatalog = p => getDynamicynamic('file/createCatalog', p);

/**
 * @function 目录名修改
 * @param {String} 老目录名，修改之后的目录名
 */
export const updateCatalog = p => getDynamicynamic('file/updateCatalog', p);

/**
 * @function 目录名删除
 * @param {String} 删除目录的名字
 */
export const deleteCatalog = p => getDynamicynamic('file/deleteCatalog', p);

/**
 * @function 文件上传
 * @param {String, File} 目录名，文件
 * @type {Map} 集合
 */
export const upload = p => post('minio/upload', p);

/**
 * @function 文件下载
 * @param {String} 文件名
 * @type {List} 数组
 */
// export const downloadAny = p => getDynamicynamic('minio/download', p);
// export const downloadMultiple = p => post('minio/downloadMultiFileToMinIO', p);

/**
 * @function 文件删除
 * @param {String} 文件名
 * @type {List} 数组
 */
export const deleteFiles = p => post('minio/deleteFiles', p);

/**
 * @function 文件分享
 * @param {String} 用户id，目录名
 * @type {Map} 集合
 */
export const shareFiles = p => post('minio/shareFiles', p);

/**
 * @function 文件查询
 * @param {String} 文件名
 */
export const selectLikeFile = p => getDynamicynamic('file/selectLikeFile', p);