##############################################################################
# File:: grammar.rake
# Purpose:: Generate Java code using JavaCC grammar file.
#
# Author::    Jeff McAffee 02/27/2010
# Copyright:: Copyright (c) 2010 Jeff McAffee. All rights reserved.
# Website::   http://ktechsystems.com
##############################################################################

require 'rake'
require 'rake/clean'
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

  task :generate => [:init, :jj_tree_target, :javacc_target] do
    puts "grammar generated"
  end

  #######################################

  task :build_parser => [:init, :jj_tree_target, :javacc_target] do
    puts "parser built"
  end

  #######################################

  task :jj_tree_target => [:init] do
    jjt_task = JJTreeTask.new
    #jjt_task.output_file(t.name)
    #jjt_task.static("false")
    jjt_task.output_dir(GRAMMAR_BUILD_DIR)
    jjt_task.generate_from(JJTREE_GRAMMAR_FILE)

    puts "jj_tree_target completed"
  end

  #######################################

  task :javacc_target => [:init] do
    javacc_task = JavaCCTask.new
    javacc_task.output_dir(GRAMMAR_BUILD_DIR)
    javacc_task.generate_from(JAVACC_GRAMMAR_FILE)

    puts "javacc_target completed"
  end

  #######################################

  task :backup => [BACKUP_PARSER_DIR] do
    java_list = FileList.new(Dir.glob("#{SRC_PARSER_DIR}/*.*"))
    java_list.each do |f|
      cp_r(f, "#{BACKUP_PARSER_DIR}")
    end

  end

  #######################################

  task :copy_to_app_src do
    java_list = FileList.new(Dir.glob("#{GRAMMAR_BUILD_DIR}/*.java"))
    java_list.include(Dir.glob("#{GRAMMAR_BUILD_DIR}/*.jj"))
    java_list.each do |f|
      cp_r(f, "#{SRC_PARSER_DIR}")
    end

  end
  #######################################

  task :replace_custom_src do
#    del_files = %w( ASTEqualityCompute.java ASTLogicalCompute.java ASTMathCompute.java BaseNode.java Compute.java SimpleNode.java )
#    del_files.each do |f|
#      rm_f("#{SRC_PARSER_DIR}/#{f}")
#    end
    cust_src_list = FileList.new(Dir.glob("#{GRAMMAR_SRC_DIR}/*.java"))
    cust_src_list.each do |f|
      dest_file = "#{SRC_PARSER_DIR}/" + File.basename(f)
      if( File.exists?(dest_file) )
        rm_f(dest_file)
      end
      cp_r(f, "#{SRC_PARSER_DIR}")
    end

  end


end # namespace :grammar
