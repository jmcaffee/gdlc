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

DOCS_DIR          = "docs"
LIBS_DIR          = "libs"
MISC_DIR          = "misc"
DIST_DIR          = "./dist"

# Subdirs
GRAMMAR_DIR         = "grammar"
GRAMMAR_BUILD_DIR   = "#{GRAMMAR_DIR}/build"

GEM_SRC_DIR       = "gem"
GEM_DOCS_DIR      = "#{GEM_SRC_DIR}/docs"
GEM_RES_DIR       = "#{GEM_SRC_DIR}/res"

# Vendor Libraries
OSTERMILLERUTILS	= File.expand_path(File.join(LIBS_DIR, "ostermillerutils","ostermillerutils*.jar"))

#### Cleanup tasks
CLEAN.include("#{BUILD_DIR}/**/*.*")
CLEAN.include("#{DIST_DIR}/**/*.*")
CLEAN.include("#{SRC_PARSER_DIR}/**/*.*")
CLEAN.include("#{GEM_DOCS_DIR}/**/*.*")
CLEAN.include("#{GEM_RES_DIR}/**/*.*")
CLOBBER.include("#{BUILD_DIR}")
CLOBBER.include("#{DIST_DIR}")
CLOBBER.include("#{SRC_PARSER_DIR}")
CLOBBER.include("#{GEM_DOCS_DIR}")
CLOBBER.include("#{GEM_RES_DIR}")


#### Directory creation tasks
directory BUILD_DIR
directory DIST_DIR
directory BACKUP_DIR
directory BACKUP_PARSER_DIR
directory SRC_PARSER_DIR


#######################################

#task :default => [:clean, :generateParser]
task :default do
  puts "default task does nothing... yet"
end


#######################################

task :init => [SRC_PARSER_DIR, BUILD_DIR, DIST_DIR]

#######################################

desc "Build GDLC application distribution"

task :dist => [:init, :build, :jar, :copy_jar_to_dist, :copy_libs_to_dist, :update_gem_files, "gem:build"] do
  puts
  puts "GDLC distro built"
  puts
end

#######################################

task :update_gem_resources => [:init, :copy_jar_to_dist, :copy_libs_to_dist] do
  gem_resource_dir = Pathname(GEM_RES_DIR)

  if gem_resource_dir.exist?
    gem_resource_dir.rmtree
    gem_resource_dir.mkpath
  end

  dist_files = FileList.new(Dir.glob("#{DIST_DIR}/**/*.*"))
  dist_files.each do |f|
    cp f, gem_resource_dir
  end

  cp "#{MISC_DIR}/manifest.mf", gem_resource_dir

  puts "gem resource files updated"
end

#######################################

desc 'Generate GDLC manual HTML'
task :man do

  ENV['TITLE'] = 'GDLC User Manual'

  Rake::Task['markdown:md2html'].invoke( 'docs/GDLC_manual.md' )
  Rake::Task['markdown:md2html'].reenable

end

#######################################

task :update_gem_docs => [:init, GEM_DOCS_DIR, :man] do
  gem_docs_dir = Pathname(GEM_DOCS_DIR)

  if gem_docs_dir.exist?
    gem_docs_dir.rmtree
    gem_docs_dir.mkpath
  end

  doc_files = FileList.new(Dir["#{DOCS_DIR}/GDLC_manual.*"])
  doc_files.each do |f|
    cp f, gem_docs_dir
  end

  puts "gem doc files updated"
end

#######################################

task :update_gem_files => [:init, :update_gem_resources, :update_gem_docs]

#######################################

task :copy_jar_to_dist => [:init, :jar, DIST_DIR] do
  cp("#{BUILD_DIR}/gdlc.jar", "#{DIST_DIR}")
  puts "gdlc.jar copied to #{DIST_DIR}."
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
                      "grammar:copy_to_app_src",
                      "grammar:replace_custom_src"] do
  puts "Grammar source files built"
end

#######################################

desc "compile all java src files"

task :compile => [:init] do
  puts "Compiling java source."

  # Note: on windows, may need to change file separators:
  #cvsjar = File.expand_path("#{OSTERMILLERUTILS}").gsub(/\//, "\\")

  jopts = []
  jopts << "-classpath #{OSTERMILLERUTILS}"
  jopts << "-sourcepath #{SRC_DIR}"
  jopts << "-d #{BUILD_DIR}"            # Destination dir
  jopts << "-nowarn"                    # Don't show compile warnings
  jopts << "-O"                         # Optimize for speed
  jopts << "-source 1.7"                # Source is 1.6 compatible
  jopts << "-target 1.7"                # Build to run on 1.6 or better
  #jopts << "-g"                        # Include debugging info
  #jopts << "-Xlint:unchecked"          # Run lint for unchecked warnings

  source_files = ""
  src_files = FileList.new(Dir.glob("#{SRC_DIR}/**/*.java"))
  src_files.each do |f|
    source_files << " #{f}"
  end

  output = `javac #{jopts.join(' ')} #{source_files}`
  puts output
end

#######################################

desc "compile each src file individually"

task :compile_each => [:init] do
  puts "Compiling each java source file individually."

  # Note: on windows, may need to change file separators:
  #cvsjar = File.expand_path("#{OSTERMILLERUTILS}").gsub(/\//, "\\")

  jopts = []
  jopts << "-classpath #{OSTERMILLERUTILS}"
  jopts << "-sourcepath #{SRC_DIR}"
  jopts << "-d #{BUILD_DIR}"            # Destination dir
  jopts << "-nowarn"                    # Don't show compile warnings
  jopts << "-O"                         # Optimize for speed
  #jopts << "-g"                        # Include debugging info
  #jopts << "-Xlint:unchecked"          # Run lint for unchecked warnings

  source_files = ""
  src_files = FileList.new(Dir.glob("#{SRC_DIR}/**/*.java"))
  src_files.each do |f|
    puts "Compiling: #{f}"
    output = `javac #{jopts.join(' ')} #{f}`
    puts output
  end
end

#######################################

desc "generate application jar file"

task :jar => [:compile] do
  puts "Building java archive file (jar)."
  target    = "gdlc.jar"
  manifest  = "../#{MISC_DIR}/manifest.mf"

  # Everything is relative to the build dir (where class files are coming from).
  cd("#{BUILD_DIR}") do |dir|
    `jar -cvfm "#{target}" "#{manifest}" "runtime"`
    # '-C' flag works from the command line but not here.
    # jar -cvfm jars/gdlc.zip misc/manifest.mf -C build .
  end
end


#######################################

task :copy_libs_to_dist => [:init, DIST_DIR] do
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

