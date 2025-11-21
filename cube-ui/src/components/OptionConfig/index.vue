<template>
  <el-dialog 
    title="AI选项配置" 
    v-model="visible" 
    width="1200px" 
    append-to-body
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="option-config-container">
      <!-- 左侧：选项列表 -->
      <div class="left-panel">
        <div class="panel-header">
          <h3>选项列表</h3>
          <div class="header-actions">
            <el-dropdown @command="handleQuickAdd" size="small">
              <el-button type="success" size="small">
                <i class="el-icon-magic-stick"></i> 快速添加 <i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="model_select">模型选择</el-dropdown-item>
                  <el-dropdown-item command="search_mode">搜索模式</el-dropdown-item>
                  <el-dropdown-item command="output_format">输出格式</el-dropdown-item>
                  <el-dropdown-item command="enable_feature">功能开关</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button type="primary" size="small" @click="handleAddOption">
              <i class="el-icon-plus"></i> 自定义
            </el-button>
          </div>
        </div>
        
        <div class="options-list" v-if="options && options.length > 0">
          <div 
            v-for="(option, index) in options" 
            :key="option.id"
            class="option-card"
            :class="{ 'active': selectedIndex === index }"
            @click="selectOption(index)"
          >
            <div class="option-header">
              <span class="option-type" :class="'type-' + option.type">
                {{ getTypeLabel(option.type) }}
              </span>
              <span class="option-label">{{ option.label || '未命名选项' }}</span>
            </div>
            <div class="option-info">
              <span class="option-id">ID: {{ option.id || '未设置' }}</span>
              <div class="option-actions">
                <el-button type="text" size="small" @click.stop="editOption(index)">
                  <i class="el-icon-edit"></i>
                </el-button>
                <el-button type="text" size="small" @click.stop="deleteOption(index)">
                  <i class="el-icon-delete"></i>
                </el-button>
              </div>
            </div>
          </div>
        </div>
        
        <div v-else class="empty-state">
          <i class="el-icon-box"></i>
          <p>暂无选项配置</p>
          <p>点击"添加选项"开始配置</p>
        </div>
      </div>
      
      <!-- 右侧：选项编辑 -->
      <div class="right-panel">
        <div class="panel-header">
          <h3>{{ editingIndex >= 0 ? '编辑选项' : '添加选项' }}</h3>
          <div v-if="editingIndex >= 0">
            <el-button size="small" @click="cancelEdit">取消</el-button>
            <el-button type="primary" size="small" @click="saveOption">保存</el-button>
          </div>
        </div>
        
        <div v-if="editingIndex >= 0 || isAdding" class="option-form">
          <el-form :model="currentOption" :rules="rules" ref="optionForm" label-width="100px">
            <!-- 基础信息 -->
            <div class="form-section">
              <h4>基础信息</h4>
              <el-form-item label="选项类型" prop="type">
                <el-select v-model="currentOption.type" placeholder="请选择选项类型" @change="onTypeChange">
                  <el-option label="单选框" value="select" />
                  <el-option label="多选框" value="checkbox" />
                  <el-option label="输入框" value="input" />
                  <el-option label="按钮" value="button" />
                  <el-option label="开关" value="switch" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="选项名称" prop="label">
                <el-input v-model="currentOption.label" placeholder="请输入选项显示名称" />
              </el-form-item>
              
              <el-form-item label="选项ID" prop="id">
                <el-input v-model="currentOption.id" placeholder="请输入唯一标识符" @blur="generateIdFromLabel" />
                <div class="form-tip">
                  用于标识选项的唯一ID，建议使用英文和下划线
                  <el-button type="text" size="small" @click="generateIdFromLabel">
                    <i class="el-icon-refresh"></i> 自动生成
                  </el-button>
                </div>
              </el-form-item>
              
              <el-form-item label="后端字段" prop="backendField">
                <el-input v-model="currentOption.backendField" placeholder="请输入后端接收的字段名" />
                <div class="form-tip">后端API接收此选项值时使用的字段名</div>
              </el-form-item>
            </div>
            
            <!-- 选项类型特定配置 -->
            <div class="form-section" v-if="currentOption.type">
              <h4>{{ getTypeLabel(currentOption.type) }}配置</h4>
              
              <!-- 按钮类型 -->
              <div v-if="currentOption.type === 'button'">
                <el-form-item label="按钮值" prop="value">
                  <el-input v-model="currentOption.value" placeholder="请输入按钮点击时的值" />
                </el-form-item>
              </div>
              
              <!-- 输入框类型 -->
              <div v-if="currentOption.type === 'input'">
                <el-form-item label="输入类型" prop="inputType">
                  <el-select v-model="currentOption.inputType" placeholder="请选择输入类型">
                    <el-option label="文本" value="text" />
                    <el-option label="数字" value="number" />
                    <el-option label="密码" value="password" />
                  </el-select>
                </el-form-item>
                <el-form-item label="占位符" prop="placeholder">
                  <el-input v-model="currentOption.placeholder" placeholder="请输入提示文字" />
                </el-form-item>
              </div>
              
              <!-- 选择类型（单选/多选） -->
              <div v-if="['select', 'checkbox'].includes(currentOption.type)">
                <el-form-item label="选择类型" prop="selectType">
                  <el-radio-group v-model="currentOption.selectType">
                    <el-radio label="single">单选</el-radio>
                    <el-radio label="multiple">多选</el-radio>
                  </el-radio-group>
                </el-form-item>
                
                <el-form-item label="选项值">
                  <div class="values-config">
                    <div class="values-header">
                      <span>选项值列表</span>
                      <el-button type="primary" size="small" @click="addValue">
                        <i class="el-icon-plus"></i> 添加选项值
                      </el-button>
                    </div>
                    
                    <div v-if="currentOption.values && currentOption.values.length > 0" class="values-list">
                      <div 
                        v-for="(value, index) in currentOption.values" 
                        :key="index" 
                        class="value-item"
                      >
                        <el-input 
                          v-model="value.label" 
                          placeholder="显示文本" 
                          size="small"
                        />
                        <el-input 
                          v-model="value.value" 
                          placeholder="实际值" 
                          size="small"
                        />
                        <el-checkbox 
                          v-model="value.default" 
                          @change="onDefaultChange(index)"
                        >
                          默认
                        </el-checkbox>
                        <el-button 
                          type="text" 
                          size="small" 
                          @click="removeValue(index)"
                        >
                          <i class="el-icon-delete"></i>
                        </el-button>
                      </div>
                    </div>
                    
                    <div v-else class="empty-values">
                      <p>暂无选项值，点击"添加选项值"开始配置</p>
                    </div>
                  </div>
                </el-form-item>
              </div>
            </div>
          </el-form>
        </div>
        
        <div v-else class="no-selection">
          <i class="el-icon-info"></i>
          <p>请选择左侧的选项进行编辑</p>
          <p>或点击"添加选项"创建新选项</p>
        </div>
        
        <!-- 实时预览 -->
        <div v-if="editingIndex >= 0 || isAdding" class="preview-section">
          <h4>实时预览</h4>
          <div class="preview-container">
            <div class="preview-item">
              <label class="preview-label">{{ currentOption.label || '选项名称' }}</label>
              
              <!-- 按钮预览 -->
              <el-button v-if="currentOption.type === 'button'" type="primary" size="small">
                {{ currentOption.value || '按钮' }}
              </el-button>
              
              <!-- 单选框预览 -->
              <el-radio-group v-else-if="currentOption.type === 'select' && currentOption.selectType === 'single'" 
                              v-model="previewValue" size="small">
                <el-radio 
                  v-for="(value, index) in (currentOption.values || [])" 
                  :key="index" 
                  :label="value.value"
                >
                  {{ value.label }}
                </el-radio>
              </el-radio-group>
              
              <!-- 多选框预览 -->
              <el-checkbox-group v-else-if="currentOption.type === 'checkbox'" 
                                 v-model="previewValues" size="small">
                <el-checkbox 
                  v-for="(value, index) in (currentOption.values || [])" 
                  :key="index" 
                  :label="value.value"
                >
                  {{ value.label }}
                </el-checkbox>
              </el-checkbox-group>
              
              <!-- 输入框预览 -->
              <el-input v-else-if="currentOption.type === 'input'" 
                        v-model="previewValue" 
                        :type="currentOption.inputType || 'text'"
                        :placeholder="currentOption.placeholder || '请输入内容'"
                        size="small" />
              
              <!-- 开关预览 -->
              <el-switch v-else-if="currentOption.type === 'switch'" 
                         v-model="previewSwitch" />
              
              <div v-else class="preview-placeholder">
                请先选择选项类型
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 底部按钮 -->
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="info" @click="previewJson">预览JSON</el-button>
        <el-button type="primary" @click="handleConfirm">确定</el-button>
      </div>
    </template>
    
    <!-- JSON预览对话框 -->
    <el-dialog 
      title="JSON预览" 
      v-model="showPreview" 
      width="600px" 
      append-to-body
    >
      <pre class="json-preview">{{ previewContent }}</pre>
      <template #footer>
        <el-button @click="showPreview = false">关闭</el-button>
        <el-button type="primary" @click="copyJson">复制到剪贴板</el-button>
      </template>
    </el-dialog>
  </el-dialog>
