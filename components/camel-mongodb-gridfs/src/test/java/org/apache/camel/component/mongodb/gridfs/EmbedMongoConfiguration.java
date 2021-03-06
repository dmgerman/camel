begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mongodb.gridfs
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
operator|.
name|gridfs
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
name|MongoClient
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
name|Command
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
name|RuntimeConfigBuilder
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
name|process
operator|.
name|config
operator|.
name|IRuntimeConfig
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
name|com
operator|.
name|mongodb
operator|.
name|ServerAddress
operator|.
name|defaultHost
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
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EmbedMongoConfiguration
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PORT
specifier|private
specifier|static
specifier|final
name|int
name|PORT
init|=
name|findAvailableTcpPort
argument_list|(
literal|18500
argument_list|)
decl_stmt|;
comment|// 1024 is too low on CI servers to find free ports
static|static
block|{
try|try
block|{
name|IRuntimeConfig
name|runtimeConfig
init|=
operator|new
name|RuntimeConfigBuilder
argument_list|()
operator|.
name|defaultsWithLogger
argument_list|(
name|Command
operator|.
name|MongoD
argument_list|,
name|LOGGER
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
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
name|build
argument_list|()
decl_stmt|;
name|MongodStarter
operator|.
name|getInstance
argument_list|(
name|runtimeConfig
argument_list|)
operator|.
name|prepare
argument_list|(
name|mongodConfig
argument_list|)
operator|.
name|start
argument_list|()
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
operator|new
name|MongoClient
argument_list|(
name|defaultHost
argument_list|()
argument_list|,
name|PORT
argument_list|)
return|;
block|}
block|}
end_class

end_unit

