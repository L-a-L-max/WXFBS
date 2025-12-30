# 获取授权用户的资料
GET https://gitee.com/api/v5/user

Response Class
"root":
"avatar_url": string
"bio": string
"blog": string
"bot_info":
"bot_type": string 机器人类型
"status": integer 状态：1-活跃 2-暂停 3-禁用
"created_at": string
"email": string
"events_url": string
"followers": integer 关注用户的人数
"followers_url": string
"following": integer 用户关注的人数
"following_url": string
"gists_url": string
"html_url": string
"id": integer
"login": string
"member_role": string
"name": string
"organizations_url": string
"public_gists": integer
"public_repos": integer
"received_events_url": string
"remark": string 企业备注名
"repos_url": string
"stared": integer 用户收藏仓库数
"starred_url": string
"subscriptions_url": string
"type": string
"updated_at": string
"url": string
"watched": integer 用户关注仓库数
"weibo": string

## Parameters
Parameter	Value	Description	Type	Data Type
access_token	a0f4d8a67ff89d2e54a533e2c8c9b7b	用户授权码	query	string

## Response Body
"root":
"id": 8441982
"login": "yjyyds"
"name": "姚健"
"avatar_url": "https://gitee.com/assets/no_portrait.png"
"url": "https://gitee.com/api/v5/users/yjyyds"
"html_url": "https://gitee.com/yjyyds"
"remark": ""
"followers_url": "https://gitee.com/api/v5/users/yjyyds/followers"
"following_url": "https://gitee.com/api/v5/users/yjyyds/following_url{/other_user}"
"gists_url": "https://gitee.com/api/v5/users/yjyyds/gists{/gist_id}"
"starred_url": "https://gitee.com/api/v5/users/yjyyds/starred{/owner}{/repo}"
"subscriptions_url": "https://gitee.com/api/v5/users/yjyyds/subscriptions"
"organizations_url": "https://gitee.com/api/v5/users/yjyyds/orgs"
"repos_url": "https://gitee.com/api/v5/users/yjyyds/repos"
"events_url": "https://gitee.com/api/v5/users/yjyyds/events{/privacy}"
"received_events_url": "https://gitee.com/api/v5/users/yjyyds/received_events"
"type": "User"
"blog": null
"weibo": null
"bio": ""
"public_repos": 5
"public_gists": 0
"followers": 0
"following": 0
"stared": 0
"watched": 0
"created_at": "2020-12-13T14:02:55+08:00"
"updated_at": "2025-12-30T12:34:00+08:00"
"email": null


