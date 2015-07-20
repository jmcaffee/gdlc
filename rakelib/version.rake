##############################################################################
# File:       version.rb
# Purpose:    version tasks
#
# Author:     Jeff McAffee 07/20/2015
# Copyright:  Copyright (c) 2015, Jeff McAffee
#             All rights reserved. See LICENSE.txt for details.
# Website:    http://JeffMcAffee.com
##############################################################################


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

