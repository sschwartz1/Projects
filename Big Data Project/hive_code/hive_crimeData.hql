create external table sschwartz1_crime_csv(
  ID int,
  CaseNumber string,
  CrimeDate string,
  Block string,
  IUCR string,
  PrimaryCrimeType string,
  CrimeDescription string,
  LocationDescription string,
  Arrest boolean,
  Domestic boolean,
  Beat smallint,
  District tinyint,
  Ward smallint,
  CommunityArea tinyint,
  FBIcode string,
  XCoordinate int,
  YCoordinate int,
  Year smallint,
  UpdatedOn string,
  Latitude decimal,
  Longitude decimal,
  Location string)
  row format serde 'org.apache.hadoop.hive.serde2.OpenCSVSerde'

  WITH SERDEPROPERTIES (
     "separatorChar" = "\,",
     "quoteChar"     = "\""
  )

  STORED AS TEXTFILE
    location '/user/sschwartz1/crimeData';

create table sschwartz1_crime(
    ID int,
    CaseNumber string,
    Year smallint,
    Month tinyint,
    Day tinyint,
    Time string,
    Block string,
    IUCR string,
    PrimaryCrimeType string,
    CrimeDescription string,
    LocationDescription string,
    Arrest boolean,
    Domestic boolean,
    Beat smallint,
    District tinyint,
    Ward smallint,
    CommunityArea tinyint,
    FBIcode string,
    XCoordinate int,
    YCoordinate int,
    UpdatedOn string,
    Latitude decimal,
    Longitude decimal,
    Location string)
    stored as orc;

insert overwrite table sschwartz1_crime
  select ID,
  CaseNumber,
  Year,
  split(crimedate, '\\/')[0] AS Month,
  split(crimedate, '\\/')[1] AS Day,
  concat(split(crimedate, '\\ ')[1], ' ', split(crimedate, '\\ ')[2]) as Time,
  Block,
  IUCR,
  PrimaryCrimeType,
  CrimeDescription,
  LocationDescription,
  Arrest,
  Domestic,
  Beat,
  District,
  Ward,
  CommunityArea,
  FBIcode,
  XCoordinate,
  YCoordinate,
  UpdatedOn,
  Latitude,
  Longitude,
  Location
  from sschwartz1_crime_csv;
