#-------------------------------------------------------------------------------------------------------------#
# ZipFile class
#                                                                              
# Written by Jeff McAffee   9/23/07                      
# Purpose: Calls 7Zip compression utility tools
#
#------------------------------------------------------------------------------------------------------------#


#
#7-Zip (A) 4.32  Copyright (c) 1999-2005 Igor Pavlov  2005-12-09
#
#Usage: 7za <command> [<switches>...] <archive_name> [<file_names>...]
#       [<@listfiles...>]
#
#<Commands>
#  a: Add files to archive
#  d: Delete files from archive
#  e: Extract files from archive (without using directory names)
#  l: List contents of archive
#  t: Test integrity of archive
#  u: Update files to archive
#  x: eXtract files with full paths
#<Switches>
#  -ai[r[-|0]]{@listfile|!wildcard}: Include archives
#  -ax[r[-|0]]{@listfile|!wildcard}: eXclude archives
#  -bd: Disable percentage indicator
#  -i[r[-|0]]{@listfile|!wildcard}: Include filenames
#  -m{Parameters}: set compression Method
#  -o{Directory}: set Output directory
#  -p{Password}: set Password
#  -r[-|0]: Recurse subdirectories
#  -sfx[{name}]: Create SFX archive
#  -si[{name}]: read data from stdin
#  -so: write data to stdout
#  -t{Type}: Set type of archive
#  -v{Size}[b|k|m|g]: Create volumes
#  -u[-][p#][q#][r#][x#][y#][z#][!newArchiveName]: Update options
#  -w[{path}]: assign Work directory. Empty path means a temporary directory
#  -x[r[-|0]]]{@listfile|!wildcard}: eXclude filenames
#  -y: assume Yes on all queries
#

class ZipFile
  
#-------------------------------------------------------------------------------------------------------------#
# initialize - Constructor
#
#------------------------------------------------------------------------------------------------------------#
  def initialize(archive)
    @zipper   = "7za.exe"
    @archive  = archive
  end

#-------------------------------------------------------------------------------------------------------------#
# addToArchive - add a directory (recursively) to an archive
# path  - path to parent dir (of source)
# src     - source dir
#
#------------------------------------------------------------------------------------------------------------#
  def addToArchive(path, src)
    puts 'adding to archive'
    cd(path) {|dir|                                            
      puts "cd to #{path}. In dir [#{dir}]"
      system(@zipper, "-tzip", "a", "-r", @archive, "#{src}/*" )
    }
  end
# R:\sandbox\net.bd.gdlc\dist>7za -tzip a -r gdlc.zip gdlc/*


end # class ZipFile...

