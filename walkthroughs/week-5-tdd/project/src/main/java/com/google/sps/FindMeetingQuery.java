// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.lang.Object;
public final class FindMeetingQuery {
  MeetingRequest r;
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
      r = request;
    //No Attendees, anytime should work
    if(request.getAttendees().isEmpty()){
      Collection<TimeRange> w = new ArrayList<TimeRange>(Arrays.asList(TimeRange.WHOLE_DAY));
      return w;
    }
    //Duration is too long, return empty collection
    if(request.getDuration() > TimeRange.WHOLE_DAY.duration()){
      return Collections.<TimeRange>emptyList();
    }

    //get times from out events and then sort.
    ArrayList<Event> eventsAsList = new ArrayList<Event>(events);

    Collections.sort(eventsAsList, Event.ORDER_BY_START);
    ArrayList<TimeRange> recursiveParameter = new ArrayList<TimeRange>();
    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>(getNextTime(TimeRange.START_OF_DAY, eventsAsList, recursiveParameter));
    return availableTimes;
  }

  private Collection<TimeRange> getNextTime(int s, ArrayList<Event> e, Collection<TimeRange> avail){
    while((!e.isEmpty()) && (!attending(e.get(0)))){
      e.remove(0);
    }
    if(e.isEmpty()){
      if(checkAvailability(s, TimeRange.END_OF_DAY, r.getDuration())){
        avail.add(TimeRange.fromStartEnd(s, TimeRange.END_OF_DAY, true));
      }
      return avail;
    }
    TimeRange currentEventTimeRange = e.get(0).getWhen();
    if(e.size() > 1){
      TimeRange nextEventTimeRange = e.get(1).getWhen();
      if(overlapping(currentEventTimeRange, nextEventTimeRange)){
        if(nested(currentEventTimeRange, nextEventTimeRange)){         
          e.remove(1);
        } else {
          e.remove(0);
        }
        currentEventTimeRange = TimeRange.fromStartEnd(currentEventTimeRange.start(), e.get(0).getWhen().end(), true);
      }
    }
    if(checkAvailability(s, currentEventTimeRange.start(), r.getDuration())){
        avail.add(TimeRange.fromStartEnd(s, currentEventTimeRange.start(), false));
    }
    int end = e.get(0).getWhen().end();
    e.remove(0);
    return getNextTime(end, e, avail);
  }

  //Checks if anyone in a meeting request is attending a specific Event
  private boolean attending(Event e){
  ArrayList<String> meetingAttendees = new ArrayList<String>(r.getAttendees());
  for(int i = 0; i < meetingAttendees.size(); i++){
    if(e.getAttendees().contains(meetingAttendees.get(i))){
      return true;
    }
  }
  return false;
  }
  private boolean checkAvailability(long start, long end, long duration){
    if(end - start >= duration){
      return true;
    } else {
      return false;
    }
  }

  private boolean overlapping(TimeRange a, TimeRange b){
    if(b.start() < a.end()){
        return true;
      }
    return false;
  }
  //only call AFTER overlapping has been seen as true.
  private boolean nested(TimeRange a, TimeRange b){
    if(b.end() < a.end()){
      return true;
    } else {
      return false;
    }
  }
}

