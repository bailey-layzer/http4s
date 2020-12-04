/*
 * Copyright 2013-2020 http4s.org
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.http4s
package client

import cats.effect._
import cats.syntax.all._

private final class BasicManager[F[_], A <: Connection[F]](builder: ConnectionBuilder[F, A])(
    implicit F: Sync[F])
    extends ConnectionManager[F, A] {
  def borrow(requestKey: RequestKey): F[NextConnection] =
    builder(requestKey).map(NextConnection(_, fresh = true))

  override def shutdown: F[Unit] =
    F.unit

  override def invalidate(connection: A): F[Unit] =
    F.delay(connection.shutdown())

  override def release(connection: A): F[Unit] =
    invalidate(connection)
}
