begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

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
name|Exchange
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
name|Message
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|util
operator|.
name|FileUtil
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
name|util
operator|.
name|IOHelper
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
name|CreateMode
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
name|ZooDefs
operator|.
name|Ids
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
name|apache
operator|.
name|zookeeper
operator|.
name|data
operator|.
name|ACL
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
name|data
operator|.
name|Stat
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
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_class
DECL|class|ZooKeeperTestSupport
specifier|public
class|class
name|ZooKeeperTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|server
specifier|protected
specifier|static
name|TestZookeeperServer
name|server
decl_stmt|;
DECL|field|client
specifier|protected
specifier|static
name|TestZookeeperClient
name|client
decl_stmt|;
DECL|field|port
specifier|private
specifier|static
specifier|volatile
name|int
name|port
decl_stmt|;
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
name|ZooKeeperTestSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|testPayload
specifier|protected
name|String
name|testPayload
init|=
literal|"This is a test"
decl_stmt|;
DECL|field|testPayloadBytes
specifier|protected
name|byte
index|[]
name|testPayloadBytes
init|=
name|testPayload
operator|.
name|getBytes
argument_list|()
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setupTestServer ()
specifier|public
specifier|static
name|void
name|setupTestServer
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|39913
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting Zookeeper Test Infrastructure"
argument_list|)
expr_stmt|;
name|server
operator|=
operator|new
name|TestZookeeperServer
argument_list|(
name|getServerPort
argument_list|()
argument_list|,
name|clearServerData
argument_list|()
argument_list|)
expr_stmt|;
name|waitForServerUp
argument_list|(
literal|"localhost:"
operator|+
name|getServerPort
argument_list|()
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|client
operator|=
operator|new
name|TestZookeeperClient
argument_list|(
name|getServerPort
argument_list|()
argument_list|,
name|getTestClientSessionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Started Zookeeper Test Infrastructure on port "
operator|+
name|getServerPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|ZooKeeper
name|getConnection
parameter_list|()
block|{
return|return
name|client
operator|.
name|getConnection
argument_list|()
return|;
block|}
annotation|@
name|AfterClass
DECL|method|shutdownServer ()
specifier|public
specifier|static
name|void
name|shutdownServer
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping Zookeeper Test Infrastructure"
argument_list|)
expr_stmt|;
name|client
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|waitForServerDown
argument_list|(
literal|"localhost:"
operator|+
name|getServerPort
argument_list|()
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopped Zookeeper Test Infrastructure"
argument_list|)
expr_stmt|;
block|}
DECL|method|getServerPort ()
specifier|protected
specifier|static
name|int
name|getServerPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getTestClientSessionTimeout ()
specifier|protected
specifier|static
name|int
name|getTestClientSessionTimeout
parameter_list|()
block|{
return|return
literal|100000
return|;
block|}
DECL|method|clearServerData ()
specifier|protected
specifier|static
name|boolean
name|clearServerData
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|class|TestZookeeperServer
specifier|public
specifier|static
class|class
name|TestZookeeperServer
block|{
DECL|field|count
specifier|private
specifier|static
name|int
name|count
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|NIOServerCnxnFactory
name|connectionFactory
decl_stmt|;
DECL|field|zkServer
specifier|private
name|ZooKeeperServer
name|zkServer
decl_stmt|;
DECL|field|zookeeperBaseDir
specifier|private
name|File
name|zookeeperBaseDir
decl_stmt|;
DECL|method|TestZookeeperServer (int clientPort, boolean clearServerData)
specifier|public
name|TestZookeeperServer
parameter_list|(
name|int
name|clientPort
parameter_list|,
name|boolean
name|clearServerData
parameter_list|)
throws|throws
name|Exception
block|{
comment|// TODO This is necessary as zookeeper does not delete the log dir when it shuts down. Remove as soon as zookeeper shutdown works
name|zookeeperBaseDir
operator|=
operator|new
name|File
argument_list|(
literal|"./target/zookeeper"
operator|+
name|count
operator|++
argument_list|)
expr_stmt|;
if|if
condition|(
name|clearServerData
condition|)
block|{
name|cleanZookeeperDir
argument_list|()
expr_stmt|;
block|}
name|zkServer
operator|=
operator|new
name|ZooKeeperServer
argument_list|()
expr_stmt|;
name|File
name|dataDir
init|=
operator|new
name|File
argument_list|(
name|zookeeperBaseDir
argument_list|,
literal|"log"
argument_list|)
decl_stmt|;
name|File
name|snapDir
init|=
operator|new
name|File
argument_list|(
name|zookeeperBaseDir
argument_list|,
literal|"data"
argument_list|)
decl_stmt|;
name|FileTxnSnapLog
name|ftxn
init|=
operator|new
name|FileTxnSnapLog
argument_list|(
name|dataDir
argument_list|,
name|snapDir
argument_list|)
decl_stmt|;
name|zkServer
operator|.
name|setTxnLogFactory
argument_list|(
name|ftxn
argument_list|)
expr_stmt|;
name|zkServer
operator|.
name|setTickTime
argument_list|(
literal|1000
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
operator|new
name|InetSocketAddress
argument_list|(
literal|"localhost"
argument_list|,
name|clientPort
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|connectionFactory
operator|.
name|startup
argument_list|(
name|zkServer
argument_list|)
expr_stmt|;
block|}
DECL|method|cleanZookeeperDir ()
specifier|private
name|void
name|cleanZookeeperDir
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|working
init|=
name|zookeeperBaseDir
decl_stmt|;
name|deleteDir
argument_list|(
name|working
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|connectionFactory
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|connectionFactory
operator|.
name|join
argument_list|()
expr_stmt|;
name|zkServer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
while|while
condition|(
name|zkServer
operator|.
name|isRunning
argument_list|()
condition|)
block|{
name|zkServer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
name|cleanZookeeperDir
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|TestZookeeperClient
specifier|public
specifier|static
class|class
name|TestZookeeperClient
implements|implements
name|Watcher
block|{
DECL|field|x
specifier|public
specifier|static
name|int
name|x
decl_stmt|;
DECL|field|log
specifier|private
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|zk
specifier|private
name|ZooKeeper
name|zk
decl_stmt|;
DECL|field|connected
specifier|private
name|CountDownLatch
name|connected
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|method|TestZookeeperClient (int port, int timeout)
specifier|public
name|TestZookeeperClient
parameter_list|(
name|int
name|port
parameter_list|,
name|int
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
name|zk
operator|=
operator|new
name|ZooKeeper
argument_list|(
literal|"localhost:"
operator|+
name|port
argument_list|,
name|timeout
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|connected
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
DECL|method|getConnection ()
specifier|public
name|ZooKeeper
name|getConnection
parameter_list|()
block|{
return|return
name|zk
return|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|zk
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|waitForNodeChange (String node)
specifier|public
name|byte
index|[]
name|waitForNodeChange
parameter_list|(
name|String
name|node
parameter_list|)
throws|throws
name|Exception
block|{
name|Stat
name|stat
init|=
operator|new
name|Stat
argument_list|()
decl_stmt|;
return|return
name|zk
operator|.
name|getData
argument_list|(
name|node
argument_list|,
name|this
argument_list|,
name|stat
argument_list|)
return|;
block|}
DECL|method|create (String node, String data)
specifier|public
name|void
name|create
parameter_list|(
name|String
name|node
parameter_list|,
name|String
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Creating node '%s' with data '%s' "
argument_list|,
name|node
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|node
argument_list|,
name|data
argument_list|,
name|Ids
operator|.
name|OPEN_ACL_UNSAFE
argument_list|,
name|CreateMode
operator|.
name|EPHEMERAL
argument_list|)
expr_stmt|;
block|}
DECL|method|createPersistent (String node, String data)
specifier|public
name|void
name|createPersistent
parameter_list|(
name|String
name|node
parameter_list|,
name|String
name|data
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Creating node '%s' with data '%s' "
argument_list|,
name|node
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|node
argument_list|,
name|data
argument_list|,
name|Ids
operator|.
name|OPEN_ACL_UNSAFE
argument_list|,
name|CreateMode
operator|.
name|PERSISTENT
argument_list|)
expr_stmt|;
block|}
DECL|method|create (String znode, String data, List<ACL> access, CreateMode mode)
specifier|public
name|void
name|create
parameter_list|(
name|String
name|znode
parameter_list|,
name|String
name|data
parameter_list|,
name|List
argument_list|<
name|ACL
argument_list|>
name|access
parameter_list|,
name|CreateMode
name|mode
parameter_list|)
throws|throws
name|Exception
block|{
name|delay
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|String
name|created
init|=
name|zk
operator|.
name|create
argument_list|(
name|znode
argument_list|,
name|data
operator|!=
literal|null
condition|?
name|data
operator|.
name|getBytes
argument_list|()
else|:
literal|null
argument_list|,
name|access
argument_list|,
name|mode
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Created znode named '%s'"
argument_list|,
name|created
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setData (String node, String data, int version)
specifier|public
name|Stat
name|setData
parameter_list|(
name|String
name|node
parameter_list|,
name|String
name|data
parameter_list|,
name|int
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"TestClient Updating data of node %s to %s"
argument_list|,
name|node
argument_list|,
name|data
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|zk
operator|.
name|setData
argument_list|(
name|node
argument_list|,
name|data
operator|.
name|getBytes
argument_list|()
argument_list|,
name|version
argument_list|)
return|;
block|}
DECL|method|getData (String znode)
specifier|public
name|byte
index|[]
name|getData
parameter_list|(
name|String
name|znode
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|zk
operator|.
name|getData
argument_list|(
name|znode
argument_list|,
literal|false
argument_list|,
operator|new
name|Stat
argument_list|()
argument_list|)
return|;
block|}
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
name|log
operator|.
name|info
argument_list|(
literal|"TestClient connected"
argument_list|)
expr_stmt|;
name|connected
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
else|else
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
name|Disconnected
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"TestClient connected ?"
operator|+
name|zk
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|deleteAll (String node)
specifier|public
name|void
name|deleteAll
parameter_list|(
name|String
name|node
parameter_list|)
throws|throws
name|Exception
block|{
name|delay
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Deleting {} and it's immediate children"
argument_list|,
name|node
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|child
range|:
name|zk
operator|.
name|getChildren
argument_list|(
name|node
argument_list|,
literal|false
argument_list|)
control|)
block|{
name|delete
argument_list|(
name|node
operator|+
literal|"/"
operator|+
name|child
argument_list|)
expr_stmt|;
block|}
name|delete
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
DECL|method|delete (String node)
specifier|public
name|void
name|delete
parameter_list|(
name|String
name|node
parameter_list|)
throws|throws
name|Exception
block|{
name|delay
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Deleting node "
operator|+
name|node
argument_list|)
expr_stmt|;
name|zk
operator|.
name|delete
argument_list|(
name|node
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Wait methods are taken directly from the Zookeeper tests. A tests jar
comment|// would be nice! Another good reason the keeper folks should move to maven.
DECL|method|waitForServerUp (String hp, long timeout)
specifier|private
specifier|static
name|boolean
name|waitForServerUp
parameter_list|(
name|String
name|hp
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
comment|// if there are multiple hostports, just take the first one
name|hp
operator|=
name|hp
operator|.
name|split
argument_list|(
literal|","
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
name|String
name|result
init|=
name|send4LetterWord
argument_list|(
name|hp
argument_list|,
literal|"stat"
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|.
name|startsWith
argument_list|(
literal|"Zookeeper version:"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"server {} not up {}"
argument_list|,
name|hp
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|>
name|start
operator|+
name|timeout
condition|)
block|{
break|break;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|send4LetterWord (String hp, String cmd)
specifier|private
specifier|static
name|String
name|send4LetterWord
parameter_list|(
name|String
name|hp
parameter_list|,
name|String
name|cmd
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|split
index|[]
init|=
name|hp
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|split
index|[
literal|0
index|]
decl_stmt|;
name|int
name|port
decl_stmt|;
try|try
block|{
name|port
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|split
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Problem parsing "
operator|+
name|hp
operator|+
name|e
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
name|Socket
name|sock
init|=
operator|new
name|Socket
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
decl_stmt|;
name|BufferedReader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|OutputStream
name|outstream
init|=
name|sock
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|outstream
operator|.
name|write
argument_list|(
name|cmd
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|outstream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|reader
operator|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|sock
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
name|line
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|line
operator|+
literal|"\n"
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
finally|finally
block|{
name|sock
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|waitForServerDown (String hp, long timeout)
specifier|private
specifier|static
name|boolean
name|waitForServerDown
parameter_list|(
name|String
name|hp
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
try|try
block|{
name|send4LetterWord
argument_list|(
name|hp
argument_list|,
literal|"stat"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|>
name|start
operator|+
name|timeout
condition|)
block|{
break|break;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|deleteDir (File f)
specifier|public
specifier|static
name|void
name|deleteDir
parameter_list|(
name|File
name|f
parameter_list|)
block|{
name|LinkedList
argument_list|<
name|File
argument_list|>
name|deleteStack
init|=
operator|new
name|LinkedList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|deleteStack
operator|.
name|addLast
argument_list|(
name|f
argument_list|)
expr_stmt|;
name|deleteDir
argument_list|(
name|deleteStack
argument_list|)
expr_stmt|;
block|}
DECL|method|deleteDir (Deque<File> deleteStack)
specifier|private
specifier|static
name|void
name|deleteDir
parameter_list|(
name|Deque
argument_list|<
name|File
argument_list|>
name|deleteStack
parameter_list|)
block|{
name|File
name|f
init|=
name|deleteStack
operator|.
name|pollLast
argument_list|()
decl_stmt|;
if|if
condition|(
name|f
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|f
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|File
index|[]
name|files
init|=
name|f
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
name|child
range|:
name|files
control|)
block|{
name|deleteStack
operator|.
name|addLast
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|deleteDir
argument_list|(
name|deleteStack
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|delay (int wait)
specifier|public
specifier|static
name|void
name|delay
parameter_list|(
name|int
name|wait
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|wait
argument_list|)
expr_stmt|;
block|}
DECL|method|createChildListing (String... children)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|createChildListing
parameter_list|(
name|String
modifier|...
name|children
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|children
argument_list|)
return|;
block|}
DECL|method|validateExchangesReceivedInOrderWithIncreasingVersion (MockEndpoint mock)
specifier|protected
name|void
name|validateExchangesReceivedInOrderWithIncreasingVersion
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|)
block|{
name|int
name|lastVersion
init|=
operator|-
literal|1
decl_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|received
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|received
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|Message
name|zkm
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
name|x
argument_list|)
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|int
name|version
init|=
name|ZooKeeperMessage
operator|.
name|getStatistics
argument_list|(
name|zkm
argument_list|)
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Version did not increase"
argument_list|,
name|lastVersion
operator|<
name|version
argument_list|)
expr_stmt|;
name|lastVersion
operator|=
name|version
expr_stmt|;
block|}
block|}
DECL|method|verifyAccessControlList (String node, List<ACL> expected)
specifier|protected
name|void
name|verifyAccessControlList
parameter_list|(
name|String
name|node
parameter_list|,
name|List
argument_list|<
name|ACL
argument_list|>
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
name|getConnection
argument_list|()
operator|.
name|getACL
argument_list|(
name|node
argument_list|,
operator|new
name|Stat
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyNodeContainsData (String node, byte[] expected)
specifier|protected
name|void
name|verifyNodeContainsData
parameter_list|(
name|String
name|node
parameter_list|,
name|byte
index|[]
name|expected
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|expected
operator|==
literal|null
condition|)
block|{
name|assertNull
argument_list|(
name|client
operator|.
name|getData
argument_list|(
name|node
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|assertEquals
argument_list|(
operator|new
name|String
argument_list|(
name|expected
argument_list|)
argument_list|,
operator|new
name|String
argument_list|(
name|client
operator|.
name|getData
argument_list|(
name|node
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

