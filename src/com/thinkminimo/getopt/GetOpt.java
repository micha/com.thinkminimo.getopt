package com.thinkminimo.getopt;

import java.io.*;
import java.util.*;
import gnu.getopt.LongOpt;

public class GetOpt {

  private ArrayList<LongOpt>      mLongOpts   = new ArrayList<LongOpt>();
  private ArrayList<String>       mDescs      = new ArrayList<String>();
  private HashMap<String, String> mOpts       = new HashMap<String, String>();

  private String                  mAppname    = "";
  private String[]                mArgv       = null;
  private File                    mFile       = null;

  public GetOpt(String[] argv) {
    this("getopt", argv);
  }

  public GetOpt(String name, String[] argv) {
    this(name, argv, null);
  }

  public GetOpt(String name, String[] argv, File file) {
    mAppname = name;
    mArgv    = argv;
    mFile    = file;
    
    addFlag("help", "Print this help info and exit.");

    mLongOpts.add(new LongOpt("showconfig", LongOpt.NO_ARGUMENT,  null, 3));
    mDescs.add("Print the saved configuration for this application and exit.");

    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        try {
          save();
        } catch (IOException e) {
          // ....
        }
      }
    });
  }

  private void load() throws IOException {
    if (mFile == null)
      return;

    BufferedReader in = new BufferedReader(new FileReader(mFile));
    String line;
    for (LongOpt l : mLongOpts)
      if ((line = in.readLine()) != null && 
          l.getHasArg() != LongOpt.NO_ARGUMENT)
        setOpt(l.getName(), line);
    in.close();
  }

  private void save() throws IOException {
    if (mFile == null)
      return;

    PrintWriter out = new PrintWriter(mFile);
    for (LongOpt l : mLongOpts)
      out.println(getOpt(l.getName()));
    out.close();
  }

  public GetOpt go() {
    try {
      load();
    } catch (IOException e) {
      // ...
    }
    LongOpt[] l = mLongOpts.toArray(new LongOpt[mLongOpts.size()]);
    gnu.getopt.Getopt g = new gnu.getopt.Getopt(mAppname, mArgv, "", l);
    int c;
    while ((c = g.getopt()) != -1) {
      switch (c) {
        case '?':
          System.exit(1);
        case 1:
          mOpts.put(mLongOpts.get(g.getLongind()).getName(), g.getOptarg());
          break;
        case 2:
          mOpts.put(mLongOpts.get(g.getLongind()).getName(), "true");
          break;
        case 3:
          showConfig();
          break;
      }
    }
    return this;
  }

  public String getOpt(String opt) {
    return mOpts.get(opt);
  }

  public GetOpt setOpt(String opt, String val) {
    mOpts.put(opt, val);
    return this;
  }

  public boolean getFlag(String opt) {
    return Boolean.valueOf(mOpts.get(opt));
  }

  public GetOpt setFlag(String opt, boolean val) {
    mOpts.put(opt, (new Boolean(val)).toString());
    return this;
  }

  public GetOpt addOpt(String lng) {
    return addOpt(lng, null);
  }

  public GetOpt addOpt(String lng, String desc) {
    mLongOpts.add(
        new LongOpt(lng, LongOpt.REQUIRED_ARGUMENT,  null, 1));
    mDescs.add(desc == null ? "" : desc);
    return this;
  }

  public GetOpt addFlag(String lng) {
    return addFlag(lng, null);
  }

  public GetOpt addFlag(String lng, String desc) {
    mLongOpts.add(new LongOpt(lng, LongOpt.NO_ARGUMENT,  null, 2));
    mDescs.add(desc == null ? "" : desc);
    return this;
  }

  public void showConfig() {
    for (LongOpt l : mLongOpts)
      if (l.getHasArg() != LongOpt.NO_ARGUMENT)
        System.out.printf("%15s => '%s'\n", l.getName(), getOpt(l.getName()));
    System.exit(0);
  }

  public void printUsage(int status) {
    System.out.println("OPTIONS:");
    System.out.printf("\n");

    for (int i = 0; i < mLongOpts.size(); i++) {
      LongOpt       o       = mLongOpts.get(i);
      String        name    = o.getName();
      int           flag    = o.getVal();
      String        desc    = mDescs.get(i);
      int           harg    = o.getHasArg();

      String arg = "";
      if (harg == LongOpt.REQUIRED_ARGUMENT)
        arg = " <arg>";
      else if (harg == LongOpt.OPTIONAL_ARGUMENT)
        arg = " [arg]";

      System.out.printf("    --%s%s\n", name, arg);
      para(desc, 65, "        ");
      System.out.printf("\n");
    }
    System.exit(status);
  }

  public static void para(String text, int w) {
    para(text, w, "");
  }

  public static void para(String text, int w, String pre) {
    while (text.length() > 0) {
      String tmp = (text.length() < w) ? text : 
        text.substring(0, w).replaceFirst("[^ \t\n]*$", "");
      text = text.substring(tmp.length());
      System.out.println(pre + tmp);
    }
  }
}
