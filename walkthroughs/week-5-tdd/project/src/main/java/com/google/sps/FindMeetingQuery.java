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

import java.lang.Math;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;

public final class FindMeetingQuery {
  /* Identify conflicting events, then iterating through the day, skipping conflicts, and 
  identifying time ranges longer than the duration of the requested meeting. */
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> results = new ArrayList<TimeRange>();

    ArrayList<TimeRange> conflicts = findConflicts(events, request);
    Iterator<TimeRange> conflictIterator = conflicts.iterator();
    int duration = (int) request.getDuration();
    int currentTime = TimeRange.START_OF_DAY;

    while (conflictIterator.hasNext()) {
      TimeRange conflictTime = conflictIterator.next();
      int start = conflictTime.start();
      if ((start - currentTime) >= duration) {
        results.add(TimeRange.fromStartEnd(currentTime, start, false));
      }
      currentTime = Math.max(currentTime, conflictTime.end());
    }

    if ((TimeRange.END_OF_DAY - currentTime) >= duration) {
      results.add(TimeRange.fromStartEnd(currentTime, TimeRange.END_OF_DAY, true));
    }
    return results;
  }

   private ArrayList<TimeRange> findConflicts(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> conflicts = new ArrayList<TimeRange>();
    for (Event event : events) {
      if (!Collections.disjoint(event.getAttendees(), request.getAttendees())) {
        conflicts.add(event.getWhen());
      }
    }
    Collections.sort(conflicts, TimeRange.ORDER_BY_START);
    return conflicts;
   }
}