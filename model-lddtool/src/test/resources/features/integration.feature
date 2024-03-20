Feature: Running integration tests for lddtool
  As an lddtool user
  I want to run lddtool with specific command arguments
  So that I can verify if it provides the expected response

  Scenario Outline: Running lddtool with different parameters
    Given a test <testName> with <commandArgs> as arguments
    When I execute lddtool
    Then the produced output from lddtool command should be similar to <expectedResponse> or no error reported.

    Examples:
      | testName          | commandArgs        | expectedResponse                   |
      | "help"            | "--help"           | "usage: LDDTool"                   |
      | "version"         | "--version"        | "LDDTool Version: 14.4.0-SNAPSHOT" |
