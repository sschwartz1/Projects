#!/usr/bin/perl -w
# Program: cass_sample.pl
# Note: includes bug fixes for Net::Async::CassandraCQL 0.11 version

use strict;
use warnings;
use 5.10.0;
use FindBin;

use Scalar::Util qw(
        blessed
    );
use Try::Tiny;

use Kafka::Connection;
use Kafka::Producer;

use Data::Dumper;
use CGI qw/:standard/, 'Vars';

my $ca_number = param('community_area_number');
my $date = param('crime_date');
my $temp = param('temp');
my $crime_type = param('crime_type');

my ( $connection, $producer );
try {
    #-- Connection
    $connection = Kafka::Connection->new( host => '10.0.0.2', port => 6667 ); # cluster
    # $connection = Kafka::Connection->new( host => 'localhost', port => 9092 ); # VM

    #-- Producer
    $producer = Kafka::Producer->new( Connection => $connection );
    # Only put in the station_id and weather elements because those are the only ones we care about
    my $message = '{"communityAreaNumber":'.$ca_number.','.'"crimeDate":'.'"'.$date.'",'.'"temp":'.$temp.','.'"crimeType":'.'"'.$crime_type.'"}';

    # Sending a single message
    my $response = $producer->send(
	'sschwartz1_crime',          # topic
	0,                                 # partition
	$message                           # message
        );
} catch {
    if ( blessed( $_ ) && $_->isa( 'Kafka::Exception' ) ) {
	warn 'Error: (', $_->code, ') ',  $_->message, "\n";
	exit;
    } else {
	die $_;
    }
};

# Closes the producer and cleans up
undef $producer;
undef $connection;

print header, start_html(-title=>'Submit weather',-head=>Link({-rel=>'stylesheet',-href=>'/table.css',-type=>'text/css'}));
print 'Crime Report Submitted!';

#print $protocol->getTransport->getBuffer;
print end_html;
