#!/bin/bash
set -e 
ROOT_DIR="$PWD/core/core.classic-5.8-2021.01-patch1"

rm -rf $ROOT_DIR
mkdir -p $ROOT_DIR/artifact
rm -rf $ROOT_DIR/core.log
touch $ROOT_DIR/core.log

function log()
{
	echo `date` $1 >> $ROOT_DIR/core.log
}


log ">>>> 拉取镜像："

echo ampregistry:5000/kafka-exporter:v1.2.0.debug.66251851 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/kafka-exporter:v1.2.0.debug.66251851

echo ampregistry:5000/weed:release-weed.5.ha-c690af86-build433041 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/weed:release-weed.5.ha-c690af86-build433041

echo ampregistry:5000/jaegertracing-all-in-one:1.20 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/jaegertracing-all-in-one:1.20

echo ampregistry:5000/iot-weed:GSP-V1.1-1.45-d21084c7-build318205 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/iot-weed:GSP-V1.1-1.45-d21084c7-build318205

echo ampregistry:5000/bigdata-postgresql-11.5:v2.5.20201111 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/bigdata-postgresql-11.5:v2.5.20201111

echo ampregistry:5000/grafana:7.1.0.megvii-ipu.plugins >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/grafana:7.1.0.megvii-ipu.plugins

echo ampregistry:5000/kafka:2.11-2.0.0.181225 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/kafka:2.11-2.0.0.181225

echo ampregistry:5000/camera-proxy:v2.2.0-bb3f1996-build266045 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/camera-proxy:v2.2.0-bb3f1996-build266045

echo ampregistry:5000/etcd:v3.4.2 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/etcd:v3.4.2

echo ampregistry:5000/bigdata-clickhouse-20.3:v1.0.20200819 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/bigdata-clickhouse-20.3:v1.0.20200819

echo ampregistry:5000/profilo-tool:v1.0.20101322 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/profilo-tool:v1.0.20101322

echo ampregistry:5000/bigdata-postgresql-10.6:v1.4.20190228 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/bigdata-postgresql-10.6:v1.4.20190228

echo ampregistry:5000/prom_node_exporter:latest >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/prom_node_exporter:latest

echo ampregistry:5000/redis:5.0.5-alpine >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/redis:5.0.5-alpine

echo ampregistry:5000/zetcd:v0.0.5-d557351b495a60a68069af57c61ee0b6d6abb158 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/zetcd:v0.0.5-d557351b495a60a68069af57c61ee0b6d6abb158

echo ampregistry:5000/sng-biz-cassandra:3.11.5.1912272158-bf65c2f9-release >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/sng-biz-cassandra:3.11.5.1912272158-bf65c2f9-release

echo ampregistry:5000/pushgateway:v1.1.0 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/pushgateway:v1.1.0

echo ampregistry:5000/elasticsearch:7.6.2-int8 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/elasticsearch:7.6.2-int8

echo ampregistry:5000/prometheus:v2.19.3 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/prometheus:v2.19.3

echo ampregistry:5000/bigdata-postgres_exporter_v0.5.1:v1.0.20190915 >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/bigdata-postgres_exporter_v0.5.1:v1.0.20190915

echo ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.soft.ebg >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.soft.ebg

echo ampregistry:5000/screlease:pangu.custom-20211111_5.8-2021.01-release.ebg-dac3e80f-build723307.soft.ebg >> $ROOT_DIR/artifact/base_images.txt
docker pull ampregistry:5000/screlease:pangu.custom-20211111_5.8-2021.01-release.ebg-dac3e80f-build723307.soft.ebg


echo ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.ebg
echo ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.ebg >> $ROOT_DIR/hard.ebg.x86-64/images.txt
docker pull ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.ebg

log ">> 保存硬狗镜像包 "
all_core_image=""
mkdir -p $ROOT_DIR/hard.ebg.x86-64
cp $ROOT_DIR/artifact/base_images.txt $ROOT_DIR/hard.ebg.x86-64/images.txt
all_core_image="$all_core_image ampregistry:5000/screlease:pangu.custom-20211111_5.8-2021.01-release.ebg-dac3e80f-build723307.soft.ebg"
docker save ampregistry:5000/kafka-exporter:v1.2.0.debug.66251851 ampregistry:5000/weed:release-weed.5.ha-c690af86-build433041 ampregistry:5000/jaegertracing-all-in-one:1.20 ampregistry:5000/iot-weed:GSP-V1.1-1.45-d21084c7-build318205 ampregistry:5000/bigdata-postgresql-11.5:v2.5.20201111 ampregistry:5000/grafana:7.1.0.megvii-ipu.plugins ampregistry:5000/kafka:2.11-2.0.0.181225 ampregistry:5000/camera-proxy:v2.2.0-bb3f1996-build266045 ampregistry:5000/etcd:v3.4.2 ampregistry:5000/bigdata-clickhouse-20.3:v1.0.20200819 ampregistry:5000/profilo-tool:v1.0.20101322 ampregistry:5000/bigdata-postgresql-10.6:v1.4.20190228 ampregistry:5000/prom_node_exporter:latest ampregistry:5000/redis:5.0.5-alpine ampregistry:5000/zetcd:v0.0.5-d557351b495a60a68069af57c61ee0b6d6abb158 ampregistry:5000/sng-biz-cassandra:3.11.5.1912272158-bf65c2f9-release ampregistry:5000/pushgateway:v1.1.0 ampregistry:5000/elasticsearch:7.6.2-int8 ampregistry:5000/prometheus:v2.19.3 ampregistry:5000/bigdata-postgres_exporter_v0.5.1:v1.0.20190915 $all_core_image | pv | pixz | pv > $ROOT_DIR/hard.ebg.x86-64/core.classic-5.8-2021.01-patch1.ebg.custom.tar.xz
md5sum $ROOT_DIR/hard.ebg.x86-64/core.classic-5.8-2021.01-patch1.ebg.custom.tar.xz > $ROOT_DIR/hard.ebg.x86-64/image_md5

