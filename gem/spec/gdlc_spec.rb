require "spec_helper"
require "gdlc/gdlc"

include Gdlc

describe GDLC do

  before(:each) do
    GDLC.clear_flags
    GDLC.clear_include_dirs
  end

  context "add_flag" do
    it "should store flag values" do
      GDLC.add_flag '-v'
      GDLC.get_flags.should eq "-v"
    end # it "should store flag values"

    it "does not store duplicate flags" do
      GDLC.add_flag '-v'
      GDLC.add_flag '-v'
      GDLC.get_flags.should eq "-v"
    end # it "does not store duplicate flags"

  end

  context "get_flags" do
    it "returns multiple flags separated by a space" do
      GDLC.add_flag '-v'
      GDLC.add_flag '-vp'
      GDLC.get_flags.should eq "-v -vp"
    end # it "returns multiple flags separated by a space"

  end # context "get_flags"

  context "clear_flags" do
    it "clears all flags" do
      GDLC.clear_flags
      GDLC.add_flag '-v'
      GDLC.get_flags.should eq "-v"

      GDLC.clear_flags
      GDLC.get_flags.should eq ""
    end # it "clears all flags"

  end # context "clear_flags"

  context "add_include_dirs" do
    it "should store include dirs" do
      GDLC.add_include_dirs '/test/include/dir'
      GDLC.incdirs[0].should eq '/test/include/dir'
    end # it "should store include dirs"

    it "can store an array of include dirs" do
      GDLC.add_include_dirs ['/test/include/dir', 'another/test/include/dir']
      GDLC.incdirs.size.should eq 2
    end

    it "does NOT store duplicates" do
      GDLC.add_include_dirs ['/test/include/dir', 'another/test/include/dir', '/test/include/dir']
      GDLC.incdirs.size.should eq 2
    end

  end # context "add_include_dirs"

  context "get_include_dirs" do
    it "returns include dirs as space separated values prefixed with '/I'" do
      GDLC.add_include_dirs '/test/include/dir'
      GDLC.add_include_dirs 'another/test/include/dir'
      GDLC.get_include_dirs.should eq '/I/test/include/dir /Ianother/test/include/dir'
    end

  end # context "get_include_dirs"

  context "clear_include_dirs" do
    it "clears all include dirs" do
      GDLC.add_include_dirs '/test/include/dir'
      GDLC.add_include_dirs 'another/test/include/dir'
      GDLC.clear_include_dirs
      GDLC.incdirs.should be_empty
    end

  end # context "clear_include_dirs"

end
