namespace :gem do

  require "bundler/gem_helper"
  gemdir = File.realpath(File.join(File.dirname(__FILE__), "../gem"))
  Bundler::GemHelper.install_tasks :dir => gemdir  #, :name => "gdlc"

end

