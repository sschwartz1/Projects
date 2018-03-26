create external table sschwartz1_chicago_weather_csv(
  Station string,
  ChicagoLocation string,
  ChicagoDate string,
  MaxTemp tinyint,
  MinTemp tinyint)
row format serde 'org.apache.hadoop.hive.serde2.OpenCSVSerde'

WITH SERDEPROPERTIES (
   "separatorChar" = "\,",
   "quoteChar"     = "\"")

STORED AS TEXTFILE
  location '/user/sschwartz1/chicagoWeather';

create table sschwartz1_chicago_weather(
  Station string,
  ChicagoLocation string,
  Year smallint,
  Month tinyint,
  Day tinyint,
  MaxTemp tinyint,
  MinTemp tinyint)
  stored as orc;

insert overwrite table sschwartz1_chicago_weather
    select Station,
    ChicagoLocation,
    split(ChicagoDate, '\\-')[0],
    split(ChicagoDate, '\\-')[1],
    split(ChicagoDate, '\\-')[2],
    MaxTemp,
    MinTemp
    FROM sschwartz1_chicago_weather_csv;
