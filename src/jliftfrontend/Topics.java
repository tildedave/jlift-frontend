/*
 * File          : Topics.java
 * Project       : jlift-frontend
 * Description   : <insert description here>
 * Author(s)     : dhking
 *
 * Created       : Feb 11, 2008 11:33:25 AM 
 *
 * Copyright (c) 2007-2008 The Pennsylvania State University
 * Systems and Internet Infrastructure Security Laboratory
 *
 */
package jliftfrontend;

import polyglot.main.Report;

/**
 * Extension information for jliftfrontend extension.
 */
public class Topics {
    public static final String jliftfrontend = "jliftfrontend";

    static {
        Report.topics.add(jliftfrontend);
    }
}
