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

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.*;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> results = new ArrayList<TimeRange>();

    // Find conflicts.
    ArrayList<TimeRange> conflicts = new ArrayList<TimeRange>();
    for (Event event : events) {
      if (!Collections.disjoint(event.getAttendees(), request.getAttendees())) {
        conflicts.add(event.getWhen());
      }
    }
    Collections.sort(conflicts, TimeRange.ORDER_BY_START);
    Iterator<TimeRange> conflictIterator = conflicts.iterator();

    // Find possible meeting times.
    long duration = request.getDuration();
    long currentTime = Long.valueOf(TimeRange.START_OF_DAY);
    TimeRange previous = TimeRange.fromStartDuration(TimeRange.START_OF_DAY, 0);
    while (conflictIterator.hasNext()) {
      TimeRange conflictTime = conflictIterator.next();
      if (previous.contains(conflictTime)) {
        continue;
      }
      long start = Long.valueOf(conflictTime.start());
      if ((start > currentTime) && ((Long.valueOf(start) - currentTime) >= duration)) {
        results.add(TimeRange.fromStartEnd((int)currentTime, (int)start, false));
      }
      currentTime = conflictTime.end();
      previous = conflictTime;
    }
    if ((Long.valueOf(TimeRange.END_OF_DAY) - currentTime) >= duration) {
      results.add(TimeRange.fromStartEnd((int)currentTime, TimeRange.END_OF_DAY, true));
    }
    return results;
  }
}
