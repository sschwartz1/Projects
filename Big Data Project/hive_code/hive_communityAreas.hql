create table sschwartz1_Community_Area(
  Number tinyint,
  Name string)

row format delimited fields terminated by ','
stored as textfile
location '/user/sschwartz1/communityAreas';
