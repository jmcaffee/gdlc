#############################################################################
#
# RAKEFILE.rb
#
# GDLC distribution makefile
#
#############################################################################

DEV       = "/sandbox/net.bd.gdlc"
BINDIR    = "dist/gdlc/bin"
BUILDDIR  = "build"
MISCDIR   = "misc"
JARDIR    = "jars"
GDLJAR    = "gdlc.jar"


#######################################

desc "- Create GDLC distribution"

task :dist => [:createDirs, :jar, :copyToBin, :copyToRoot, :docs] do
  puts "dist created"
end

#######################################

desc "- Create blank directories if they don't exist"

task :createDirs do
  # The folders I need to create
  shared_folders = ["dist","dist/gdlc","dist/gdlc/bin","dist/gdlc/docs"]
  
  for folder in shared_folders
    
    # Check to see if it exists
    if File.exists?(folder)
      puts "#{folder} exists"
    else
      puts "#{folder} doesn't exist: creating"
      Dir.mkdir "#{folder}"
    end
    
  end
end

task :createDirsBAD do
  # The folders I need to create
  directory "dist/gdlc/bin"
  directory "dist/gdlc/docs"
  
end



#######################################

task :generate do
  puts "source generated"
end

#######################################

task :compile do
  puts "source compiled"
end

#######################################

#task :copyToBin => ["#{BINDIR}/ostermillerutils_1_06_01.jar", "#{BINDIR}/gdlc.jar"] do
task :copyToBin => [:refreshJars] do
  puts "files copied to BIN"
end

#######################################

task :copyToRoot => ['dist/gdlc/gdlc.rb', 'dist/gdlc/gdlc.cmd'] do
  puts "files copied to root"
end

#######################################

desc "- Delete all build files"

task :clean do
  shared_folders = ["dist/gdlc/docs","dist/gdlc/bin","dist/gdlc","dist"]
  
  for folder in shared_folders
    
    # Check to see if it exists
    if File.exists?(folder)
      puts "#{folder} exists: deleting files"
      rm Dir.glob("#{folder}/*.*")
    else
      puts "#{folder} doesn't exist"
    end
    
  end
  
  JARFILE = "#{JARDIR}/#{GDLJAR}"
  if File.exists?(JARFILE)
    puts "#{JARFILE} exists: deleting file"
    rm "#{JARFILE}"
  else
    puts "#{JARFILE} doesn't exist"
  end
    
  
end

#######################################

desc "- Delete all build directories"

task :clobber do
  shared_folders = ["dist"]
  
  for folder in shared_folders
    
    # Check to see if it exists
    if File.exists?(folder)
      puts "#{folder} exists: deleting"
      remove_dir(folder)
      #Dir.rmdir "#{folder}"
    else
      puts "#{folder} doesn't exist"
    end
    
  end
end

#######################################

file 'dist/gdlc/gdlc.rb' => 'misc/gdlc.rb' do |t|
  cp("#{MISCDIR}/gdlc.rb", t.name )
end



#######################################

file 'dist/gdlc/gdlc.cmd' => 'misc/gdlc.cmd' do |t|
  cp(t.prerequisites[0], t.name )
end

#######################################

file 'dist/gdlc/docs/GdlGrammar.html' => ['dist/gdlc/docs/GdlGrammar.jj', :grammarHtml] do |t|
  
end

#######################################

file 'dist/gdlc/docs/GdlGrammar.jj'  => 'build/runtime/parser/GdlGrammar.jj' do |t|
  cp(t.prerequisites[0], t.name )
end

#######################################

task :refreshJars
directory BINDIR

#######################################

def make_jar_copy_task(jarname)
  stagejar = "#{BINDIR}/#{jarname}.jar"
  devjar   = "jars/#{jarname}.jar"

  task :refreshJars => [ stagejar ]

  file stagejar => [ devjar, BINDIR ] do
    cp devjar, stagejar
  end
end

#######################################

make_jar_copy_task("ostermillerutils_1_06_01")

#######################################

make_jar_copy_task("gdlc")

#######################################

desc "- Create GDLC jar file"

task :jar => [:generate, :compile] do
  cd('build') {|dir|                                            # Everything is relative to the build dir (where class files are coming from).
    `jar -cvfm "../#{JARDIR}/#{GDLJAR}" "../#{MISCDIR}/manifest.mf" "runtime"`
    # jar -cvfm jars/gdlc.zip misc/manifest.mf -C build .     # '-C' flag works from the command line but not here.
  }
end

#######################################

desc "- Deploy GDLC to tools dir"

task :deploy => [:dist] do
  distSrc = "dist/gdlc"
  dest    = "C:/tools"
  batDest = "C:/batch"
  
  scriptSrc  = "#{dest}/gdlc/gdlc.cmd"
  scriptDest = "#{batDest}/gdlc.cmd"
  
  
  cp_r distSrc, dest
  cp scriptSrc, scriptDest
end

#######################################

desc "- Create compiler documentation"

task :docs => [:createDirs, 'dist/gdlc/docs/GdlGrammar.html'] do
  puts "Documents created"
end

#######################################

task :grammarHtml do
  srcName     = "GdlGrammar"
  src         = "#{srcName}.jj"
  docsDir     = "dist/gdlc/docs"

  jjdoc       = "m:/javacc/bin/jjdoc.bat"
  
  cd(docsDir) {|dir|                                            
    system(jjdoc, src)
  }
  
end