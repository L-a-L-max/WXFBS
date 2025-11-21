<template>
	<view class="container">
		<view class="swiper-container">
			<swiper class="swiper" circular :indicator-dots="true" :autoplay="true" interval="3000" duration="500">
				<swiper-item v-for="(item, index) in banners" :key="index">
					<image :src="item.image" mode="aspectFill" class="swiper-image" />
				</swiper-item>
			</swiper>
		</view>
		<scroll-view class="content" scroll-y>
			<view class="section">
				<view class="section-title">
					<uni-icons type="star" size="24" color="#4169E1" />
					<text class="title-text">优立方AI主机</text>
				</view>
				<view class="feature-cards">
					<!-- 🔥 离线AI添加offline-card类，显示半透明效果 -->
					<view class="feature-card" :class="{ 'offline-card': feature.isOffline }" v-for="(feature, index) in features" :key="index">
						<view class="feature-header">
							<image :src="feature.icon" mode="aspectFit" class="feature-icon" :class="{ 'offline-icon': feature.isOffline }" />
							<!-- 🔥 显示AI在线/离线状态 -->
							<view class="status-badge" :style="{ backgroundColor: feature.statusColor }">
								<text class="status-text">{{ feature.statusLabel }}</text>
							</view>
						</view>
						<text class="feature-title">{{ feature.title }}</text>
						<text class="feature-desc">{{ feature.description }}</text>
						<!-- 🔥 离线提示 -->
						<text v-if="feature.isOffline" class="offline-tip">暂时无法使用</text>
					</view>
				</view>
			</view>
			<view class="section">
				<!-- <view class="section-title">
					<uni-icons type="medal" size="24" color="#4169E1" />
					<text class="title-text">系统优势</text>
				</view> -->
				<view class="advantage-list">
					<view class="advantage-item" v-for="(advantage, index) in advantages" :key="index">
						<view class="advantage-icon-wrapper">
							<uni-icons :type="advantage.icon" size="24" color="#4169E1" />
						</view>
						<view class="advantage-content">
							<text class="advantage-title">{{ advantage.title }}</text>
							<text class="advantage-desc">{{ advantage.description }}</text>
						</view>
					</view>
				</view>
			</view>
			<view class="section">
				<!-- <view class="section-title">
					<uni-icons type="gear" size="24" color="#4169E1" />
					<text class="title-text">应用场景</text>
				</view> -->
				<view class="scenario-grid">
					<view class="scenario-card" v-for="(scenario, index) in scenarios" :key="index">
						<image :src="scenario.image" mode="aspectFill" class="scenario-image" />
						<view class="scenario-info">
							<text class="scenario-title">{{ scenario.title }}</text>
							<text class="scenario-desc">{{ scenario.description }}</text>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>
<script>
import { mapState, mapActions } from 'vuex'
import storage from '@/utils/storage'
import constant from '@/utils/constant'