# 获取授权用户的所有Issues
GET https://gitee.com/api/v5/user/issues
Response Class
"root":
"assignee":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string
"body": string 描述
"body_html": string 描述 html 格式
"branch": string 关联分支
"collaborators":
0:
"comments": integer 评论数量
"comments_url": string
"created_at": string 创建时间
"cve_id": string CVE identifier for security issues
"deadline": string 结束时间
"depth": integer 所在层级
"finished_at": string 完成时间
"html_url": string
"id": integer
"issue_state_detail":
"id": integer 状态 ID
"title": string 状态的名称
"color": string 状态的颜色
"icon": string 任务状态的 Icon
"command": string 任务状态的 指令
"serial": integer 任务状态的 排序
"created_at": string 任务状态创建时间
"updated_at": string 任务状态更新时间
"issue_type_detail":
"id": integer 任务类型 ID
"title": string 任务类型的名称
"template": string 任务类型模板
"ident": string 唯一标识符
"color": string 颜色
"is_system": boolean 是否系统默认类型
"created_at": string 任务类型创建时间
"updated_at": string 任务类型更新时间
"labels":
0:
"labels_url": string
"milestone":
"url": string
"html_url": string
"number": integer
"repository_id": integer
"state": string
"title": string
"description": string
"updated_at": string
"created_at": string
"open_issues": integer
"closed_issues": integer
"due_on": string
"number": string 唯一标识
"parent_id": integer 上级 id
"parent_url": string
"plan_started_at": string 计划开始时间
"priority": integer 优先级(0: 不指定 1: 不重要 2: 次要 3: 主要 4: 严重)
"program":
"id": integer 项目id
"name": string 项目名称
"description": string 项目描述
"assignee":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string
"author":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string
"repository":
"id": integer
"full_name": string
"human_name": string
"url": string
"namespace":
"id": integer namespace id
"type": string namespace 类型，企业：Enterprise，组织：Group，用户：null
"name": string namespace 名称
"path": string namespace 路径
"html_url": string namespace 地址
"path": string 仓库路径
"name": string 仓库名称
"owner":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string
"assigner":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string
"description": string 仓库描述
"private": boolean 是否私有
"public": boolean 是否公开
"internal": boolean 是否内部开源
"fork": boolean 是否是fork仓库
"html_url": string 仓库地址
"ssh_url": string
"forks_url": string
"keys_url": string
"collaborators_url": string
"hooks_url": string
"branches_url": string
"tags_url": string
"blobs_url": string
"stargazers_url": string
"contributors_url": string
"commits_url": string
"comments_url": string
"issue_comment_url": string
"issues_url": string
"pulls_url": string
"milestones_url": string
"notifications_url": string
"labels_url": string
"releases_url": string
"recommend": boolean 是否是推荐仓库
"gvp": boolean 是否是 GVP 仓库
"homepage": string 主页
"language": string 语言
"forks_count": integer 仓库fork数量
"stargazers_count": integer 仓库star数量
"watchers_count": integer 仓库watch数量
"default_branch": string 默认分支
"open_issues_count": integer 开启的issue数量
"has_issues": boolean 是否开启issue功能
"has_wiki": boolean 是否开启Wiki功能
"issue_comment": boolean 是否允许用户对“关闭”状态的 Issue 进行评论
"can_comment": boolean 是否允许用户对仓库进行评论
"pull_requests_enabled": boolean 是否接受 Pull Request，协作开发
"has_page": boolean 是否开启了 Pages
"license": string 开源许可
"outsourced": boolean 仓库类型（内部/外包）
"project_creator": string 仓库创建者的 username
"members":
0: string
"pushed_at": string 最近一次代码推送时间
"created_at": string
"updated_at": string
"parent":
"id": integer
"full_name": string
"human_name": string
"url": string
"namespace":
"id": integer namespace id
"type": string namespace 类型，企业：Enterprise，组织：Group，用户：null
"name": string namespace 名称
"path": string namespace 路径
"html_url": string namespace 地址
"path": string 仓库路径
"name": string 仓库名称
"owner":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string
"assigner":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string
"description": string 仓库描述
"private": boolean 是否私有
"public": boolean 是否公开
"internal": boolean 是否内部开源
"fork": boolean 是否是fork仓库
"html_url": string 仓库地址
"ssh_url": string
"forks_url": string
"keys_url": string
"collaborators_url": string
"hooks_url": string
"branches_url": string
"tags_url": string
"blobs_url": string
"stargazers_url": string
"contributors_url": string
"commits_url": string
"comments_url": string
"issue_comment_url": string
"issues_url": string
"pulls_url": string
"milestones_url": string
"notifications_url": string
"labels_url": string
"releases_url": string
"recommend": boolean 是否是推荐仓库
"gvp": boolean 是否是 GVP 仓库
"homepage": string 主页
"language": string 语言
"forks_count": integer 仓库fork数量
"stargazers_count": integer 仓库star数量
"watchers_count": integer 仓库watch数量
"default_branch": string 默认分支
"open_issues_count": integer 开启的issue数量
"has_issues": boolean 是否开启issue功能
"has_wiki": boolean 是否开启Wiki功能
"issue_comment": boolean 是否允许用户对“关闭”状态的 Issue 进行评论
"can_comment": boolean 是否允许用户对仓库进行评论
"pull_requests_enabled": boolean 是否接受 Pull Request，协作开发
"has_page": boolean 是否开启了 Pages
"license": string 开源许可
"outsourced": boolean 仓库类型（内部/外包）
"project_creator": string 仓库创建者的 username
"members":
0: string
"pushed_at": string 最近一次代码推送时间
"created_at": string
"updated_at": string
"paas": string
"stared": boolean 是否 star（此字段已废弃，固定返回 false）
"watched": boolean 是否 watch
"permission": Object 操作权限
"relation": string 当前用户相对于仓库的角色
"assignees_number": integer 代码审查设置，审查人数
"testers_number": integer 代码审查设置，测试人数
"assignee":
0:
"testers":
0:
"status": string 仓库状态
"programs":
0:
"enterprise":
"id": integer namespace id
"type": string namespace 类型，企业：Enterprise，组织：Group，用户：null
"name": string namespace 名称
"path": string namespace 路径
"html_url": string namespace 地址
"project_labels":
0:
"issue_template_source": string Issue 模版来源 project: 使用仓库 Issue Template 作为模版； enterprise: 使用企业工作项作为模版
"paas": string
"stared": boolean 是否 star（此字段已废弃，固定返回 false）
"watched": boolean 是否 watch
"permission": Object 操作权限
"relation": string 当前用户相对于仓库的角色
"assignees_number": integer 代码审查设置，审查人数
"testers_number": integer 代码审查设置，测试人数
"assignee":
0:
"testers":
0:
"status": string 仓库状态
"programs":
0:
"enterprise":
"id": integer namespace id
"type": string namespace 类型，企业：Enterprise，组织：Group，用户：null
"name": string namespace 名称
"path": string namespace 路径
"html_url": string namespace 地址
"project_labels":
0:
"issue_template_source": string Issue 模版来源 project: 使用仓库 Issue Template 作为模版； enterprise: 使用企业工作项作为模版
"repository_url": string
"scheduled_time": integer 预计工期
"security_hole": boolean 是否为私有issue
"state": string 状态
"title": string 标题
"updated_at": string 更新时间
"url": string
"user":
"id": integer
"login": string
"name": string
"avatar_url": string
"url": string
"html_url": string
"remark": string 企业备注名
"followers_url": string
"following_url": string
"gists_url": string
"starred_url": string
"subscriptions_url": string
"organizations_url": string
"repos_url": string
"events_url": string
"received_events_url": string
"type": string
"member_role": string

