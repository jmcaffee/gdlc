@echo off
if "%COMPUTERNAME%" == "ERIS" goto :ErisSetup
if "%COMPUTERNAME%" == "ARCHIMETES" goto :ArchimetesSetup
if "%COMPUTERNAME%" == "BS136083" goto :WorkSetup
:: TODO: Verify the computer name is correct.
goto :CompNameError

::##########################################################################
:: Setup dirs for WorkComputer.
:WorkSetup
set TOOLSDEST=C:\tools
set BATCHDEST=C:\batch

goto CheckPath

::##########################################################################
:: Setup dirs for Archimetes.
:ArchimetesSetup
set TOOLSDEST=C:\tools
set BATCHDEST=C:\batch

goto CheckPath

::##########################################################################
:: Setup dirs for Eris
:ErisSetup
set TOOLSDEST=M:\tools
set BATCHDEST=C:\batch

goto CheckPath


::##########################################################################
:CheckPath
if "%GDLCPATHSET%" == "true" goto :PathDone
set GDLCPATHSET=true

::set PATH=%TOOLSDEST%\gdlc\lib;%PATH%

:PathDone


::java -jar %TOOLSDEST%\gdlc\lib\gdlc.jar %1 %2 %3 %4 %5 %6 %7 %8 %9
java -jar %TOOLSDEST%\gdlc\bin\gdlc.jar %*

goto Cleanup




::##########################################################################
:CompNameError
echo.
echo =====================================================================
echo.                                   GDLC
echo =====================================================================
echo.
echo Unknown computer name!
echo This computer is named: %COMPUTERNAME%
echo.
echo Script aborted.

goto :EOF


::##########################################################################
:Cleanup
set TOOLSDEST=
set BATCHDEST=
