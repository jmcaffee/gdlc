#-------------------------------------------------------------------------------------------------------------#
# GDLCompiler class
#                                                                              
# Written by Jeff McAffee   9/14/07                      
# Purpose: Calls GDLC tools 
#             BrokerConnectAdmin UI.                   
#
#------------------------------------------------------------------------------------------------------------#

include Watir

class CvAdmin
  
#-------------------------------------------------------------------------------------------------------------#
# initialize - Constructor
# url         - URL root to environment
# ie          - Watir::IE instance used to access Internet Explorer. Defaults to a new instance.
#
#------------------------------------------------------------------------------------------------------------#
  def initialize(url, ie=IE.new)
    @ie   = ie
    @url  = url
    @maxAtATime = 2
    
    # Setup various admin URLs
    @urlLogin   = "#@url" + '/user/login.aspx?ReturnUrl=%2fBrokerConnectAdmin%2fDefault.aspx'
    @urlLogout    = "#@url" + '/user/logout.aspx'
    @urlGuidelines  = "#@url" + '/admin/decision/guidelines.aspx'
    @urlAddGuideline  = "#@url" + '/admin/decision/guideline.aspx'
    @urlVersionAll  = "#@url" + '/admin/decision/versionAllGuideline.aspx'
  end

#-------------------------------------------------------------------------------------------------------------#
# login - Log in to the application
# user    - User name to login as
# pass    - Password to login with
#
#------------------------------------------------------------------------------------------------------------#
  def login(user, pass)
    @ie.goto(@urlLogin)
    @ie.text_field(:name, "txtUsername").set(user)      # txtUsername is the name of the username field
    @ie.text_field(:name, "txtPassword").set(pass)    # txtPassword is the name of the password field
    @ie.form(:name, "frmlogin").submit              # CV is using an image as the login button by default. 
                                    # Submit the form instead of clicking a button.
# if @ie.contains_text("Bear Stearns")
#   @ie.button(:name, "btnSubmit").click        # "btnSubmit" is the name of the Sign On button
# else
#   @ie.form(:name, "frmlogin").submit          # CV is using an image as the login button by default. 
# end                               # Submit the form instead of clicking a button.
  end

 #-------------------------------------------------------------------------------------------------------------#
# logout - Log out of the admin application
#
#------------------------------------------------------------------------------------------------------------#
 def logout()
    @ie.goto(@urlLogout)

  end

#-------------------------------------------------------------------------------------------------------------#
# versionAll - Version guidelines using the CV Admin app's 'Version All' functionality
# names     - A list of guideline names to version
#
#------------------------------------------------------------------------------------------------------------#
  def versionAll(names)
    @ie.goto("#@urlVersionAll")
    i = 0

  names.each do | gdlName |
       @ie.select_list( :name , "tsGuidelines:lstAvailable").select(gdlName)
       @ie.button(:name, "tsGuidelines:btnAdd").click
       puts "... versioning #{gdlName}"
    i += 1
    unless (i < @maxAtATime) then
      @ie.button(:name, "cmdSave").click
      if (!@ie.contains_text("Setup Guidelines"))   # Verify that the webapp returned to the correct page.
        raise "Version All did not complete correctly"
      end
        @ie.goto("#@urlVersionAll")
      i = 0
    end
    
  end

  if (i != 0) then 
    @ie.button(:name, "cmdSave").click 
  
    if (!@ie.contains_text("Setup Guidelines"))   # Verify that the webapp returned to the correct page.
      raise "Version All did not complete correctly"
    end # if (!@ie.contains_text...
  end # if (i != 0...
  end # def versionAll...

#-------------------------------------------------------------------------------------------------------------#
# addGuidelines - Create a new guideline record
# names     - A list of guideline names to add
#
#------------------------------------------------------------------------------------------------------------#
  def addGuidelines(names)
    @ie.goto("#@urlAddGuideline")

    names.each do | gdlName |
      puts "... creating guideline: #{gdlName}"
      @ie.text_field(  :name, "txtName").set(gdlName)
      @ie.button(:name, "cmdSave").click

      if (!@ie.contains_text("Setup Guidelines"))   # Verify that the webapp returned to the correct page.
        raise "AddGuideline did not complete correctly"
      end # if (!@ie.contains_text...

      @ie.goto("#@urlAddGuideline")
    end
    
  end # def addGuideline...

end # class CvAdmin...




# GuidelineSetup: /admin/decision/guidelines.aspx
