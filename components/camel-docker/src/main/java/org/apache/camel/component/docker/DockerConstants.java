begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.docker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|docker
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Docker Component constants  */
end_comment

begin_class
DECL|class|DockerConstants
specifier|public
specifier|final
class|class
name|DockerConstants
block|{
DECL|field|DOCKER_PREFIX
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PREFIX
init|=
literal|"CamelDocker"
decl_stmt|;
DECL|field|DOCKER_DEFAULT_PARAMETERS
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|DOCKER_DEFAULT_PARAMETERS
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Connectivity *      */
DECL|field|DOCKER_CLIENT_PROFILE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CLIENT_PROFILE
init|=
literal|"CamelDockerClientProfile"
decl_stmt|;
comment|/**      * Connectivity *      */
DECL|field|DOCKER_API_REQUEST_TIMEOUT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_API_REQUEST_TIMEOUT
init|=
literal|"CamelDockerRequestTimeout"
decl_stmt|;
DECL|field|DOCKER_CERT_PATH
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CERT_PATH
init|=
literal|"CamelDockerCertPath"
decl_stmt|;
DECL|field|DOCKER_HOST
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_HOST
init|=
literal|"CamelDockerHost"
decl_stmt|;
DECL|field|DOCKER_PORT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PORT
init|=
literal|"CamelDockerPort"
decl_stmt|;
DECL|field|DOCKER_MAX_PER_ROUTE_CONNECTIONS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_MAX_PER_ROUTE_CONNECTIONS
init|=
literal|"CamelDockerMaxPerRouteConnections"
decl_stmt|;
DECL|field|DOCKER_MAX_TOTAL_CONNECTIONS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_MAX_TOTAL_CONNECTIONS
init|=
literal|"CamelDockerMaxTotalConnections"
decl_stmt|;
DECL|field|DOCKER_SECURE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_SECURE
init|=
literal|"CamelDockerSecure"
decl_stmt|;
DECL|field|DOCKER_FOLLOW_REDIRECT_FILTER
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_FOLLOW_REDIRECT_FILTER
init|=
literal|"CamelDockerFollowRedirectFilter"
decl_stmt|;
DECL|field|DOCKER_LOGGING_FILTER
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_LOGGING_FILTER
init|=
literal|"CamelDockerLoggingFilter"
decl_stmt|;
comment|/**      * List Images *      */
DECL|field|DOCKER_FILTER
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_FILTER
init|=
literal|"CamelDockerFilter"
decl_stmt|;
DECL|field|DOCKER_SHOW_ALL
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_SHOW_ALL
init|=
literal|"CamelDockerShowAll"
decl_stmt|;
comment|/**      * Common *      */
DECL|field|DOCKER_CONTAINER_ID
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CONTAINER_ID
init|=
literal|"CamelDockerContainerId"
decl_stmt|;
DECL|field|DOCKER_IMAGE_ID
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_IMAGE_ID
init|=
literal|"CamelDockerImageId"
decl_stmt|;
comment|/**      * Auth *      */
DECL|field|DOCKER_EMAIL
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_EMAIL
init|=
literal|"CamelDockerEmail"
decl_stmt|;
DECL|field|DOCKER_PASSWORD
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PASSWORD
init|=
literal|"CamelDockerPassword"
decl_stmt|;
DECL|field|DOCKER_SERVER_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_SERVER_ADDRESS
init|=
literal|"CamelDockerServerAddress"
decl_stmt|;
DECL|field|DOCKER_USERNAME
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_USERNAME
init|=
literal|"CamelDockerUsername"
decl_stmt|;
comment|/**      * Pull *      */
DECL|field|DOCKER_REGISTRY
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_REGISTRY
init|=
literal|"CamelDockerRegistry"
decl_stmt|;
DECL|field|DOCKER_REPOSITORY
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_REPOSITORY
init|=
literal|"CamelDockerRepository"
decl_stmt|;
DECL|field|DOCKER_TAG
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TAG
init|=
literal|"CamelDockerTag"
decl_stmt|;
comment|/**      * Push *      */
DECL|field|DOCKER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_NAME
init|=
literal|"CamelDockerName"
decl_stmt|;
comment|/**      * Search *      */
DECL|field|DOCKER_TERM
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TERM
init|=
literal|"CamelDockerTerm"
decl_stmt|;
comment|/**      * Remove *      */
DECL|field|DOCKER_FORCE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_FORCE
init|=
literal|"CamelDockerForce"
decl_stmt|;
DECL|field|DOCKER_NO_PRUNE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_NO_PRUNE
init|=
literal|"CamelDockerNoPrune"
decl_stmt|;
comment|/**      * Events *      */
DECL|field|DOCKER_INITIAL_RANGE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_INITIAL_RANGE
init|=
literal|"CamelDockerInitialRange"
decl_stmt|;
comment|/**      * List Container *      */
DECL|field|DOCKER_BEFORE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_BEFORE
init|=
literal|"CamelDockerBefore"
decl_stmt|;
DECL|field|DOCKER_LIMIT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_LIMIT
init|=
literal|"CamelDockerLimit"
decl_stmt|;
DECL|field|DOCKER_SHOW_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_SHOW_SIZE
init|=
literal|"CamelDockerShowSize"
decl_stmt|;
DECL|field|DOCKER_SINCE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_SINCE
init|=
literal|"CamelDockerSince"
decl_stmt|;
comment|/**      * Remove Container *      */
DECL|field|DOCKER_REMOVE_VOLUMES
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_REMOVE_VOLUMES
init|=
literal|"CamelDockerRemoveVolumes"
decl_stmt|;
comment|/**      * Attach Container *      */
DECL|field|DOCKER_FOLLOW_STREAM
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_FOLLOW_STREAM
init|=
literal|"CamelDockerFollowStream"
decl_stmt|;
DECL|field|DOCKER_LOGS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_LOGS
init|=
literal|"CamelDockerLogs"
decl_stmt|;
DECL|field|DOCKER_STD_ERR
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_STD_ERR
init|=
literal|"CamelDockerStdErr"
decl_stmt|;
DECL|field|DOCKER_STD_OUT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_STD_OUT
init|=
literal|"CamelDockerStdOut"
decl_stmt|;
DECL|field|DOCKER_TIMESTAMPS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TIMESTAMPS
init|=
literal|"CamelDockerTimestamps"
decl_stmt|;
comment|/**      * Logs *      */
DECL|field|DOCKER_TAIL
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TAIL
init|=
literal|"CamelDockerTail"
decl_stmt|;
DECL|field|DOCKER_TAIL_ALL
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TAIL_ALL
init|=
literal|"CamelDockerTailAll"
decl_stmt|;
comment|/**      * Copy *      */
DECL|field|DOCKER_HOST_PATH
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_HOST_PATH
init|=
literal|"CamelDockerHostPath"
decl_stmt|;
DECL|field|DOCKER_RESOURCE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_RESOURCE
init|=
literal|"CamelDockerResource"
decl_stmt|;
comment|/**      * Diff Container *      */
DECL|field|DOCKER_CONTAINER_ID_DIFF
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CONTAINER_ID_DIFF
init|=
literal|"CamelDockerContainerIdDiff"
decl_stmt|;
comment|/**      * Stop Container *      */
DECL|field|DOCKER_TIMEOUT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TIMEOUT
init|=
literal|"CamelDockerTimeout"
decl_stmt|;
comment|/**      * Kill Container *      */
DECL|field|DOCKER_SIGNAL
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_SIGNAL
init|=
literal|"CamelDockerSignal"
decl_stmt|;
comment|/**      * Top Container *      */
DECL|field|DOCKER_PS_ARGS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PS_ARGS
init|=
literal|"CamelDockerPsArgs"
decl_stmt|;
comment|/**      * Build Image *      */
DECL|field|DOCKER_NO_CACHE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_NO_CACHE
init|=
literal|"CamelDockerNoCache"
decl_stmt|;
DECL|field|DOCKER_QUIET
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_QUIET
init|=
literal|"CamelDockerQuiet"
decl_stmt|;
DECL|field|DOCKER_REMOVE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_REMOVE
init|=
literal|"CamelDockerRemove"
decl_stmt|;
DECL|field|DOCKER_TAR_INPUT_STREAM
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TAR_INPUT_STREAM
init|=
literal|"CamelDockerTarInputStream"
decl_stmt|;
comment|/**      * Commit Container *      */
DECL|field|DOCKER_ATTACH_STD_ERR
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_ATTACH_STD_ERR
init|=
literal|"CamelDockerAttachStdErr"
decl_stmt|;
DECL|field|DOCKER_ATTACH_STD_IN
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_ATTACH_STD_IN
init|=
literal|"CamelDockerAttachStdIn"
decl_stmt|;
DECL|field|DOCKER_ATTACH_STD_OUT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_ATTACH_STD_OUT
init|=
literal|"CamelDockerAttachStdOut"
decl_stmt|;
DECL|field|DOCKER_AUTHOR
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_AUTHOR
init|=
literal|"CamelDockerAuthor"
decl_stmt|;
DECL|field|DOCKER_CMD
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CMD
init|=
literal|"CamelDockerCmd"
decl_stmt|;
DECL|field|DOCKER_COMMENT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_COMMENT
init|=
literal|"CamelDockerComment"
decl_stmt|;
DECL|field|DOCKER_DISABLE_NETWORK
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_DISABLE_NETWORK
init|=
literal|"CamelDockerDisableNetwork"
decl_stmt|;
DECL|field|DOCKER_ENV
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_ENV
init|=
literal|"CamelDockerEnv"
decl_stmt|;
DECL|field|DOCKER_EXPOSED_PORTS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_EXPOSED_PORTS
init|=
literal|"CamelDockerExposedPorts"
decl_stmt|;
DECL|field|DOCKER_HOSTNAME
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_HOSTNAME
init|=
literal|"CamelDockerHostname"
decl_stmt|;
DECL|field|DOCKER_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_MESSAGE
init|=
literal|"CamelDockerMessage"
decl_stmt|;
DECL|field|DOCKER_MEMORY
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_MEMORY
init|=
literal|"CamelDockerMemory"
decl_stmt|;
DECL|field|DOCKER_MEMORY_SWAP
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_MEMORY_SWAP
init|=
literal|"CamelDockerMemorySwap"
decl_stmt|;
DECL|field|DOCKER_OPEN_STD_IN
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_OPEN_STD_IN
init|=
literal|"CamelDockerOpenStdIn"
decl_stmt|;
DECL|field|DOCKER_PAUSE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PAUSE
init|=
literal|"CamelDockerPause"
decl_stmt|;
DECL|field|DOCKER_PORT_SPECS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PORT_SPECS
init|=
literal|"CamelDockerPortSpecs"
decl_stmt|;
DECL|field|DOCKER_STD_IN_ONCE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_STD_IN_ONCE
init|=
literal|"CamelDockerStdInOnce"
decl_stmt|;
DECL|field|DOCKER_TTY
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_TTY
init|=
literal|"CamelDockerTty"
decl_stmt|;
DECL|field|DOCKER_USER
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_USER
init|=
literal|"CamelDockerUser"
decl_stmt|;
DECL|field|DOCKER_VOLUMES
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_VOLUMES
init|=
literal|"CamelDockerVolumes"
decl_stmt|;
DECL|field|DOCKER_WORKING_DIR
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_WORKING_DIR
init|=
literal|"CamelDockerWorkingDir"
decl_stmt|;
comment|/**      * Create Container *      */
DECL|field|DOCKER_CPU_SHARES
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CPU_SHARES
init|=
literal|"CamelDockerCpuShares"
decl_stmt|;
DECL|field|DOCKER_DNS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_DNS
init|=
literal|"CamelDockerDns"
decl_stmt|;
DECL|field|DOCKER_ENTRYPOINT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_ENTRYPOINT
init|=
literal|"CamelDockerEntryPoint"
decl_stmt|;
DECL|field|DOCKER_HOST_CONFIG
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_HOST_CONFIG
init|=
literal|"CamelDockerHostConfig"
decl_stmt|;
DECL|field|DOCKER_IMAGE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_IMAGE
init|=
literal|"CamelDockerImage"
decl_stmt|;
DECL|field|DOCKER_MEMORY_LIMIT
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_MEMORY_LIMIT
init|=
literal|"CamelDockerMemoryLimit"
decl_stmt|;
DECL|field|DOCKER_STD_IN_OPEN
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_STD_IN_OPEN
init|=
literal|"CamelDockerStdInOpen"
decl_stmt|;
DECL|field|DOCKER_VOLUMES_FROM
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_VOLUMES_FROM
init|=
literal|"CamelDockerVolumesFrom"
decl_stmt|;
DECL|field|DOCKER_DOMAIN_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_DOMAIN_NAME
init|=
literal|"CamelDockerDomainName"
decl_stmt|;
comment|/**      * Start Container *      */
DECL|field|DOCKER_BINDS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_BINDS
init|=
literal|"CamelDockerBinds"
decl_stmt|;
DECL|field|DOCKER_CAP_ADD
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CAP_ADD
init|=
literal|"CamelDockerCapAdd"
decl_stmt|;
DECL|field|DOCKER_CAP_DROP
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_CAP_DROP
init|=
literal|"CamelDockerCapDrop"
decl_stmt|;
DECL|field|DOCKER_DEVICES
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_DEVICES
init|=
literal|"CamelDockeDevices"
decl_stmt|;
DECL|field|DOCKER_DNS_SEARCH
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_DNS_SEARCH
init|=
literal|"CamelDockerDnsSearch"
decl_stmt|;
DECL|field|DOCKER_LINKS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_LINKS
init|=
literal|"CamelDockerLinks"
decl_stmt|;
DECL|field|DOCKER_LXC_CONF
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_LXC_CONF
init|=
literal|"CamelDockerLxcConf"
decl_stmt|;
DECL|field|DOCKER_NETWORK_MODE
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_NETWORK_MODE
init|=
literal|"CamelNetworkMode"
decl_stmt|;
DECL|field|DOCKER_PORT_BINDINGS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PORT_BINDINGS
init|=
literal|"CamelDockerPortBinding"
decl_stmt|;
DECL|field|DOCKER_PORTS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PORTS
init|=
literal|"CamelDockerPorts"
decl_stmt|;
DECL|field|DOCKER_PRIVILEGED
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PRIVILEGED
init|=
literal|"CamelDockerDnsPrivileged"
decl_stmt|;
DECL|field|DOCKER_PUBLISH_ALL_PORTS
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_PUBLISH_ALL_PORTS
init|=
literal|"CamelDockerPublishAllPorts"
decl_stmt|;
DECL|field|DOCKER_RESTART_POLICY
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_RESTART_POLICY
init|=
literal|"CamelDockerRestartPolicy"
decl_stmt|;
comment|/**      * Exec *      */
DECL|field|DOCKER_DETACH
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_DETACH
init|=
literal|"CamelDockerDetach"
decl_stmt|;
DECL|field|DOCKER_EXEC_ID
specifier|public
specifier|static
specifier|final
name|String
name|DOCKER_EXEC_ID
init|=
literal|"CamelDockerExecId"
decl_stmt|;
static|static
block|{
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_CERT_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_CLIENT_PROFILE
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_EMAIL
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_HOST
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_PASSWORD
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_PORT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_SECURE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_SERVER_ADDRESS
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|DOCKER_DEFAULT_PARAMETERS
operator|.
name|put
argument_list|(
name|DOCKER_USERNAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|DockerConstants ()
specifier|private
name|DockerConstants
parameter_list|()
block|{
comment|// Helper class
block|}
block|}
end_class

end_unit

