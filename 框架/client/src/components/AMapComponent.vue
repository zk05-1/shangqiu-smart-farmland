<template>
  <div class="amap-container">
    <div ref="mapContainer" class="map-wrapper"></div>
    <div class="search-box" v-if="showSearch">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索地点"
        :prefix-icon="Search"
        clearable
        @keyup.enter="handleSearch"
        style="width: 200px"
      />
      <el-button type="primary" icon="Search" @click="handleSearch" style="margin-left: 10px">搜索</el-button>
    </div>
    <div class="coordinate-info" v-if="currentPosition">
      <el-tag type="success">经度: {{ currentPosition.lng.toFixed(6) }}</el-tag>
      <el-tag type="primary" style="margin-left: 10px">纬度: {{ currentPosition.lat.toFixed(6) }}</el-tag>
    </div>
    <div class="map-toolbar" v-if="showToolbar">
      <el-button-group>
        <el-button size="small" icon="Location" @click="handleMarkMode">标记模式</el-button>
        <el-button size="small" icon="View" @click="handleViewMode">查看模式</el-button>
        <el-button size="small" icon="Refresh" @click="handleResetView">重置视图</el-button>
      </el-button-group>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import AMapLoader from '@amap/amap-jsapi-loader'

const props = defineProps({
  centerLng: {
    type: [Number, String],
    default: 115.65
  },
  centerLat: {
    type: [Number, String],
    default: 34.44
  },
  zoom: {
    type: Number,
    default: 12
  },
  showSearch: {
    type: Boolean,
    default: true
  },
  showToolbar: {
    type: Boolean,
    default: true
  },
  mode: {
    type: String,
    default: 'view'
  },
  markers: {
    type: Array,
    default: () => []
  },
  selectedPosition: {
    type: Object,
    default: null
  },
  autoInit: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['positionChange', 'markerClick', 'mapReady'])

let mapInstance = null
let markerInstance = null
let markersArray = []
let cluster = null
let AMap = null

const mapContainer = ref(null)
const searchKeyword = ref('')
const currentPosition = ref(null)
const isMarkMode = ref(props.mode === 'edit')

const AMAP_KEY = 'a6ff8e6fc25de9056e1fa2c3a40a1e15'
const AMAP_SECURITY_CODE = 'f846246df3898b67153c6a34a32a3b44'

const initAMap = async () => {
  try {
    if (!mapContainer.value) {
      ElMessage.warning('地图容器未找到')
      return
    }

    if (mapInstance) {
      mapInstance.destroy()
      mapInstance = null
    }

    window._AMapSecurityConfig = {
      securityJsCode: AMAP_SECURITY_CODE,
    }

    AMap = await AMapLoader.load({
      key: AMAP_KEY,
      version: '2.0',
      plugins: ['AMap.Scale', 'AMap.ToolBar', 'AMap.PlaceSearch', 'AMap.MarkerClusterer'],
      AMapUI: {
        version: '1.1',
        plugins: []
      }
    })

    mapInstance = new AMap.Map(mapContainer.value, {
      zoom: props.zoom,
      center: [parseFloat(props.centerLng), parseFloat(props.centerLat)],
      mapStyle: 'amap://styles/normal'
    })

    AMap.plugin(['AMap.Scale', 'AMap.ToolBar'], () => {
      mapInstance.addControl(new AMap.Scale())
      mapInstance.addControl(new AMap.ToolBar({
        position: 'RB'
      }))
    })

    if (props.markers && props.markers.length > 0) {
      addMarkers(props.markers)
    }

    if (props.selectedPosition) {
      setCurrentMarker(props.selectedPosition.lng, props.selectedPosition.lat)
    }

    mapInstance.on('click', (e) => {
      if (isMarkMode.value) {
        const lng = e.lnglat.getLng()
        const lat = e.lnglat.getLat()
        setCurrentMarker(lng, lat)
        currentPosition.value = { lng, lat }
        emit('positionChange', { lng, lat })
      }
    })

    emit('mapReady', mapInstance)
  } catch (error) {
    console.error('加载高德地图失败:', error)
    ElMessage.error('加载地图失败: ' + error.message)
  }
}

const setCurrentMarker = (lng, lat, title = '当前位置') => {
  if (!mapInstance || !AMap) return

  if (markerInstance) {
    mapInstance.remove(markerInstance)
  }

  const marker = new AMap.Marker({
    position: [lng, lat],
    title: title,
    icon: new AMap.Icon({
      size: new AMap.Size(32, 32),
      image: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzIiIGhlaWdodD0iMzIiIHZpZXdCb3g9IjAgMCAzMiAzMiIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMTYgMjhjLTQuNDIgMC04LTMuNTgtOC04czMuNTgtOCA4LTggOCAzLjU4IDggOC0zLjU4IDgtOCA4em0wLTFjMy44NyAwLTctMy4xMy03LTdzMy4xMy03IDctNyA3IDMuMTMgNyA3LTMuMTMgNy03IDd6IiBmaWxsPSIjNDA5RUZGIi8+PHBhdGggZD0iTTYgMmMwLTEuMTEuODktMiAyLTJoMTZjMS4xMSAwIDIgLjg5IDIgMnY0aC0yVjJoLTJ2NGgtMlYyem0yMCAwem0wLTZ2NGgtMnY0aDJ2NGgydi00aDJ2LTRoMnYtNGgtMnYtNGgtMnY0em0tOCAwem0wLTRoMnY0aC0ydi00eiIgZmlsbD0iIzQwOUVGRiIvPjwvc3ZnPg==',
      imageSize: new AMap.Size(32, 32)
    }),
    animation: 'AMAP_ANIMATION_DROP'
  })

  const infoWindow = new AMap.InfoWindow({
    content: `<div style="padding:10px;">
      <strong>${title}</strong><br/>
      经度: ${lng.toFixed(6)}<br/>
      纬度: ${lat.toFixed(6)}
    </div>`,
    offset: new AMap.Pixel(0, -30)
  })

  marker.on('click', () => {
    infoWindow.open(mapInstance, marker.getPosition())
  })

  mapInstance.add(marker)
  markerInstance = marker
  mapInstance.setCenter([lng, lat])
}

const addMarkers = (markerList) => {
  if (!mapInstance || !AMap) return
  clearMarkers()

  const markerArr = []

  markerList.forEach(item => {
    const marker = new AMap.Marker({
      position: [item.lng, item.lat],
      title: item.title || item.name || '农田位置',
      icon: new AMap.Icon({
        size: new AMap.Size(24, 24),
        image: getMarkerIcon(item.status),
        imageSize: new AMap.Size(24, 24)
      }),
      extData: item
    })

    marker.on('click', () => {
      emit('markerClick', item)
    })

    markerArr.push(marker)
    markersArray.push(marker)
  })

  mapInstance.add(markerArr)

  if (markerArr.length > 10) {
    AMap.plugin('AMap.MarkerClusterer', () => {
      cluster = new AMap.MarkerClusterer(mapInstance, markerArr, {
        gridSize: 80,
        minClusterSize: 2
      })
    })
  }
}

const getMarkerIcon = (status) => {
  const icons = {
    USE: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMTIgMmM1LjUxNCAwIDEwIDQuNDg2IDEwIDEwcy00LjQ4NiAxMC0xMCAxMC0xMC00LjQ4Ni0xMC0xMCA0LjQ4Ni0xMCAxMC0xMHptMC0xLjhjNC40MTggMCA4IDMuNTgyIDggOHMtMy41ODIgOC04IDgtOC0zLjU4Mi04LTggMy41ODItOCA4LTh6IiBmaWxsPSIjMjU1YzIiLz48L3N2Zz4=',
    FALLOW: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMTIgMmM1LjUxNCAwIDEwIDQuNDg2IDEwIDEwcy00LjQ4NiAxMC0xMCAxMC0xMC00LjQ4Ni0xMC0xMCA0LjQ4Ni0xMCAxMC0xMHptMC0xLjhjNC40MTggMCA4IDMuNTgyIDggOHMtMy41ODIgOC04IDgtOC0zLjU4Mi04LTggMy41ODItOCA4LTh6IiBmaWxsPSIjN2U1MTIwIi8+PC9zdmc+',
    IDLE: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMTIgMmM1LjUxNCAwIDEwIDQuNDg2IDEwIDEwcy00LjQ4NiAxMC0xMCAxMC0xMC00LjQ4Ni0xMC0xMCA0LjQ4Ni0xMCAxMC0xMHptMC0xLjhjNC40MTggMCA4IDMuNTgyIDggOHMtMy41ODIgOC04IDgtOC0zLjU4Mi04LTggMy41ODItOCA4LTh6IiBmaWxsPSIjNzc4ODk5Ii8+PC9zdmc+'
  }
  return icons[status] || icons.IDLE
}

const clearMarkers = () => {
  markersArray.forEach(marker => {
    mapInstance.remove(marker)
  })
  markersArray = []
  if (cluster) {
    cluster.clearMarkers()
    cluster = null
  }
}

const handleSearch = async () => {
  if (!searchKeyword.value) {
    ElMessage.warning('请输入搜索关键词')
    return
  }

  try {
    AMap.plugin('AMap.PlaceSearch', () => {
      const placeSearch = new AMap.PlaceSearch({
        pageSize: 10,
        pageIndex: 1,
        city: '商丘'
      })

      placeSearch.search(searchKeyword.value, (status, result) => {
        if (status === 'complete' && result.poiList && result.poiList.pois.length > 0) {
          const firstPoi = result.poiList.pois[0]
          const lng = firstPoi.location.lng
          const lat = firstPoi.location.lat

          mapInstance.setCenter([lng, lat])
          mapInstance.setZoom(15)

          if (isMarkMode.value) {
            setCurrentMarker(lng, lat, firstPoi.name)
            currentPosition.value = { lng, lat }
            emit('positionChange', { lng, lat })
          }

          ElMessage.success(`找到: ${firstPoi.name}`)
        } else {
          ElMessage.warning('未找到相关地点')
        }
      })
    })
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败')
  }
}

const handleMarkMode = () => {
  isMarkMode.value = true
  ElMessage.info('已进入标记模式，点击地图选择位置')
}

const handleViewMode = () => {
  isMarkMode.value = false
  ElMessage.info('已进入查看模式')
}

const handleResetView = () => {
  if (mapInstance) {
    mapInstance.setCenter([parseFloat(props.centerLng), parseFloat(props.centerLat)])
    mapInstance.setZoom(props.zoom)
    ElMessage.info('视图已重置')
  }
}

watch(() => props.selectedPosition, (newVal) => {
  if (newVal && mapInstance) {
    setCurrentMarker(newVal.lng, newVal.lat)
    currentPosition.value = newVal
  }
}, { deep: true })

watch(() => props.markers, (newVal) => {
  if (mapInstance && newVal) {
    addMarkers(newVal)
  }
}, { deep: true })

const initMap = () => {
  nextTick(() => {
    initAMap()
  })
}

onMounted(() => {
  if (props.autoInit) {
    initMap()
  }
})

onUnmounted(() => {
  if (mapInstance) {
    mapInstance.destroy()
    mapInstance = null
  }
})

defineExpose({
  getMapInstance: () => mapInstance,
  setCurrentMarker,
  addMarkers,
  clearMarkers,
  handleResetView,
  initMap
})
</script>

<style scoped>
.amap-container {
  width: 100%;
  height: 100%;
  position: relative;
}

.map-wrapper {
  width: 100%;
  height: 100%;
  min-height: 300px;
}

.search-box {
  position: absolute;
  top: 10px;
  left: 10px;
  z-index: 100;
  background: rgba(255, 255, 255, 0.95);
  padding: 10px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.coordinate-info {
  position: absolute;
  bottom: 60px;
  left: 10px;
  z-index: 100;
  background: rgba(255, 255, 255, 0.95);
  padding: 8px 12px;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.map-toolbar {
  position: absolute;
  top: 60px;
  left: 10px;
  z-index: 100;
  background: rgba(255, 255, 255, 0.95);
  padding: 6px;
  border-radius: 6px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.custom-marker {
  position: relative;
}
</style>