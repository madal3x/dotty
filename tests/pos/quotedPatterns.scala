object Test {

  val x = '{1 + 2}

  def f(x: Int) = x
  def g(x: Int, y: Int) = x * y

  def res given tasty.Reflection: quoted.Expr[Int] = x match {
    case '{1 + 2} => '{0}
    case '{f($y)} => y
    case '{g($y, $z)} => '{$y * $z}
    case '{ ((a: Int) => 3)($y) } => y
    case '{ 1 + ($y: Int)} => y
    case '{ val a = 1 + ($y: Int); 3 } => y
      // currently gives an unreachable case warning
      // but only when used in conjunction with the others.
      // I believe this is because implicit arguments are not taken
      // into account when checking whether we have already seen an `unapply` before.
    case '{ val $y: Int = $z; 1 } =>
      val a: quoted.matching.Binding[Int] = y
      z
    case '{ (($y: Int) => 1 + y + ($z: Int))(2) } =>
      val a: quoted.matching.Binding[Int] = y
      z
    case '{ def $ff: Int = $z; ff } =>
      val a: quoted.matching.Binding[Int] = ff
      z
    case '{ def $ff(i: Int): Int = $z; 2 } =>
      val a: quoted.matching.Binding[Int => Int] = ff
      z
    case '{ def $ff(i: Int)(j: Int): Int = $z; 2 } =>
      val a: quoted.matching.Binding[Int => Int => Int] = ff
      z
    // FIXME
//    case '{ def `$ff`[T](i: T): Int = $z; 2 } =>
//      val a: quoted.matching.Binding[[T] => T => Int] = ff // TODO make Binding any-kinded
//      z
    case _ => '{1}
  }
}