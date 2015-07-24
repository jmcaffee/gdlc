# GDLC

This gem provides a rubygem wrapper around the Java based Guideline Compiler (GDLC).

## Installation

Add this line to your project's Gemfile:

    gem 'gdlc'

And then execute:

    $ bundle install

Or install it yourself as:

    $ gem install gdlc

## Usage

Here's an example rake file, stored in my project's `rakelib/` directory
so rake auto-loads it.

_compile.rake_:

```ruby

require 'gdlc'
require 'pathname'

# Constants populated in Rakefile:
# BUILDDIR - location generated .xml files are placed
# SRCDIR   - location of main .gdl guideline files
# COMDIR   - location of common .gdl files
# CSVDIR   - location of PowerLookup .csv files
#

# Use config dir in root of project
GFLAGS = ["--Cconfig"]

# Guideline include dirs to be searched by compiler.
GDL_INC_DIRS  = [ CSVDIR,
                  SRCDIR,
                  "#{SRCDIR}/inc",
                  "#{COMDIR}/inc",
                ]


namespace :compile do

  ##
  # Map a .gdl file to a .xml file in BUILDDIR
  #

  def target_file_from_source(srcfile)
    target_file = srcfile.pathmap("%{.gdl,.xml}f")
    target_dir = Pathname(BUILDDIR)
    target_path = target_dir + target_file
    target_path.to_s
  end

  ##
  # Compile a guideline into XML
  #

  def compile_guideline gdlfile, gflags = [], incdirs = []
    gdlfile += '.gdl' unless gdlfile.end_with?('.gdl')

    unless File.exists? gdlfile
      gdlfile = File.join(SRCDIR, gdlfile)
    end

    puts "Compiling: #{gdlfile}"

    # Set compiler flags
    gflags = Array(gflags)
    gflags.each do |flag|
      Gdlc::GDLC.add_flag flag
    end

    # Set compiler include dirs
    inc_dirs = Array(inc_dirs)
    if inc_dirs.empty?
      puts "WARNING: No include directories have been provided."
    end
    Gdlc::GDLC.add_include_dirs inc_dirs

    # Compile
    Gdlc::GDLC.compile(gdlfile, target_file_from_source(gdlfile))
  end

  ##
  # Create a compile task for each *.gdl file in the root SRCDIR.
  #

  gdl_files = Dir["#{SRCDIR}/*.gdl"]
  gdl_files.each do |gdl|
    #desc "compile#{File.basename(gdl,'.gdl').to_sym}"
    task File.basename(gdl,'.gdl').to_sym => [BUILDDIR] do
      compile_guideline gdl, GFLAGS, GDL_INC_DIRS
    end
  end

  ##
  # A task to compile all 'main' guidelines
  #

  desc 'Compile all guidelines'
  task :all do

    gdl_files = Dir["#{SRCDIR}/*.gdl"]
    gdl_files.each do |gdl|
      compile_guideline gdl, GFLAGS, GDL_INC_DIRS
    end
  end

  ##
  # A task to compile an individual guideline
  #

  desc 'Compile a GDL file'
  task :file, [:gdlfile] => [BUILDDIR] do |t, args|
    args.with_defaults(:gdlfile => nil)
    if args[:gdlfile].nil?
      puts "ERROR: Full path to file required (unless it's located within the src dir)."
      puts
      puts 'usage:  rake compile:file[path/to/file]'
      puts '         OR'
      puts '        rake compile:gdlname'
      puts '  where gdlname is the basename of a gdl located in the src dir.'
      exit
    end

    if BUILDDIR.nil?
      puts 'ERROR: BUILDDIR must be set.'
      exit
    end

    compile_guideline args[:gdlfile], GFLAGS, GDL_INC_DIRS
  end

end # namespace :compile
```

## Contributing

This gem is a subset of the GDLC project. To contribute, see the parent
project at https://github.com/jmcaffee/gdlc.

