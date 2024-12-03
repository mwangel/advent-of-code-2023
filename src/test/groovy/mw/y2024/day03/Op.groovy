package mw.y2024.day03

interface Op
{
  def execute()
}

class Do implements Op
{
  def execute() { /* nop */ }
}

class Dont implements Op
{
  def execute() { /* nop */ }
}