</template>

<script>
export default {
  name: 'OptionConfig',
  props: {
    modelValue: {
      type: Boolean,
      default: false
    },
    configJson: {
      type: String,
      default: ''
    }
  },
  emits: ['update:modelValue', 'confirm'],
  data() {
    return {
      visible: false,
      options: [],
      selectedIndex: -1,
      editingIndex: -1,
      isAdding: false,
      showPreview: false,
      previewContent: '',
      // 预览相关数据
      previewValue: '',
      previewValues: [],
      previewSwitch: false,
      currentOption: {
        type: '',
        label: '',
        id: '',
        backendField: '',
        value: '',
        values: [],
        selectType: 'single',
        inputType: 'text',
        placeholder: ''
      },
      rules: {
        type: [
          { required: true, message: '请选择选项类型', trigger: 'change' }
        ],
        label: [
          { required: true, message: '请输入选项名称', trigger: 'blur' }
        ],
        id: [
          { required: true, message: '请输入选项ID', trigger: 'blur' },
          { pattern: /^[a-zA-Z_][a-zA-Z0-9_]*$/, message: 'ID只能包含字母、数字和下划线，且不能以数字开头', trigger: 'blur' }
        ],
        backendField: [
          { required: true, message: '请输入后端字段名', trigger: 'blur' }
        ]
      }
    };
  },
  watch: {
    modelValue: {
      handler(val) {
        this.visible = val;
        if (val) {
          this.parseConfigJson();
        }
      },
      immediate: true
    },
    visible(val) {
      this.$emit('update:modelValue', val);
    }
  },
  methods: {
    // 解析配置JSON
    parseConfigJson() {
      try {
        if (this.configJson) {
          const config = JSON.parse(this.configJson);
          this.options = config.options || [];
        } else {
          this.options = [];
        }
      } catch (error) {
        console.error('解析配置JSON失败:', error);
        this.options = [];
      }
      this.resetSelection();
    },
    
    // 重置选择状态
    resetSelection() {
      this.selectedIndex = -1;
      this.editingIndex = -1;
      this.isAdding = false;
      this.resetCurrentOption();
    },
    
    // 重置当前选项
    resetCurrentOption() {
      this.currentOption = {
        type: '',
        label: '',
        id: '',
        backendField: '',
        value: '',
        values: [],
        selectType: 'single',
        inputType: 'text',
        placeholder: ''
      };
    },
    
    // 获取类型标签
    getTypeLabel(type) {
      const labels = {
        select: '单选框',
        checkbox: '多选框',
        input: '输入框',
        button: '按钮',
        switch: '开关'
      };
      return labels[type] || type;
    },
    
    // 选择选项
    selectOption(index) {
      this.selectedIndex = index;
      this.editingIndex = -1;
      this.isAdding = false;
    },
    
    // 添加选项
    handleAddOption() {
      this.isAdding = true;
      this.editingIndex = -1;
      this.selectedIndex = -1;
      this.resetCurrentOption();
    },
    
    // 编辑选项
    editOption(index) {
      this.editingIndex = index;
      this.selectedIndex = index;
      this.isAdding = false;
      this.currentOption = JSON.parse(JSON.stringify(this.options[index]));
      
      // 确保values数组存在
      if (!this.currentOption.values) {
        this.currentOption.values = [];
      }
      
      // 初始化预览值
      this.initPreviewValues();
    },
    
    // 取消编辑
    cancelEdit() {
      this.editingIndex = -1;
      this.isAdding = false;
      this.resetCurrentOption();
    },
    
    // 保存选项
    saveOption() {
      this.$refs.optionForm.validate((valid) => {
        if (valid) {
          // 检查ID冲突
          const existingIndex = this.options.findIndex((opt, idx) => 
            opt.id === this.currentOption.id && idx !== this.editingIndex
          );
          
          if (existingIndex >= 0) {
            this.$message.error(`选项ID "${this.currentOption.id}" 已存在，请使用其他ID`);
            return;
          }
          
          if (this.editingIndex >= 0) {
            // 更新现有选项
            this.$set(this.options, this.editingIndex, JSON.parse(JSON.stringify(this.currentOption)));
          } else {
            // 添加新选项
            this.options.push(JSON.parse(JSON.stringify(this.currentOption)));
          }
          
          this.cancelEdit();
          this.$message.success('选项保存成功');
        }
      });
    },
    
    // 删除选项
    deleteOption(index) {
      this.$confirm('确定要删除这个选项吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.options.splice(index, 1);
        this.resetSelection();
        this.$message.success('选项删除成功');
      });
    },
    
    // 类型改变
    onTypeChange(type) {
      // 清理不相关的配置
      if (type !== 'button') {
        this.currentOption.value = '';
      }
      if (type !== 'input') {
        this.currentOption.inputType = 'text';
        this.currentOption.placeholder = '';
      }
      if (!['select', 'checkbox'].includes(type)) {
        this.currentOption.values = [];
        this.currentOption.selectType = 'single';
      }
    },
    
    // 添加选项值
    addValue() {
      if (!this.currentOption.values) {
        this.currentOption.values = [];
      }
      this.currentOption.values.push({
        label: '',
        value: '',
        default: false
      });
    },
    
    // 删除选项值
    removeValue(index) {
      this.currentOption.values.splice(index, 1);
    },
    
    // 默认值改变
    onDefaultChange(index) {
      if (this.currentOption.selectType === 'single') {
        // 单选时，只能有一个默认值
        this.currentOption.values.forEach((value, idx) => {
          if (idx !== index) {
            value.default = false;
          }
        });
      }
    },
    
    // 预览JSON
    previewJson() {
      const config = {
        options: this.options
      };
      this.previewContent = JSON.stringify(config, null, 2);
      this.showPreview = true;
    },
    
    // 复制JSON
    copyJson() {
      navigator.clipboard.writeText(this.previewContent).then(() => {
        this.$message.success('JSON已复制到剪贴板');
      });
    },
    
    // 关闭对话框
    handleClose() {
      this.visible = false;
    },
    
    // 确认配置
    handleConfirm() {
      const config = {
        options: this.options
      };
      const jsonString = JSON.stringify(config, null, 2);
      this.$emit('confirm', jsonString);
      this.handleClose();
    },

    // 从标签自动生成ID
    generateIdFromLabel() {
      if (!this.currentOption.label || this.currentOption.id) return;
      
      // 将中文标签转换为拼音或英文ID
      let id = this.currentOption.label
        .toLowerCase()
        .replace(/[\u4e00-\u9fa5]/g, '') // 移除中文字符
        .replace(/[^a-z0-9]/g, '_') // 非字母数字替换为下划线
        .replace(/_+/g, '_') // 多个下划线合并为一个
        .replace(/^_|_$/g, ''); // 移除开头和结尾的下划线
      
      // 如果为空，使用类型+时间戳
      if (!id) {
        id = this.currentOption.type + '_' + Date.now();
      }
      
      // 确保ID唯一
      let finalId = id;
      let counter = 1;
      while (this.options.some(opt => opt.id === finalId)) {
        finalId = id + '_' + counter;
        counter++;
      }
      
      this.currentOption.id = finalId;
    },

    // 快速添加预设选项
    handleQuickAdd(command) {
      const templates = {
        model_select: {
          type: 'select',
          label: '模型选择',
          id: 'model_select',
          backendField: 'model',
          selectType: 'single',
          values: [
            { label: 'GPT-4', value: 'gpt4', default: true },
            { label: 'GPT-3.5', value: 'gpt35', default: false },
            { label: 'Claude', value: 'claude', default: false }
          ]
        },
        search_mode: {
          type: 'checkbox',
          label: '搜索模式',
          id: 'search_mode',
          backendField: 'searchMode',
          selectType: 'multiple',
          values: [
            { label: '深度搜索', value: 'deep', default: true },
            { label: '实时搜索', value: 'realtime', default: false },
            { label: '联网搜索', value: 'web', default: false }
          ]
        },
        output_format: {
          type: 'select',
          label: '输出格式',
          id: 'output_format',
          backendField: 'outputFormat',
          selectType: 'single',
          values: [
            { label: '纯文本', value: 'text', default: true },
            { label: 'Markdown', value: 'markdown', default: false },
            { label: 'JSON', value: 'json', default: false }
          ]
        },
        enable_feature: {
          type: 'switch',
          label: '启用高级功能',
          id: 'enable_advanced',
          backendField: 'enableAdvanced'
        }
      };

      const template = templates[command];
      if (template) {
        // 确保ID唯一
        let finalId = template.id;
        let counter = 1;
        while (this.options.some(opt => opt.id === finalId)) {
          finalId = template.id + '_' + counter;
          counter++;
        }
        template.id = finalId;

        // 添加到选项列表
        this.options.push(JSON.parse(JSON.stringify(template)));
        this.$message.success(`已添加"${template.label}"选项`);
      }
    },

    // 初始化预览值
    initPreviewValues() {
      // 重置预览值
      this.previewValue = '';
      this.previewValues = [];
      this.previewSwitch = false;

      if (!this.currentOption.values) return;

      // 根据选项类型设置默认预览值
      if (this.currentOption.type === 'select' && this.currentOption.selectType === 'single') {
        const defaultValue = this.currentOption.values.find(v => v.default);
        this.previewValue = defaultValue ? defaultValue.value : '';
      } else if (this.currentOption.type === 'checkbox') {
        this.previewValues = this.currentOption.values
          .filter(v => v.default)
          .map(v => v.value);
      }
    }
  }
};
</script>

