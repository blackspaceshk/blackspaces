package ru.blackspaces.clients

import com.restfb.DefaultFacebookClient
import com.restfb.Parameter
import com.restfb.Version.LATEST
import com.restfb.types.Post
import com.restfb.types.User

class FacebookClient(private val accessToken: String) : SnClient {
    private val client = DefaultFacebookClient(accessToken, LATEST)

    override fun token(): String {
        return accessToken
    }

    override fun posts(): Iterable<CommonSnPost> {
        return client.fetchConnection(
            "me/posts",
            Post::class.java,
            Parameter.with("fields", "message,actions,privacy,permalink_url,created_time")
        )
            .asIterable()
            .flatMap { it.toList() }
            .filter { it.message != null }
            .filterNotNull()
            .map { CommonSnPost(it.message, it.link, it.createdTime.toInstant(), it.privacy.allow == "ALL_FRIENDS") }
    }

    override fun userId(): String {
        return client.fetchObject("me", User::class.java).id
    }
}

