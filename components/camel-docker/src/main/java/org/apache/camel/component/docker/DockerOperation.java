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
comment|/**  * Operations the Docker Component supports  */
end_comment

begin_enum
DECL|enum|DockerOperation
specifier|public
enum|enum
name|DockerOperation
block|{
DECL|enumConstant|EVENTS
name|EVENTS
argument_list|(
literal|"events"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_INITIAL_RANGE
argument_list|,
name|Long
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|AUTH
name|AUTH
argument_list|(
literal|"auth"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_USERNAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_PASSWORD
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_EMAIL
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_SERVER_ADDRESS
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|INFO
name|INFO
argument_list|(
literal|"info"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
block|,
DECL|enumConstant|PING
name|PING
argument_list|(
literal|"ping"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
block|,
DECL|enumConstant|VERSION
name|VERSION
argument_list|(
literal|"version"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
block|,
DECL|enumConstant|LIST_IMAGES
name|LIST_IMAGES
argument_list|(
literal|"imagelist"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_FILTER
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_SHOW_ALL
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|PULL_IMAGE
name|PULL_IMAGE
argument_list|(
literal|"imagepull"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_REGISTRY
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TAG
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_REPOSITORY
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|PUSH_IMAGE
name|PUSH_IMAGE
argument_list|(
literal|"imagepush"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_USERNAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_PASSWORD
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_EMAIL
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_SERVER_ADDRESS
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|CREATE_IMAGE
name|CREATE_IMAGE
argument_list|(
literal|"imagecreate"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_REPOSITORY
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|SEARCH_IMAGES
name|SEARCH_IMAGES
argument_list|(
literal|"imagesearch"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TERM
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|REMOVE_IMAGE
name|REMOVE_IMAGE
argument_list|(
literal|"imageremove"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_IMAGE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_FORCE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_NO_PRUNE
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|INSPECT_IMAGE
name|INSPECT_IMAGE
argument_list|(
literal|"imageinspect"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_IMAGE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_NO_PRUNE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_FORCE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|LIST_CONTAINERS
name|LIST_CONTAINERS
argument_list|(
literal|"containerlist"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_LIMIT
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_SHOW_ALL
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_SHOW_SIZE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_BEFORE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_SINCE
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|WAIT_CONTAINER
name|WAIT_CONTAINER
argument_list|(
literal|"containerwait"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|INSPECT_CONTAINER
name|INSPECT_CONTAINER
argument_list|(
literal|"inspectcontainer"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|REMOVE_CONTAINER
name|REMOVE_CONTAINER
argument_list|(
literal|"removecontainer"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_FORCE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_REMOVE_VOLUMES
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|ATTACH_CONTAINER
name|ATTACH_CONTAINER
argument_list|(
literal|"containerattach"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_FOLLOW_STREAM
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TIMESTAMPS
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_STD_OUT
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_STD_ERR
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_LOGS
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|LOG_CONTAINER
name|LOG_CONTAINER
argument_list|(
literal|"containerlog"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_FOLLOW_STREAM
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TIMESTAMPS
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_STD_OUT
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_STD_ERR
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TAIL
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TAIL_ALL
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|CONTAINER_COPY_FILE
name|CONTAINER_COPY_FILE
argument_list|(
literal|"containercopyfile"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_RESOURCE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_HOST_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|DIFF_CONTAINER
name|DIFF_CONTAINER
argument_list|(
literal|"containerdiff"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|STOP_CONTAINER
name|STOP_CONTAINER
argument_list|(
literal|"containerstop"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TIMEOUT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|KILL_CONTAINER
name|KILL_CONTAINER
argument_list|(
literal|"containerkill"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_SIGNAL
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|RESTART_CONTAINER
name|RESTART_CONTAINER
argument_list|(
literal|"containerrestart"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TIMEOUT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|TOP_CONTAINER
name|TOP_CONTAINER
argument_list|(
literal|"containertop"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TIMEOUT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|TAG_IMAGE
name|TAG_IMAGE
argument_list|(
literal|"imagetag"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_IMAGE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_REPOSITORY
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_FORCE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|PAUSE_CONTAINER
name|PAUSE_CONTAINER
argument_list|(
literal|"containerpause"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|UNPAUSE_CONTAINER
name|UNPAUSE_CONTAINER
argument_list|(
literal|"containerunpause"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|BUILD_IMAGE
name|BUILD_IMAGE
argument_list|(
literal|"imagebuild"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_NO_CACHE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_REMOVE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_QUIET
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|COMMIT_CONTAINER
name|COMMIT_CONTAINER
argument_list|(
literal|"containercommit"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_REPOSITORY
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TAG
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_MESSAGE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_AUTHOR
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_ERR
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_IN
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_OUT
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_PAUSE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ENV
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_HOSTNAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_MEMORY
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_MEMORY_SWAP
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_OPEN_STD_IN
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_PORT_SPECS
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_STD_IN_ONCE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TTY
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_WORKING_DIR
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|CREATE_CONTAINER
name|CREATE_CONTAINER
argument_list|(
literal|"containercreate"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_IMAGE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_WORKING_DIR
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_DISABLE_NETWORK
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_HOSTNAME
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_PORT_SPECS
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_USER
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_TTY
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_STD_IN_OPEN
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_STD_IN_ONCE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_MEMORY_LIMIT
argument_list|,
name|Long
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_MEMORY_SWAP
argument_list|,
name|Long
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CPU_SHARES
argument_list|,
name|Integer
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_IN
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_OUT
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ATTACH_STD_ERR
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_ENV
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CMD
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_DNS
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_IMAGE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_VOLUMES_FROM
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|,
DECL|enumConstant|START_CONTAINER
name|START_CONTAINER
argument_list|(
literal|"containerstart"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_PUBLISH_ALL_PORTS
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_PRIVILEGED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_DNS
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_DNS_SEARCH
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CONTAINER_ID
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_VOLUMES_FROM
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_NETWORK_MODE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CAP_ADD
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|DockerConstants
operator|.
name|DOCKER_CAP_DROP
argument_list|,
name|String
operator|.
name|class
argument_list|)
block|;
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|field|canConsume
specifier|private
name|boolean
name|canConsume
decl_stmt|;
DECL|field|canProduce
specifier|private
name|boolean
name|canProduce
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|parameters
decl_stmt|;
DECL|method|DockerOperation (String text, boolean canConsume, boolean canProduce, Object... params)
specifier|private
name|DockerOperation
parameter_list|(
name|String
name|text
parameter_list|,
name|boolean
name|canConsume
parameter_list|,
name|boolean
name|canProduce
parameter_list|,
name|Object
modifier|...
name|params
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
name|this
operator|.
name|canConsume
operator|=
name|canConsume
expr_stmt|;
name|this
operator|.
name|canProduce
operator|=
name|canProduce
expr_stmt|;
name|parameters
operator|=
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
expr_stmt|;
if|if
condition|(
name|params
operator|.
name|length
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|params
operator|.
name|length
operator|%
literal|2
operator|!=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid parameter list, "
operator|+
literal|"must be of the form 'String name1, Class class1, String name2, Class class2..."
argument_list|)
throw|;
block|}
name|int
name|nParameters
init|=
name|params
operator|.
name|length
operator|/
literal|2
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nParameters
condition|;
name|i
operator|++
control|)
block|{
name|parameters
operator|.
name|put
argument_list|(
operator|(
name|String
operator|)
name|params
index|[
name|i
operator|*
literal|2
index|]
argument_list|,
operator|(
name|Class
argument_list|<
name|?
argument_list|>
operator|)
name|params
index|[
name|i
operator|*
literal|2
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|canConsume ()
specifier|public
name|boolean
name|canConsume
parameter_list|()
block|{
return|return
name|canConsume
return|;
block|}
DECL|method|canProduce ()
specifier|public
name|boolean
name|canProduce
parameter_list|()
block|{
return|return
name|canProduce
return|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|getDockerOperation (String name)
specifier|public
specifier|static
name|DockerOperation
name|getDockerOperation
parameter_list|(
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|DockerOperation
name|dockerOperation
range|:
name|DockerOperation
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|dockerOperation
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
name|dockerOperation
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_enum

end_unit

