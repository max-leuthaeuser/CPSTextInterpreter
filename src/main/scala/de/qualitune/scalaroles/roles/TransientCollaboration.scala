/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package de.qualitune.scalaroles.roles

import de.qualitune.scalaroles.roles.internal._

trait TransientCollaboration extends Collaboration {

  trait Role[Player <: AnyRef] extends TransientRole[Player]

  trait RoleMapper[Player <: AnyRef, RoleType <: AbstractRole[Player]] extends AbstractRoleMapper[Player, RoleType]

}

