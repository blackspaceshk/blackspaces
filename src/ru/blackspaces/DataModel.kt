package ru.blackspaces.ru.blackspaces

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Tasks : UUIDTable() {
    val facebookId = varchar("facebook_id", 512)
    val facebookAccessToken = varchar("facebook_access_token", 512)
}

object Posts : UUIDTable() {
    val facebookId = varchar("facebook_id", 512)
    val facebookLink = varchar("facebook_link", 512)
    val task = reference("task", Tasks)
    val text = text("text").nullable()
    val link = text("link").nullable()
    val privacy = varchar("privacy", 32).nullable()
    val createdDate = varchar("created_date", 32)
}

class Task(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Task>(Tasks)

    var facebookId by Tasks.facebookId
    var facebookAccessToken by Tasks.facebookAccessToken
    val posts by Post referrersOn Posts.task
}

class Post(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Post>(Posts)

    var facebookId by Posts.facebookId
    var facebookLink by Posts.facebookLink
    var task by Task referencedOn Posts.task
    var text by Posts.text
    var privacy by Posts.privacy
    var link by Posts.link
    var createdDate by Posts.createdDate
}

data class ResponsePost(
    val facebookId: String,
    val facebookLink: String,
    val text: String,
    val link: String,
    val privacy: String,
    val createdDate: String
)