create table sschwartz1_crime_overview(
  CommunityAreaNumber tinyint,
  CommunityAreaName string,
  Crime_Date string,
  Average_Day_Temp float,
  HOMICIDE bigint,
  ROBBERY bigint,
  BATTERY_ASSAULT bigint,
  BURGLARY_THEFT_MOTORVEHICLETHEFT bigint,
  ARSON bigint,
  DECEPTIVE_PRACTICE bigint,
  CRIMINAL_DAMAGE_TRESPASS bigint,
  WEAPONS_VIOLATION bigint,
  SEX_OFFENSE bigint,
  OFFENSE_INVOLVING_CHILDREN bigint,
  NARCOTICS bigint,
  OTHER bigint)
  stored as orc;

insert overwrite table sschwartz1_crime_overview
  select CommunityAreaNumber,
  CommunityAreaName,
  concat(month, '/', day, '/', year),
  AvgDayTemp,
  count(if(crime = 'HOMICIDE', 1, null)),
  count(if(crime = 'ROBBERY', 1, null)),
  count(if((crime = 'BATTERY') OR (crime = 'ASSAULT') OR (crime = 'CRIM SEXUAL ASSUALT'), 1, null)),
  count(if((crime = 'BURGLARY') OR (crime = 'THEFT') OR (crime = 'MOTOR VEHICLE THEFT'), 1, null)),
  count(if(crime = 'ARSON', 1, null)),
  count(if(crime = 'DECEPTIVE PRACTICE', 1, null)),
  count(if((crime = 'CRIMINAL DAMAGE') OR (crime = 'CRIMINAL TRESPASS'), 1, null)),
  count(if((crime = 'WEAPONS VIOLATION') OR (crime = 'CONCEALED CARRY LICENSE VIOLATION'), 1, null)),
  count(if((crime = 'SEX OFFENSE') OR (crime = 'PROSTITUTION') OR (crime = 'PUBLIC INDENCENCY'), 1, null)),
  count(if(crime = 'OFFENSE INVOLVING CHILDREN', 1, null)),
  count(if(crime = 'NARCOTICS', 1, null)),
  count(if(crime != 'HOMICIDE' AND crime != 'ROBBERY' AND crime != 'BATTERY' AND crime != 'ASSAULT' AND crime != 'CRIM SEXUAL ASSAULT' AND crime != 'BURGLARY' AND crime != 'THEFT' AND crime != 'MOTOR VEHICLE THEFT' AND crime != 'ARSON' AND crime != 'DECEPTIVE PRACTICE' AND crime != 'CRIMINAL DAMAGE' AND crime != 'CRIMINAL TRESPASS' AND crime != 'WEAPONS VIOLATION' AND crime != 'CONCEALED CARRY LICENSE VIOLATION' AND crime != 'SEX OFFENSE' AND crime != 'PROSTITUTION' AND crime != 'PUBLIC INDENCENCY' AND crime != 'OFFENSE INVOLVING CHILDREN' AND crime != 'NARCOTICS', 1, null))
  from sschwartz1_crime_weather
  group by CommunityAreaNumber, CommunityAreaName, AvgDayTemp, Year, Month, Day;