<style scoped>
.option-config-container {
  display: flex;
  height: 600px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
}

.left-panel, .right-panel {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.left-panel {
  border-right: 1px solid #e4e7ed;
  background-color: #fafafa;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.panel-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
}

.header-actions {
  display: flex;
  gap: 8px;
}

.option-card {
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 12px;
  margin-bottom: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.option-card:hover {
  border-color: #409eff;
  box-shadow: 0 2px 4px rgba(64, 158, 255, 0.1);
}

.option-card.active {
  border-color: #409eff;
  background-color: #f0f8ff;
}

.option-header {
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.option-type {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  color: white;
  margin-right: 8px;
}

.type-select { background-color: #409eff; }
.type-checkbox { background-color: #e6a23c; }
.type-input { background-color: #909399; }
.type-button { background-color: #67c23a; }
.type-switch { background-color: #f56c6c; }

.option-label {
  font-weight: 600;
  color: #303133;
}

.option-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.option-id {
  font-size: 12px;
  color: #909399;
}

.option-actions {
  display: flex;
  gap: 4px;
}

.empty-state, .no-selection {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

.empty-state i, .no-selection i {
  font-size: 48px;
  margin-bottom: 16px;
  display: block;
}

.form-section {
  margin-bottom: 24px;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 4px;
}

.form-section h4 {
  margin: 0 0 16px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 4px;
}

.values-config {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 12px;
  background-color: white;
}

.values-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-weight: 600;
}

.value-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.value-item .el-input {
  flex: 1;
}

.empty-values {
  text-align: center;
  padding: 20px;
  color: #909399;
}

.json-preview {
  background-color: #f5f5f5;
  padding: 16px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  max-height: 400px;
  overflow-y: auto;
}

.dialog-footer {
  text-align: right;
}

.preview-section {
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid #e4e7ed;
}

.preview-section h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.preview-container {
  background-color: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 16px;
}

.preview-item {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.preview-label {
  font-weight: 600;
  color: #303133;
  font-size: 14px;
}

.preview-placeholder {
  color: #909399;
  font-style: italic;
  text-align: center;
  padding: 20px;
}
</style>
