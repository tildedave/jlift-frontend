/*
 * File          : Version.java
 * Project       : jlift-frontend
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:33:24 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend;

/**
 * Version information for jliftfrontend extension
 */
public class Version extends polyglot.main.Version {
    public String name() { return "jliftfrontend"; }

    // TODO: define a version number, the default (below) is 0.1.0
    public int major() { return 0; }
    public int minor() { return 1; }
    public int patch_level() { return 0; }
}
