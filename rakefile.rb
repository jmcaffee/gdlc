##############################################################################
# File:: rakefile.rb
# Purpose:: Generate Java code using JavaCC grammar file.
# 
# Author::    Jeff McAffee 02/26/2010
# Copyright:: Copyright (c) 2010 kTech Systems LLC. All rights reserved.
# Website::   http://ktechsystems.com
##############################################################################

require 'rake'
require 'rake/clean'
require 'rakeUtils.rb'


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
# Note: Rake loads imports only after this rakefile has been completely loaded.
import "#{GRAMMAR_DIR}/grammar.rake"

#######################################

#task :default => [:clean, :generateParser]
task :default do
  puts "default task does nothing... yet"
end


#######################################

task :init => [SRC_PARSER_DIR, BUILD_DIR, DIST_DIR]

#######################################

desc "Build GDLC application distribution"

task :dist => [:init, :build, :jar, DIST_DIR, :copyLibsToDist] do
  cp_r("#{BUILD_DIR}/gdlc.jar", "#{DIST_DIR}")
  
  puts "GDLC distro built"
end

#######################################

desc "Build GDLC application"

task :build => [:init, :buildGrammar, :compile] do
  puts "Application compiled"
end

#######################################

task :buildGrammar => [:init, "grammar:generate", 
                      :clean_backup,
                      "grammar:backup", 
                      "grammar:copyToAppSrc",
                      "grammar:replaceCustomSrc"] do
  puts "Grammar source files built"
end

#######################################

desc "compile all java src files"

task :compile => [:init] do
  cvsjar = File.expand_path("#{LIBS_DIR}/ostermillerutils_1_06_01.jar").gsub(/\//, "\\")
  cvsjar = "#{LIBS_DIR}/ostermillerutils_1_06_01.jar"

  classpath = "-classpath #{cvsjar}"
  
  options = "#{classpath}"
  options << " -sourcepath #{SRC_DIR}"
  options << " -d #{BUILD_DIR}"           # Destination dir
  options << " -nowarn"                   # Don't show compile warnings
  options << " -O"                        # Optimize for speed
  #options << " -g"                        # Include debugging info
  #options << " -Xlint:unchecked"          # Run lint for unchecked warnings

  sourceFiles = ""
  srcFiles = FileList.new(Dir.glob("#{SRC_DIR}/**/*.java"))
  srcFiles.each do |f|
    sourceFiles << " #{f}"
  end
  
  output = `javac #{options} #{sourceFiles}`
  puts output
  
end

#######################################

desc "compile each src file individually"

task :compile_each => [:init] do
  cvsjar = File.expand_path("#{LIBS_DIR}/ostermillerutils_1_06_01.jar").gsub(/\//, "\\")
  cvsjar = File.expand_path("#{LIBS_DIR}/ostermillerutils_1_06_01.jar")
  cvsjar = "#{LIBS_DIR}/ostermillerutils_1_06_01.jar"

  classpath = "-classpath #{cvsjar}"
  
  options = "#{classpath}"
  options << " -sourcepath ./#{SRC_DIR}"
  options << " -d #{BUILD_DIR}"           # Destination dir
  options << " -nowarn"                   # Don't show compile warnings
  options << " -O"                        # Optimize for speed
  #options << " -g"                        # Include debugging info
  #options << " -Xlint:unchecked"          # Run lint for unchecked warnings

  sourceFiles = ""
  srcFiles = FileList.new(Dir.glob("#{SRC_DIR}/**/*.java"))
  srcFiles.each do |f|
    puts "Compiling: #{f}"
    output = `javac #{options} #{f}`
    puts output
  end
    
end

#######################################

desc "generate application jar file"

task :jar => [:compile] do
  target    = "gdlc.jar"
  manifest  = "../#{MISC_DIR}/manifest.mf"
  
  cd("#{BUILD_DIR}") do |dir| # Everything is relative to the build dir (where class files are coming from).
    `jar -cvfm "#{target}" "#{manifest}" "runtime"`
    # jar -cvfm jars/gdlc.zip misc/manifest.mf -C build .     # '-C' flag works from the command line but not here.
  end
  
end


#######################################

task :copyLibsToDist do
  FileList.new(Dir.glob("#{LIBS_DIR}/**.*")).each do |f|
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

