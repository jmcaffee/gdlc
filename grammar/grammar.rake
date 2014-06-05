##############################################################################
# File:: grammar.rake
# Purpose:: Generate Java code using JavaCC grammar file.
# 
# Author::    Jeff McAffee 02/27/2010
# Copyright:: Copyright (c) 2010 kTech Systems LLC. All rights reserved.
# Website::   http://ktechsystems.com
##############################################################################

require 'rake'
require 'rake/clean'
require 'rakeutils'
require 'rakeutils/jjtreetask'
require 'rakeutils/javacctask'


#### Directories
GRAMMAR_SRC_DIR = "#{GRAMMAR_DIR}/src"


#### Files
# Basename of grammar file
GRAMMAR_BASENAME    = "gdlGrammar"


# Grammar files
JJTREE_GRAMMAR_FILE = "#{GRAMMAR_DIR}/#{GRAMMAR_BASENAME}.jjt"
JAVACC_GRAMMAR_FILE = "#{GRAMMAR_BUILD_DIR}/#{GRAMMAR_BASENAME}.jj"


#### Cleanup tasks
CLEAN.include("#{GRAMMAR_BUILD_DIR}/**/*.*")
CLOBBER.include("#{GRAMMAR_BUILD_DIR}")


#### Directory creation tasks
directory GRAMMAR_BUILD_DIR


namespace :grammar do

  #######################################

  task :init => [GRAMMAR_BUILD_DIR]

  #######################################

  desc "Generate GDLC parsing classes (java)"

  task :generate => [:init, :jjTreeTarget, :javaccTarget] do
    puts "grammar generated"
  end

  #######################################

  task :buildParser => [:init, :jjTreeTarget, :javaccTarget] do
    puts "parser built"
  end

  #######################################

  task :jjTreeTarget => [:init] do
    jjTreeTask = JJTreeTask.new
    #jjTreeTask.outputFile(t.name)
    #jjTreeTask.static("false")
    jjTreeTask.outputDir(GRAMMAR_BUILD_DIR)
    jjTreeTask.generateFrom(JJTREE_GRAMMAR_FILE)

    puts "jjTreeTarget completed"
  end

  #######################################

  task :javaccTarget => [:init] do
    javaCcTask = JavaCCTask.new
    javaCcTask.outputDir(GRAMMAR_BUILD_DIR)
    javaCcTask.generateFrom(JAVACC_GRAMMAR_FILE)

    puts "javaccTarget completed"
  end

  #######################################

  task :backup => [BACKUP_PARSER_DIR] do
    javaList = FileList.new(Dir.glob("#{SRC_PARSER_DIR}/*.*"))
    javaList.each do |f|
      cp_r(f, "#{BACKUP_PARSER_DIR}")
    end

  end

  #######################################

  task :copyToAppSrc do
    javaList = FileList.new(Dir.glob("#{GRAMMAR_BUILD_DIR}/*.java"))
    javaList.include(Dir.glob("#{GRAMMAR_BUILD_DIR}/*.jj"))
    javaList.each do |f|
      cp_r(f, "#{SRC_PARSER_DIR}")
    end

  end
  #######################################

  task :replaceCustomSrc do
#    delFiles = %w( ASTEqualityCompute.java ASTLogicalCompute.java ASTMathCompute.java BaseNode.java Compute.java SimpleNode.java )
#    delFiles.each do |f|
#      rm_f("#{SRC_PARSER_DIR}/#{f}")
#    end
    custSrcList = FileList.new(Dir.glob("#{GRAMMAR_SRC_DIR}/*.java"))
    custSrcList.each do |f|
      destFile = "#{SRC_PARSER_DIR}/" + File.basename(f)
      if( File.exists?(destFile) )
        rm_f(destFile)
      end
      cp_r(f, "#{SRC_PARSER_DIR}")
    end

  end


end # namespace :grammar
