package srvs.Gql
import sangria.macros.derive._
import domains.Tag
import domains.Task
import domains.Title
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcBackend
import scala.concurrent.ExecutionContext

class Mutation(
    db: JdbcBackend#DatabaseDef
)(implicit
    ec: ExecutionContext
) {
  val taskRepo = Task.tasks
  val tagRepo = Tag.tags
  val titleRepo = Title.titles

  @GraphQLField
  def saveTask(task: Task.Task) = {
    db.run(taskRepo.insertOrUpdate(task))
    task
  }

  @GraphQLField
  def saveTasks(titleId: String, tasks: Seq[Task.Task]) = {
    db.run(taskRepo.filter(_.titleId === titleId).delete)
      .flatMap(i => db.run(taskRepo ++= tasks))
    titleId
  }

  @GraphQLField
  def saveTitle(title: Title.Title) = {
    db.run(titleRepo.insertOrUpdate(title))
    title
  }

  @GraphQLField
  def saveTag(tag: Tag.Tag) = {
    db.run(tagRepo.insertOrUpdate(tag))
    tag
  }

  @GraphQLField
  def deleteTask(id: String) = {
    db.run(taskRepo.filter(_.id === id).delete)
    id
  }

  @GraphQLField
  def deleteTitle(id: String) = {
    db.run(titleRepo.filter(_.id === id).delete)
    id
  }

  @GraphQLField
  def deleteTag(id: String) = {
    db.run(tagRepo.filter(_.id === id).delete)
    id
  }
}