## 接口请求参数表

# 列出授权用户的所有仓库
GET https://gitee.com/api/v5/user/repos

## Response Class
"root":
"assignee": 1 item
"assignees_number": integer 代码审查设置，审查人数
"assigner": 18 properties
"blobs_url": string
"branches_url": string
"can_comment": boolean 是否允许用户对仓库进行评论
"collaborators_url": string
"comments_url": string
"commits_url": string
"contributors_url": string
"created_at": string
"default_branch": string 默认分支
"description": string 仓库描述
"enterprise": 5 properties
"fork": boolean 是否是fork仓库
"forks_count": integer 仓库fork数量
"forks_url": string
"full_name": string
"gvp": boolean 是否是 GVP 仓库
"has_issues": boolean 是否开启issue功能
"has_page": boolean 是否开启了 Pages
"has_wiki": boolean 是否开启Wiki功能
"homepage": string 主页
"hooks_url": string
"html_url": string 仓库地址
"human_name": string
"id": integer
"internal": boolean 是否内部开源
"issue_comment": boolean 是否允许用户对“关闭”状态的 Issue 进行评论
"issue_comment_url": string
"issue_template_source": string Issue 模版来源 project: 使用仓库 Issue Template 作为模版； enterprise: 使用企业工作项作为模版
"issues_url": string
"keys_url": string
"labels_url": string
"language": string 语言
"license": string 开源许可
"members": 1 item
"milestones_url": string
"name": string 仓库名称
"namespace": 5 properties
"notifications_url": string
"open_issues_count": integer 开启的issue数量
"outsourced": boolean 仓库类型（内部/外包）
"owner": 18 properties
"paas": string
"parent": 70 properties
"path": string 仓库路径
"permission": Object 操作权限
"private": boolean 是否私有
"programs": 1 item
"project_creator": string 仓库创建者的 username
"project_labels": 1 item
"public": boolean 是否公开
"pull_requests_enabled": boolean 是否接受 Pull Request，协作开发
"pulls_url": string
"pushed_at": string 最近一次代码推送时间
"recommend": boolean 是否是推荐仓库
"relation": string 当前用户相对于仓库的角色
"releases_url": string
"ssh_url": string
"stared": boolean 是否 star（此字段已废弃，固定返回 false）
"stargazers_count": integer 仓库star数量
"stargazers_url": string
"status": string 仓库状态
"tags_url": string
"testers": 1 item
"testers_number": integer 代码审查设置，测试人数
"updated_at": string
"url": string
"watched": boolean 是否 watch
"watchers_count": integer 仓库watch数量

