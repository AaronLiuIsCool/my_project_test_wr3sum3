# Introduction
 - This folder contains some helper scripts and raw data
 - 因为脚本比较简答 这里没有做太多dynamic的东西， inputFileName, outputFileName, columnNames 都是hardcode 进去的 如果有需要 可以手动调整一下

 - Python version: 3.6 +

# Run the script
 - Manually convert excel data to csv 
 - Delete unnecessary data
 - `python converterName.py` to run the script
 - Copy the data into `webapp/data`

## province-city-converter.py
 - 把csv 格式(id, 省份，城市) 转换成json array
 - 目前省份并没有使用 但保留了数据字段

 
## ca-university-converter.py
 - 把北美大学 csv 格式(major name) 转换成json array
 

## cn-university.py
 - 把中国大学csv 格式(???) 转换成json array
 - 需要保留的字段待决定


