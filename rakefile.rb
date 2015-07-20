##############################################################################
# File:: rakefile.rb
# Purpose:: Generate Java code using JavaCC grammar file.
#
# Author::    Jeff McAffee 02/26/2010
# Copyright:: Copyright (c) 2010 Jeff McAffee. All rights reserved.
# Website::   http://ktechsystems.com
##############################################################################

require 'rake/clean'

# Setup load paths for bundler gems.
#require 'bundler/setup'

require 'rakeutils/filegentask'

$verbose = true

#### Directories
BUILD_DIR         = "./build"

SRC_DIR           = "src"
SRC_PARSER_DIR    = "#{SRC_DIR}/runtime/parser"

BACKUP_DIR        = "backup"
BACKUP_PARSER_DIR = "#{BACKUP_DIR}/runtime/parser"

LIBS_DIR          = "libs"
MISC_DIR          = "misc"
DIST_DIR          = "./dist"

# Subdirs
GRAMMAR_DIR         = "grammar"
GRAMMAR_BUILD_DIR   = "#{GRAMMAR_DIR}/build"

# Vendor Libraries
OSTERMILLERUTILS	= File.expand_path(File.join(LIBS_DIR, "ostermillerutils","ostermillerutils*.jar"))

#### Cleanup tasks
CLEAN.include("#{BUILD_DIR}/**/*.*")
CLEAN.include("#{DIST_DIR}/**/*.*")
CLEAN.include("#{SRC_PARSER_DIR}/**/*.*")
CLOBBER.include("#{BUILD_DIR}")
CLOBBER.include("#{DIST_DIR}")
CLOBBER.include("#{SRC_PARSER_DIR}")


#### Directory creation tasks
directory BUILD_DIR
directory DIST_DIR
directory BACKUP_DIR
directory BACKUP_PARSER_DIR
directory SRC_PARSER_DIR

#### Imports
# Note: Rake loads imports only after the current rakefile has been completely loaded.
# Load grammar tasks.
import "#{GRAMMAR_DIR}/grammar.rake"

# Load local tasks.
imports = FileList['tasks/**/*.rake']
imports.each do |imp|
  puts "Importing local task file: #{imp}" if $verbose
  import "#{imp}"
end




#######################################

#task :default => [:clean, :generateParser]
task :default do
  puts "default task does nothing... yet"
end


#######################################

task :init => [SRC_PARSER_DIR, BUILD_DIR, DIST_DIR]

#######################################

desc "Build GDLC application distribution"

task :dist => [:init, :build, :jar, DIST_DIR, :copy_libs_to_dist] do
  cp_r("#{BUILD_DIR}/gdlc.jar", "#{DIST_DIR}")

  puts "GDLC distro built"
end

#######################################

desc "Build GDLC application"

task :build => [:init, :build_grammar, "version:generate_version_file", :compile] do
  puts "Application compiled"
end

#######################################

task :build_grammar => [:init, "grammar:generate",
                      :clean_backup,
                      "grammar:backup",
                      "grammar:copyToAppSrc",
                      "grammar:replaceCustomSrc"] do
  puts "Grammar source files built"
end

#######################################

desc "compile all java src files"

task :compile => [:init] do
  puts "Compiling java source."
#  cvsjar = File.expand_path("#{OSTERMILLERUTILS}").gsub(/\//, "\\")
  cvsjar = "#{OSTERMILLERUTILS}"

  classpath = "-classpath #{cvsjar}"

  options = "#{classpath}"
  options << " -sourcepath #{SRC_DIR}"
  options << " -d #{BUILD_DIR}"           # Destination dir
  options << " -nowarn"                   # Don't show compile warnings
  options << " -O"                        # Optimize for speed
  #options << " -g"                        # Include debugging info
  #options << " -Xlint:unchecked"          # Run lint for unchecked warnings

  source_files = ""
  src_files = FileList.new(Dir.glob("#{SRC_DIR}/**/*.java"))
  src_files.each do |f|
    source_files << " #{f}"
  end

  output = `javac #{options} #{source_files}`
  puts output
end

#######################################

desc "compile each src file individually"