## Parameters
Parameter	Value	Description	Type	Data Type
access_token	a0f4d8a67ff89d2e54a533e2c8c9b7b	用户授权码	query	string
visibility	all	公开 (public)、私有 (private) 或者所有 (all)，默认：所有 (all)	query	string
affiliation		owner (授权用户拥有的仓库)、collaborator (授权用户为仓库成员)、organization_member (授权用户为所在组织所有访问仓库权限)、enterprise_member (授权用户为授权用户所在企业并有访问仓库权限)、admin (所有有权限的仓库，包括所管理的组织中所有仓库、所管理的企业的所有仓库。可以用逗号分隔组合。如 owner, collaborator, organization_member)	query	string
type	请选择	筛选用户仓库，其创建 (owner)、个人 (personal)、其为成员 (member)、公开 (public)、私有 (private)，不能与 visibility 或 affiliation 参数一并使用，否则会报 422 错误	query	string
sort	full_name	排序方式：创建时间 (created)，更新时间 (updated)，最后推送时间 (pushed)，仓库所属与名称 (full_name)。默认:full_name	query	string
direction	请选择	如果 sort 参数为 full_name，用升序 (asc)。否则降序 (desc)	query	string
q		搜索关键字	query	string
page	1	当前的页码	query	integer
per_page	20	每页的数量，最大为 100	query	integer

