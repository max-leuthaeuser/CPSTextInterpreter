/*
 * Copyright (c) 2008, Michael Pradel
 * All rights reserved. See LICENSE for details.
 */

package scalaroles.roles

import scalaroles.roles.internal._

trait StickyCollaboration extends Collaboration {

  trait Role[Player <: AnyRef] extends StickyRole[Player]

}

