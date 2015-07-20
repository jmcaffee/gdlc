##############################################################################
# File::    gdlc.rb
# Purpose:: GDLC Gem
# 
# Author::    Jeff McAffee 01/31/2013
# Copyright:: Copyright (c) 2013, kTech Systems LLC. All rights reserved.
# Website::   http://ktechsystems.com
##############################################################################

module Gdlc
  class GltException
  end

  class GDLC

  GDLC_PATH = 'gdlc'
  OUTDIR_PATH = "C:/temp"

    # Compiler flags
    @@flags    = []

    # Include directories
    @@incdirs  = []

    # Path to GDLC executable
    @@app_path = GDLC_PATH

    ### Class methods

    def GDLC.app_path
      @@app_path
    end

    def GDLC.app_path=(apppath)
      @@app_path = apppath
    end

    # clear all flags
    #
    def GDLC.clear_flags
      @@flags = []
    end # GDLC.clear_flags

    # Add a compiler flag
    #
    # Duplicate flags are discarded.
    #
    # flg  - Flag to set.
    #
    def GDLC.add_flag(flg)
      @@flags << flg unless @@flags.include? flg
    end # add_flag

    # Return a string containing all flags as input parameters
    #
    #
    # returns params - string containing all flags separated with space
    #
    def GDLC.get_flags
      return @@flags.join " "
    end # get_flags

    # Return compiler include dirs array
    #
    def GDLC.incdirs
      @@incdirs
    end

    # Add include dir(s)
    #
    # dirName  - Name of directory
    #
    def GDLC.add_include_dirs(dirs)
      if(dirs.class == Array)
        dirs.each do |d|
          @@incdirs << d unless @@incdirs.include? d
        end
      else
        @@incdirs << dirs unless @@incdirs.include? dirs
      end
    end

    # Return a string containing all include dirs as input parameters
    # Formats string as '/Isome/dir/name /Isome/other/dir'
    #
    # returns params - Parameter string
    #
    def GDLC.get_include_dirs
      @@incdirs
      params = @@incdirs.map { |d| "--I#{d}" }
      params.join ' '
    end

    # Clear out the include directory list
    #
    def GDLC.clear_include_dirs
      @@incdirs.clear

    end

    # Compile a guideline
    #
    # srcfile - Name of source file to compile
    # outname - Name of output file
    #
    def GDLC.compile(srcfile, outname)
      GDLC.execute_GDLC("#{GDLC.get_flags} #{GDLC.get_include_dirs} #{srcfile} #{outname}")
    end

    private

    # Execute GDLC application
    #
    # cmdLine - Command line to pass to the application
    #
    def GDLC.execute_GDLC(cmdLine)
      app_cmd = "#{@@app_path} #{cmdLine}"
      puts "Executing: #{app_cmd}"

      output = `#{app_cmd}`
      exitcode = $?.exitstatus

      # Currently unable to retrieve exit code from java jar execution.
      # May be because 'system' executes a sub-process which is executing
      # the JVM which returns the exit code.
      # Regardless of the JVM exit code, the sub-process returns 0.
      #
      # For now, we'll look for a specific message in the output and fail if
      # its not found.
      puts output
      fail 'compile failed' unless output.include?('XML written to file')
    end

  end # class GDLC
end # module