## Response Body
"root":
0:
"id": 19046675
"full_name": "mycyy1/ShoppingSystem"
"human_name": "从前慢y/拼夕夕_网上商城"
"url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem"
"namespace":
"id": 7931763
"type": "personal"
"name": "从前慢y"
"path": "mycyy1"
"html_url": "https://gitee.com/mycyy1"
"path": "ShoppingSystem"
"name": "拼夕夕_网上商城"
"owner":
"id": 9257401
"login": "mycyy1"
"name": "从前慢y"
"avatar_url": "https://foruda.gitee.com/avatar/1677169305162449848/9257401_mycyy1_1636037466.png"
"url": "https://gitee.com/api/v5/users/mycyy1"
"html_url": "https://gitee.com/mycyy1"
"remark": ""
"followers_url": "https://gitee.com/api/v5/users/mycyy1/followers"
"following_url": "https://gitee.com/api/v5/users/mycyy1/following_url{/other_user}"
"gists_url": "https://gitee.com/api/v5/users/mycyy1/gists{/gist_id}"
"starred_url": "https://gitee.com/api/v5/users/mycyy1/starred{/owner}{/repo}"
"subscriptions_url": "https://gitee.com/api/v5/users/mycyy1/subscriptions"
"organizations_url": "https://gitee.com/api/v5/users/mycyy1/orgs"
"repos_url": "https://gitee.com/api/v5/users/mycyy1/repos"
"events_url": "https://gitee.com/api/v5/users/mycyy1/events{/privacy}"
"received_events_url": "https://gitee.com/api/v5/users/mycyy1/received_events"
"type": "User"
"assigner":
"id": 9257401
"login": "mycyy1"
"name": "从前慢y"
"avatar_url": "https://foruda.gitee.com/avatar/1677169305162449848/9257401_mycyy1_1636037466.png"
"url": "https://gitee.com/api/v5/users/mycyy1"
"html_url": "https://gitee.com/mycyy1"
"remark": ""
"followers_url": "https://gitee.com/api/v5/users/mycyy1/followers"
"following_url": "https://gitee.com/api/v5/users/mycyy1/following_url{/other_user}"
"gists_url": "https://gitee.com/api/v5/users/mycyy1/gists{/gist_id}"
"starred_url": "https://gitee.com/api/v5/users/mycyy1/starred{/owner}{/repo}"
"subscriptions_url": "https://gitee.com/api/v5/users/mycyy1/subscriptions"
"organizations_url": "https://gitee.com/api/v5/users/mycyy1/orgs"
"repos_url": "https://gitee.com/api/v5/users/mycyy1/repos"
"events_url": "https://gitee.com/api/v5/users/mycyy1/events{/privacy}"
"received_events_url": "https://gitee.com/api/v5/users/mycyy1/received_events"
"type": "User"
"description": "基于SpringBoot+Mybatis+Vue+Element开发的网上商城"
"private": false
"public": true
"internal": false
"fork": false
"html_url": "https://gitee.com/mycyy1/ShoppingSystem.git"
"ssh_url": "git@gitee.com:mycyy1/ShoppingSystem.git"
"forks_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/forks"
"keys_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/keys{/key_id}"
"collaborators_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/collaborators{/collaborator}"
"hooks_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/hooks"
"branches_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/branches{/branch}"
"tags_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/tags"
"blobs_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/blobs{/sha}"
"stargazers_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/stargazers"
"contributors_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/contributors"
"commits_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/commits{/sha}"
"comments_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/comments{/number}"
"issue_comment_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/issues/comments{/number}"
"issues_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/issues{/number}"
"pulls_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/pulls{/number}"
"milestones_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/milestones{/number}"
"notifications_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/notifications{?since,all,participating}"
"labels_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/labels{/name}"
"releases_url": "https://gitee.com/api/v5/repos/mycyy1/ShoppingSystem/releases{/id}"
"recommend": false
"gvp": false
"homepage": ""
"language": null
"forks_count": 4
"stargazers_count": 10
"watchers_count": 7
"default_branch": "master"
"open_issues_count": 0
"has_issues": true
"has_wiki": true
"issue_comment": false
"can_comment": true
"pull_requests_enabled": true
"has_page": false
"license": "MulanPSL-2.0"
"outsourced": false
"project_creator": "mycyy1"
"members":
0: "ZTian111"
1: "yjyyds"
2: "tan-jiajian"
3: "zqf2437320007"
4: "guet-lxq"
5: "linjiawin"
6: "mycyy1"
"pushed_at": "2021-12-24T16:15:58+08:00"
"created_at": "2021-11-04T22:55:52+08:00"
"updated_at": "2024-10-16T16:49:23+08:00"
"parent": null
"paas": null
"stared": false
"watched": true
"permission":
"pull": true
"push": true
"admin": false
"relation": "developer"
"assignees_number": 1
"testers_number": 1
"assignee":
0:
"id": 9257401
"login": "mycyy1"
"name": "从前慢y"
"avatar_url": "https://foruda.gitee.com/avatar/1677169305162449848/9257401_mycyy1_1636037466.png"
"url": "https://gitee.com/api/v5/users/mycyy1"
"html_url": "https://gitee.com/mycyy1"
"remark": ""
"followers_url": "https://gitee.com/api/v5/users/mycyy1/followers"
"following_url": "https://gitee.com/api/v5/users/mycyy1/following_url{/other_user}"
"gists_url": "https://gitee.com/api/v5/users/mycyy1/gists{/gist_id}"
"starred_url": "https://gitee.com/api/v5/users/mycyy1/starred{/owner}{/repo}"
"subscriptions_url": "https://gitee.com/api/v5/users/mycyy1/subscriptions"
"organizations_url": "https://gitee.com/api/v5/users/mycyy1/orgs"
"repos_url": "https://gitee.com/api/v5/users/mycyy1/repos"
"events_url": "https://gitee.com/api/v5/users/mycyy1/events{/privacy}"
"received_events_url": "https://gitee.com/api/v5/users/mycyy1/received_events"
"type": "User"
"testers":
0:
"id": 9257401
"login": "mycyy1"
"name": "从前慢y"
"avatar_url": "https://foruda.gitee.com/avatar/1677169305162449848/9257401_mycyy1_1636037466.png"
"url": "https://gitee.com/api/v5/users/mycyy1"
"html_url": "https://gitee.com/mycyy1"
"remark": ""
"followers_url": "https://gitee.com/api/v5/users/mycyy1/followers"
"following_url": "https://gitee.com/api/v5/users/mycyy1/following_url{/other_user}"
"gists_url": "https://gitee.com/api/v5/users/mycyy1/gists{/gist_id}"
"starred_url": "https://gitee.com/api/v5/users/mycyy1/starred{/owner}{/repo}"
"subscriptions_url": "https://gitee.com/api/v5/users/mycyy1/subscriptions"
"organizations_url": "https://gitee.com/api/v5/users/mycyy1/orgs"
"repos_url": "https://gitee.com/api/v5/users/mycyy1/repos"
"events_url": "https://gitee.com/api/v5/users/mycyy1/events{/privacy}"
"received_events_url": "https://gitee.com/api/v5/users/mycyy1/received_events"
"type": "User"
"status": "开始"
"programs":
"enterprise": null
"project_labels":
"issue_template_source": "project"
1: 71 properties
2: 71 properties
3: 71 properties
4: 71 properties
5: 71 properties
6: 71 properties
7: 71 properties
8: 71 properties
9: 71 properties

