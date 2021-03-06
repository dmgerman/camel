begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
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
name|server
operator|.
name|NIOServerCnxnFactory
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
name|server
operator|.
name|ZooKeeperServer
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
name|server
operator|.
name|persistence
operator|.
name|FileTxnSnapLog
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|FactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|InitializingBean
import|;
end_import

begin_comment
comment|/**  * A simple ZK server for testing ZK related code in unit tests  */
end_comment

begin_class
DECL|class|ZKServerFactoryBean
specifier|public
class|class
name|ZKServerFactoryBean
implements|implements
name|FactoryBean
argument_list|<
name|ZooKeeperServer
argument_list|>
implements|,
name|InitializingBean
implements|,
name|DisposableBean
block|{
DECL|field|zooKeeperServer
specifier|private
name|ZooKeeperServer
name|zooKeeperServer
init|=
operator|new
name|ZooKeeperServer
argument_list|()
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|NIOServerCnxnFactory
name|connectionFactory
decl_stmt|;
DECL|field|dataLogDir
specifier|private
name|File
name|dataLogDir
decl_stmt|;
DECL|field|dataDir
specifier|private
name|File
name|dataDir
decl_stmt|;
DECL|field|purge
specifier|private
name|boolean
name|purge
decl_stmt|;
DECL|field|tickTime
specifier|private
name|int
name|tickTime
init|=
name|ZooKeeperServer
operator|.
name|DEFAULT_TICK_TIME
decl_stmt|;
comment|/**      * defaults to -1 if not set explicitly      */
DECL|field|minSessionTimeout
specifier|private
name|int
name|minSessionTimeout
init|=
operator|-
literal|1
decl_stmt|;
comment|/**      * defaults to -1 if not set explicitly      */
DECL|field|maxSessionTimeout
specifier|private
name|int
name|maxSessionTimeout
init|=
operator|-
literal|1
decl_stmt|;
DECL|field|clientPortAddress
specifier|private
name|InetSocketAddress
name|clientPortAddress
decl_stmt|;
DECL|field|maxClientConnections
specifier|private
name|int
name|maxClientConnections
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
init|=
literal|2181
decl_stmt|;
annotation|@
name|Override
DECL|method|getObject ()
specifier|public
name|ZooKeeperServer
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|zooKeeperServer
return|;
block|}
annotation|@
name|Override
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|ZooKeeperServer
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|ZooKeeperServer
operator|.
name|class
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|afterPropertiesSet ()
specifier|public
name|void
name|afterPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|purge
condition|)
block|{
name|deleteFilesInDir
argument_list|(
name|getDataLogDir
argument_list|()
argument_list|)
expr_stmt|;
name|deleteFilesInDir
argument_list|(
name|getDataDir
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|FileTxnSnapLog
name|ftxn
init|=
operator|new
name|FileTxnSnapLog
argument_list|(
name|getDataLogDir
argument_list|()
argument_list|,
name|getDataDir
argument_list|()
argument_list|)
decl_stmt|;
name|zooKeeperServer
operator|.
name|setTxnLogFactory
argument_list|(
name|ftxn
argument_list|)
expr_stmt|;
name|zooKeeperServer
operator|.
name|setTickTime
argument_list|(
name|getTickTime
argument_list|()
argument_list|)
expr_stmt|;
name|zooKeeperServer
operator|.
name|setMinSessionTimeout
argument_list|(
name|getMinSessionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|zooKeeperServer
operator|.
name|setMaxSessionTimeout
argument_list|(
name|getMaxSessionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|connectionFactory
operator|=
operator|new
name|NIOServerCnxnFactory
argument_list|()
expr_stmt|;
name|connectionFactory
operator|.
name|configure
argument_list|(
name|getClientPortAddress
argument_list|()
argument_list|,
name|getMaxClientConnections
argument_list|()
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|startup
argument_list|(
name|zooKeeperServer
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteFilesInDir (File dir)
specifier|private
name|void
name|deleteFilesInDir
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
name|File
index|[]
name|files
init|=
name|dir
operator|.
name|listFiles
argument_list|()
decl_stmt|;
if|if
condition|(
name|files
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|deleteFilesInDir
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|shutdown ()
specifier|protected
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
name|connectionFactory
operator|!=
literal|null
condition|)
block|{
name|connectionFactory
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
name|connectionFactory
operator|.
name|join
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
name|connectionFactory
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|zooKeeperServer
operator|!=
literal|null
condition|)
block|{
name|zooKeeperServer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|zooKeeperServer
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getZooKeeperServer ()
specifier|public
name|ZooKeeperServer
name|getZooKeeperServer
parameter_list|()
block|{
return|return
name|zooKeeperServer
return|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|NIOServerCnxnFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|getDataLogDir ()
specifier|public
name|File
name|getDataLogDir
parameter_list|()
block|{
if|if
condition|(
name|dataLogDir
operator|==
literal|null
condition|)
block|{
name|dataLogDir
operator|=
operator|new
name|File
argument_list|(
name|getZKOutputDir
argument_list|()
argument_list|,
literal|"log"
argument_list|)
expr_stmt|;
name|dataLogDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
return|return
name|dataLogDir
return|;
block|}
DECL|method|getDataDir ()
specifier|public
name|File
name|getDataDir
parameter_list|()
block|{
if|if
condition|(
name|dataDir
operator|==
literal|null
condition|)
block|{
name|dataDir
operator|=
operator|new
name|File
argument_list|(
name|getZKOutputDir
argument_list|()
argument_list|,
literal|"data"
argument_list|)
expr_stmt|;
name|dataDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
return|return
name|dataDir
return|;
block|}
DECL|method|getTickTime ()
specifier|public
name|int
name|getTickTime
parameter_list|()
block|{
return|return
name|tickTime
return|;
block|}
DECL|method|getMinSessionTimeout ()
specifier|public
name|int
name|getMinSessionTimeout
parameter_list|()
block|{
return|return
name|minSessionTimeout
return|;
block|}
DECL|method|getMaxSessionTimeout ()
specifier|public
name|int
name|getMaxSessionTimeout
parameter_list|()
block|{
return|return
name|maxSessionTimeout
return|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getClientPortAddress ()
specifier|public
name|InetSocketAddress
name|getClientPortAddress
parameter_list|()
block|{
if|if
condition|(
name|clientPortAddress
operator|==
literal|null
condition|)
block|{
name|clientPortAddress
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
return|return
name|clientPortAddress
return|;
block|}
DECL|method|getMaxClientConnections ()
specifier|public
name|int
name|getMaxClientConnections
parameter_list|()
block|{
return|return
name|maxClientConnections
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|setClientPortAddress (InetSocketAddress clientPortAddress)
specifier|public
name|void
name|setClientPortAddress
parameter_list|(
name|InetSocketAddress
name|clientPortAddress
parameter_list|)
block|{
name|this
operator|.
name|clientPortAddress
operator|=
name|clientPortAddress
expr_stmt|;
block|}
DECL|method|setConnectionFactory (NIOServerCnxnFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|NIOServerCnxnFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
DECL|method|setDataDir (File dataDir)
specifier|public
name|void
name|setDataDir
parameter_list|(
name|File
name|dataDir
parameter_list|)
block|{
name|this
operator|.
name|dataDir
operator|=
name|dataDir
expr_stmt|;
block|}
DECL|method|setDataLogDir (File dataLogDir)
specifier|public
name|void
name|setDataLogDir
parameter_list|(
name|File
name|dataLogDir
parameter_list|)
block|{
name|this
operator|.
name|dataLogDir
operator|=
name|dataLogDir
expr_stmt|;
block|}
DECL|method|setMaxClientConnections (int maxClientConnections)
specifier|public
name|void
name|setMaxClientConnections
parameter_list|(
name|int
name|maxClientConnections
parameter_list|)
block|{
name|this
operator|.
name|maxClientConnections
operator|=
name|maxClientConnections
expr_stmt|;
block|}
DECL|method|setMaxSessionTimeout (int maxSessionTimeout)
specifier|public
name|void
name|setMaxSessionTimeout
parameter_list|(
name|int
name|maxSessionTimeout
parameter_list|)
block|{
name|this
operator|.
name|maxSessionTimeout
operator|=
name|maxSessionTimeout
expr_stmt|;
block|}
DECL|method|setMinSessionTimeout (int minSessionTimeout)
specifier|public
name|void
name|setMinSessionTimeout
parameter_list|(
name|int
name|minSessionTimeout
parameter_list|)
block|{
name|this
operator|.
name|minSessionTimeout
operator|=
name|minSessionTimeout
expr_stmt|;
block|}
DECL|method|setTickTime (int tickTime)
specifier|public
name|void
name|setTickTime
parameter_list|(
name|int
name|tickTime
parameter_list|)
block|{
name|this
operator|.
name|tickTime
operator|=
name|tickTime
expr_stmt|;
block|}
DECL|method|setZooKeeperServer (ZooKeeperServer zooKeeperServer)
specifier|public
name|void
name|setZooKeeperServer
parameter_list|(
name|ZooKeeperServer
name|zooKeeperServer
parameter_list|)
block|{
name|this
operator|.
name|zooKeeperServer
operator|=
name|zooKeeperServer
expr_stmt|;
block|}
DECL|method|isPurge ()
specifier|public
name|boolean
name|isPurge
parameter_list|()
block|{
return|return
name|purge
return|;
block|}
DECL|method|setPurge (boolean purge)
specifier|public
name|void
name|setPurge
parameter_list|(
name|boolean
name|purge
parameter_list|)
block|{
name|this
operator|.
name|purge
operator|=
name|purge
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
DECL|method|getZKOutputDir ()
specifier|protected
name|File
name|getZKOutputDir
parameter_list|()
block|{
name|String
name|baseDir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|baseDir
operator|+
literal|"/target/zk"
argument_list|)
decl_stmt|;
name|dir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
return|return
name|dir
return|;
block|}
block|}
end_class

end_unit

