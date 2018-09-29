import 'dart:async';

import 'package:flutter/services.dart';

const EventChannel _rotationEventChanel =
    EventChannel('sensors.congphuong.com/rotation');

class RotationEvent {
  final List<double> matrix;

  RotationEvent(this.matrix);
}

Stream<RotationEvent> _rotationEvent;

Stream<RotationEvent> get rotationEvent {
  if (_rotationEvent == null) {
    _rotationEvent = _rotationEventChanel
        .receiveBroadcastStream()
        .map((dynamic event) => RotationEvent(event.cast<double>()));
  }
  return _rotationEvent;
}
