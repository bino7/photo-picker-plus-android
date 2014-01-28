/**
 * The MIT License (MIT)

Copyright (c) 2013 Chute

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chute.android.photopickerplus.models.enums;

/**
 * Enumeration for the type of the local service displayed.
 * 
 */
public enum LocalMediaType {
  CAMERA_MEDIA("camera media"), ALL_MEDIA("all media"), LAST_PHOTO_TAKEN(
      "last photo taken"), LAST_VIDEO_CAPTURED("last video captured"), TAKE_PHOTO(
      "take photo"), RECORD_VIDEO("record video");

  private final String name;

  private LocalMediaType(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  };

  public String getName() {
    return name;
  }
}