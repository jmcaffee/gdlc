##############################################################################
# File::    markdown.rb
# Purpose:: Markdown helper tasks
# 
# Author::    Jeff McAffee 03/26/2013
# Copyright:: Copyright (c) 2013, kTech Systems LLC. All rights reserved.
# Website::   http://ktechsystems.com
##############################################################################

require "pathname"
require 'tartancloth'

# Call this as: rake md2html[path/to/file/to/convert.md]
#
desc "Convert a .MD file to HTML"
task :md2html, [:mdfile] do |t, args|
  Rake::Task['markdown:md2html'].invoke( args[:mdfile] )
end


namespace :markdown do

  desc "md2html usage instructions"
  task :help do
    puts <<HELP

----------------------------------------------------------------------

Usage: md2html

Generate HTML from a markdown document

  The generated HTML document will be located in the same location as
  the source markdown document.

  To generate the document, call it as follows:

    rake md2html[path/to/doc.md]

  Note that no quotes are needed.

  To set the title of the document, provide it as an ENV variable:

    TITLE="My Title" rake md2html[path/to/doc.md]

  If no title is given, the title will default to the filename.

----------------------------------------------------------------------

HELP
  end


  task :md2html, [:mdfile] do |t, args|
    args.with_defaults(:mdfile => nil)
    if args[:mdfile].nil?
      puts "ERROR: Full path to file to convert required."
      puts
      puts "usage:  rake md2html['path/to/md/file.md']"
      exit
    end

    mdsrc = args[:mdfile]
    mdout = mdsrc.pathmap( "%X.html" )
    title = ENV['TITLE']

    puts "Title:  #{title}"
    puts "Source: #{mdsrc}"
    puts "Output: #{mdout}"

    TartanCloth.new( mdsrc, title ).to_html_file( mdout )
  end

end # namespace :markdown
