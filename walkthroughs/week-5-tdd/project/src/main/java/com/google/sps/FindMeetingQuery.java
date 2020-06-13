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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

public final class FindMeetingQuery {
  /* If we can accomodate optional attendees, do so. Otherwise if there are required attendees,
  ignore optional attendees.*/
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<String> allAttendees = new ArrayList<String>();
    allAttendees.addAll(request.getAttendees());
    allAttendees.addAll(request.getOptionalAttendees());

    MeetingRequest requestWithOptionals = new MeetingRequest(allAttendees, 
        request.getDuration());
    Collection<TimeRange> optResults = queryInternal(events, requestWithOptionals);
    
    if ((optResults.isEmpty()) && (!request.getAttendees().isEmpty())) {
      return queryBFS(events, requestWithOptionals, request.getAttendees(),
          request.getOptionalAttendees());
    }
    else {
      return optResults;
    }
  }

  /* Follow a BFS, removing optional attendees gradually and returning when you find a timerange
  which accomodates the remaining optional attendees and the required attendees. If you never
  find such a timerange, return the results just accounting for required attendees. */
  public Collection<TimeRange> queryBFS(Collection<Event> events, MeetingRequest request, Collection<String> reqAttendees,
        Collection<String> optAttendees) {
    Queue<Collection<String>> queue = new ArrayDeque<Collection<String>>();
    queue.add(optAttendees);

    while (!queue.isEmpty()) {
      Collection<String> curOptAttendees = queue.remove();        
      Collection<String> curAttendees = new ArrayList<String>();
      curAttendees.addAll(curOptAttendees);
      curAttendees.addAll(reqAttendees);

      MeetingRequest curRequest = new MeetingRequest(curAttendees, 
          request.getDuration());
      Collection<TimeRange> results = queryInternal(events, curRequest);
      if (!results.isEmpty()) {
        return results;
      }
      
      else {
        for (String optAttendee : curOptAttendees) {
          Collection<String> newOptAttendees = new ArrayList<String>();
          newOptAttendees.addAll(curOptAttendees);
          newOptAttendees.remove(optAttendee);
          queue.add(newOptAttendees);
        }
      }
    }
    MeetingRequest reqRequest = new MeetingRequest(reqAttendees, request.getDuration());
    return queryInternal(events, reqRequest);
  }

  /* Identify conflicting events, then iterating through the day, skipping conflicts, and 
  identifying time ranges longer than the duration of the requested meeting. */
  public Collection<TimeRange> queryInternal(Collection<Event> events, MeetingRequest request) {
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