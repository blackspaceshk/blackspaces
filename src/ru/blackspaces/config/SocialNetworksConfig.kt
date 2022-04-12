package ru.blackspaces.config

data class SocialNetworkOAuth2Config(
    val applicationId: Long,
    val applicationSecret: String
)

data class SocialNetworksConfig(
    val facebook: SocialNetworkOAuth2Config,
    val vk: SocialNetworkOAuth2Config
)
