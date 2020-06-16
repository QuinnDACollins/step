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
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    //No Attendees, anytime should work
    if(request.getAttendees().isEmpty()){
      Collection<TimeRange> w = new ArrayList<TimeRange>(Arrays.asList(TimeRange.WHOLE_DAY));
      return w;
    }
    //Duration is too long, return empty collection
    if(request.getDuration() > TimeRange.WHOLE_DAY.duration()){
      return Collections.<TimeRange>emptyList();
    }
    Collection<TimeRange> available = new ArrayList<TimeRange>();
    //get times from out events and then sort.
    ArrayList<TimeRange> times = new ArrayList<TimeRange>();
    Iterator<Event> it = events.iterator();
    while(it.hasNext()){
      times.add(it.next().getWhen());
    }
    Collections.sort(times, TimeRange.ORDER_BY_START);
    ArrayList<TimeRange> availableTimes = new ArrayList<TimeRange>();
    getNextTime(TimeRange.START_OF_DAY, times, availableTimes);
    return availableTimes;
  }

  private Collection<TimeRange> getNextTime(int s, ArrayList<TimeRange> t, Collection<TimeRange> avail){
    if(t.isEmpty()){
      avail.add(TimeRange.fromStartEnd(s, TimeRange.END_OF_DAY, true));
      return avail;
    }
    avail.add(TimeRange.fromStartEnd(s, t.get(0).start(), false));
    if(t.size() > 1){
      if(t.get(1).start() < t.get(0).end()){
        if(t.get(1).end() > t.get(0).end()){
          t.remove(0);
        }
      }
    }
    int end = t.get(0).end();
    t.remove(0);
    return getNextTime(end, t, avail);
  }
}