export default {
	data() {
		return {
			banners: [{
				image: 'https://ai-public.mastergo.com/ai/img_res/1747114705cd89a0b636d09b6117fc5f.jpg'
			},
			{
				image: 'https://ai-public.mastergo.com/ai/img_res/21749e007ca9c553e43e57e2ca755cbb.jpg'
			},
			{
				image: 'https://ai-public.mastergo.com/ai/img_res/ea68981430e1307646a14e900ce6d3e6.jpg'
			}
			],
			advantages: [],
			scenarios: [],
			// 用户信息
			userId: storage.get(constant.userId) || null
		}
	},
	computed: {
		...mapState('aiagent', {
			aiListFromStore: 'aiList',
			loading: 'loading',
			isUserSpecific: 'isUserSpecific'
		}),
		// 从store获取AI列表，转换为features格式
		features() {
			return this.aiListFromStore.map(ai => ({
				name: ai.name,
				avatar: ai.avatar,
				icon: ai.avatar,
				title: ai.name,
				description: ai.configJson?.description || ai.name,
				type: ai.agentCode,
				agentCode: ai.agentCode,
				onlineStatus: ai.onlineStatus,
				agentStatus: ai.agentStatus,
				// 🔥 显示在线/离线状态标识，优化视觉效果
				statusLabel: ai.onlineStatus === 1 ? '在线' : '离线',
				statusColor: ai.onlineStatus === 1 ? '#67c23a' : '#f56c6c',  // 在线=绿色，离线=红色
				// 🔥 添加离线状态的额外标识
				isOffline: ai.onlineStatus !== 1
			}))
		},
		// 是否已登录
		isLoggedIn() {
			return !!this.userId
		}
	},
	onLoad() {
		// 页面加载时获取最新用户ID
		this.userId = storage.get(constant.userId) || null
		this.loadAIList()
	},
	onShow() {
		// 每次显示页面时重新加载，确保状态同步
		console.log('📍 [小程序主页] 页面显示，重新加载AI列表')
		// 更新用户登录状态
		this.userId = storage.get(constant.userId) || null
		this.loadAIList()
	},
	methods: {
		...mapActions('aiagent', ['loadAvailableAiList', 'loadAllActiveAiList']),
		async loadAIList() {
			if (this.isLoggedIn) {
				// 已登录：加载用户可用的AI列表
				console.log('✅ [小程序主页] 用户已登录（ID:' + this.userId + '），加载用户可用AI列表')
				await this.loadAvailableAiList()
				console.log('✅ [小程序主页] 已加载', this.features.length, '个用户可用AI')
			} else {
				// 未登录：加载所有上架的AI列表
				console.log('ℹ️ [小程序主页] 用户未登录，加载所有上架AI列表')
				await this.loadAllActiveAiList()
				console.log('✅ [小程序主页] 已加载', this.features.length, '个上架AI')
			}
			
			// 🔥 输出AI状态详情（用于调试）
			if (this.features.length > 0) {
				this.features.forEach(ai => {
					const statusIcon = ai.onlineStatus === 1 ? '🟢' : '🔴'
					console.log(`${statusIcon} ${ai.name}: ${ai.statusLabel}`)
				})
			} else {
				console.warn('⚠️ [小程序主页] 未加载到AI列表，请检查后端服务')
			}
		}
	}
}
</script>
<style>
page {
	height: 100%;
}

.container {
	display: flex;
	flex-direction: column;
	height: 100%;
	background-color: #F5F7FA;
}

.swiper-container {
	flex-shrink: 0;
	height: 400rpx;
	background-color: #fff;
}

.swiper {
	width: 100%;
	height: 100%;
}

.swiper-image {
	width: 100%;
	height: 100%;
}

.content {
	flex: 1;
	overflow: auto;
}

