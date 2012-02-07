/**
 * CPSTextInterpreter - parses and interprets the CPSText DSL.
 * Copyright (C) 2011 Max Leuthaeuser
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.qualitune.interpreter

import de.qualitune.ast.{CPSProgram, Context}
import de.qualitune.ast.variable.{InitVariableDecl, EmptyVariableDecl}
import collection.mutable.Map
import de.qualitune.ast.role._
import exceptions._

/**
 *  Object containing some methods for checking a CPSProgram statically.
 *
 * @author Max Leuthaeuser
 * @date 22.11.2011
 */
object CPSChecks {
  /**
   * Check if names are used more than once and hiding is possible to
   * generate warnings.
   *
   * @param cst: CPSProgram to check
   * @throws DuplicateNameException if the same name is used multiple times at one specific level.
   */
  def checkNames(cst: CPSProgram) {
    var contextNames = List[String]()
    var roleNames = List[String]()
    val varNames = Map[Int, String]()

    def checkContextNames(c: List[Context]) {
      c.foreach(e => {
        if (contextNames.contains(e.name))
          throw new DuplicateNameException("Context '" + e.name + "' is already defined!")
        else
          contextNames = e.name :: contextNames
      })
      c.foreach(e => {
        checkContextNames(e.inner)
      })
    }

    def checkRoleNames(c: List[Context]) {
      c.foreach(e => {
        e.roles.foreach(r => {
          if (roleNames.contains(r.name))
            throw new DuplicateNameException("Role '" + e.name + "' is already defined!")
          else
            roleNames = r.name :: roleNames
        })
      })
      c.foreach(e => {
        checkRoleNames(e.inner)
      })
    }

    def checkVariableNames(c: List[Context], level: Int = 0) {
      def matches(o: ScalaObject) {
        o match {
          case c: Context => {
            var vl = List[String]()
            c.variables.foreach(_ match {
              case va: EmptyVariableDecl => {
                val name = va.asInstanceOf[EmptyVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
              case va: InitVariableDecl => {
                val name = va.asInstanceOf[InitVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
            })
          }
          case r: Role => {
            var vl = List[String]()
            r.variables.foreach(_ match {
              case va: EmptyVariableDecl => {
                val name = va.asInstanceOf[EmptyVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
              case va: InitVariableDecl => {
                val name = va.asInstanceOf[InitVariableDecl].name
                if (level > 0) {
                  varNames.foreach(p => {
                    if (p._1 < level && p._2.equals(name))
                      println("\t\tHiding Warning: Variable '" + name + "' is defined more than once.")
                  })
                }
                if (vl.contains(name))
                  throw new DuplicateNameException("Variable '" + name + "' is already defined!")
                else
                  vl = name :: vl
                varNames(level) = name
              }
            })
          }
        }
      }

      // in contexts first:
      c.foreach(e => {
        matches(e)
      })
      c.foreach(e => {
        checkVariableNames(e.inner, level + 1)
      })

      // roles now:
      c.foreach(e => {
        e.roles.foreach(r => {
          matches(r)
        })
      })
    }

    /**
     * Algorithm:
     *  - run through all contexts and
     *  - run through all roles
     * and check if their names and embedded variables are unique.
     *
     * Same name for role or contexts generates error, same name for variables on top level
     * generate error, same name for variables in embedded contexts/roles will generate hiding warning.
     */
    checkContextNames(cst.contexts)
    checkRoleNames(cst.contexts)
    checkVariableNames(cst.contexts)
  }

  /**
   * Check if all given bindings from CPS object to roles are well formed,
   * hence the same assignment is not allowed to be defined more than once
   * and no binding should be left without definition and visa versa.
   *
   * @param cst: CPSProgram to check
   */
  def checkBindings(cst: CPSProgram) {
    def check(c: List[Context]) {
      c.foreach(co => {
        var vlist = List[String]()
        co.activations.foreach(ac => {
          // check if no variable is used in more than one activation block,
          // this would not be possible, because Scala does not allow dynamic typing which
          // would be needed to handle dynamic role bindings.
          ac.activateFor.foreach(af => {
            if (vlist.contains(af.variableName)) {
              throw new InvalidActivationRuleException("[Context '" + co.name + "'] Activation rule variable '" + af.variableName + "' must not be used for different activation blocks!")
            } else {
              vlist = af.variableName :: vlist
            }
          })
        })

        co.activations.foreach(ac => {
          // check if variables are no CPSystems
          val cpsList = cst.robots.map(_.name)
          ac.activateFor.foreach(va => {
            if (cpsList.contains((va.variableName)))
              throw new InvalidActivationRuleException("[Context '" + co.name + "'] Activation rule variable '" + va.variableName + "' must not be a CPSystem.")
          })

          var v = List[String]()
          var b = List[String]()

          ac.activateFor.foreach(va => {
            if (v.contains((va.variableName)))
              throw new InvalidActivationRuleException("[Context '" + co.name + "'] Activation rule variable '" + va.variableName + "' is already defined.")
            else
              v = va.variableName :: v
          })
          ac.bindings.foreach(vb => {
            if (b.contains((vb.variableName)))
              throw new InvalidActivationRuleException("[Context '" + co.name + "'] Activation rule variable '" + vb.variableName + "' is already defined.")
            else
              b = vb.variableName :: b
          })

          if (v.length != b.length)
            throw new InvalidActivationRuleException("[Context '" + co.name + "'] It seems that not every activation rule variable is bound to a role or visa versa.")

          v.foreach(va => {
            if (!b.contains(va))
              throw new InvalidActivationRuleException("[Context '" + co.name + "'] Activation rule variable '" + va + "' is not bound to any role.")
          })

          b.foreach(vb => {
            if (!v.contains(vb))
              throw new InvalidActivationRuleException("[Context '" + co.name + "'] Activation rule binding '" + vb + "' is not defined by any variable.")
          })

          check(co.inner)
        })
      })
    }
    check(cst.contexts)
  }

  private def getRoles(c: List[Context]): Map[String, Role] = {
    var l = Map[String, Role]()
    c.foreach(e => {
      e.roles.foreach(r => {
        l(r.name) = r
      })
      l = l ++ getRoles(e.inner)
    })
    l
  }

  private def getConstraints(c: List[Context]): List[RoleConstraint] = {
    var l = List[RoleConstraint]()
    c.foreach(e => {
      e.constraints.foreach(r => {
        l = r :: l
      })
      l = l ++ getConstraints(e.inner)
    })
    l
  }

  /**
   * Check if all role definitions are well formed, hence no cyclic playedBy definitions.
   *
   * @param cst: CPSProgram to check
   */
  def checkRoles(cst: CPSProgram) {
    // TODO need further tests
    // collect all roles
    val rl = getRoles(cst.contexts)
    var pl = List[String]()

    // calculate played by reference list recursively
    def playedBy(r: Role) {
      if (!pl.contains(r.playedBy)) {
        val elem = rl.getOrElse(r.playedBy, null)
        if (elem != null) {
          pl = r.playedBy :: pl
          playedBy(elem)
        }
      }
    }

    rl.foreach(e => {
      if (e._1.equals(e._2.playedBy))
        throw new InvalidPlayedByException("Role '" + e._1 + "' cannot played by itself!")

      playedBy(e._2)
      if (pl.size > 1 && pl.contains(e._2.playedBy))
        throw new InvalidPlayedByException("Cyclid playedBy definition found! [" + pl.reverse.mkString("", "->", "->") + e._2.playedBy + "]")
      pl = List[String]()
    })
  }

  /**
   * Check if all constraints are well formed.
   *
   * @param cst: CPSProgram to check
   */
  def checkConstrains(cst: CPSProgram) {
    // collect all roles
    val rl = getRoles(cst.contexts)
    // every role in every constraint needs to be actually defined
    // so we first collect all roles from all constraints
    val cstrs = getConstraints(cst.contexts)
    cstrs.foreach(e => {
      if (!rl.contains(e.source)) {
        throw new InvalidConstraintException("Role '" + e.source + "' from constraint '" + e + "' is not defined!")
      }
      if (!rl.contains(e.target)) {
        throw new InvalidConstraintException("Role '" + e.target + "' from constraint '" + e + "' is not defined!")
      }
      // the same role name before and after the constraints is not valid
      if (e.source.equals(e.target)) {
        throw new InvalidConstraintException("Constraint '" + e + "' is invalid. Same role for source and target are not allowed!")
      }

      e match {
        // role A cant imply role B if it is prohibited somewhere else
        case x: ImplicationConstraint => {
          val pc1 = ProhibitionConstraint(x.source, x.target)
          val pc2 = ProhibitionConstraint(x.target, x.source)
          if (cstrs.contains(pc1))
            throw new InvalidConstraintException("Role '" + x.source + "' can not imply role '" + x.target + "' if it is prohibited in constraint '" + pc1 + "'!")
          if (cstrs.contains(pc2))
            throw new InvalidConstraintException("Role '" + x.source + "' can not imply role '" + x.target + "' if it is prohibited in constraint '" + pc2 + "'!")
        }
        // role A cant be equivalent to role B if it is prohibited somewhere else
        case x: EquivalenceConstraint => {
          val pc1 = ProhibitionConstraint(x.source, x.target)
          val pc2 = ProhibitionConstraint(x.target, x.source)
          if (cstrs.contains(pc1))
            throw new InvalidConstraintException("Role '" + x.source + "' can not be equivalent to role '" + x.target + "' if it is prohibited in constraint '" + pc1 + "'!")
          if (cstrs.contains(pc2))
            throw new InvalidConstraintException("Role '" + x.source + "' can not be equivalent to role '" + x.target + "' if it is prohibited in constraint '" + pc2 + "'!")
        }
        case _ => // can be ignored
      }
    })
  }

  /**
   * Check if CPSObjects are well formed, hence no same IP:PORT or same name for different devices.
   *
   * @param cst: CPSProgram to check
   */
  def checkCPSObjects(cst: CPSProgram) {
    var ipportList = List[String]()
    cst.robots.foreach(r => {
      val ipport = r.ip + ":" + r.port
      if (!ipportList.contains(ipport)) {
        ipportList = ipport :: ipportList
      } else {
        throw new DuplicateCPSDeviceException("IP:PORT of CPS device '" + r.name + "' is already used!")
      }
    })
  }

  /**
   * Check if all import statements are wellformed, hence no statement appears more than once.
   *
   * @param cst: CPSProgram to check
   */
  def checkImports(cst: CPSProgram) {
    var l = List[String]()
    cst.imports.foreach(i => {
      if (l.contains(i)) {
        throw new DuplicateImportException("The import statement '" + i + "' can not be declared multiple times!")
      } else {
        l = i :: l
      }
    })
  }
}