import request from '@/utils/request'

export function getPlayWrighDrafts(query) {
  return request({
    url: '/aigc/getPlayWrighDrafts',
    method: 'get',
    params: query
  })
}
export function getNodeLog(query) {
  return request({
    url: '/aigc/getNodeLog',
    method: 'get',
    params: query
  })
}




export function message(data) {
  return request({
    url: '/aigc/message',
    method: 'post',
    data: data
  })
}

// 获取评分拼接提示词
export function getScoreWord() {
  return request({
    url: `/media/getScoreWord`,
    method: 'get'
  })
}

// 获取媒体平台提示词
export function getMediaCallWord(platformId) {
  return request({
    url: `/media/getCallWord/${platformId}`,
    method: 'get'
  })
}


// 获取媒体平台提示词列表
export function getMediaCallWordList(params) {
  return request({
    url: `/media/getCallWordList`,
    method: 'get',
    params,
  })
}


// 更新媒体平台提示词
export function updateMediaCallWord(data) {
  return request({
    url: `/media/updateCallWord`,
    method: 'put',
    data,
  })
}

// 删除媒体平台提示词
export function deleteMediaCallWord(platformIds) {
  return request({
    url: `/media/deleteCallWord`,
    method: 'delete',
    data: platformIds,
  })
}

// 设置平台提示词为公共/私有
export function setCallWordCommon(platformId, isCommon) {
  return request({
    url: `/media/setCallWordCommon/${platformId}/${isCommon}`,
    method: 'put',
  })
}

// 上架平台提示词
export function publishCallWord(data) {
  return request({
    url: `/media/publishCallWord`,
    method: 'put',
    data,
  })
}

export function unpublishCallWord(platformId) {
  return request({
    url: `/media/unpublishCallWord/${platformId}`,
    method: 'put',
  })
}

// 根据ID获取评分提示词
export function getScorePrompt(id) {
  return request({
    url: `/mini/getScorePrompt/${id}`,
    method: 'get'
  })
}

// 根据所有评分提示词
export function getAllScorePrompt() {
  return request({
    url: `/mini/getAllScorePrompt`,
    method: 'get'
  })
}


// 获取评分提示词列表
export function getScorePromptList(params) {
  return request({
    url: `/mini/getScorePromptList`,
    method: 'get',
    params,
  })
}

// 新增评分提示词
export function saveScorePrompt(data) {
  return request({
    url: `/mini/saveScorePrompt`,
    method: 'post',
    data,
  })
}

// 更新评分提示词
export function updateScorePrompt(data) {
  return request({
    url: `/mini/updateScorePrompt`,
    method: 'put',
    data,
  })
}

// 删除评分提示词
export function deleteScorePrompt(ids) {
  return request({
    url: `/mini/deleteScorePrompt`,
    method: 'delete',
    data: ids,
  })
}

// 设置评分模板为公共/私有
export function setScorePromptCommon(id, isCommon) {
  return request({
    url: `/mini/setScorePromptCommon/${id}/${isCommon}`,
    method: 'put',
  })
}

// 根据ID获取思路提示词
export function getIdeaPrompt(id) {
  return request({
    url: `/mini/getIdeaPrompt/${id}`,
    method: 'get'
  })
}

// 根据所有思路提示词
export function getAllIdeaPrompt() {
  return request({
    url: `/mini/getAllIdeaPrompt`,
    method: 'get'
  })
}


// 获取思路提示词列表
export function getIdeaPromptList(params) {
  return request({
    url: `/mini/getIdeaPromptList`,
    method: 'get',
    params,
  })
}

// 新增思路提示词
export function saveIdeaPrompt(data) {
  return request({
    url: `/mini/saveIdeaPrompt`,
    method: 'post',
    data,
  })
}

// 更新思路提示词
export function updateIdeaPrompt(data) {
  return request({
    url: `/mini/updateIdeaPrompt`,
    method: 'put',
    data,
  })
}

// 删除思路提示词
export function deleteIdeaPrompt(ids) {
  return request({
    url: `/mini/deleteIdeaPrompt`,
    method: 'delete',
    data: ids,
  })
}

// 设置思路模板为公共/私有
export function setIdeaPromptCommon(id, isCommon) {
  return request({
    url: `/mini/setIdeaPromptCommon/${id}/${isCommon}`,
    method: 'put',
  })
}

// 根据ID获取文章提示词
export function getArtPrompt(id) {
  return request({
    url: `/mini/getArtPrompt/${id}`,
    method: 'get'
  })
}

// 根据所有文章提示词
export function getAllArtPrompt() {
  return request({
    url: `/mini/getAllArtPrompt`,
    method: 'get'
  })
}


// 获取文章提示词列表
export function getArtPromptList(params) {
  return request({
    url: `/mini/getArtPromptList`,
    method: 'get',
    params,
  })
}

// 新增文章提示词
export function saveArtPrompt(data) {
  return request({
    url: `/mini/saveArtPrompt`,
    method: 'post',
    data,
  })
}

// 更新文章提示词
export function updateArtPrompt(data) {
  return request({
    url: `/mini/updateArtPrompt`,
    method: 'put',
    data,
  })
}

// 删除文章提示词
export function deleteArtPrompt(ids) {
  return request({
    url: `/mini/deleteArtPrompt`,
    method: 'delete',
    data: ids,
  })
}

// 设置文章模板为公共/私有
export function setArtPromptCommon(id, isCommon) {
  return request({
    url: `/mini/setArtPromptCommon/${id}/${isCommon}`,
    method: 'put',
  })
}

export function saveUserChatData(data) {
  return request({
    url: '/aigc/saveUserChatData',
    method: 'post',
    data: data
  })
}

export function getChatHistory(userId,isAll) {
  return request({
    url: '/aigc/getChatHistory?userId=' + userId + '&isAll=' + isAll,
    method: 'get'
  })
}
export function pushAutoOffice(data) {
  return request({
    url: '/mini/pushAutoOffice',
    method: 'post',
    data: data
  })
}
