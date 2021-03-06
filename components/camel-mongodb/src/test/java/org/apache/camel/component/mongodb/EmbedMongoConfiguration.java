begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mongodb
package|;
end_package

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
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|MongoClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|mongodb
operator|.
name|client
operator|.
name|MongoClients
import|;
end_import

begin_import
import|import
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|mongo
operator|.
name|MongodExecutable
import|;
end_import

begin_import
import|import
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|mongo
operator|.
name|MongodStarter
import|;
end_import

begin_import
import|import
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|mongo
operator|.
name|config
operator|.
name|IMongodConfig
import|;
end_import

begin_import
import|import
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|mongo
operator|.
name|config
operator|.
name|MongodConfigBuilder
import|;
end_import

begin_import
import|import
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|mongo
operator|.
name|config
operator|.
name|Net
import|;
end_import

begin_import
import|import
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|mongo
operator|.
name|config
operator|.
name|Storage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bson
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import static
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|mongo
operator|.
name|distribution
operator|.
name|Version
operator|.
name|Main
operator|.
name|PRODUCTION
import|;
end_import

begin_import
import|import static
name|de
operator|.
name|flapdoodle
operator|.
name|embed
operator|.
name|process
operator|.
name|runtime
operator|.
name|Network
operator|.
name|localhostIsIPv6
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|SocketUtils
operator|.
name|findAvailableTcpPort
import|;
end_import

begin_class
annotation|@
name|Configuration
DECL|class|EmbedMongoConfiguration
specifier|public
class|class
name|EmbedMongoConfiguration
block|{
DECL|field|PORT
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|findAvailableTcpPort
argument_list|()
decl_stmt|;
static|static
block|{
try|try
block|{
name|IMongodConfig
name|mongodConfig
init|=
operator|new
name|MongodConfigBuilder
argument_list|()
operator|.
name|version
argument_list|(
name|PRODUCTION
argument_list|)
operator|.
name|net
argument_list|(
operator|new
name|Net
argument_list|(
name|PORT
argument_list|,
name|localhostIsIPv6
argument_list|()
argument_list|)
argument_list|)
operator|.
name|replication
argument_list|(
operator|new
name|Storage
argument_list|(
literal|null
argument_list|,
literal|"replicationName"
argument_list|,
literal|5000
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|MongodExecutable
name|mongodExecutable
init|=
name|MongodStarter
operator|.
name|getDefaultInstance
argument_list|()
operator|.
name|prepare
argument_list|(
name|mongodConfig
argument_list|)
decl_stmt|;
name|mongodExecutable
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// init replica set
name|MongoClient
name|client
init|=
name|MongoClients
operator|.
name|create
argument_list|(
literal|"mongodb://localhost:"
operator|+
name|PORT
argument_list|)
decl_stmt|;
name|client
operator|.
name|getDatabase
argument_list|(
literal|"admin"
argument_list|)
operator|.
name|runCommand
argument_list|(
operator|new
name|Document
argument_list|(
literal|"replSetInitiate"
argument_list|,
operator|new
name|Document
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Bean
DECL|method|myDb ()
specifier|public
name|MongoClient
name|myDb
parameter_list|()
throws|throws
name|UnknownHostException
block|{
return|return
name|MongoClients
operator|.
name|create
argument_list|(
literal|"mongodb://localhost:"
operator|+
name|PORT
argument_list|)
return|;
block|}
annotation|@
name|Bean
DECL|method|myDbS ()
specifier|public
name|MongoClient
name|myDbS
parameter_list|()
throws|throws
name|UnknownHostException
block|{
return|return
name|MongoClients
operator|.
name|create
argument_list|(
literal|"mongodb://localhost:"
operator|+
name|PORT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

