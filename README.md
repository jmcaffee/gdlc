# GDLC Guideline Compiler

GDLC is an XML guideline rule compiler for the Commerce Velocity origination
and loss mitigation application platforms.

It provides developers the ability to write easily versioned and maintained
rules that are compiled to the XML ('object') code the CV decision engine
understands.

No more point-and-click rule development!

## Installation

The easiest way to install and use GDLC is via the ruby gem of the same name.
See [its README](gem/README.md) for details on its use.

## Dependencies

The GDLC binary is written in Java. To use GDLC, you must have a Java runtime
of version 1.7 (Java 7) or better.

## Usage

Typing `GDLC` without any parameters (or with the `-h` option) will result in
the following usage message:

```
======================================================================
GDLC GuideLine Compiler
Usage:  GDLC inFile [outFile] [-switch]* [--I]* [--C]*

        [] = optional
        *  = 0 or more, separated by spaces

        inFile    name of file to compile.
        outFile   name of XML output file. (default is guideline name)

    --switches--
        -h, -help     show usage instructions.
        -version      display the application version.

        -no,-nooutput do not generate output.
        -r, -raw      force output of all rules/sets/lookups.
                      outFile is a required parameter when -raw is used.
        -v, -verbose  show all status messages.
        -vp,          show parse tree.

        -validxml     output valid xml.

    --parameters--
        --Ipath       path to include dir. Default: current dir
        --Cpath       path to search for config files.

======================================================================
```

See the [User Manual](docs/GDLC_manual.md) for further information and details
on the GDLC language.

## Contributing

Bug reports and pull requests are welcome on GitHub at https://github.com/jmcaffee/gdlc.

1. Fork it ( https://github.com/jmcaffee/gdlc/fork )
1. Clone it (`git clone git@github.com:[my-github-username]/gdlc.git`)
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Create tests for your feature branch (pull requests not accepted without tests)
4. Commit your changes (`git commit -am 'Add some feature'`)
5. Push to the branch (`git push origin my-new-feature`)
6. Create a new Pull Request

## License

The compiler is available as open source under the terms of the [GPLv3](http://www.gnu.org/licenses/gpl.html).