# 列出授权用户的所有通知
GET https://gitee.com/api/v5/notifications/threads

## Parameters
Parameter	Value	Description	Type	Data Type
access_token	a0f4d8a67ff89d2e54a533e2c8c9b7b	用户授权码	query	string
unread	请选择	是否只获取未读消息，默认：否	query	boolean
participating	请选择	是否只获取自己直接参与的消息，默认：否	query	boolean
type	all	筛选消息类型的通知，all：所有，event：事件通知，referer：@通知	query	string
since		只获取在给定时间后更新的消息，要求时间格式为 ISO 8601	query	string
before		只获取在给定时间前更新的消息，要求时间格式为 ISO 8601	query	string
ids		指定一组通知 ID，以，分隔	query	string
page	1	当前的页码	query	integer
per_page	20	每页的数量，最大为 100

## Response Body
"root":
"total_count": 12
"list":
0:
"id": 566909445
"content": "Yer11214 更改了任务 #I6VUSA 秒杀活动为什么不能在前端创建了，是只能调用端口创建了吗？ 的状态为 已完成"
"type": "issue_state_change"
"unread": true
"mute": false
"updated_at": "2023-07-04T10:32:28+08:00"
"url": "https://gitee.com/api/v5/notification/threads/566909445"
"html_url": "https://gitee.com/beijing_hongye_huicheng/lilishop-uniapp/issues/I6VUSA"
"actor":
"id": 2056053
"login": "Yer11214"
"name": "Yer11214"
"avatar_url": "https://foruda.gitee.com/avatar/1677654072383960868/2056053_lrelia_1677654072.png"
"url": "https://gitee.com/api/v5/users/Yer11214"
"html_url": "https://gitee.com/Yer11214"
"remark": ""
"followers_url": "https://gitee.com/api/v5/users/Yer11214/followers"
"following_url": "https://gitee.com/api/v5/users/Yer11214/following_url{/other_user}"
"gists_url": "https://gitee.com/api/v5/users/Yer11214/gists{/gist_id}"
"starred_url": "https://gitee.com/api/v5/users/Yer11214/starred{/owner}{/repo}"
"subscriptions_url": "https://gitee.com/api/v5/users/Yer11214/subscriptions"
"organizations_url": "https://gitee.com/api/v5/users/Yer11214/orgs"
"repos_url": "https://gitee.com/api/v5/users/Yer11214/repos"
"events_url": "https://gitee.com/api/v5/users/Yer11214/events{/privacy}"
"received_events_url": "https://gitee.com/api/v5/users/Yer11214/received_events"
"type": "User"
"repository":
"id": 15836011
"full_name": "beijing_hongye_huicheng/lilishop-uniapp"
"human_name": "Lilishop商城/lilishop 商城 小程序 uni 移动端"
"url": "https://gitee.com/api/v5/repos/beijing_hongye_huicheng/lilishop-uniapp"
"namespace": 5 properties
"path": "lilishop-uniapp"
"name": "lilishop 商城 小程序 uni 移动端"
"owner": 17 properties
"assigner": 17 properties
"description": "lilishop电商商城系统 商城移动端，使用Uniapp开发，可编译为所有移动终端项目及各小程序。java商城基于SpringBoot 开源商城，更有微服务商城、商城中台、SAAS商城 。模式包含 B2B2C商城 S2B2C商城 O2O商城 B2B商城 多语言商城 跨境电商 分销商城 积分商城 内购商城。"
"private": false
"public": true
"internal": false
"fork": false
"html_url": "https://gitee.com/beijing_hongye_huicheng/lilishop-uniapp.git"
"ssh_url": "git@gitee.com:beijing_hongye_huicheng/lilishop-uniapp.git"
"subject":
"title": "秒杀活动为什么不能在前端创建了，是只能调用端口创建了吗？"
"url": "https://gitee.com/api/v5/repos/beijing_hongye_huicheng/lilishop-uniapp/issues/I6VUSA"
"latest_comment_url": null
"type": "Issue"
"namespaces":
0: 3 properties
1: 12 properties
2: 12 properties
3: 12 properties
4: 12 properties
5: 12 properties
6: 12 properties
7: 12 properties
8: 12 properties
9: 12 properties
10: 12 properties
11: 12 properties