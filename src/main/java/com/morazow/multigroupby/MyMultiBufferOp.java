package com.morazow.multigroupby;

import cascading.tuple.Tuple;
import cascading.tuple.Fields;
import com.liveramp.cascading_ext.multi_group_by.MultiBuffer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.collections.keyvalue.MultiKey;

public class MyMultiBufferOp extends MultiBuffer {

  private static final long serialVersionUID = 1L;

  public MyMultiBufferOp(Fields output) {
    super(output);
  }

  @Override
  public void operate() {

    // First pipe: UserAges <USERID, AGE>
    Iterator<Tuple> userAges = getArgumentsIterator(0);
    if (!userAges.hasNext())
      return ;

    Tuple userAgesTuple = userAges.next();
    int user_age = userAgesTuple.getInteger(1); // second field is age


    // Data structure to store the count
    MultiKey key = null;
    Map<MultiKey, Integer> countMap = new HashMap<MultiKey, Integer>();


    // Second pipe: Purchases <USERID, TIMESTAMP, STATE, PURCHASES>
    Iterator<Tuple> purchases = getArgumentsIterator(1);

    while (purchases.hasNext()) {
      Tuple purchasesTuple = purchases.next();

      int state = purchasesTuple.getInteger(2); // third column is state

      key = new MultiKey(state, user_age);
      if (countMap.containsKey(key)) {
        countMap.put(key, countMap.get(key) + 1);
      }
      else {
        countMap.put(key, 1);
      }
    }

    // We just calculated <STATE, AGE, COUNT> results stored in 'countMap'
    // Now we just have to emit COUNT, because we gave <STATE, AGE>
    // as grouping names when calling this buffer operation
    for (Map.Entry<MultiKey, Integer> entry : countMap.entrySet()) {
      key = entry.getKey();

      int state = (Integer) key.getKey(0);
      int age = (Integer) key.getKey(1);
      int count = entry.getValue();
      emit(new Tuple(state, age, count));
    }
  }
}
