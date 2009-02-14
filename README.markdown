com.thinkminimo.GetOpt
======================

Getopt is a simplified commandline parsing class for java. Here are the
features it has:

+ Really simple setup and use. Minimum number of lines of code to get it
  to do useful things.

+ Parses long options, either with a mandatory argument or as a flag with
  no arguments allowed. No support for short options or optional arguments,
  though.

+ Takes care of printing out the options usage screen. You provide a
  description for each option and it will print out a nicely formatted
  usage guide if the user does the built-in --help option.

+ Can optionally keep track of options in a configuration file. It can
  save its state to the file on exit and then load the configuration from
  it the next time the application runs, with no extra code to write.

+ Option values can be interpreted as a boolean flag or as a string by
  accessing them via the <tt>getOpt(String)</tt> or <tt>getFlag(String)</tt>
  methods.

+ Acts like a HashMap in that you can pass it around and different parts
  of the application can have access to the configuration via a simple
  <tt>obj.getOpt("optname")</tt> or <tt>obj.getFlag("flagname")</tt>.

Quickstart
----------

Try doing <tt>ant -projecthelp</tt> to see how to build the jar file, or
just copy it into your project.

This is from the Main.java file included in the repo here:

    package com.thinkminimo.getopt;

    import java.io.File;

    class Main {
      public static void main(String[] argv) {
        GetOpt o = new GetOpt("myapp", argv, new File(".golfconfig"));

        o.addOpt("foo", "Foo is the option that will make all your dreams come true. All you really have to do is believe in it. You look very nice today. OK. I understand. Well I'll be back later for that crack I loaned you.");

        o.addFlag("bar", "Bar is a flag that can be true (i.e. if you specify it on the command line) or false (i.e. if you don't).");

        o.go();

        if (o.getFlag("help")) {
          System.out.println("\nUsage: myapp [OPTIONS] arg arg\n");
          o.printUsage(1);
        } else {
          System.out.println("The value of 'foo' is '" + o.getOpt("foo") + "'.");
        }
      }
    }