task :compile_each => [:init] do
  puts "Compiling each java source file individually."

  cvsjar = File.expand_path("#{OSTERMILLERUTILS}").gsub(/\//, "\\")
  cvsjar = File.expand_path("#{OSTERMILLERUTILS}")
  cvsjar = "#{OSTERMILLERUTILS}"

  classpath = "-classpath #{cvsjar}"

  options = "#{classpath}"
  options << " -sourcepath ./#{SRC_DIR}"
  options << " -d #{BUILD_DIR}"           # Destination dir
  options << " -nowarn"                   # Don't show compile warnings
  options << " -O"                        # Optimize for speed
  #options << " -g"                        # Include debugging info
  #options << " -Xlint:unchecked"          # Run lint for unchecked warnings

  source_files = ""
  src_files = FileList.new(Dir.glob("#{SRC_DIR}/**/*.java"))
  src_files.each do |f|
    puts "Compiling: #{f}"
    output = `javac #{options} #{f}`
    puts output
  end
end

#######################################

desc "generate application jar file"

task :jar => [:compile] do
  puts "Building java archive file (jar)."
  target    = "gdlc.jar"
  manifest  = "../#{MISC_DIR}/manifest.mf"

  cd("#{BUILD_DIR}") do |dir| # Everything is relative to the build dir (where class files are coming from).
    `jar -cvfm "#{target}" "#{manifest}" "runtime"`
    # jar -cvfm jars/gdlc.zip misc/manifest.mf -C build .     # '-C' flag works from the command line but not here.
  end
end


#######################################

task :copy_libs_to_dist do
  FileList.new(Dir.glob("#{LIBS_DIR}/**/**.*")).each do |f|
    cp_r(f, "#{DIST_DIR}")
  end
  puts "Libraries copied to #{DIST_DIR}."
end

#######################################

desc "clean all files in backup dir"

task :clean_backup do
  if( File.directory?(BACKUP_DIR) )
    FileList.new(Dir.glob("#{BACKUP_DIR}/**.*")).each do |f|
      rm_f(f)
    end
    puts "Backup directory cleaned"
  end
end

#######################################

desc "clobber backup dir"

task :clobber_backup do
  if( File.directory?(BACKUP_DIR) )
    rm_rf(BACKUP_DIR)
    puts "Backup directory clobbered"
  end

end

#######################################
#######################################

namespace :version do

  def display_gem_version_update_notice version_hash
    vmajor = version_hash["version_major"]
    vminor = version_hash["version_minor"]
    vbuild = version_hash["version_build"]

    puts "You must manually update the gem version to #{vmajor}.#{vminor}.#{vbuild}"
  end

  #######################################

  desc "increment major version number"

  task :inc_major do
    yml = DataFile.new
    key = "version_major"
    minorkey = "version_minor"
    buildkey = "version_build"
    data = yml.read("#{MISC_DIR}/projectproperties.yml")
    data["#{key}"] = data["#{key}"] + 1
    data["#{minorkey}"] = 0
    data["#{buildkey}"] = 0
    yml.write("#{MISC_DIR}/projectproperties.yml", data)

    display_gem_version_update_notice data
  end

  #######################################

  desc "increment minor version number"

  task :inc_minor do
    yml = DataFile.new
    key = "version_minor"
    buildkey = "version_build"
    data = yml.read("#{MISC_DIR}/projectproperties.yml")
    data["#{key}"] = data["#{key}"] + 1
    data["#{buildkey}"] = 0
    yml.write("#{MISC_DIR}/projectproperties.yml", data)

    display_gem_version_update_notice data
  end

  #######################################

  desc "increment build version number"

  task :inc_build do
    yml = DataFile.new
    key = "version_build"
    data = yml.read("#{MISC_DIR}/projectproperties.yml")
    data["#{key}"] = data["#{key}"] + 1
    yml.write("#{MISC_DIR}/projectproperties.yml", data)

    display_gem_version_update_notice data
  end

  #######################################

  #desc "generate app version file"

  task :generate_version_file do
    appVersionFile = "Constants.java"
    versionFile = File.join(MISC_DIR, "projectproperties.yml")

    srcDir = MISC_DIR
    destDir = "src/runtime/main"

    srcPath = File.join(srcDir, appVersionFile)
    destPath = File.join(destDir, appVersionFile)

    gen = FileGenTask.new(true)
    gen.generate(srcPath, destPath, versionFile)

  end

  #######################################

  #desc "create yml"

  task :create_yml do
    yml = DataFile.new
    data = {}
    data["version_major"] = 1
    data["version_minor"] = 0
    data["version_build"] = 0
    yml.write("#{MISC_DIR}/newprops.yml", data)
    puts "New version file written to [ #{MISC_DIR}/newprops.yml ]."

  end

end # namespace :version

#######################################
#######################################

def current_version_string
  yml = DataFile.new
  key = "version_major"
  minorkey = "version_minor"
  buildkey = "version_build"

  data = yml.read("#{MISC_DIR}/projectproperties.yml")
  major = data["#{key}"]
  minor = data["#{minorkey}"]

  # The version file is incremented at the end of the build so it's ready for the next.
  # To be sure the filename will match the actual version, decrement the build #.
  build = data["#{buildkey}"] - 1

  version_str = "#{major}.#{minor}.#{build}"
end

#######################################
#######################################

