package com.nekopiano.scala.sandbox.spec.specs2

import org.specs2._

class QuickStartSpec extends Specification { def is = s2"""

 This is my first specification
   it is working                 $ok
   really working!               $ok
                                 """
}
