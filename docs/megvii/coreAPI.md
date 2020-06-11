## offlineVideo

- 创建离线任务

  ```json
  {
      "description": "东莞员工",
      "url": "http://10.122.101.59:8080/v5/objectStorage/Nxl-test-20200604111215/weed---8-_3dfc6d6977c592_meta",
      "name": "东莞员工",
      "faceAnalyzer": {
          "capture": {
              "analysisSettings": {
                  "age": true,
                  "gender": true,
                  "minority": true,
                  "eyeStatus": true,
                  "mouthStatus": true,
                  "liveness": true
              },
              "cropMode": {
                  "cropFace": {
                      "enabled": true
                  },
                  "cropFull": {
                      "enabled": true
                  }
              },
              "output": {
                  "topic": "sys.default",
                  "outputFeature": true,
                  "kafkaTopic": "capture"
              },
              "wholeTrack": true,
              "filter": {
                  "minWidthPixels": 0,
                  "minHeightPixels": 0
              },
              "ttlSeconds": "0",
              "includeLowQuality": true,
              "indexTtlSeconds": "0"
          },
          "monitors": [
              {
                  "clipSettings": {
                      "beforeSeconds": -1,
                      "afterSeconds": -1
                  },
                  "faceGroupId": "ca079e79-b415-46a0-9c9c-fe887364b72f",
                  "threshold": 80,
                  "topic": "aiparkTopic",
                  "limit": 20,
                  "monitorIntervalMillis": "40",
                  "cropMode": {
                      "cropFace": {
                          "enabled": true
                      },
                      "cropFull": {
                          "enabled": true
                      }
                  },
                  "kafkaTopic": "alert"
              }
          ],
          "enable": true,
          "alertPolicy": {
              "messageTypePolicy": "SINGLE",
              "messageFilterPolicy": "FULL"
          }
      },
      "stateChangeTopic": "sys.default"
  }
  ```

  

