package com.software.jpetstore.persistence;

import com.software.jpetstore.domain.Sequence;

public interface SequenceMapper {

  Sequence getSequence(Sequence sequence);
  void updateSequence(Sequence sequence);
}
