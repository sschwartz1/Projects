create table sschwartz1_crime_weather(
  Year smallint,
  Month tinyint,
  Day tinyint,
  Time string,
  AvgDayTemp float,
  CommunityAreaNumber tinyint,
  CommunityAreaName string,
  Crime string,
  Arrest boolean,
  Domestic boolean)
  stored as orc;

insert overwrite table sschwartz1_crime_weather
  select c.year as Year,
  c.month as Month,
  c.day as Day,
  c.time as Time,
  w.maxtemp as Temp,
  c.communityarea as CommunityAreaNumber,
  ca.name as CommunityAreaName,
  c.primarycrimetype as Crime,
  c.arrest as Arrest,
  c.domestic as Domestic
  from sschwartz1_crime as c join sschwartz1_chicago_weather as w join sschwartz1_community_area as ca
  on c.year = w.year and c.month = w.month and c.day = w.day and c.communityarea = ca.number;
