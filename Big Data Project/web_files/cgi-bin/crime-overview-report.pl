#!/usr/bin/perl -w
# Creates an html table of crime overview for the given date/Chicago Neighboorhood

# Needed includes
use strict;
use warnings;
use 5.10.0;
use HBase::JSONRest;
use CGI qw/:standard/;
use Data::Dumper;

# Read the community area number and date as CGI parameters
my $ca_number = param('community_area_number');
my $date = param('crime_date');

# Define a connection template to access the HBase REST server
my $hbase = HBase::JSONRest->new(host => "10.0.0.5:8082");

# This function takes a row and gives you the value of the given column
# E.g., cellValue($row, 'delay:rain_delay') gives the value of the
# rain_delay column in the delay family.
# It uses somewhat tricky perl, so you can treat it as a black box
sub cellValue {
    my $row = $_[0];
    my $field_name = $_[1];
    my $row_cells = ${$row}{'columns'};
    foreach my $cell (@$row_cells) {
	if ($$cell{'name'} eq $field_name) {
	    return $$cell{'value'};
	}
    }
    return 'missing';
}

# Query hbase for the community area crime stats based on the community area number - crime date key
my $batch_records = $hbase->get({
  table => 'sschwartz1_crime_overview_main',
  where => {
    key_equals => $ca_number.'-'.$date
  },
});

my $speed_records = $hbase->get({
  table => 'sschwartz1_crime_overview_main_speed',
  where => {
    key_equals => $ca_number.'-'.$date
  },
});

sub combinedCellValue {
    my $field_name = $_[0];
    if(!@$speed_records && !@$batch_records) {
      return "missing";
    }
    my $result = 0;
    if(@$batch_records) {
	$result += cellValue(@$batch_records[0], $field_name);
    }
    if(@$speed_records) {
	my $packed_value = cellValue(@$speed_records[0], $field_name);
	if($packed_value ne "missing") {
	    $result += unpack('Q>', $packed_value);
	}
    }
    return $result;
}

# There will only be one record for this key, which will be the
# "zeroth" row returned
my $batch_row = @$batch_records[0];
my $speed_row = @$speed_records[0];

# Get the value of all the columns we need and store them in named variables
# Perl's ability to assign a list of values all at once is very convenient here
my($community_area_name, $avg_day_temp, $homicide, $robbery, $battery_assault, $burglary_theft_motorvehicletheft,
  $arson, $deceptive_practice, $criminal_damage_trespass, $weapons_violation, $sex_offense,
  $offense_involving_children, $narcotics, $other)
 =  (cellValue($batch_row, 'crime:CommunityAreaName'), combinedCellValue('crime:Average_Day_Temp'), combinedCellValue('crime:HOMICIDE'),
    combinedCellValue('crime:ROBBERY'), combinedCellValue('crime:BATTERY_ASSAULT'),
    combinedCellValue('crime:BURGLARY_THEFT_MOTORVEHICLETHEFT'), combinedCellValue('crime:ARSON'),
    combinedCellValue('crime:DECEPTIVE_PRACTICE'), combinedCellValue('crime:CRIMINAL_DAMAGE_TRESPASS'),
    combinedCellValue('crime:WEAPONS_VIOLATION'), combinedCellValue('crime:SEX_OFFENSE'),
    combinedCellValue('crime:OFFENSE_INVOLVING_CHILDREN'), combinedCellValue('crime:NARCOTICS'),
    combinedCellValue('crime:OTHER'));

# Print an HTML page with the table. Perl CGI has commands for all the
# common HTML tags
print header, start_html(-title=>'hello CGI',-head=>Link({-rel=>'stylesheet',-href=>'/table.css',-type=>'text/css'}));
print div({-style=>'margin-left:275px;margin-right:auto;display:inline-block;box-shadow: 10px 10px 5px #888888;border:1px solid #000000;-moz-border-radius-bottomleft:9px;-webkit-border-bottom-left-radius:9px;border-bottom-left-radius:9px;-moz-border-radius-bottomright:9px;-webkit-border-bottom-right-radius:9px;border-bottom-right-radius:9px;-moz-border-radius-topright:9px;-webkit-border-top-right-radius:9px;border-top-right-radius:9px;-moz-border-radius-topleft:9px;-webkit-border-top-left-radius:9px;border-top-left-radius:9px;background:white'}, '&nbsp;Crime Count By Neighboord and Day&nbsp;');
print     p({-style=>"bottom-margin:10px"});
print table({-class=>'CSS_Table_Example', -style=>'width:60%;margin:auto;'},
	    Tr([td(['Community Area Name', 'Avg Day Temp', 'Homicide', 'Robbery', 'Battery or Assault', 'Burglary or Theft or Motor Vehicle Theft', 'Arson', 'Deceptive Practice', 'Criminal Damage or Trespass', 'Weapons Violation', 'Sex Offense', 'Offense Involving Children', 'Narcotics', 'Other']),
                td([$community_area_name, $avg_day_temp, $homicide,
                    $robbery, $battery_assault,
                    $burglary_theft_motorvehicletheft, $arson,
                    $deceptive_practice, $criminal_damage_trespass,
                    $weapons_violation, $sex_offense,
                    $offense_involving_children, $narcotics,
                    $other])])),
    p({-style=>"bottom-margin:10px"})
    ;

print end_html;
