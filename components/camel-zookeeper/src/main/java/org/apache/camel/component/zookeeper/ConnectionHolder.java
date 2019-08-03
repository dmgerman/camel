begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|RuntimeCamelException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|WatchedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|Watcher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|Watcher
operator|.
name|Event
operator|.
name|KeeperState
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|ZooKeeper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  *<code>ConnectionHolder</code> watches for Connection based events from  * {@link ZooKeeper} and can be used to block until a connection has been  * established.  */
end_comment

begin_class
DECL|class|ConnectionHolder
specifier|public
class|class
name|ConnectionHolder
implements|implements
name|Watcher
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ConnectionHolder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connectionLatch
specifier|private
name|CountDownLatch
name|connectionLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|ZooKeeperConfiguration
name|configuration
decl_stmt|;
DECL|field|zookeeper
specifier|private
name|ZooKeeper
name|zookeeper
decl_stmt|;
DECL|method|ConnectionHolder (ZooKeeperConfiguration configuration)
specifier|public
name|ConnectionHolder
parameter_list|(
name|ZooKeeperConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getZooKeeper ()
specifier|public
name|ZooKeeper
name|getZooKeeper
parameter_list|()
block|{
if|if
condition|(
name|zookeeper
operator|!=
literal|null
condition|)
block|{
return|return
name|zookeeper
return|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getConnectString
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot create ZooKeeper connection as connection string is null. Have servers been configured?"
argument_list|)
throw|;
block|}
try|try
block|{
name|zookeeper
operator|=
operator|new
name|ZooKeeper
argument_list|(
name|configuration
operator|.
name|getConnectString
argument_list|()
argument_list|,
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|awaitConnection
argument_list|()
expr_stmt|;
return|return
name|zookeeper
return|;
block|}
DECL|method|isConnected ()
specifier|public
name|boolean
name|isConnected
parameter_list|()
block|{
return|return
name|connectionLatch
operator|.
name|getCount
argument_list|()
operator|==
literal|0
return|;
block|}
DECL|method|awaitConnection ()
specifier|public
name|void
name|awaitConnection
parameter_list|()
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Awaiting Connection event from Zookeeper cluster {}"
argument_list|,
name|configuration
operator|.
name|getConnectString
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|connectionLatch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (WatchedEvent event)
specifier|public
name|void
name|process
parameter_list|(
name|WatchedEvent
name|event
parameter_list|)
block|{
if|if
condition|(
name|event
operator|.
name|getState
argument_list|()
operator|==
name|KeeperState
operator|.
name|SyncConnected
condition|)
block|{
name|connectionLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
name|connectionLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
DECL|method|closeConnection ()
specifier|public
name|void
name|closeConnection
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|zookeeper
operator|!=
literal|null
condition|)
block|{
name|zookeeper
operator|.
name|close
argument_list|()
expr_stmt|;
name|zookeeper
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutting down connection to Zookeeper cluster {}"
argument_list|,
name|configuration
operator|.
name|getConnectString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error closing zookeeper connection "
operator|+
name|configuration
operator|.
name|getConnectString
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

