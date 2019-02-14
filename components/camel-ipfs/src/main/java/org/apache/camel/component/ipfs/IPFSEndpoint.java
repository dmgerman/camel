begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ipfs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ipfs
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
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
name|ExecutionException
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
name|Future
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
name|TimeUnit
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|io
operator|.
name|ipfs
operator|.
name|multihash
operator|.
name|Multihash
import|;
end_import

begin_import
import|import
name|io
operator|.
name|nessus
operator|.
name|ipfs
operator|.
name|client
operator|.
name|IPFSClient
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|ipfs
operator|.
name|IPFSConfiguration
operator|.
name|IPFSCommand
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The camel-ipfs component provides access to the Interplanetary File System  * (IPFS).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.23.0"
argument_list|,
name|scheme
operator|=
literal|"ipfs"
argument_list|,
name|title
operator|=
literal|"IPFS"
argument_list|,
name|syntax
operator|=
literal|"ipfs:host:port/cmd"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"file,ipfs"
argument_list|)
DECL|class|IPFSEndpoint
specifier|public
class|class
name|IPFSEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|defaultTimeout
specifier|static
name|long
name|defaultTimeout
init|=
literal|10000L
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
specifier|final
name|IPFSConfiguration
name|configuration
decl_stmt|;
DECL|method|IPFSEndpoint (String uri, IPFSComponent component, IPFSConfiguration configuration)
specifier|public
name|IPFSEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|IPFSComponent
name|component
parameter_list|,
name|IPFSConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|IPFSComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|IPFSComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|IPFSProducer
argument_list|(
name|this
argument_list|)
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
literal|false
return|;
block|}
DECL|method|getConfiguration ()
name|IPFSConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getCommand ()
name|IPFSCommand
name|getCommand
parameter_list|()
block|{
name|String
name|cmd
init|=
name|configuration
operator|.
name|getIpfsCmd
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|IPFSCommand
operator|.
name|valueOf
argument_list|(
name|cmd
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported command: "
operator|+
name|cmd
argument_list|)
throw|;
block|}
block|}
DECL|method|ipfsVersion ()
name|String
name|ipfsVersion
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|ipfs
argument_list|()
operator|.
name|version
argument_list|()
return|;
block|}
DECL|method|ipfsAdd (Path path)
name|List
argument_list|<
name|String
argument_list|>
name|ipfsAdd
parameter_list|(
name|Path
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Multihash
argument_list|>
name|cids
init|=
name|ipfs
argument_list|()
operator|.
name|add
argument_list|(
name|path
argument_list|)
decl_stmt|;
return|return
name|cids
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|mh
lambda|->
name|mh
operator|.
name|toBase58
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|ipfsCat (String cid)
name|InputStream
name|ipfsCat
parameter_list|(
name|String
name|cid
parameter_list|)
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
name|Multihash
name|mhash
init|=
name|Multihash
operator|.
name|fromBase58
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|InputStream
argument_list|>
name|future
init|=
name|ipfs
argument_list|()
operator|.
name|cat
argument_list|(
name|mhash
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|(
name|defaultTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot obtain: "
operator|+
name|cid
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
DECL|method|ipfsGet (String cid, Path outdir)
name|Path
name|ipfsGet
parameter_list|(
name|String
name|cid
parameter_list|,
name|Path
name|outdir
parameter_list|)
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
name|Multihash
name|mhash
init|=
name|Multihash
operator|.
name|fromBase58
argument_list|(
name|cid
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|Path
argument_list|>
name|future
init|=
name|ipfs
argument_list|()
operator|.
name|get
argument_list|(
name|mhash
argument_list|,
name|outdir
argument_list|)
decl_stmt|;
try|try
block|{
return|return
name|future
operator|.
name|get
argument_list|(
name|defaultTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|ExecutionException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot obtain: "
operator|+
name|cid
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
DECL|method|ipfs ()
specifier|private
name|IPFSClient
name|ipfs
parameter_list|()
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getIPFSClient
argument_list|()
return|;
block|}
block|}
end_class

end_unit