log ">> 保存软狗镜像包 "
all_core_image=""
mkdir -p $ROOT_DIR/soft.ebg.x86-64
cp $ROOT_DIR/artifact/base_images.txt $ROOT_DIR/soft.ebg.x86-64/images.txt

echo ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.soft.ebg
echo ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.soft.ebg >> $ROOT_DIR/soft.ebg.x86-64/images.txt
docker pull ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000.soft.ebg
all_core_image="$all_core_image ampregistry:5000/screlease:pangu.custom-20211111_5.8-2021.01-release.softdog.ebg-dac3e80f-build723307.soft.ebg"


docker save ampregistry:5000/kafka-exporter:v1.2.0.debug.66251851 ampregistry:5000/weed:release-weed.5.ha-c690af86-build433041 ampregistry:5000/jaegertracing-all-in-one:1.20 ampregistry:5000/iot-weed:GSP-V1.1-1.45-d21084c7-build318205 ampregistry:5000/bigdata-postgresql-11.5:v2.5.20201111 ampregistry:5000/grafana:7.1.0.megvii-ipu.plugins ampregistry:5000/kafka:2.11-2.0.0.181225 ampregistry:5000/camera-proxy:v2.2.0-bb3f1996-build266045 ampregistry:5000/etcd:v3.4.2 ampregistry:5000/bigdata-clickhouse-20.3:v1.0.20200819 ampregistry:5000/profilo-tool:v1.0.20101322 ampregistry:5000/bigdata-postgresql-10.6:v1.4.20190228 ampregistry:5000/prom_node_exporter:latest ampregistry:5000/redis:5.0.5-alpine ampregistry:5000/zetcd:v0.0.5-d557351b495a60a68069af57c61ee0b6d6abb158 ampregistry:5000/sng-biz-cassandra:3.11.5.1912272158-bf65c2f9-release ampregistry:5000/pushgateway:v1.1.0 ampregistry:5000/elasticsearch:7.6.2-int8 ampregistry:5000/prometheus:v2.19.3 ampregistry:5000/bigdata-postgres_exporter_v0.5.1:v1.0.20190915 $all_core_image | pv | pixz | pv > $ROOT_DIR/soft.ebg.x86-64/core.classic-5.8-2021.01-patch1.soft.ebg.custom.tar.xz
md5sum $ROOT_DIR/soft.ebg.x86-64/core.classic-5.8-2021.01-patch1.soft.ebg.custom.tar.xz > $ROOT_DIR/soft.ebg.x86-64/image_md5


log ">> 导出数据包"

devops-svcctl dpkg-dump --image ampregistry:5000/screlease:core.classic-5.8-2021.01-patch1-6a9a30f8-build514000 --manifest /devops-volumes/devops.model.json --save $ROOT_DIR/artifact \
  ||  (log "不包含数据包，生成完毕." ; exit 1)


ls $ROOT_DIR/artifact/data-*.devops_dpkg | xargs -n 1 -P 32 md5sum >> $ROOT_DIR/artifact/devops_dpkg_md5
DPKG_NAME=`basename $ROOT_DIR/artifact/data-*.devops_dpkg`

log ">> 导出硬狗dpkg_release包"

tar cvf $ROOT_DIR/hard.ebg.x86-64/core.classic-5.8-2021.01-patch1.ebg.devops_release_pkg -C $ROOT_DIR/hard.ebg.x86-64 core.classic-5.8-2021.01-patch1.ebg.custom.tar.xz -C $ROOT_DIR/artifact $DPKG_NAME
md5sum $ROOT_DIR/hard.ebg.x86-64/core.classic-5.8-2021.01-patch1.ebg.devops_release_pkg > $ROOT_DIR/hard.ebg.x86-64/devops_release_pkg_md5

log ">> 导出软狗dpkg_release包"
tar cvf $ROOT_DIR/soft.ebg.x86-64/core.classic-5.8-2021.01-patch1.soft.ebg.devops_release_pkg -C $ROOT_DIR/soft.ebg.x86-64 core.classic-5.8-2021.01-patch1.soft.ebg.custom.tar.xz -C $ROOT_DIR/artifact $DPKG_NAME
md5sum $ROOT_DIR/soft.ebg.x86-64/core.classic-5.8-2021.01-patch1.soft.ebg.devops_release_pkg > $ROOT_DIR/soft.ebg.x86-64/devops_release_pkg_md5

log "生成完毕."