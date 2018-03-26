create external table sschwartz1_crime_overview_main(
  Crime_Key string,
  CommunityAreaName string,
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
STORED BY 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
WITH SERDEPROPERTIES ('hbase.columns.mapping' = ':key, crime:CommunityAreaName, crime:Average_Day_Temp, crime:HOMICIDE, crime:ROBBERY, crime:BATTERY_ASSAULT, crime:BURGLARY_THEFT_MOTORVEHICLETHEFT, crime:ARSON, crime:DECEPTIVE_PRACTICE, crime:CRIMINAL_DAMAGE_TRESPASS, crime:WEAPONS_VIOLATION, crime:SEX_OFFENSE, crime:OFFENSE_INVOLVING_CHILDREN, crime:NARCOTICS, crime:OTHER')
TBLPROPERTIES ('hbase.table.name' = 'sschwartz1_crime_overview_main');

insert into sschwartz1_crime_overview_main
  select concat(CommunityAreaNumber, '-', Crime_Date),
  CommunityAreaName,
  Average_Day_Temp,
  HOMICIDE,
  ROBBERY,
  BATTERY_ASSAULT,
  BURGLARY_THEFT_MOTORVEHICLETHEFT,
  ARSON,
  DECEPTIVE_PRACTICE,
  CRIMINAL_DAMAGE_TRESPASS,
  WEAPONS_VIOLATION,
  SEX_OFFENSE,
  OFFENSE_INVOLVING_CHILDREN,
  NARCOTICS,
  OTHER
  from sschwartz1_crime_overview;
