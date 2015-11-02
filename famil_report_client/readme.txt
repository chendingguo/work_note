select * from t_order where submit_time> to_date('02-12-2015 09:00:00', 'dd-mm-yyyy hh24:mi:ss') and id_user='7281' and order_from='2'


config.properties文件说明　
#-------------------------------------------------cxf service url  
monitor.service.url=http://192.168.200.183:7070/gsinfo/services/entInfo

#专线用户ukey id
monitor.add.parameter.uid=F2903F60BD27E2B4
#用户密码
monitor.add.parameter.password=123456
monitor.add.parameter.keytype=2

#企业名称csv文件夹,每个文件中放不大于40万条记录
monitor.add.input.file.path=/home/airsupply/develope/monitor-app/input_file
#运行时没有被加入监控的企业名单
monitor.add.out.file.path=/home/airsupply/develope/monitor-app/output_file/error.txt

#--线程配置
monitor.add.thread.number=200
monitor.add.retry.max.number=1