.section {
	margin: 24rpx 16rpx;
	background: linear-gradient(135deg, #ffffff 0%, #f8faff 100%);
	border-radius: 20rpx;
	padding: 32rpx 24rpx;
	box-shadow:
		0 6rpx 24rpx rgba(0, 0, 0, 0.04),
		0 2rpx 12rpx rgba(0, 0, 0, 0.02);
	border: 1.5rpx solid rgba(102, 126, 234, 0.08);
	position: relative;
	overflow: hidden;
}

.section::before {
	content: '';
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	height: 3rpx;
	background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
	opacity: 0.6;
}

.section-title {
	display: flex;
	align-items: center;
	margin-bottom: 32rpx;
	position: relative;
	z-index: 2;
	justify-content: center;
}

.title-text {
	margin-left: 12rpx;
	font-size: 20px;
	font-weight: 700;
	color: #2d3748;
	letter-spacing: 0.5px;
	position: relative;
	background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
	-webkit-background-clip: text;
	-webkit-text-fill-color: transparent;
	background-clip: text;
}

.title-text::after {
	content: '';
	position: absolute;
	bottom: -8rpx;
	left: 0;
	width: 60rpx;
	height: 4rpx;
	background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
	border-radius: 2rpx;
	opacity: 0.7;
}

.feature-cards {
	display: grid;
	grid-template-columns: repeat(2, 1fr);
	gap: 20rpx;
	margin-top: 16rpx;
}

.feature-card {
	background: linear-gradient(145deg, #ffffff 0%, #f8faff 100%);
	padding: 24rpx 16rpx 20rpx;
	border-radius: 16rpx;
	display: flex;
	flex-direction: column;
	align-items: center;
	position: relative;
	overflow: hidden;
	box-shadow:
		0 4rpx 16rpx rgba(0, 0, 0, 0.05),
		0 1rpx 6rpx rgba(0, 0, 0, 0.03);
	border: 1.5rpx solid rgba(102, 126, 234, 0.1);
	transition: all 0.3s cubic-bezier(0.25, 0.46, 0.45, 0.94);
	min-height: 180rpx;
}

.feature-card::before {
	content: '';
	position: absolute;
	top: 0;
	left: 0;
	right: 0;
	height: 4rpx;
	background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
	opacity: 0.7;
	border-radius: 16rpx 16rpx 0 0;
}

.feature-card::after {
	content: '';
	position: absolute;
	top: -50%;
	left: -50%;
	width: 200%;
	height: 200%;
	background: radial-gradient(circle, rgba(102, 126, 234, 0.03) 0%, transparent 70%);
	opacity: 0;
	transition: opacity 0.4s ease;
}

.feature-card:hover {
	transform: translateY(-8rpx) scale(1.01);
	box-shadow:
		0 16rpx 32rpx rgba(102, 126, 234, 0.12),
		0 4rpx 16rpx rgba(0, 0, 0, 0.08);
	border-color: rgba(102, 126, 234, 0.15);
}

.feature-card:hover::after {
	opacity: 1;
}

/* 🔥 AI状态显示样式 */
.feature-header {
	position: relative;
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 16rpx;
}

.feature-icon {
	width: 56rpx;
	height: 56rpx;
	border-radius: 12rpx;
	box-shadow: 0 3rpx 10rpx rgba(0, 0, 0, 0.08);
	transition: all 0.3s ease;
	position: relative;
	z-index: 2;
}

.feature-card:hover .feature-icon {
	transform: translateY(-4rpx) scale(1.05);
	box-shadow: 0 8rpx 20rpx rgba(102, 126, 234, 0.15);
}

/* 状态徽章 */
.status-badge {
	margin-top: 8rpx;
	padding: 4rpx 12rpx;
	border-radius: 16rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.08);
	transition: all 0.3s ease;
}

.status-text {
	font-size: 10px;
	color: #ffffff;
	font-weight: 600;
	letter-spacing: 0.3px;
}

/* 🔥 离线AI样式 */
.offline-card {
	opacity: 0.6;
	filter: grayscale(30%);
	position: relative;
}

.offline-card::after {
	opacity: 0.3;
}

.offline-icon {
	opacity: 0.7;
	filter: grayscale(40%);
}

.offline-tip {
	margin-top: 12rpx;
	font-size: 10px;
	color: #f56c6c;
	text-align: center;
	font-weight: 500;
	padding: 3rpx 10rpx;
	background-color: rgba(245, 108, 108, 0.1);
	border-radius: 12rpx;
	position: relative;
	z-index: 2;
}

.feature-title {
	font-size: 14px;
	font-weight: 600;
	color: #2d3748;
	margin-bottom: 8rpx;
	letter-spacing: 0.2px;
	text-align: center;
	position: relative;
	z-index: 2;
	line-height: 1.3;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	max-width: 100%;
}

.feature-desc {
	font-size: 11px;
	color: #64748b;
	text-align: center;
	line-height: 1.5;
	position: relative;
	z-index: 2;
	opacity: 0.8;
	display: -webkit-box;
	-webkit-line-clamp: 2;
	-webkit-box-orient: vertical;
	overflow: hidden;
	text-overflow: ellipsis;
}

.advantage-list {
	display: flex;
	flex-direction: column;
	gap: 20rpx;
}

.advantage-item {
	display: flex;
	align-items: flex-start;
	padding: 20rpx;
	background-color: #F8F9FF;
	border-radius: 12rpx;
}

.advantage-icon-wrapper {
	flex-shrink: 0;
	width: 80rpx;
	height: 80rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	background-color: #E6EFFF;
	border-radius: 50%;
}

.advantage-content {
	margin-left: 20rpx;
}

.advantage-title {
	font-size: 16px;
	font-weight: bold;
	color: #333;
	margin-bottom: 8rpx;
}

.advantage-desc {
	font-size: 14px;
	color: #666;
}

.scenario-grid {
	display: flex;
	flex-wrap: wrap;
	gap: 20rpx;
}

.scenario-card {
	flex: 1;
	min-width: 280rpx;
	background-color: #fff;
	border-radius: 12rpx;
	overflow: hidden;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.scenario-image {
	width: 100%;
	height: 200rpx;
}

.scenario-info {
	padding: 20rpx;
}

.scenario-title {
	font-size: 16px;
	font-weight: bold;
	color: #333;
	margin-bottom: 8rpx;
}

.scenario-desc {
	font-size: 14px;
	color: #666;
}
</style>
