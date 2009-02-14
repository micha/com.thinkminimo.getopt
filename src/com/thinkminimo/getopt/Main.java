package com.thinkminimo.getopt;

import java.io.File;

class Main {
  public static void main(String[] argv) {
    GetOpt o = new GetOpt("myapp", argv);

    o.addOpt("foo", "Foo is the option that will make all your dreams come true. All you really have to do is believe in it. You look very nice today. OK. I understand. Well I'll be back later for that crack I loaned you.");

    o.addFlag("bar", "Bar is a flag that can be true (i.e. if you specify it on the command line) or false (i.e. if you don't).");

    System.out.println("command line nonoption arguments are:");
    for (String s : o.getArgv())
      System.out.println("  > "+s);

    o.go();

    if (o.getFlag("help")) {
      System.out.println("\nUsage: myapp [OPTIONS] arg arg\n");
      o.printUsage(1);
    } else {
      System.out.println("The value of 'foo' is '" + o.getOpt("foo") + "'.");
    }
  }
}
